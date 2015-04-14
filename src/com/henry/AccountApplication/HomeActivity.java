package com.henry.AccountApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.ericssonlabs.BarCodeTestActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import utils.MySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends Activity {
    @ViewInject(R.id.listView_home)
    private ListView listView_home;
    @ViewInject(R.id.textView_emptyinfo_home)
    private TextView textView_emptyinfo_home;

    private MySQLiteOpenHelper dbHelper;
    private List<Map<String, String>> totalList;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ViewUtils.inject(this);


        totalList = new ArrayList<Map<String, String>>();
        String[] arrTime = {"今天", "本周", "本月", "本年"};
        for (int i = 0; i < 4; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("time", arrTime[i]);
            map.put("incoming", "100");
            map.put("createTime", "2015");
            map.put("consume", "200");
            totalList.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, totalList, R.layout.item_listview_home,
                new String[]{"time", "incoming", "createTime", "consume"},
                new int[]{R.id.textView_time_home, R.id.textView_incoming_home, R.id.textView_createTime_home, R.id.textView_consume_home}
        );
        listView_home.setAdapter(adapter);
        listView_home.setEmptyView(textView_emptyinfo_home);
        listView_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //进入详情页
                Intent intent = new Intent();
                intent.setClass(HomeActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("time", totalList.get(position).get("time"));
                intent.replaceExtras(bundle);
                startActivity(intent);
            }
        });

        //初始化数据库
        initDB();

//        ActionBar actionBar = this.getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);


        CountPercentView countPercentView = (CountPercentView) findViewById(R.id.countPercentView);
        double[] arrPercent = {0.48, 0.42, 0.08, 0.02};//android ios heimei other
        countPercentView.setArrPercent(arrPercent);
    }

    /**
     * 初始化数据库
     */
    private void initDB() {
        //创建数据库
        dbHelper = new MySQLiteOpenHelper(this);

        //收入表的创建,工资和外快
        String incomingSQL = "create table if not exists incoming(_id integer primary key autoincrement,salary,extra_money)";
        //使用数据库对象创建表
        dbHelper.dbConn.execSQL(incomingSQL);
        //支出表的创建，吃穿住行用
        String consumeSQL = "create table if not exists consume(consume_id integer primary key autoincrement," +
                // "consumeDetail_id ,"+
                // "foreign key(consumeDetail_id) references consumeDetail(_id),"+//外键
                "eat,wear,live,route,use)";
        //使用数据库对象创建表
        dbHelper.dbConn.execSQL(consumeSQL);
        //支出详细表的创建，吃穿住行用的具体内容
        String consumeDetailSQL = "create table if not exists consumeDetail(_id integer primary key autoincrement," +
                "consume_id," +
                "eat_daily_cost ,eat_treat,eat_tobacco_alcohol," +//日常花销，请客，烟酒
                " wear_yourself,wear_gift," +//自用，礼物
                "live_rent,live_utilities," +//房租，水电费
                "route_bus,route_taxi," +//公交，出租车
                "use_study,use_life)";//学习，生活
        //使用数据库对象创建表
        dbHelper.dbConn.execSQL(consumeDetailSQL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.action_rectPic:

                intent.setClass(this, RectViewActivity.class);
                startActivity(intent);
                break;
            case R.id.action_consume:
                intent.setClass(this, ConsumeActivity.class);
                startActivity(intent);
                break;
            case R.id.action_incoming:
                intent.setClass(this, IncomingActivity.class);
                startActivity(intent);
                break;
            case R.id.action_barcode:
                intent.setClass(this, BarCodeTestActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
