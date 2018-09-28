package com.cn.danceland.myapplication.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.Data;
import com.cn.danceland.myapplication.bean.RequestInfoBean;
import com.cn.danceland.myapplication.evntbus.StringEvent;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.MyStringRequest;
import com.cn.danceland.myapplication.utils.PhoneFormatCheckUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.GET;

public class ResetPhoneActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTvGetsms;
    private EditText mEtSms;
    private String smsCode = "";
    private EditText mEtPhone;
    private int recLen = 30;//倒计时时长
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            recLen--;
            mTvGetsms.setText("" + recLen + "秒后重试");
            handler.postDelayed(this, 1000);

            if (recLen <= 0) {
                //倒计时结束后设置可以点击
                mTvGetsms.setFocusable(true);
                mTvGetsms.setClickable(true);
                mTvGetsms.setTextColor(Color.WHITE);
                mTvGetsms.setText("获取验证码");
                handler.removeCallbacks(runnable);

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        initView();
    }

    private void initView() {
        mTvGetsms = findViewById(R.id.tv_getsms);
        mTvGetsms.setOnClickListener(this);
        mEtPhone = findViewById(R.id.et_phone);

        mEtSms = findViewById(R.id.et_sms);
        findViewById(R.id.btn_commit).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);


        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //    修改手机号后清空验证码
                mEtSms.setText("");
                smsCode = "";
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_getsms:
                //判断电话号码是否为空
                if (TextUtils.isEmpty(mEtPhone.getText().toString())) {
                    Toast.makeText(ResetPhoneActivity.this, "请输入电话号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                //判断电话号码是否合法
                if (!PhoneFormatCheckUtils.isPhoneLegal(mEtPhone.getText().toString())) {
                    Toast.makeText(ResetPhoneActivity.this, "电话号码有误，请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.equals(getIntent().getStringExtra("phone"), mEtPhone.getText().toString().trim())) {

                    ToastUtils.showToastShort("请输入新的手机号");
                    return;
                }
                //设置不能点击
                mTvGetsms.setFocusable(false);
                mTvGetsms.setClickable(false);
                mTvGetsms.setTextColor(Color.GRAY);
                recLen = 30;
                mTvGetsms.setText("" + recLen + "秒后重试");
                //设置倒计时
                handler.postDelayed(runnable, 1000);

                getSMS();
                break;
            case R.id.btn_commit:
                //判断验证码是否为空
                if (TextUtils.isEmpty(mEtSms.getText().toString().trim())) {
                    Toast.makeText(ResetPhoneActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }

//                if (!TextUtils.equals(smsCode, mEtSms.getText().toString().trim())) {
//
//                    ToastUtils.showToastShort("验证码有误，请重新输入");
//                    return;
//                }


                resetPhone();//重置手机号


                break;
            case R.id.iv_back://返回
                finish();
                break;
            default:
                break;
        }

    }


    /**
     * 获取短信验证码
     */
    private void getSMS() {

        String params = mEtPhone.getText().toString().trim();

        String url = Constants.GET_SMS_URL + params;

        StringRequest request = new StringRequest(GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                Gson gson = new Gson();
                RequestInfoBean requestInfoBean = new RequestInfoBean();
                requestInfoBean = gson.fromJson(s, RequestInfoBean.class);
                if (requestInfoBean.getSuccess()) {
                    smsCode = requestInfoBean.getData().getVerCode();
                    if (Constants.DEV_CONFIG){
                        ToastUtils.showToastLong("验证码是："
                                + smsCode);
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                LogUtil.i(volleyError.toString());
                ToastUtils.showToastShort("请求失败，请查看网络连接");
            }
        });
        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("get_sms");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);

    }

    private void resetPhone() {

        MyStringRequest jsonRequest = new MyStringRequest(Request.Method.PUT, Constants.RESET_PHONE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);
                Gson gson = new Gson();
                RequestInfoBean infoBean = new RequestInfoBean();
                infoBean = gson.fromJson(s, RequestInfoBean.class);
                if (infoBean.getSuccess()) {

                    ToastUtils.showToastShort("手机号修改成功");
                    //发送事件
                    EventBus.getDefault().post(new StringEvent(mEtPhone.getText().toString().trim(),111));
                    Data data= (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);
                    data.getPerson().setPhone_no(mEtPhone.getText().toString().trim());
                    DataInfoCache.saveOneCache(data,Constants.MY_INFO);

                    finish();
                } else {

                    ToastUtils.showToastShort("修改失败，原因：" + infoBean.getErrorMsg());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.i(volleyError.toString());
                ToastUtils.showToastShort("请查看网络连接");
            }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                //   map.put("id", SPUtils.getString(Constants.MY_USERID, ""));
                map.put("phone", mEtPhone.getText().toString().trim());
                map.put("validateCode", mEtSms.getText().toString().trim());

                return map;
            }


        };

        MyApplication.getHttpQueues().add(jsonRequest);
    }


}
