package tellh.com.recyclertreeview_lib;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void bindView(int position, TreeNode<T> node);

    @NonNull
    public abstract View getExpandableButton();

    @Nullable
    public abstract ImageView getExpandableArrow();

    protected <V extends View> V findViewById(@IdRes int id) {
        return itemView.findViewById(id);
    }

    protected Context getContext() {
        return itemView.getContext();
    }


}
