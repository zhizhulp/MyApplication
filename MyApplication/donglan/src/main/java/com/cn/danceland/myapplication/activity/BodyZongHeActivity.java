package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.bean.DLResult;
import com.cn.danceland.myapplication.bean.HeadImageBean;
import com.cn.danceland.myapplication.bean.bca.bcaanalysis.BcaAnalysis;
import com.cn.danceland.myapplication.bean.bca.bcaanalysis.BcaAnalysisRequest;
import com.cn.danceland.myapplication.bean.bca.bcaresult.BcaResult;
import com.cn.danceland.myapplication.evntbus.StringEvent;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.DataInfoCache;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.cn.danceland.myapplication.utils.PictureUtil;
import com.cn.danceland.myapplication.utils.ToastUtils;
import com.cn.danceland.myapplication.utils.multipartrequest.MultipartRequest;
import com.cn.danceland.myapplication.utils.multipartrequest.MultipartRequestParams;
import com.cn.danceland.myapplication.view.DongLanTitleView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by feng on 2018/4/12.
 */

public class BodyZongHeActivity extends Activity {

    RelativeLayout rl_01,rl_02,rl_03;
    Uri uri;
    String cameraPath;
    public static String SAVED_IMAGE_DIR_PATH =
            Environment.getExternalStorageDirectory().getPath()
                    + "/DCIM/camera/";// 拍照路径
    Gson gson;
    String num;//判断第几个图片
    HashMap<Integer,String> numMap;
    ImageView img_01,img_02,img_03;
    DongLanTitleView body_zonghe_title;
    List<BcaResult> resultList;
    EditText et_content;
    Button btn_commit;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bodyzonghe);
        initHost();
        initView();
    }

    private void initHost() {

        resultList = (List<BcaResult>)getIntent().getSerializableExtra("resultList");
        if(resultList==null){
            resultList = new ArrayList<>();
        }

        numMap = new HashMap<>();

        gson = new Gson();

    }
    private void initView() {

        body_zonghe_title = findViewById(R.id.body_zonghe_title);
        body_zonghe_title.setTitle("综合评价");
        et_content = findViewById(R.id.et_content);
        btn_commit = findViewById(R.id.btn_commit);

        rl_01 = findViewById(R.id.rl_01);
        rl_02 = findViewById(R.id.rl_02);
        rl_03 = findViewById(R.id.rl_03);

        img_01 = findViewById(R.id.img_01);
        img_02 = findViewById(R.id.img_02);
        img_03 = findViewById(R.id.img_03);

        setOnClick();

    }

    private void setOnClick() {
        rl_01.setOnClickListener(onClickListener);
        rl_02.setOnClickListener(onClickListener);
        rl_03.setOnClickListener(onClickListener);
        btn_commit.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rl_01:
                    num = "1";
                    takePhoto();
                    break;
                case R.id.rl_02:
                    num = "2";
                    takePhoto();
                    break;
                case R.id.rl_03:
                    num = "3";
                    takePhoto();
                    break;
                case R.id.btn_commit:
                    if(numMap.size()!=3){
                        ToastUtils.showToastShort("请拍照完成后提交！");
                    }else{
                        save();
                    }
                    break;
            }

        }
    };


    /**
     * @方法说明:新增体测分析
     **/
    public void save() {
        BcaAnalysis bcaAnalysis = new BcaAnalysis();
        BcaAnalysisRequest request = new BcaAnalysisRequest();
        bcaAnalysis.setMember_id((long)4);
        bcaAnalysis.setMember_no(10000008+"");
        bcaAnalysis.setFrontal_path(numMap.get(1));//正面照
        bcaAnalysis.setSide_path(numMap.get(2));//侧面照
        bcaAnalysis.setBehind_path(numMap.get(3));//背面照
        bcaAnalysis.setResult(resultList);
        if(et_content.getText()!=null){
            bcaAnalysis.setContent(et_content.getText().toString());
        }
        request.save(bcaAnalysis, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject json) {
                DLResult<Integer> result = gson.fromJson(json.toString(), new TypeToken<DLResult<Integer>>() {
                }.getType());
                if (result.isSuccess()) {
                    ToastUtils.showToastShort("提交成功！");
                } else {
                    ToastUtils.showToastShort("保存数据失败,请检查手机网络！");
                }
            }
        });

    }

    private void takePhoto(){

        // 指定相机拍摄照片保存地址
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            cameraPath = SAVED_IMAGE_DIR_PATH + System.currentTimeMillis() + ".png";
            Intent intent = new Intent();
            // 指定开启系统相机的Action
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            String out_file_path = SAVED_IMAGE_DIR_PATH;
            File dir = new File(out_file_path);
            if (!dir.exists()) {
                dir.mkdirs();
            } // 把文件地址转换成Uri格式
            if (PictureUtil.getSDKV() < 24) {
                uri = Uri.fromFile(new File(cameraPath));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, 1);
            } else {
                // 设置系统相机拍摄照片完成后图片文件的存放地址
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, cameraPath);
                uri = getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, 1);
            }
            //uri = Uri.fromFile(new File(cameraPath));

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode==1){
                MultipartRequestParams params = new MultipartRequestParams();
                if(cameraPath!=null){

                    if("1".equals(num)){
                        rl_01.setVisibility(View.GONE);
                        img_01.setVisibility(View.VISIBLE);
                        Glide.with(BodyZongHeActivity.this).load(uri).into(img_01);
                    }else if("2".equals(num)){
                        rl_02.setVisibility(View.GONE);
                        img_02.setVisibility(View.VISIBLE);
                        Glide.with(BodyZongHeActivity.this).load(uri).into(img_02);
                    }else if("3".equals(num)){
                        rl_03.setVisibility(View.GONE);
                        img_03.setVisibility(View.VISIBLE);
                        Glide.with(BodyZongHeActivity.this).load(uri).into(img_03);
                    }

                    //上传图片
                    File file = new File(cameraPath);
                    params.put("file", file);

                    MultipartRequest request = new MultipartRequest(Request.Method.POST, params, Constants.BCAUPLOAD, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String s) {
                            HeadImageBean headImageBean = gson.fromJson(s, HeadImageBean.class);
                            if (headImageBean != null && headImageBean.getData() != null) {
                                String imgPath = headImageBean.getData().getImgPath();
                                String imgUrl = headImageBean.getData().getImgUrl();
                                if("1".equals(num)){
                                    numMap.put(1,imgPath);
                                }else if("2".equals(num)){
                                    numMap.put(2,imgPath);
                                }else if("3".equals(num)){
                                    numMap.put(3,imgPath);
                                }
                                ToastUtils.showToastShort("上传图片成功！");
                            }else{
                                ToastUtils.showToastShort("上传图片失败！请重新拍照！");
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                        }
                    }
                    );

                    MyApplication.getHttpQueues().add(request);

                }

            }
        }
    }
}