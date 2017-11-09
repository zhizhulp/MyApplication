package com.cn.danceland.myapplication.db;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.bean.CityBean;
import com.cn.danceland.myapplication.bean.RootBean;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.internal.Utils;

import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2017/11/6.
 */

public class DBData {

    public ArrayList<String> cityDis = new ArrayList<String>();
    DonglanDao donglanDao;

    //从接口获取数据存入数据库
    public void setCityInfo(Context context){
        final Gson gson = new Gson();
        final Donglan dl = new Donglan();

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.ZONE, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Type type = new TypeToken<ArrayList<CityBean>>() {
                }.getType();
                RootBean rootBean = gson.fromJson(jsonObject.toString(), RootBean.class);
                ArrayList<CityBean> arrayList = gson.fromJson(rootBean.data, type);

                if(arrayList!=null&&arrayList.size()>0){
                    for(int i =0;i<arrayList.size();i++){
                        StringBuilder sb = new StringBuilder();
                        List childrenList = arrayList.get(i).getChildren();
                        dl.setId(i);
                        dl.setProvince(arrayList.get(i).getValue()+"@"+arrayList.get(i).getLabel());
                        if(childrenList!=null&&childrenList.size()>0){
                            for(int n=0;n<childrenList.size();n++){
                                String str = childrenList.get(n).toString();
                                sb.append(getCity(str)+"]");
                            }
                            dl.setCity(sb.toString());
                        }
                        //LogUtil.e("zzf",arrayList.get(i).getChildren().get(0).toString());
                        addD(dl);
                        dl.clear();
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("Authorization","Bearer+eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxODM5NTIwMTUxNSIsImNyZWF0ZWQiOjE1MTAwMTkxMzA2NzEsImV4cCI6MTUxMDYyMzkzMH0.-PnRgcHSndkSPvXDtaOUKKIrzcDkUtAtVfh6wIAJKKHckoFbxuwmQ6u7ileBFUe-8nhfKVq3XqRc6hflTZcufQ");

                return hashMap;
            }
        };

        requestQueue.add(jsonObjectRequest);


    }

//获取城市，直辖市获取区县
    public String getCity(String str){
        if(str!=null){
            String[] split = str.split(",");
            if(split!=null&&split.length>1){
                return split[0]+"@"+split[1];
            }
        }
        return "";
    }

    //添加记录
    public void addD(Donglan d){
        if(donglanDao==null){
            donglanDao = MyApplication.getInstance().getDaoSession().getDonglanDao();
        }
        donglanDao.insert(d);
    }

    //查询数据
    public List<Donglan> getCityList(){
        if(donglanDao==null){
            donglanDao = MyApplication.getInstance().getDaoSession().getDonglanDao();
        }
        List<Donglan> donglanList = donglanDao.loadAll();
        return donglanList;
    }

    //改变数据
    public void upDate(Donglan d){
        if(donglanDao==null){
            donglanDao = MyApplication.getInstance().getDaoSession().getDonglanDao();
        }
        donglanDao.update(d);
    }

}