package com.cn.danceland.myapplication.bean.bca.bcanotesstep;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import com.cn.danceland.myapplication.MyApplication;
import com.cn.danceland.myapplication.utils.Constants;
import com.cn.danceland.myapplication.utils.SPUtils;
import com.cn.danceland.myapplication.utils.ToastUtils;

/**
 * @友情提示: 请清理掉用不到的代码包括这段注释
 **/

/**
 * @类说明:体测分析注意事项--网络请求层
 * @author:高振中
 * @date:2018-03-29 11:54:48
 **/
public class BcaNotesStepRequest {

	/**
	 * @方法说明:新增体测分析注意事项
	 **/
	public void save(BcaNotesStep bcaNotesStep, Listener<JSONObject> listener) {
		JsonObjectRequest request = new JsonObjectRequest(1, Constants.HOST + "bcaNotesStep/save", new Gson().toJson(bcaNotesStep), listener, new Response.ErrorListener() {
			public void onErrorResponse(VolleyError error) {
				ToastUtils.showToastShort("请检查手机网络！");
			}
		}) {
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
				return map;
			}
		};
		MyApplication.getHttpQueues().add(request);
	}

	

	/**
	 * @方法说明:修改体测分析注意事项
	 **/
	public void update(BcaNotesStep bcaNotesStep, Listener<JSONObject> listener) {
		JsonObjectRequest request = new JsonObjectRequest(1, Constants.HOST + "bcaNotesStep/update", new Gson().toJson(bcaNotesStep), listener, new Response.ErrorListener() {
			public void onErrorResponse(VolleyError error) {
				ToastUtils.showToastShort("请检查手机网络！");
			}
		}) {
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
				return map;
			}
		};
		MyApplication.getHttpQueues().add(request);
	}


	/**
	 * @方法说明:按主键删除体测分析注意事项
	 **/
	public void delete(Long id, Listener<String> listener) {
		StringRequest request = new StringRequest(1, Constants.HOST + "bcaNotesStep/delete?id=" + id, listener, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				ToastUtils.showToastShort("请检查手机网络！");
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
				return map;
			}
		};
		MyApplication.getHttpQueues().add(request);
	}


	/**
	 * @方法说明:按条件查询体测分析注意事项列表
	 **/
	public void queryList(BcaNotesStepCond cond, Listener<JSONObject> listener) {
		JsonObjectRequest request = new JsonObjectRequest(1, Constants.HOST + "bcaNotesStep/queryList", new Gson().toJson(cond), listener, new Response.ErrorListener() {
			public void onErrorResponse(VolleyError error) {
				ToastUtils.showToastShort("请检查手机网络！");
			}
		}) {
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
				return map;
			}
		};
		MyApplication.getHttpQueues().add(request);
	}

 

	/**
	 * @方法说明:按条件查询体测分析注意事项分页列表
	 **/
	public void queryPage(BcaNotesStepCond cond, Listener<JSONObject> listener) {
		JsonObjectRequest request = new JsonObjectRequest(1, Constants.HOST + "bcaNotesStep/queryPage", new Gson().toJson(cond), listener, new Response.ErrorListener() {
			public void onErrorResponse(VolleyError error) {
				ToastUtils.showToastShort("请检查手机网络！");
			}
		}) {
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
				return map;
			}
		};
		MyApplication.getHttpQueues().add(request);
	}

 

	/**
	 * @方法说明:按主键查询体测分析注意事项单个数据
	 **/
	public void findById(Long id, Listener<String> listener) {
		StringRequest request = new StringRequest(1, Constants.HOST + "bcaNotesStep/findById?id=" + id, listener, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				ToastUtils.showToastShort("请检查手机网络！");
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
				return map;
			}
		};
		MyApplication.getHttpQueues().add(request);
	}

 

	/**
	 * @方法说明:按条件查询体测分析注意事项数据个数
	 **/
	public void queryCount(BcaNotesStepCond cond, Listener<JSONObject> listener) {
		JsonObjectRequest request = new JsonObjectRequest(1, Constants.HOST + "bcaNotesStep/queryCount", new Gson().toJson(cond), listener, new Response.ErrorListener() {
			public void onErrorResponse(VolleyError error) {
				ToastUtils.showToastShort("请检查手机网络！");
			}
		}) {
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("Authorization", SPUtils.getString(Constants.MY_TOKEN, ""));
				return map;
			}
		};
		MyApplication.getHttpQueues().add(request);
	}
}