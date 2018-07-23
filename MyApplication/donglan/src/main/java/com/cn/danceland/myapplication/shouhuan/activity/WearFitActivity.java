package com.cn.danceland.myapplication.shouhuan.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.shouhuan.command.CommandManager;
import com.cn.danceland.myapplication.shouhuan.constans.BleConstans;
import com.cn.danceland.myapplication.shouhuan.service.BluetoothLeService;
import com.cn.danceland.myapplication.shouhuan.utils.DataHandlerUtils;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.StringUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.view.DongLanTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2018/6/19.
 */

public class WearFitActivity extends Activity {
    private static final int REQUEST_SEARCH = 1;
    private static final int REQUEST_SETTING = 3;
    private TextView tv_connect;
    private String address;
    private String name;
    private GridView gv_wearfit;
    private DongLanTitleView shouhuan_title;
    private int[] imgID = {R.drawable.shouhuan_heart, R.drawable.shouhuan_sleep, R.drawable.shouhuan_pilao, R.drawable.shouhuan_photo, R.drawable.shouhuan_plan, R.drawable.shouhuan_find
            , R.drawable.shouhuan_setting};
    private String[] text1s = {"心率", "睡眠", "疲劳", "摇摇拍照", "健身计划", "查找手环", "设置"};
    private String[] text2s = {"--bpm", "-时-分", "--", "", "", "", ""};
    private ArrayList<ItemBean> itemBeans;
    private ProgressDialog progressDialog;
    private String address1;
    private RelativeLayout rl_connect;
    private CommandManager commandManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearfit);
        initHost();
        initView();
    }

    private void initHost() {
        commandManager = CommandManager.getInstance(this);
        progressDialog = new ProgressDialog(this);
        itemBeans = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            ItemBean itemBean = new ItemBean();
            itemBean.img_url = imgID[i];
            itemBean.text1 = text1s[i];
            itemBean.text2 = text2s[i];
            itemBeans.add(itemBean);
        }
        address1 = SPUtils.getString(Constants.ADDRESS, "");


    }


    private void initView() {
        rl_connect = findViewById(R.id.rl_connect);
        gv_wearfit = findViewById(R.id.gv_wearfit);
        gv_wearfit.setAdapter(new WearFitAdapter());
        shouhuan_title = findViewById(R.id.shouhuan_title);
        shouhuan_title.setTitle("我的手环");
        tv_connect = findViewById(R.id.tv_connect);
        setListener();
        if (!MyApplication.mBluetoothConnected && !StringUtils.isNullorEmpty(address1)) {
            try {
                if (MyApplication.mBluetoothLeService.connect(address1)) {
                    //tv_status.setText(name+"--"+address);
                    //ToastUtils.showToastShort("正在连接...");
                    progressDialog.setMessage("正在连接...");
                    progressDialog.show();
                } else {
                    ToastUtils.showToastShort("连接失败");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            MyApplication.isBluetoothConnecting = true;
            invalidateOptionsMenu();
        } else {
            rl_connect.setVisibility(View.VISIBLE);
            tv_connect.setText("还未绑定手环，点击绑定");
        }
    }

    private class WearFitAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return itemBeans.size();
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
            View inflate = View.inflate(WearFitActivity.this, R.layout.shouhuan_gv_item, null);
            ImageView img = inflate.findViewById(R.id.img);
            TextView text1 = inflate.findViewById(R.id.tv_text1);
            TextView text2 = inflate.findViewById(R.id.tv_text2);
            Glide.with(WearFitActivity.this).load(itemBeans.get(i).img_url).into(img);
            text1.setText(itemBeans.get(i).text1);
            text2.setText(itemBeans.get(i).text2);
            return inflate;
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (MyApplication.mBluetoothConnected) {
            rl_connect.setVisibility(View.GONE);
        } else {
            rl_connect.setVisibility(View.VISIBLE);
            tv_connect.setText("还未绑定手环，点击绑定");
        }
    }

    private void setListener() {

        tv_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WearFitActivity.this, WearFitEquipmentActivity.class);
                startActivity(intent);
            }
        });

        gv_wearfit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0://心率



                        commandManager.realTimeAndOnceMeasure(0x80,1);
                        break;
                    case 1://睡眠

                        PackageManager manager =  getPackageManager();
                        //要查找的BroadCastReceiver
                        Intent intent = new Intent(BleConstans.ACTION_SEND_DATA_TO_BLE);

                        List<ResolveInfo> resolveInfos = manager.queryBroadcastReceivers(intent,  PackageManager.GET_INTENT_FILTERS);

                        if (resolveInfos.size() == 0) {
                            LogUtil.i( "该BroadCast不存在");
                        }


                        break;
                    case 2://疲劳
                        break;
                    case 3://摇摇拍照
                        break;
                    case 4://健身计划
                        break;
                    case 5://查找手环
                        startActivityForResult(new Intent(WearFitActivity.this, DeviceScanActivity.class), REQUEST_SEARCH);
                        break;
                    case 6://设置
                        startActivity(new Intent(WearFitActivity.this, WearFitSettingActivity.class));
                        break;

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SEARCH && resultCode == RESULT_OK) {
            address = data.getStringExtra(Constants.ADDRESS);
            name = data.getStringExtra(Constants.NAME);
            SPUtils.setString(Constants.ADDRESS, address);
            SPUtils.setString(Constants.NAME, name);
            if (!TextUtils.isEmpty(address)) {

                try {
                    if (MyApplication.mBluetoothLeService.connect(address)) {
                        //tv_status.setText(name+"--"+address);
                        //ToastUtils.showToastShort("正在连接...");
                        progressDialog.setMessage("正在连接...");
                        progressDialog.show();
                    } else {
                        ToastUtils.showToastShort("连接失败");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                MyApplication.isBluetoothConnecting = true;
                invalidateOptionsMenu();//显示正在连接 ...
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    private IntentFilter makeGattUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    //接收蓝牙状态改变的广播
    private BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                MyApplication.mBluetoothConnected = true;
                MyApplication.isBluetoothConnecting = false;
                //todo 更改界面ui
                invalidateOptionsMenu();//更新菜单栏
                ToastUtils.showToastShort("连接成功");
                rl_connect.setVisibility(View.GONE);
                progressDialog.dismiss();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                MyApplication.mBluetoothConnected = false;
                //todo 更改界面ui
                invalidateOptionsMenu();//更新菜单栏
                try {
                    MyApplication.mBluetoothLeService.close();//断开更彻底(没有这一句，在某些机型，重连会连不上)
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                ToastUtils.showToastShort("已断开");
                tv_connect.setText("还未绑定手环，点击绑定");
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
//                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
//                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
//                Log.i("zgy", "接收到的数据：" + intent.getStringExtra(BluetoothLeService.EXTRA_DATA));

                final byte[] txValue = intent
                        .getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                //LogUtil.i("BluetoothLeService", "接收的数据：" + DataHandlerUtils.bytesToHexStr(txValue));

                List<Integer> datas = DataHandlerUtils.bytesToArrayList(txValue);

                if (datas.get(4) == 0x51 && datas.get(5) == 17) {
                    LogUtil.i(datas.toString());
                }
                if (datas.get(4) == 0x51 && datas.get(5) == 8) {
                    LogUtil.i(datas.toString());
                }

            }
        }
    };

    private class ItemBean {
        int img_url;
        String text1;
        String text2;

    }
}
