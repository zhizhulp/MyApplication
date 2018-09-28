package com.cn.danceland.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.app.AppManager;
import com.cn.danceland.myapplication.bean.RequestInfoBean;
import com.cn.danceland.myapplication.bean.RequsetFindUserBean;
import com.cn.danceland.myapplication.evntbus.EventConstants;
import com.cn.danceland.myapplication.evntbus.StringEvent;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.MyStringRequest;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cn.danceland.myapplication.R.id.ll_result;

/**
 * Created by shy on 2017/10/30 09:32
 * <p>
 * Email:644563767@qq.com
 */


public class AddFriendsActivity extends BaseActivity implements View.OnClickListener {
    EditText mEtPhone;
    private ImageButton iv_del;
    //   private LinearLayout ll_result;
    private LinearLayout ll_search;
    private TextView tv_search;
    private TextView tv_nickname;
    private ImageView iv_avatar;
    private TextView tv_guanzhu;
    private TextView tv_title;
   // private RequsetUserDynInfoBean userInfo;
    private List<RequsetFindUserBean.Data> dataList = new ArrayList<>();

    private String from;
    int memberId;
    int personId;
    String member_no;
    private ListView listView;
    private MyListAatapter myListAatapter = new MyListAatapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        AppManager.getAppManager().addActivity(this);
        from = getIntent().getStringExtra("from");
        iv_del = findViewById(R.id.iv_del);
        ll_search = findViewById(R.id.ll_search);
        listView = findViewById(R.id.listview);
        listView.setAdapter(myListAatapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("体测".equals(from)) {
                    if ("true".equals(getIntent().getStringExtra("isAnalysis"))) {
                        Intent intent = new Intent(AddFriendsActivity.this, FitnessTestSearchResultActivity.class);
                        intent.putExtra("requsetInfo", dataList.get(position));
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(AddFriendsActivity.this, ReadyTestActivity.class);
                        intent.putExtra("id", dataList.get(position).getPerson_id());
                        intent.putExtra("memberId", dataList.get(position).getId());
                        intent.putExtra("member_no", dataList.get(position).getMember_no());
                        startActivity(intent);
                    }

                    //finish();
                } else {
                    startActivity(new Intent(AddFriendsActivity.this, UserSelfHomeActivity.class).putExtra("id", dataList.get(position).getId()));
                }
            }
        });
        //ll_result = findViewById(ll_result);
        tv_search = findViewById(R.id.tv_search);
        mEtPhone = findViewById(R.id.et_phone);
//        tv_nickname = findViewById(R.id.tv_nickname);
//        iv_avatar = findViewById(R.id.iv_avatar);
//        iv_avatar.setOnClickListener(this);
        //   ll_result.setOnClickListener(this);
//        iv_avatar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });


        tv_guanzhu = findViewById(R.id.tv_guanzhu);
        tv_title = findViewById(R.id.tv_title);
        if ("体测".equals(from)) {
//            tv_guanzhu.setVisibility(View.GONE);
            tv_title.setText("搜索会员");
        }
//        findViewById(R.id.tv_guanzhu).setOnClickListener(this);
        //  tv_result_null = findViewById(R.id.tv_result_null);
        setListener();

        mEtPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if ("体测".equals(from)) {
                        searchMember(mEtPhone.getText().toString().trim());
                    } else {
                        findUser(mEtPhone.getText().toString().trim());
                    }


                }
                return false;
            }
        });

    }

    private void setListener() {
        ll_search.setOnClickListener(this);
        iv_del.setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

//                ll_result.setVisibility(View.GONE);
//                tv_result_null.setVisibility(View.GONE);
            }

            //监听edit
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(mEtPhone.getText().toString().trim())) {
                    iv_del.setVisibility(View.VISIBLE);
                    ll_search.setVisibility(View.VISIBLE);

                    tv_search.setText("搜索：“" + mEtPhone.getText().toString().trim() + "”");

                } else {
                    iv_del.setVisibility(View.GONE);
                    ll_search.setVisibility(View.GONE);
                    tv_search.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_del:
                mEtPhone.setText("");
                if ("体测".equals(from)) {
                    searchMember(mEtPhone.getText().toString().trim());
                } else {
                    findUser(mEtPhone.getText().toString().trim());
                }
                break;
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.tv_guanzhu://加关注
//                if (!userInfo.getData().getIs_follow()) {//如果未加关注
//                    addGuanzhu(userInfo.getData().getPerson().getId(), true);
//                }

                break;
            case R.id.ll_search://搜索

                if ("体测".equals(from)) {
                    searchMember(mEtPhone.getText().toString().trim());
                } else {
                    findUser(mEtPhone.getText().toString().trim());
                }
                // ToastUtils.showToastShort(mEtPhone.getText().toString());

                break;
            case ll_result:
                if ("体测".equals(from)) {
                    if ("true".equals(getIntent().getStringExtra("isAnalysis"))) {
                        Intent intent = new Intent(AddFriendsActivity.this, BodyBaseActivity.class);
                        intent.putExtra("id", personId + "");
                        intent.putExtra("memberId", memberId + "");
                        intent.putExtra("member_no", member_no);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(AddFriendsActivity.this, ReadyTestActivity.class);
                        intent.putExtra("id", personId + "");
                        intent.putExtra("memberId", memberId + "");
                        intent.putExtra("member_no", member_no);
                        startActivity(intent);
                    }

                    //finish();
                } else {
                 //   startActivity(new Intent(AddFriendsActivity.this, UserSelfHomeActivity.class).putExtra("id", userInfo.getData().getPerson().getId()));
                }
                break;
            default:
                break;
        }
    }


    class StrBean1 {
        public boolean is_follower;
        public String user_id;

    }


    /**
     * 加关注
     *
     * @param id
     * @param b
     */
    private void addGuanzhu(final String id, final boolean b) {

        StrBean1 strBean1 = new StrBean1();
        strBean1.is_follower = b;
        strBean1.user_id = id;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.ADD_GUANZHU, new Gson().toJson(strBean1), new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.i(jsonObject.toString());
                Gson gson = new Gson();
                RequestInfoBean requestInfoBean = new RequestInfoBean();
                requestInfoBean = gson.fromJson(jsonObject.toString(), RequestInfoBean.class);
                if (requestInfoBean.getSuccess()) {
//                    data.get(pos).setFollower(true);
//                    notifyDataSetChanged();

                    ToastUtils.showToastShort("关注成功");
                    tv_guanzhu.setText("已关注");
                    tv_guanzhu.setClickable(false);

                    EventBus.getDefault().post(new StringEvent(id, EventConstants.ADD_GUANZHU));

                } else {
                    ToastUtils.showToastShort("关注失败");
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                LogUtil.i(volleyError.toString());
                ToastUtils.showToastShort("请查看网络连接");
            }

        }
        ) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
                // LogUtil.i("Bearer+"+SPUtils.getString(Constants.MY_TOKEN,null));
                LogUtil.i(SPUtils.getString(Constants.MY_TOKEN, null));
                return map;
            }
        };
        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("addGuanzhu");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);

    }


    private void searchMember(final String phone) {

        MyStringRequest stringRequest = new MyStringRequest(Request.Method.POST, Constants.FINDMEMBER, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                Gson gson = new Gson();
//                SearchMember searchMember = gson.fromJson(s, SearchMember.class);
//                SearchMember.Data data = searchMember.getData();
//                if (data != null) {
//                    memberId = data.getId();
//                    personId = data.getPerson_id();
//                    member_no = data.getMember_no();
//                }



                RequsetFindUserBean infoBean = gson.fromJson(s, RequsetFindUserBean.class);

                if (infoBean.getSuccess()) {
                    dataList=infoBean.getData();
                    myListAatapter.notifyDataSetChanged();


                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("phone", phone);
                return map;
            }


        };

        MyApplication.getHttpQueues().add(stringRequest);

    }


    /*** 查找用户加关注
     *
     * @param phone
     */

    private void findUser(final String phone) {


        MyStringRequest request = new MyStringRequest(Request.Method.POST, Constants.FIND_ADD_USER_USRL, new Response.Listener<String>() {


            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                Gson gson = new Gson();
                RequsetFindUserBean infoBean = gson.fromJson(s, RequsetFindUserBean.class);

                if (infoBean.getSuccess()) {
                    dataList=infoBean.getData();
                    myListAatapter.notifyDataSetChanged();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                LogUtil.i(volleyError.toString() + volleyError.networkResponse.statusCode);

            }

        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("phone", phone);
                return map;
            }

        };
        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("findUser");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);
    }


    class MyListAatapter extends BaseAdapter {

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

                convertView = View.inflate(AddFriendsActivity.this, R.layout.listview_item_finduser, null);
                vh.iv_avatar = convertView.findViewById(R.id.iv_avatar);

                vh.tv_name = convertView.findViewById(R.id.tv_nickname);


                convertView.setTag(vh);

            } else {

                vh = (ViewHolder) convertView.getTag();

            }
            RequestOptions options = new RequestOptions().placeholder(R.drawable.img_my_avatar);

            if ("体测".equals(from)){
                vh.tv_name.setText(dataList.get(position).getCname());
                Glide.with(AddFriendsActivity.this).load(dataList.get(position).getAvatar_url() ).apply(options).into(vh.iv_avatar);
            }else {
                vh.tv_name.setText(dataList.get(position).getNick_name());
                Glide.with(AddFriendsActivity.this).load(dataList.get(position).getSelf_avatar_url()).apply(options).into(vh.iv_avatar);
            }



            return convertView;

        }


    }
    class ViewHolder {
        public ImageView iv_avatar;
        public ImageView iv_callphone;
        public TextView tv_name;
        public ImageView iv_sex;
        public TextView tv_lasttime;
        public LinearLayout ll_item;
        public ImageView iv_hx_msg;
    }
}


