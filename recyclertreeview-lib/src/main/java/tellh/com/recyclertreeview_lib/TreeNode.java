package tellh.com.recyclertreeview_lib;


import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tlh on 2016/10/1 :)
 */

public class TreeNode<T> implements Cloneable {
    private T data;
    private TreeNode<T> parent;
    private List<TreeNode<T>> childList;
    private boolean isExpand;
    private boolean isLocked;
    private boolean isInTheWayOfSearch;
    private boolean isRequiredElement;
    //the tree high
    private int height = UNDEFINE;

    private static final int UNDEFINE = -1;

    public TreeNode(@NonNull T data) {
        this.data = data;
        this.childList = new ArrayList<>();
    }

    public int getHeight() {
        if (isRoot())
            height = 0;
        else if (height == UNDEFINE)
            height = parent.getHeight() + 1;
        return height;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isParentRoot(){
        if(isRoot()){
            return false;
        }
        return getParent().isRoot();
    }

    public boolean isLeaf() {
        return childList == null || childList.isEmpty();
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public List<TreeNode<T>> getChildList() {
        return childList;
    }

    public void setChildList(List<TreeNode<T>> childList) {
        this.childList.clear();
        for (TreeNode<T> treeNode : childList) {
            addChild(treeNode);
        }
    }

    public TreeNode<T> addChild(TreeNode<T> node) {
        if (childList == null)
            childList = new ArrayList<>();
        childList.add(node);
        node.parent = this;
        return this;
    }

    public boolean toggle() {
        isExpand = !isExpand;
        return isExpand;
    }

    public void collapse() {
        if (isExpand) {
            isExpand = false;
        }
    }

    public void collapseAll() {
        if (childList == null || childList.isEmpty()) {
            return;
        }
        for (TreeNode child : this.childList) {
            child.collapseAll();
        }
    }

    public void expand() {
        if (!isExpand) {
            isExpand = true;
        }
    }

    public void expandAll() {
        expand();
        if (childList == null || childList.isEmpty()) {
            return;
        }
        for (TreeNode child : this.childList) {
            child.expandAll();
        }
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    public TreeNode<T> getParent() {
        return parent;
    }

    public TreeNode<T> lock() {
        isLocked = true;
        return this;
    }

    public TreeNode<T> unlock() {
        isLocked = false;
        return this;
    }

    public boolean search(TreeNode<T> node, T value, List<TreeNode<T>> track) {
        if (node == null) return false;

        if (node.data.equals(value)) {
            node.setRequiredElement(true);
            track.add(node);
            return true;
        }

        for (TreeNode<T> child : node.childList) {
            if (search(child, value, track)) {
                track.add(0, node);
                return true;
            }
        }

        return false;
    }


    public boolean isLocked() {
        return isLocked;
    }

    public boolean isInTheWayOfSearch() {
        return isInTheWayOfSearch;
    }

    public boolean isRequiredElement() {
        return isRequiredElement;
    }

    public void setInTheWayOfSearch(boolean inTheWayOfSearch) {
        isInTheWayOfSearch = inTheWayOfSearch;
    }

    public int getPositionInParentChildList() {
        if (parent == null) {
            return UNDEFINE;
        }
        return parent.childList.indexOf(this);
    }

    public boolean isLastElementInParentChildList() {
        if (parent == null) {
            return false;
        }
        return getPositionInParentChildList() == parent.childList.size() - 1;
    }

    public void setRequiredElement(boolean requiredElement) {
        isRequiredElement = requiredElement;
    }

    @NonNull
    @Override
    public String toString() {
        return "TreeNode{" +
                "data=" + this.data +
                ", parent=" + (parent == null ? "null" : parent.getData().toString()) +
                ", childList=" + (childList == null ? "null" : childList.toString()) +
                ", isExpand=" + isExpand +
                '}';
    }

    @Override
    protected TreeNode<T> clone() throws CloneNotSupportedException {
        TreeNode<T> clone = new TreeNode<>(this.data);
        clone.isExpand = this.isExpand;
        return clone;
    }
}
