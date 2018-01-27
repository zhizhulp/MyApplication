package com.cn.danceland.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import com.cjt2325.cameralibrary.CaptureLayout;
import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.listener.CaptureListener;
import com.cjt2325.cameralibrary.listener.ClickListener;
import com.cjt2325.cameralibrary.listener.ErrorListener;
import com.cjt2325.cameralibrary.listener.JCameraListener;
import com.cjt2325.cameralibrary.listener.TypeListener;
import com.cjt2325.cameralibrary.util.FileUtil;
import com.cn.danceland.myapplication.R;
import com.cn.danceland.myapplication.utils.LogUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.cn.danceland.myapplication.pictureviewer.PictureConfig.path;

/**
 * Created by feng on 2018/1/26.
 */

public class ShowCameraActivity extends Activity {

    JCameraView jCameraView;
    CaptureLayout capture_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showcamera);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initView();

    }

    private void initView() {

        //1.1.1
        jCameraView = findViewById(R.id.jcameraview);
        capture_layout = jCameraView.findViewById(R.id.capture_layout);

//        //设置视频保存路径
//        jCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "JCamera");

        //设置只能录像或只能拍照或两种都可以（默认两种都可以）
        jCameraView.setFeatures(JCameraView.BUTTON_STATE_BOTH);

        //设置视频质量
        jCameraView.setMediaQuality(5*1024*1024);

        //JCameraView监听
        jCameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {

            }

            @Override
            public void AudioPermissionError() {

            }
        });

        jCameraView.setJCameraLisenter(new JCameraListener() {
            //获取图片bitmap
            @Override
            public void captureSuccess(Bitmap bitmap) {
                File file = saveBitmapFile(bitmap);
                Intent intent = new Intent();
                intent.putExtra("path", file.getAbsolutePath());
                LogUtil.e("zzf",file.getAbsolutePath());
                setResult(101, intent);
                finish();
            }
            //获取视频路径
            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                LogUtil.e("zzf",url);
                File path = saveBitmapFile(firstFrame);
                Intent intent = new Intent();
                intent.putExtra("path", path);
                setResult(101, intent);
                finish();

            }
        });
        //左边按钮点击事件
        jCameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                //CameraActivity.this.finish();
                //finish();
                LogUtil.e("zzf","1111");
            }
        });
        //右边按钮点击事件
        jCameraView.setRightClickListener(new ClickListener() {
            @Override
            public void onClick() {
                LogUtil.e("zzf","2222");

                //finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        //全屏
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        jCameraView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        jCameraView.onPause();
    }

    public File saveBitmapFile(Bitmap bitmap) {
        File file=new File(Environment.getExternalStorageDirectory().getPath()
                + "/donglan/camera/"+System.currentTimeMillis()+".png");//将要保存图片的路径
        File dir = new File(Environment.getExternalStorageDirectory().getPath()
                + "/donglan/camera/");
        if(!dir.exists()){
            dir.mkdirs();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();

        }catch (IOException e){
            e.printStackTrace();
        }
        return file;
    }
}
