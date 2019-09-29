package tellh.com.recyclertreeview_lib;

import android.view.LayoutInflater;
import android.view.ViewGroup;

public abstract class ViewHolderFactory<T> {
    public abstract BaseViewHolder<T> create(LayoutInflater layoutInflater, ViewGroup parent);
}
