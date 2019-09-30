package tellh.com.recyclertreeview_lib;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tlh on 2016/10/1 :)
 */
public class TreeViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder<T>> {
    private static final String KEY_IS_EXPAND = "IS_EXPAND";
    private final ViewHolderFactory<T> factory;
    private List<TreeNode<T>> displayNodes;
    private int padding = 30;
    private OnTreeNodeListener onTreeNodeListener;
    private boolean toCollapseChild;

    public TreeViewAdapter(ViewHolderFactory<T> factory, List<TreeNode<T>> nodes) {
        this.factory = factory;
        displayNodes = new ArrayList<>();
        if (nodes != null)
            findDisplayNodes(nodes);
    }

    public TreeViewAdapter(ViewHolderFactory<T> factory) {
        this(factory, null);
    }


    /**
     * 从nodes的结点中寻找展开了的非叶结点，添加到displayNodes中。
     *
     * @param nodes 基准点
     */
    private void findDisplayNodes(List<TreeNode<T>> nodes) {
        for (TreeNode<T> node : nodes) {
            displayNodes.add(node);
            if (!node.isLeaf() && node.isExpand())
                findDisplayNodes(node.getChildList());
        }
    }

    @NonNull
    @Override
    public BaseViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return factory.create(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<T> holder, int position, @NonNull List<Object> payloads) {
        if (!payloads.isEmpty()) {
            Bundle b = (Bundle) payloads.get(0);
            for (String key : b.keySet()) {
                if (KEY_IS_EXPAND.equals(key)) {
                    if (onTreeNodeListener != null) {
                        animateArrow(holder.getExpandableArrow(), b.getBoolean(key));
                    }
                }
            }
        }
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseViewHolder<T> holder, int position) {
        if (holder.getExpandableButton() != null) {

            holder.getExpandableButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TreeNode<T> selectedNode = displayNodes.get(holder.getLayoutPosition());

                    // Prevent multi-click during the short interval.
                    try {
                        long lastClickTime = (long) holder.itemView.getTag();
                        if (System.currentTimeMillis() - lastClickTime < 500)
                            return;
                    } catch (Exception e) {
                        holder.itemView.setTag(System.currentTimeMillis());
                    }
                    holder.itemView.setTag(System.currentTimeMillis());

                    // This TreeNode was locked to click.
                    if (selectedNode.isLocked()) return;
                    boolean isExpand = selectedNode.isExpand();

                    animateArrow(holder.getExpandableArrow(), isExpand);


                    int positionStart = displayNodes.indexOf(selectedNode) + 1;
                    if (!isExpand) {
                        notifyItemRangeInserted(positionStart, addChildNodes(selectedNode, positionStart));
                    } else {
                        notifyItemRangeRemoved(positionStart, removeChildNodes(selectedNode, true));
                    }
                }
            });
        }

        if (holder.getCollapseParentButton() != null) {
            holder.getCollapseParentButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TreeNode<T> node = displayNodes.get(holder.getLayoutPosition());
                    if (node.isRoot()) {
                        return;
                    }
                    TreeNode<T> parent = node.getParent();

                    // Prevent multi-click during the short interval.
                    try {
                        long lastClickTime = (long) holder.itemView.getTag();
                        if (System.currentTimeMillis() - lastClickTime < 500)
                            return;
                    } catch (Exception e) {
                        holder.itemView.setTag(System.currentTimeMillis());
                    }
                    holder.itemView.setTag(System.currentTimeMillis());

                    // This TreeNode was locked to click.
                    if (parent.isLocked()) return;
                    boolean isExpand = parent.isExpand();
                    if (!isExpand) {
                        return;
                    }
                    animateArrow(holder.getCollapseParentArrow(), true);


                    int positionStart = displayNodes.indexOf(parent) + 1;
                    notifyItemRangeRemoved(positionStart, removeChildNodes(parent, true));
                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTreeNodeListener != null) {
                    onTreeNodeListener.onClick(displayNodes.get(holder.getLayoutPosition()));
                }
            }
        });

        holder.bindView(position, displayNodes.get(position));
    }

    private void animateArrow(@Nullable ImageView ivArrow, boolean isExpand) {
        int rotateDegree = isExpand ? 180 : -180;
        if (ivArrow != null) {
            ivArrow.animate().rotationBy(rotateDegree)
                    .start();
        }
    }

    private int addChildNodes(TreeNode<T> pNode, int startIndex) {
        List<TreeNode<T>> childList = pNode.getChildList();
        int addChildCount = 0;
        for (TreeNode<T> treeNode : childList) {
            displayNodes.add(startIndex + addChildCount++, treeNode);
            if (treeNode.isExpand()) {
                addChildCount += addChildNodes(treeNode, startIndex + addChildCount);
            }
        }
        if (!pNode.isExpand())
            pNode.toggle();
        return addChildCount;
    }

    private int removeChildNodes(TreeNode<T> pNode) {
        return removeChildNodes(pNode, true);
    }

    private int removeChildNodes(TreeNode<T> pNode, boolean shouldToggle) {
        if (pNode.isLeaf())
            return 0;
        List<TreeNode<T>> childList = pNode.getChildList();
        int removeChildCount = childList.size();
        displayNodes.removeAll(childList);
        for (TreeNode<T> child : childList) {
            if (child.isExpand()) {
                if (toCollapseChild)
                    child.toggle();
                removeChildCount += removeChildNodes(child, false);
            }
        }
        if (shouldToggle)
            pNode.toggle();
        return removeChildCount;
    }

    @Override
    public int getItemCount() {
        return displayNodes == null ? 0 : displayNodes.size();
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public void ifCollapseChildWhileCollapseParent(boolean toCollapseChild) {
        this.toCollapseChild = toCollapseChild;
    }

    public void setOnTreeNodeListener(OnTreeNodeListener onTreeNodeListener) {
        this.onTreeNodeListener = onTreeNodeListener;
    }

    public interface OnTreeNodeListener {
        /**
         * called when TreeNodes were clicked.
         */
        void onClick(TreeNode node);

    }

    public void refresh(List<TreeNode<T>> treeNodes) {
        displayNodes.clear();
        findDisplayNodes(treeNodes);
        notifyDataSetChanged();
    }

    public Iterator<TreeNode<T>> getDisplayNodesIterator() {
        return displayNodes.iterator();
    }

    private void notifyDiff(final List<TreeNode<T>> temp) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return temp.size();
            }

            @Override
            public int getNewListSize() {
                return displayNodes.size();
            }

            // judge if the same items
            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return TreeViewAdapter.this.areItemsTheSame(temp.get(oldItemPosition), displayNodes.get(newItemPosition));
            }

            // if they are the same items, whether the contents has bean changed.
            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return TreeViewAdapter.this.areContentsTheSame(temp.get(oldItemPosition), displayNodes.get(newItemPosition));
            }

            @Nullable
            @Override
            public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                return TreeViewAdapter.this.getChangePayload(temp.get(oldItemPosition), displayNodes.get(newItemPosition));
            }
        });
        diffResult.dispatchUpdatesTo(this);
    }

    private Object getChangePayload(TreeNode<T> oldNode, TreeNode<T> newNode) {
        Bundle diffBundle = new Bundle();
        if (newNode.isExpand() != oldNode.isExpand()) {
            diffBundle.putBoolean(KEY_IS_EXPAND, newNode.isExpand());
        }
        if (diffBundle.size() == 0)
            return null;
        return diffBundle;
    }

    // For DiffUtil, if they are the same items, whether the contents has bean changed.
    private boolean areContentsTheSame(TreeNode oldNode, TreeNode newNode) {
        return oldNode.getData() != null && oldNode.getData().equals(newNode.getData())
                && oldNode.isExpand() == newNode.isExpand()
                && oldNode.isInTheWayOfSearch() == newNode.isInTheWayOfSearch()
                && oldNode.isRequiredElement() == newNode.isInTheWayOfSearch();
    }

    // judge if the same item for DiffUtil
    private boolean areItemsTheSame(TreeNode oldNode, TreeNode newNode) {
        return oldNode.getData() != null && oldNode.getData().equals(newNode.getData());
    }

    /**
     * collapse all root nodes.
     */
    public void collapseAll() {
        // Back up the nodes are displaying.
        List<TreeNode<T>> temp = backupDisplayNodes();
        //find all root nodes.
        List<TreeNode<T>> roots = new ArrayList<>();
        for (TreeNode<T> displayNode : displayNodes) {
            if (displayNode.isRoot())
                roots.add(displayNode);
        }
        //Close all root nodes.
        for (TreeNode<T> root : roots) {
            if (root.isExpand())
                removeChildNodes(root);
        }
        notifyDiff(temp);
    }

    @NonNull
    private List<TreeNode<T>> backupDisplayNodes() {
        List<TreeNode<T>> temp = new ArrayList<>();
        for (TreeNode<T> displayNode : displayNodes) {
            try {
                temp.add(displayNode.clone());
            } catch (CloneNotSupportedException e) {
                temp.add(displayNode);
            }
        }
        return temp;
    }

    public void collapseNode(TreeNode<T> pNode) {
        List<TreeNode<T>> temp = backupDisplayNodes();
        removeChildNodes(pNode);
        notifyDiff(temp);
    }

    public void collapseBrotherNode(TreeNode<T> pNode) {
        List<TreeNode<T>> temp = backupDisplayNodes();
        if (pNode.isRoot()) {
            List<TreeNode<T>> roots = new ArrayList<>();
            for (TreeNode<T> displayNode : displayNodes) {
                if (displayNode.isRoot())
                    roots.add(displayNode);
            }
            //Close all root nodes.
            for (TreeNode<T> root : roots) {
                if (root.isExpand() && !root.equals(pNode))
                    removeChildNodes(root);
            }
        } else {
            TreeNode<T> parent = pNode.getParent();
            if (parent == null)
                return;
            List<TreeNode<T>> childList = parent.getChildList();
            for (TreeNode<T> node : childList) {
                if (node.equals(pNode) || !node.isExpand())
                    continue;
                removeChildNodes(node);
            }
        }
        notifyDiff(temp);
    }

}
