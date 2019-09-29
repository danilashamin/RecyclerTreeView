package tellh.com.recyclertreeview;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tellh.com.recyclertreeview.bean.Department;
import tellh.com.recyclertreeview.viewbinder.DepartmentViewHolderFactory;
import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;
    private TreeViewAdapter<Department> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {

        List<TreeNode<Department>> departments = new ArrayList<>();

        TreeNode<Department> firstRoot = new TreeNode<>(Department.create());
        TreeNode<Department> firstChild = new TreeNode<>(Department.create());
        TreeNode<Department> secondChild = new TreeNode<>(Department.create());
        firstRoot.addChild(firstChild);
        firstRoot.addChild(secondChild);
        firstChild.setChildList(Arrays.asList(new TreeNode<>(Department.create()),new TreeNode<>(Department.create()),new TreeNode<>(Department.create())));
        secondChild.setChildList(Arrays.asList(new TreeNode<>(Department.create()),new TreeNode<>(Department.create()),new TreeNode<>(Department.create())));

        TreeNode<Department> secondRoot = new TreeNode<>(Department.create());
        TreeNode<Department> thirdChild = new TreeNode<>(Department.create());
        TreeNode<Department> fourthChild = new TreeNode<>(Department.create());
        secondRoot.addChild(firstChild);
        secondRoot.addChild(secondChild);
        thirdChild.setChildList(Arrays.asList(new TreeNode<>(Department.create()),new TreeNode<>(Department.create()),new TreeNode<>(Department.create())));
        fourthChild.setChildList(Arrays.asList(new TreeNode<>(Department.create()),new TreeNode<>(Department.create()),new TreeNode<>(Department.create())));

        departments.add(firstRoot);
        departments.add(secondRoot);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TreeViewAdapter<>(new DepartmentViewHolderFactory(), departments);
        // whether collapse child nodes when their parent node was close.
//        adapter.ifCollapseChildWhileCollapseParent(true);
        adapter.ifCollapseChildWhileCollapseParent(true);
        adapter.setOnTreeNodeListener(new TreeViewAdapter.OnTreeNodeListener() {
            @Override
            public void onClick(TreeNode node) {

            }

        });
        rv.setAdapter(adapter);
    }

    private void initView() {
        rv = findViewById(R.id.rv);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.id_action_close_all:
                adapter.collapseAll();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
