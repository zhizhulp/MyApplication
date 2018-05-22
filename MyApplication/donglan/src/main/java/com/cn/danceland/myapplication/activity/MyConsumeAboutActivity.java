package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.MyConSumeBean;
import com.cn.danceland.myapplication.bean.MyConsumeCon;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.TimeUtils;
import com.cn.danceland.myapplication.view.DongLanTitleView;
import com.cn.danceland.myapplication.view.XCRoundRectImageView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2018/5/9.
 */

public class MyConsumeAboutActivity extends Activity {

    Gson gson;
    String root_opt_no;
    ListView lv_consume;
    DongLanTitleView consume_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myconsume);
        initHost();
        initView();
        initData();
    }

    private void initHost() {

        root_opt_no = getIntent().getStringExtra("root_opt_no");

        gson = new Gson();

    }

    private void initView(){
        lv_consume = findViewById(R.id.lv_consume);
        consume_title = findViewById(R.id.consume_title);
        consume_title.setTitle("相关订单");
    }

    private void initData() {
        MyConsumeCon myConsumeCon = new MyConsumeCon();
        myConsumeCon.setPage(0);
        myConsumeCon.setSize(15);
        myConsumeCon.setRoot_opt_no(root_opt_no);
        String s = gson.toJson(myConsumeCon);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.MYCONSUME, s,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.i(jsonObject.toString());
                MyConSumeBean myConSumeBean = gson.fromJson(jsonObject.toString(), MyConSumeBean.class);
                if(myConSumeBean!=null && myConSumeBean.getData()!=null&&myConSumeBean.getData().getContent()!=null){
                    List<MyConSumeBean.Content> content = myConSumeBean.getData().getContent();
                    lv_consume.setAdapter(new ConsumeAdapter(content));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.i(volleyError.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
                return map;
            }

        };

        MyApplication.getHttpQueues().add(jsonObjectRequest);

    }


    private class ConsumeAdapter extends BaseAdapter {

        List<MyConSumeBean.Content> content;

        public ConsumeAdapter(List<MyConSumeBean.Content> content) {
            this.content = content;
        }

        @Override
        public int getCount() {
            return content.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView==null){
                viewHolder = new ViewHolder();
                convertView = View.inflate(MyConsumeAboutActivity.this, R.layout.item_myconsume, null);
                viewHolder.xc_img = convertView.findViewById(R.id.xc_img);
                viewHolder.tv_type = convertView.findViewById(R.id.tv_type);
                viewHolder.tv_name = convertView.findViewById(R.id.tv_name);
                viewHolder.tv_platform = convertView.findViewById(R.id.tv_platform);
                viewHolder.tv_price = convertView.findViewById(R.id.tv_price);
                viewHolder.tv_status = convertView.findViewById(R.id.tv_status);
                viewHolder.tv_time = convertView.findViewById(R.id.tv_time);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }
            MyConSumeBean.Content contentItem = content.get(position);

            switch (contentItem.getPay_way()){
                case "1":
                    break;
                case "2":
                    Glide.with(MyConsumeAboutActivity.this).load(R.drawable.alipay_logo).into(viewHolder.xc_img);
                    break;
                case "3":
                    Glide.with(MyConsumeAboutActivity.this).load(R.drawable.wechat_logo).into(viewHolder.xc_img);
                    break;
                case "4":
                    break;
                case "5":
                    Glide.with(MyConsumeAboutActivity.this).load(R.drawable.img_dl_logo).into(viewHolder.xc_img);
                    break;
            }

            viewHolder.tv_type.setText(contentItem.getProduct_type());
            if(contentItem.getProduct_name()!=null){
                viewHolder.tv_name.setText(contentItem.getProduct_name());
            }else{
                viewHolder.tv_name.setText("");
            }

            if("3".equals(contentItem.getPlatform())){
                viewHolder.tv_platform.setText("PC端");
            }else if( "1".equals(contentItem.getPlatform())|| "2".equals(contentItem.getPlatform())){
                viewHolder.tv_platform.setText("App端");
            }else if ("4".equals(contentItem.getPlatform())){
                viewHolder.tv_platform.setText("微信公众号");
            }

            viewHolder.tv_price.setText("金额: " + contentItem.getReceive()+"元");
            viewHolder.tv_status.setTextColor(Color.parseColor("#ff000000"));
            switch (contentItem.getStatus()){
                case "1":
                    viewHolder.tv_status.setText("未支付");
                    viewHolder.tv_status.setTextColor(Color.parseColor("#ff6600"));
                    break;
                case "2":
                    viewHolder.tv_status.setText("未发货");
                    break;
                case "3":
                    viewHolder.tv_status.setText("已完成");
                    break;
                case "4":
                    viewHolder.tv_status.setText("已取消");
                    break;
                case "5":
                    viewHolder.tv_status.setText("已退");
                    break;

            }

            String payTime = TimeUtils.timeStamp2Date(contentItem.getPay_time(), "yyyy-MM-dd HH:mm:ss");
            viewHolder.tv_time.setText(contentItem.getOrder_time());


            return convertView;
        }
    }

    private class ViewHolder{
        XCRoundRectImageView xc_img;
        TextView tv_type,tv_name,tv_platform,tv_price,tv_status,tv_time;
    }

}