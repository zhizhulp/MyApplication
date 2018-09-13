package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.BCAQuestionBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.view.DongLanTitleView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 体测分析-注意事项
 * <p>
 * Created by yxx on 2018-09-13.
 */

public class FitnessTestNoticeActivity extends Activity {
    private Context context;
    private DongLanTitleView title;//数据title
    private ListView listView;
    private Button btn_start;

    private ListAdapter adapter;

    private List<BCAQuestionBean.Data> dataList = new ArrayList<>();
    private String personId;
    private String memberId;
    private String member_no;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_test_notice);
        context = this;
        initHost();
        initView();
    }

    private void initHost() {
        personId = getIntent().getStringExtra("id");
        memberId = getIntent().getStringExtra("memberId");
        member_no = getIntent().getStringExtra("member_no");
    }

    private void initView() {
        title = findViewById(R.id.shouhuan_title);
        title.setTitle(context.getResources().getString(R.string.matters_needing_attention_text));
        listView = findViewById(R.id.listView);
        btn_start = findViewById(R.id.btn_start);
        adapter = new ListAdapter();
        listView.setAdapter(adapter);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BodyBaseActivity.class);
                intent.putExtra("id", personId);
                intent.putExtra("memberId", memberId);
                intent.putExtra("member_no", member_no);
                startActivity(intent);
            }
        });
        getData();
    }

    public void getData() {
        BCAQuestionBean.Data bean = new BCAQuestionBean.Data();
        bean.setType("7");// 7：体测分析-体能须知
        LogUtil.i("请求后台心率" + bean.toString());
        //获取后台数据
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.QUERY_BCAQUESTION_LIST
                , new Gson().toJson(bean), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.i("jsonObject" + jsonObject.toString());
                if (jsonObject.toString().contains("true")) {
                    BCAQuestionBean beanGson = new Gson().fromJson(jsonObject.toString(), BCAQuestionBean.class);
                    List<BCAQuestionBean.Data> data = beanGson.getData();
                    if (data != null && data.size() != 0) {
                        LogUtil.i("data.size()" + data.size());
                        dataList.addAll(data);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.showToastShort("请查看网络连接");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToastShort(context.getResources().getText(R.string.network_connection_text).toString());
                LogUtil.e("onErrorResponse", volleyError.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
                return map;
            }
        };
        MyApplication.getHttpQueues().add(request);
    }

    class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            ViewHolder vh = null;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = View.inflate(context, R.layout.fitness_test_notice_item, null);
                vh.title_tv = convertView.findViewById(R.id.title_tv);
                vh.options_layout = convertView.findViewById(R.id.options_layout);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.title_tv.setText( dataList.get(position).getOrder_no() + "." + dataList.get(position).getCentent());
            for (int i = 0; i < dataList.get(position).getOptions().size(); i++) {
                TextView textView = new TextView(context);
                //这里的Textview的父layout是ListView，所以要用ListView.LayoutParams
                ListView.LayoutParams layoutParams = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(layoutParams);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                textView.setPadding(0, 10, 0, 10);
                textView.setText(dataList.get(position).getOptions().get(i).getOrder_no().toString() + "." + dataList.get(position).getOptions().get(i).getTitle().toString());
                textView.setTextColor(context.getResources().getColor(R.color.colorGray8));
                textView.setTextSize(14);
                vh.options_layout.addView(textView);
            }
            return convertView;
        }

        class ViewHolder {
            public TextView title_tv;
            public LinearLayout options_layout;
        }
    }
}
