package tellh.com.recyclertreeview.viewbinder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import tellh.com.recyclertreeview.R;
import tellh.com.recyclertreeview.bean.Department;
import tellh.com.recyclertreeview_lib.BaseViewHolder;
import tellh.com.recyclertreeview_lib.ViewHolderFactory;

public class DepartmentViewHolderFactory extends ViewHolderFactory<Department> {
    @Override
    public BaseViewHolder<Department> create(LayoutInflater layoutInflater, ViewGroup parent) {
        return new DepartmentViewHolder(layoutInflater.inflate(R.layout.item_department, parent, false));
    }

}
