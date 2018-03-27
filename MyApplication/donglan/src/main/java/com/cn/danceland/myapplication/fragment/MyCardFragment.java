package com.cn.danceland.myapplication.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.activity.MyQRCodeActivity;
import com.cn.danceland.myapplication.bean.RequestMyCardListBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cn.danceland.myapplication.R.id.tv_cardtype;

/**
 * Created by shy on 2018/3/22 09:16
 * Email:644563767@qq.com
 */


public class MyCardFragment extends BaseFragment {
    private ListView mListView;
    private List<RequestMyCardListBean.Data> mCardList = new ArrayList<>();
    private MyListViewAdapter myListViewAdapter;
    Gson gson = new Gson();
    @Override
    public View initViews() {
        View v=View.inflate(mActivity,R.layout.fragment_my_card,null);

        mListView = v.findViewById(R.id.listview);
        myListViewAdapter = new MyListViewAdapter();
        mListView.setAdapter(myListViewAdapter);
        return  v;

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void initDta() {
        findAllCard();
    }

    /**
     * 查找全部会员卡
     */
    private void findAllCard() {

        StringRequest request = new StringRequest(Request.Method.GET, Constants.FIND_ALL_MY_CARD_LIST, new Response.Listener<String>() {


            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                RequestMyCardListBean myCardListBean = new RequestMyCardListBean();
                myCardListBean = gson.fromJson(s, RequestMyCardListBean.class);
                mCardList = myCardListBean.getData();
                myListViewAdapter.notifyDataSetChanged();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

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



    class MyListViewAdapter extends BaseAdapter

    {

        @Override
        public int getCount() {
            return mCardList.size();
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
        public View getView(final int i, View view, ViewGroup viewGroup) {

            ViewHolder viewHolder = null;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = View.inflate(mActivity, R.layout.listview_item_my_club_card, null);
                viewHolder.tv_name = view.findViewById(R.id.tv_cardname);
                viewHolder.tv_number = view.findViewById(R.id.tv_number);
                viewHolder.tv_time = view.findViewById(R.id.tv_time);

                viewHolder.tv_cardtype = view.findViewById(tv_cardtype);
                viewHolder.btn_commit=view.findViewById(R.id.btn_commit);

                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }


            if (mCardList.get(i).getCharge_mode() == 1) {//计时卡
                viewHolder.tv_cardtype.setText("卡类型：计时卡");
            }
            if (mCardList.get(i).getCharge_mode() == 2) {//计次卡
                viewHolder.tv_cardtype.setText("卡类型：计次卡");
                viewHolder.tv_cardtype.setText("卡类型：计次卡（剩余次数：" + mCardList.get(i).getTotal_count() + "次）");
            }
            if (mCardList.get(i).getCharge_mode() == 3) {//储值卡
                viewHolder.tv_cardtype.setText("卡类型：储值卡");
            }


            viewHolder.tv_name.setText(mCardList.get(i).getType_name());

//            if (!TextUtils.isEmpty(mCardList.get(i).getTotal_count())){
//                viewHolder.tv_number.setText("次数："+mCardList.get(i).getTotal_count() + "次");
//                viewHolder.tv_number.setVisibility(View.VISIBLE);
//            }else {
//                viewHolder.tv_number.setVisibility(View.GONE);
//            }
            if (TextUtils.isEmpty(mCardList.get(i).getEnd_date())) {
                viewHolder.tv_time.setText("未开卡");
                viewHolder.btn_commit.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.btn_commit.setVisibility(View.VISIBLE);
                StringBuilder sb = new StringBuilder(mCardList.get(i).getEnd_date());

                String[] b = sb.toString().split(" ");

                viewHolder.tv_time.setText(b[0] + "到期");
            }

            viewHolder.btn_commit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StringBuilder  data =new StringBuilder().append("1").append(",").append("1").append(",").append(Constants.QR_MAPPING_CARD_ENTER).append(",").append(mCardList.get(i).getId());
                   startActivity(new Intent(mActivity, MyQRCodeActivity.class).putExtra("data",data.toString()));
                }
            });

            return view;
        }

        class ViewHolder {
            TextView tv_name;
            TextView tv_number;
            TextView tv_time;
            TextView tv_cardtype;
            Button btn_commit;
        }

    }

}