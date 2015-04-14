package com.henry.AccountApplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import utils.MySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by love on 2015/4/10.
 */
public class IncomingActivity extends Activity {
    private MySQLiteOpenHelper dbHelper;
    @ViewInject(R.id.listView_incoming)
    private ListView listView_incoming;
    @ViewInject(R.id.button_incoming)
    private Button button_incoming;
    @ViewInject(R.id.textView_incoming_empty)
    private TextView textView_incoming_empty;
    private List<Map<String, String>> totalList;
    private SimpleAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming);

        //初始化
        initIncoming();
        this.registerForContextMenu(listView_incoming);
        reloadListViewData();
        //更新或删除数据
        //updateOrDeleteData();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.incoming, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_incoming:
                updateOrDeleteData();
                Toast.makeText(this,"haha",Toast.LENGTH_LONG).show();
                break;
            case R.id.action_update_incoming:
                Toast.makeText(this,"heihei",Toast.LENGTH_LONG).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * 更新或删除数据
     */
    private void updateOrDeleteData() {
        listView_incoming.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                List<Map<String, String>> list = dbHelper.selectList("select salary,extra_money from incoming where _id=?",
                        new String[]{position + ""});
                if (list.size()>=1){
                    dbHelper.selectList("delete from incoming where _id=?", new String[]{list + ""});
                    Toast.makeText(IncomingActivity.this,"删除数据成功",Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
    }

    /**
     * 刷新数据
     */
    private void reloadListViewData() {
        List<Map<String, String>> list = dbHelper.selectList("select salary,extra_money from incoming", null);
        if (list != null) {
            totalList.clear();
            totalList.addAll(list);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 初始化
     */
    private void initIncoming() {
        dbHelper = new MySQLiteOpenHelper(this);
        ViewUtils.inject(this);
        totalList = new ArrayList<Map<String, String>>();

        adapter = new SimpleAdapter(this, totalList, R.layout.item_listview_incoming,
                new String[]{"salary", "extra_money"},
                new int[]{R.id.textView_listView_salary, R.id.textView_listView_extraMoney});
        listView_incoming.setAdapter(adapter);
        listView_incoming.setEmptyView(textView_incoming_empty);

    }

    @OnClick(R.id.button_incoming)
    public void btnIncomingOnClick(View view) {
        incoming();
    }

    /**
     * 收入
     */
    public void incoming() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("收入信息");
        View view1 = getLayoutInflater().inflate(R.layout.item_dialog_incoming, null);
        final EditText editText_salary = (EditText) view1.findViewById(R.id.editText_salary);
        final EditText editText_extra = (EditText) view1.findViewById(R.id.editText_extra);
        builder.setView(view1);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String salary = editText_salary.getText().toString();
                String extra = editText_extra.getText().toString();
                if (TextUtils.isEmpty(salary) && TextUtils.isEmpty(extra)) {
                    Toast.makeText(getApplicationContext(), "您输入的信息为空", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    dbHelper.execData("insert into incoming(salary,extra_money) values (?,?)",
                            new String[]{"工资：" + salary + "￥", "外快：" + extra + "￥"});
                    reloadListViewData();
                }
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }
}