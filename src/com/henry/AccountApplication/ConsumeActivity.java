package com.henry.AccountApplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import utils.MySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by love on 2015/4/10.
 */
public class ConsumeActivity extends Activity {

    private MySQLiteOpenHelper dbHelper;
    @ViewInject(R.id.listView_consume)
    private ListView listView_consume;

    String[][] arrConsumeDetailEn = {
            {"eat_daily_cost", "eat_treat", "eat_tobacco_alcohol"},//日常花销，请客，烟酒
            {"wear_yourself", "wear_gift"},//自用，礼物
            {"live_rent", "live_utilities"},//房租，水电费
            {"route_bus", "route_taxi"},//公交，出租车
            {"use_study", "use_life"},//学习生活
    };
    String[][] arrConsumeDetailCn = {
            {"日常花销", "请客", "烟酒"},//日常花销，请客，烟酒
            {"自用", "礼物"},//自用，礼物
            {"房租", "水电费"},//房租，水电费
            {"公交", "出租车"},//公交，出租车
            {"学习", "生活"},//学习 生活
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consume);
        dbHelper = new MySQLiteOpenHelper(this);
        ViewUtils.inject(this);
        consume();
    }

    /**
     * 消费
     */
    public void consume() {

        final String[] arrConsume = {"吃", "穿", "住", "行", "用"};
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (int i = 0; i < arrConsume.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("textView_title", arrConsume[i]);
            map.put("textView_totalCost", 0 + "元");
            list.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(),
                list,
                R.layout.addconsumeinfo, new String[]{"textView_title", "textView_totalCost"},
                new int[]{R.id.textView_title, R.id.textView_totalCost});
        listView_consume.setAdapter(adapter);
        listView_consume.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ConsumeActivity.this);
                builder.setTitle(arrConsume[position]);
                //复用了布局文件
                View view1 = getLayoutInflater().inflate(R.layout.item_dialog_incoming, null);
                final EditText editText_salary = (EditText) view1.findViewById(R.id.editText_salary);
                editText_salary.setHint(arrConsumeDetailCn[position][0]);
                final EditText editText_extra = (EditText) view1.findViewById(R.id.editText_extra);
                editText_extra.setHint(arrConsumeDetailCn[position][1]);
                builder.setView(view1);

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String salary = editText_salary.getText().toString();
                        String extra = editText_extra.getText().toString();
                        if (TextUtils.isEmpty(salary) && TextUtils.isEmpty(extra)) {
                            Toast.makeText(ConsumeActivity.this, "您输入的信息为空", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            Log.i("haha-----------------", salary + ":" + extra);
                            Log.i("haha-----------------", arrConsumeDetailEn[position][0] + ":" + arrConsumeDetailEn[position][1]);
                            Log.i("haha-----------------", "insert into consumeDetail(" + arrConsumeDetailEn[position][0] + ',' + arrConsumeDetailEn[position][1] + ") values (?,?)");
                            Toast.makeText(ConsumeActivity.this, "成功插入到数据库" + arrConsumeDetailEn[position][0] + arrConsumeDetailEn[position][1], Toast.LENGTH_LONG).show();
                            boolean flag = dbHelper.execData("insert into consumeDetail(" + arrConsumeDetailEn[position][0] + ',' + arrConsumeDetailEn[position][1] + ") values (?,?)", new String[]{salary, extra});

                            if (flag) {
                                Log.i("haha-----------------", "成功");
                            }
                        }
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });
    }
}