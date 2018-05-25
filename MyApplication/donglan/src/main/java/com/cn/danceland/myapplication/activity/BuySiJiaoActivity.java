package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import com.cn.danceland.myapplication.bean.BuySiJiaoBean;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.FindSiJiaoBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.view.XCRoundRectImageView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2018/1/15.
 */

public class BuySiJiaoActivity extends Activity implements AbsListView.OnScrollListener{
    ListView lv_sijiaocard;
    ImageView buy_img;
    Gson gson;
    FindSiJiaoBean findSiJiaoBean;
    List<BuySiJiaoBean.Content> content;

    Data info;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buysijiao);
        initHost();
        intiView();
    }

    private void initHost() {
        info = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
        gson = new Gson();


    }

    private int lastVisibleItem;//最后一个可见的item
    private int totalItemCount;//总的item
    int page,totalPages,totalElements;
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.lastVisibleItem = firstVisibleItem + visibleItemCount;
        this.totalItemCount = totalItemCount;
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(lastVisibleItem == totalItemCount && scrollState==SCROLL_STATE_IDLE && page<=totalPages&&totalItemCount<totalElements){
            getData(page);
        }
    }

    private void getData(int nowPage) {
        findSiJiaoBean = new FindSiJiaoBean();
        findSiJiaoBean.setPage(nowPage);
        findSiJiaoBean.setSize(15);
        findSiJiaoBean.setBranch_id(Integer.valueOf(info.getPerson().getDefault_branch()));
        String s = gson.toJson(findSiJiaoBean);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.COURSETYPELIST,s, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if(jsonObject!=null){
                    LogUtil.e("zzf",jsonObject.toString());
                    String string = jsonObject.toString();
                    BuySiJiaoBean buySiJiaoBean = gson.fromJson(string, BuySiJiaoBean.class);
                    if(buySiJiaoBean!=null){
                        totalPages = buySiJiaoBean.getData().getTotalPages();
                        totalElements = buySiJiaoBean.getData().getTotalElements();
                        content.addAll(buySiJiaoBean.getData().getContent());
                        if(content!=null){
                            myAdapter.notifyDataSetChanged();
                            page++;
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("zzf",volleyError.toString());

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN,""));
                return map;
            }
        };

        MyApplication.getHttpQueues().add(jsonObjectRequest);

    }


    private void intiView() {

        content = new ArrayList<>();

        lv_sijiaocard = findViewById(R.id.lv_sijiaocard);
        lv_sijiaocard.setOnScrollListener(this);

        myAdapter = new MyAdapter(content);
        lv_sijiaocard.setAdapter(myAdapter);

        lv_sijiaocard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(content!=null){
                    BuySiJiaoBean.Content itemContent = BuySiJiaoActivity.this.content.get(position);
                    startActivity(new Intent(BuySiJiaoActivity.this,SellSiJiaoConfirmActivity.class).putExtra("itemContent",itemContent));
                }

            }
        });

        buy_img = findViewById(R.id.buy_img);
        buy_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getData(page);


    }

    private class MyAdapter extends BaseAdapter{

        List<BuySiJiaoBean.Content> contentList;

        MyAdapter(List<BuySiJiaoBean.Content> contentList){
            this.contentList = contentList;

        }


        @Override
        public int getCount() {
            return contentList.size();
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
                convertView = LayoutInflater.from(BuySiJiaoActivity.this).inflate(R.layout.sijiaocard, null);
                viewHolder.card_img_1 = convertView.findViewById(R.id.card_img_1);
                viewHolder.sijiao_name = convertView.findViewById(R.id.sijiao_name);
                viewHolder.sijiao_type = convertView.findViewById(R.id.sijiao_type);
                viewHolder.sijiao_amount = convertView.findViewById(R.id.sijiao_amount);
                viewHolder.sijiao_price = convertView.findViewById(R.id.sijiao_price);

                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }
            Glide.with(BuySiJiaoActivity.this).load(contentList.get(position).getImg_url()).into(viewHolder.card_img_1);
            viewHolder.sijiao_name.setText(contentList.get(position).getName());
            viewHolder.sijiao_type.setText("课程类型："+contentList.get(position).getCourse_category_name());
            viewHolder.sijiao_amount.setText("课程节数："+contentList.get(position).getCount()+"节");
            viewHolder.sijiao_price.setText("￥："+contentList.get(position).getPrice()+"元");
            viewHolder.sijiao_price.setTextColor(getResources().getColor(R.color.color_dl_yellow));
            return convertView;
        }
    }

    class ViewHolder{
        XCRoundRectImageView card_img_1;
        TextView sijiao_name,sijiao_type,sijiao_amount,sijiao_price;

    }
}
