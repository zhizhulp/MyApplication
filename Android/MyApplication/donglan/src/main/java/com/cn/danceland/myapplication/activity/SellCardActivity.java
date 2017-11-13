package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.danceland.myapplication.R;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;
import com.shehabic.droppy.animations.DroppyFadeInAnimation;

/**
 * Created by shy on 2017/11/2 16:37
 * Email:644563767@qq.com
 */


public class SellCardActivity extends Activity implements View.OnClickListener {

    private String[] names = {"黄金年卡", "白金年卡", "钻石年卡"};
    private ImageView iv_fenlie;
    private LinearLayout ll_fenlie;
    private TextView tv_tiltle;
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_card);
        initView();
    }

    private void initView() {
        listView = findViewById(R.id.listview);
        tv_tiltle = findViewById(R.id.tv_tiltle);
        findViewById(R.id.iv_back).setOnClickListener(this);

        ll_fenlie =
                findViewById(R.id.ll_fenlie);
        ll_fenlie.setOnClickListener(this);
        iv_fenlie = findViewById(R.id.iv_fenlie);
        listView.setAdapter(new MyListAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(SellCardActivity.this, SellCardConfirmActivity.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fenlie://分类
                initDroppyMenu(ll_fenlie);
                showDroppyMenu();
                break;

            case R.id.iv_back://返回
                finish();
                break;
            default:
                break;
        }
    }

    public class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int i) {
            return names[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            //         LayoutInflater.from(SellCardActivity.this).inflate( R.layout.listview_item_club_card, null);
            view = LayoutInflater.from(SellCardActivity.this).inflate(R.layout.listview_item_club_card, null);


            return view;
        }
    }

    private String[] data = new String[]{"全部", "年卡", "季卡", "月卡", "限时活动"};

    //弹出下拉框
    protected void showDroppyMenu() {

        iv_fenlie.setImageResource(R.drawable.img_down);
//        ListView listView = (ListView) droppyMenu.getMenuView().findViewById(R.id.listview);
//        listView.setAdapter(new MyAdapter());
        droppyMenu.show();
    }

    DroppyMenuPopup droppyMenu;


    public class MyAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = View.inflate(SellCardActivity.this, R.layout.listview_item_droppy, null);
            TextView textView = view.findViewById(R.id.text1);
            textView.setText(data[i]);

            return view;
        }
    }

    //绑定下拉框
    private void initDroppyMenu(View btn) {
        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(SellCardActivity.this, btn);

        for (int j = 0; j < data.length; j++) {

            droppyBuilder.addMenuItem(new DroppyMenuItem(data[j]))
                    .addSeparator();
        }



        droppyBuilder
                .setOnDismissCallback(new DroppyMenuPopup.OnDismissCallback() {
                    @Override
                    public void call() {
                        iv_fenlie.setImageResource(R.drawable.img_up);
                    }
                })
                .setOnClick(new DroppyClickCallbackInterface() {
                    @Override
                    public void call(View v, int id) {
                        iv_fenlie.setImageResource(R.drawable.img_up);
                        tv_tiltle.setText(data[id]);

                    }
                })
                .setPopupAnimation(new DroppyFadeInAnimation())
                .triggerOnAnchorClick(false);


        droppyMenu = droppyBuilder.build();
    }
}
