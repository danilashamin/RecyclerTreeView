package tellh.com.recyclertreeview.viewbinder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import tellh.com.recyclertreeview.DepartmentStructureView;
import tellh.com.recyclertreeview.R;
import tellh.com.recyclertreeview.bean.Department;
import tellh.com.recyclertreeview_lib.BaseViewHolder;
import tellh.com.recyclertreeview_lib.TreeNode;

public class DepartmentViewHolder extends BaseViewHolder<Department> {

    private AppCompatTextView tvDepartmentName;
    private AppCompatTextView tvEmployeeCount;
    private AppCompatImageView btnExpand;

    public DepartmentViewHolder(@NonNull View itemView) {
        super(itemView);
        tvDepartmentName = findViewById(R.id.tvDepartmentName);
        tvEmployeeCount = findViewById(R.id.tvEmployeeCount);
        btnExpand = findViewById(R.id.expandableButton);
    }

    @Override
    public void bindView(int position, TreeNode<Department> node) {
        Department department = node.getData();
        tvDepartmentName.setText(department.getDepartmentName());
        tvEmployeeCount.setText(department.getEmployeeCount());

        int height = node.getHeight();
        for (int i = 0; i < height; i++) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            DepartmentStructureView departmentStructureView = new DepartmentStructureView(getContext());
            departmentStructureView.setLayoutParams(lp);
            ((ViewGroup)itemView).addView(departmentStructureView, i);
        }

    }

    @NonNull
    @Override
    public View getExpandableButton() {
        return btnExpand;
    }

    @Nullable
    @Override
    public View getCollapseParentButton() {
        return null;
    }

    @Nullable
    @Override
    public ImageView getCollapseParentArrow() {
        return null;
    }

    @Nullable
    @Override
    public View getClickableView() {
        return itemView;
    }

    @Nullable
    @Override
    public ImageView getExpandableArrow() {
        return btnExpand;
    }
}
