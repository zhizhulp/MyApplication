package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import com.cn.danceland.myapplication.db.DBData;
import com.cn.danceland.myapplication.db.Donglan;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingActivity extends Activity implements View.OnClickListener {

    View locationView;
    TextView cancel_action, over_action;
    PopupWindow locationWindow;
    ListView list_province, list_city;
    LocationAdapter proAdapter, cityAdapter;
    private TextView tv_number;
    private TextView tv_phone;
    private Data mInfo;
    //   private ArrayList<Data> mInfoBean;
    DBData dbData;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initHost();
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initHost() {
        dbData = new DBData();
    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_quit).setOnClickListener(this);
        findViewById(R.id.ll_setting).setOnClickListener(this);
        findViewById(R.id.ll_setting_phone).setOnClickListener(this);
        findViewById(R.id.ll_setting_location).setOnClickListener(this);
        findViewById(R.id.ll_about_us).setOnClickListener(this);
        findViewById(R.id.ll_clear).setOnClickListener(this);

        tv_number = findViewById(R.id.tv_number);
        tv_phone = findViewById(R.id.tv_phone);


        mInfo = (Data) DataInfoCache.loadOneCache(Constants.MY_INFO);

        //  LogUtil.i(mInfoBean.get(0).toString());
        if (!TextUtils.isEmpty(mInfo.getPhone())) {
            tv_phone.setText(mInfo.getPhone());
        }

        //LogUtil.i(mInfoBean.get(0).toString());
        if (!TextUtils.isEmpty(mInfo.getPhone())) {
            tv_phone.setText(mInfo.getPhone());
        }
        if (!TextUtils.isEmpty(mInfo.getMemberNo())) {
            tv_number.setText(mInfo.getMemberNo());
        } else {
            tv_number.setText("未设置");
        }


        locationView = LayoutInflater.from(SettingActivity.this).inflate(R.layout.selectorwindowlocation, null);
        locationWindow = new PopupWindow(locationView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        locationWindow.setOutsideTouchable(true);
        locationWindow.setBackgroundDrawable(new BitmapDrawable());
        cancel_action = locationView.findViewById(R.id.cancel_action);
        over_action = locationView.findViewById(R.id.over_action);
        list_province = locationView.findViewById(R.id.list_province);
        list_city = locationView.findViewById(R.id.list_city);
        cancel_action.setOnClickListener(this);
        over_action.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.tv_quit://退出

                logOut();

                break;
            case R.id.ll_setting://设置会员
                showInputDialog();

                break;
            case R.id.ll_setting_phone://设置手机号
                showSettingPhoneDialog();
                break;
            case R.id.ll_setting_location://设置位置
                showLocation();
                break;
            case R.id.ll_about_us://关于我们

                break;
            case R.id.ll_clear://清除缓存
                Toast.makeText(this, "已清除缓存！", Toast.LENGTH_SHORT).show();
                showClearDialog();
                break;
            case R.id.cancel_action:
                dismissWindow();
                break;
            case R.id.over_action:
                dismissWindow();
                break;
            default:
                break;
        }
    }

    /**
     * 设置手机号
     */

    private void showSettingPhoneDialog() {
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(this);
        dialog.setTitle("提示");
        dialog.setMessage("是否重新绑定手机号");
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                startActivity(new Intent(SettingActivity.this, ConfirmPasswordActivity.class).putExtra("phone", tv_phone.getText().toString()));
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }


    /**
     * 清除缓存
     */
    private void showClearDialog() {
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(this);
        dialog.setTitle("提示");
        dialog.setMessage("清除缓存成功");
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    /**
     * 设置会员号
     */
    private void showInputDialog() {

        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_input_phone, null);
        inputDialog.setTitle("设置会员号");
        inputDialog.setView(dialogView);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取EditView中的输入内容
                        EditText edit_phone =
                                dialogView.findViewById(R.id.edit_phone);

                        mInfo.setMemberNo(edit_phone.getText().toString());
                        DataInfoCache.saveOneCache(mInfo, Constants.MY_INFO);

                        tv_number.setText(edit_phone.getText().toString());

//                        Toast.makeText(SettingActivity.this,
//                                edit_phone.getText().toString(),
//                                Toast.LENGTH_SHORT).show();
                    }
                });
        inputDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        inputDialog.show();
    }


    public void showLocation() {
        List<Donglan> cityList = dbData.getCityList();
        //省份和代码对应的map
        HashMap<String, String> proMap = new HashMap<String, String>();
        //城市代码和名称map
        HashMap<String, String> city = new HashMap<String, String>();
        //省份列表
        final ArrayList<String> proList = new ArrayList<String>();
        //省份和城市map
        ArrayList<String> cityList1 = new ArrayList<String>();
        final HashMap<String, ArrayList<String>> proCityMap = new HashMap<String, ArrayList<String>>();

        final HashMap<String, HashMap<String, String>> cityMap = new HashMap<String, HashMap<String, String>>();
        if (cityList != null && cityList.size() > 0) {
            for (int i = 0; i < cityList.size(); i++) {
                //城市名字为key，城市代码为value
                String prokey = cityList.get(i).getProvince().split("@")[1];
                String provalue = cityList.get(i).getProvince().split("@")[0];
                cityList1 = getCityList(cityList.get(i).getCity());
                proList.add(prokey);
                proMap.put(prokey, provalue);
                city = getCityMap(cityList.get(i).getCity());
                //cityList1 = getCityList(cityList.get(i).getCity());
                proCityMap.put(prokey, cityList1);
            }
        }


        //ArrayList<String> cityList1 = new ArrayList<String>();
//        cityList.add("郑州");
//        cityList.add("武汉");
//        cityList.add("合肥");
//        cityList.add("合肥");
//        cityList.add("合肥");
//        cityList.add("合肥");
//        cityList.add("合肥");
//        cityList.add("合肥");

        locationWindow.setContentView(locationView);
        proAdapter = new LocationAdapter(proList, this);
        list_province.setAdapter(proAdapter);
        list_province.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pro = proList.get(position);
                ArrayList<String> cityList = proCityMap.get(pro);
                if (cityList != null && cityList.size() > 0) {
                    cityAdapter = new LocationAdapter(cityList, SettingActivity.this);
                    list_city.setAdapter(cityAdapter);
                }
            }
        });

        //


        locationWindow.showAsDropDown(findViewById(R.id.tv_quit), 0, 40);
        locationWindow.setAnimationStyle(R.style.selectorMenuAnim);

    }

    public class LocationAdapter extends BaseAdapter {

        ArrayList<String> arrayList;
        LayoutInflater inflater = null;

        public LocationAdapter(ArrayList<String> list, Context context) {
            arrayList = list;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return arrayList.size();
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
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView item_text = null;
            if (view == null) {
                view = inflater.inflate(R.layout.selector_item, null);
            }
            item_text = view.findViewById(R.id.item_text);
            item_text.setText(arrayList.get(i));
            return view;
        }
    }

    public void dismissWindow() {
        if (null != locationWindow && locationWindow.isShowing()) {
            locationWindow.dismiss();
        }
    }

    /***
     * 退出登录
     */
    private void logOut() {

        String url = Constants.LOGOUT_URL;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.i(s);

                Gson gson = new Gson();
                RequestInfoBean requestInfoBean = new RequestInfoBean();
                requestInfoBean = gson.fromJson(s, RequestInfoBean.class);
                if (requestInfoBean.getSuccess()) {
                    //成功
                    startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                    SPUtils.setBoolean(Constants.ISLOGINED, false);
                    //退出主页面
                    HomeActivity.instance.finish();
                    finish();
                } else {
                    //失败
                    ToastUtils.showToastShort(requestInfoBean.getErrorMsg());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToastShort("请求失败，请查看网络连接");
//                LogUtil.i(volleyError.toString() + "Error: " + volleyError
//                        + ">>" + volleyError.networkResponse.statusCode
//                        + ">>" + volleyError.networkResponse.data
//                        + ">>" + volleyError.getCause()
//                        + ">>" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("id", SPUtils.getString(Constants.MY_USERID, null));

                // map.put("romType", "0");
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, null));
                // LogUtil.i("Bearer+"+SPUtils.getString(Constants.MY_TOKEN,null));
                return map;
            }
        };

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("logOut");
        // 设置超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);
    }

    public HashMap<String, String> getCityMap(String str) {
        if (!"".equals(str) && str != null) {
            HashMap<String, String> cityMap = new HashMap<String, String>();
            String[] ss = str.split("$");
            if (ss.length > 0) {
                for (int i = 0; i < ss.length; i++) {
                    String[] split = ss[i].split("@");
                    String value = split[0].replace("{value=", "");
                    String key = split[1].replace("label=", "");
                    cityMap.put(key, value);
                }
            }
            return cityMap;
        }
        return null;
    }

    public ArrayList<String> getCityList(String str) {
        if (!"".equals(str) && str != null) {
            ArrayList<String> arr = new ArrayList<String>();
            String[] strings = str.split("]");
            if (strings.length > 0) {
                for (int i = 0; i < strings.length; i++) {
                    String[] split = strings[i].split("@");
                    arr.add(split[1].replace("label=", ""));
                }
            }
            return arr;
        }
        return null;
    }

}
