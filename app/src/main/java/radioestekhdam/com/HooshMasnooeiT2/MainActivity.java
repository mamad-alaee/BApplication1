package radioestekhdam.com.HooshMasnooeiT2;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.blox.treeview.BaseTreeAdapter;
import de.blox.treeview.TreeNode;
import de.blox.treeview.TreeView;

public class MainActivity extends AppCompatActivity {

    private TreeNode rootNode;
    private TreeNode tempNode;
    private BaseTreeAdapter treeAdapter;
    private int nodeCount = 0;
    private boolean nodeFind = false;
    private String str_nodeFind = "";
    private List<TreeNode> treeNodes;

    private BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout bs_node_add;
    private LinearLayout bs_node_search;
    private EditText et_nodeName;
    private Button btn_Previous;
    private Button btn_add_node;
    private Button btn_search_show;
    private TextView tv_info_parent;
    private TextView tv_parent;
    private TextView tv_info_dfs;
    private TextView tv_result_dfs;
    private TextView tv_info_bfs;
    private TextView tv_result_bfs;
    private CheckBox chb_dfs;
    private CheckBox chb_bfs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TreeView treeView = findViewById(R.id.tree);
        treeAdapter = new BaseTreeAdapter<ViewHolder>(this, R.layout.node) {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(View view) {
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(ViewHolder viewHolder, Object data, int position) {
                if (str_nodeFind.equals(data.toString())) {
                    viewHolder.mTextView.setTextColor(Color.GREEN);
                }

                viewHolder.mTextView.setText(data.toString());
            }
        };
        treeView.setAdapter(treeAdapter);

        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        bs_node_add = (LinearLayout) findViewById(R.id.bs_node_add);
        bs_node_search = (LinearLayout) findViewById(R.id.bs_node_search);

        et_nodeName = (EditText) findViewById(R.id.et_nodeName);
        tv_info_parent = (TextView) findViewById(R.id.tv_info_parent);
        tv_parent = (TextView) findViewById(R.id.tv_parent);

        tv_result_dfs = (TextView) findViewById(R.id.tv_result_dfs);
        tv_info_dfs = (TextView) findViewById(R.id.tv_info_dfs);
        tv_result_bfs = (TextView) findViewById(R.id.tv_result_bfs);
        tv_info_bfs = (TextView) findViewById(R.id.tv_info_bfs);

        chb_dfs = (CheckBox) findViewById(R.id.chb_dfs);
        chb_bfs = (CheckBox) findViewById(R.id.chb_bfs);

        btn_add_node = (Button) findViewById(R.id.btn_add_node);
        btn_Previous = (Button) findViewById(R.id.btn_Previous);

        btn_search_show = (Button) findViewById(R.id.btn_search_show);
        btn_search_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bs_node_add.setVisibility(View.GONE);
                bs_node_search.setVisibility(View.VISIBLE);
            }
        });

        Button btn_search_close = (Button) findViewById(R.id.btn_search_close);
        btn_search_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bs_node_add.setVisibility(View.VISIBLE);
                bs_node_search.setVisibility(View.GONE);

                tv_info_dfs.setVisibility(View.GONE);
                tv_result_dfs.setVisibility(View.GONE);
                tv_info_bfs.setVisibility(View.GONE);
                tv_result_bfs.setVisibility(View.GONE);
            }
        });

        btn_add_node.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txt_EditText = et_nodeName.getText().toString();

                if (!txt_EditText.equals("")) {

                    if (rootNode == null) {
                        rootNode = new TreeNode(txt_EditText);
                        treeAdapter.setRootNode(rootNode);
                        btn_search_show.setEnabled(true);
                        tv_info_parent.setVisibility(View.VISIBLE);
                        tv_parent.setVisibility(View.VISIBLE);

                    } else {
                        TreeNode newNode = new TreeNode(et_nodeName.getText().toString());
                        rootNode.addChild(newNode);
                        rootNode = newNode;
                        if (!btn_Previous.isEnabled()) {
                            btn_Previous.setEnabled(true);
                        }
                    }

                    tv_parent.setText(txt_EditText);
                    et_nodeName.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "نام گره را وارد کنید!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rootNode.hasParent()) {
                    rootNode = rootNode.getParent();
                    tv_parent.setText(rootNode.getData().toString());
                }
            }
        });

        final Button btn_search_go = (Button) findViewById(R.id.btn_search_go);
        btn_search_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_nodeFind = et_nodeName.getText().toString();

                if (!str_nodeFind.equals("")) {

                    btn_search_go.setEnabled(false);

                    tempNode = rootNode;

                    for (int i = 1; i > 0; i++) {
                        if (tempNode.hasParent()) {
                            tempNode = tempNode.getParent();
                        }else {
                            break;
                        }
                    }

                    if (chb_dfs.isChecked()) {

                        nodeCount = 0;
                        nodeFind = false;
                        dfs(tempNode);
                        //Log.e("Mammad" , "it nodeCount dfs>> " + nodeCount);

                        tv_info_dfs.setVisibility(View.VISIBLE);
                        tv_result_dfs.setVisibility(View.VISIBLE);

                        tv_result_dfs.setText(String.valueOf(nodeCount));
                    }else {
                        tv_info_dfs.setVisibility(View.GONE);
                        tv_result_dfs.setVisibility(View.GONE);
                    }

                    if (chb_bfs.isChecked()) {

                        nodeCount = 0;
                        nodeFind = false;
                        bfs(tempNode);
                        //Log.e("Mammad" , "it nodeCount bfs>> " + nodeCount);

                        tv_info_bfs.setVisibility(View.VISIBLE);
                        tv_result_bfs.setVisibility(View.VISIBLE);

                        tv_result_bfs.setText(String.valueOf(nodeCount));
                    }else {
                        tv_info_bfs.setVisibility(View.GONE);
                        tv_result_bfs.setVisibility(View.GONE);
                    }

                    treeAdapter.notifyDataChanged(tempNode);
                    btn_search_go.setEnabled(true);
                    et_nodeName.setText("");

                } else {
                    Toast.makeText(MainActivity.this, "نام گره را وارد کنید!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //tree
        /*TreeNode rootNode = new TreeNode(1);

        final TreeNode child2 = new TreeNode(2);
        child2.addChild(new TreeNode(4));

        final TreeNode child2_2 = new TreeNode(5);
        child2_2.addChild(new TreeNode(8));
        final TreeNode child2_2_2 = new TreeNode(9);
        child2_2_2.addChild(new TreeNode(10));
        child2_2_2.addChild(new TreeNode(11));
        child2_2.addChild(child2_2_2);

        child2.addChild(child2_2);

        rootNode.addChild(child2);

        final TreeNode child3 = new TreeNode(3);
        child3.addChild(new TreeNode(6));
        child3.addChild(new TreeNode(7));
        rootNode.addChild(child3);

        treeAdapter.setRootNode(rootNode);*/

    }

    private void dfs(TreeNode nodes){
        //Log.e("Mammad" , "it >> " + nodes.getData().toString());
        nodeCount++;
        if (str_nodeFind.equals(nodes.getData().toString())) {
            nodeFind = true;
        } else {
            if (!nodes.getChildren().isEmpty() && nodes.getChildren() != null) {
                for (int i = 0; i < nodes.getChildren().size(); i++) {
                    if (!nodeFind)
                        dfs(nodes.getChildren().get(i));
                }
            }
        }
    }

    private void bfs(TreeNode nodes){
        if (!nodeFind(nodes)) {
            treeNodes = new ArrayList<>();
            nodeFor(nodes);
            nodeForChildren();
        }
    }
    private boolean nodeFind(TreeNode nodes){
        nodeCount++;
        //Log.e("Mammad" , "it >> " + nodes.getData().toString());
        if (str_nodeFind.equals(nodes.getData().toString())) {
            //Log.e("Mammad" , "it >> true");
            return nodeFind= true;
        }
        //Log.e("Mammad" , "it >> false");
        return nodeFind= false;
    }

    private void nodeFor(TreeNode nodes){
        if (!nodes.getChildren().isEmpty() && nodes.getChildren() != null) {
            for (int i = 0; i < nodes.getChildren().size(); i++) {
                if (nodeFind(nodes.getChildren().get(i))) {
                    treeNodes.clear();
                    break;
                }else {
                    //Log.e("Mammad" , "it >> add");
                    treeNodes.add(nodes.getChildren().get(i));
                }
            }
        }
    }

    private void nodeForChildren(){
        if (treeNodes != null && !treeNodes.isEmpty()){
            for (int i = 0; i < treeNodes.size(); i++) {
                //Log.e("Mammad" , "it >> Get nodeFor");
                nodeFor(treeNodes.get(i));
            }
        }
    }

}
