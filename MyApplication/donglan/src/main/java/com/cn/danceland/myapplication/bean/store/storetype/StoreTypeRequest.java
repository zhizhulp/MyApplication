package com.cn.danceland.myapplication.bean.store.storetype;

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
 * @类说明:储值卡类型--网络请求层
 * @author:高振中
 * @date:2018-03-14 10:16:26
 **/
public class StoreTypeRequest {

	/**
	 * @方法说明:新增储值卡类型
	 **/
	public void save(StoreType storeType, Listener<JSONObject> listener) {
		JSONObject json = null;
		try {
			json = new JSONObject(new Gson().toJson(storeType));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JsonObjectRequest request = new JsonObjectRequest(1, Constants.HOST + "storeType/save", json, listener, new Response.ErrorListener() {
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
	 * @方法说明:修改储值卡类型
	 **/
	public void update(StoreType storeType, Listener<JSONObject> listener) {
		JSONObject json = null;
		try {
			json = new JSONObject(new Gson().toJson(storeType));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JsonObjectRequest request = new JsonObjectRequest(1, Constants.HOST + "storeType/update", json, listener, new Response.ErrorListener() {
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
	 * @方法说明:按主键删除储值卡类型
	 **/
	public void delete(Long id, Listener<String> listener) {
		StringRequest request = new StringRequest(1, Constants.HOST + "storeType/delete?id=" + id, listener, new Response.ErrorListener() {
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
	 * @方法说明:按条件查询储值卡类型列表
	 **/
	public void queryList(StoreTypeCond cond, Listener<JSONObject> listener) {
		JSONObject json = null;
		try {
			json = new JSONObject(new Gson().toJson(cond));
		} catch (JSONException e) {
			// LogUtil.i(storeType.toString());
			e.printStackTrace();
		}
		JsonObjectRequest request = new JsonObjectRequest(1, Constants.HOST + "storeType/queryList", json, listener, new Response.ErrorListener() {
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
	 * @方法说明:按条件查询储值卡类型分页列表
	 **/
	public void queryPage(StoreTypeCond cond, Listener<JSONObject> listener) {

		JSONObject json = null;
		try {
			json = new JSONObject(new Gson().toJson(cond));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JsonObjectRequest request = new JsonObjectRequest(1, Constants.HOST + "storeType/queryPage", json, listener, new Response.ErrorListener() {
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
	 * @方法说明:按主键查询储值卡类型单个数据
	 **/
	public void findById(Long id, Listener<String> listener) {
		StringRequest request = new StringRequest(1, Constants.HOST + "storeType/findById?id=" + id, listener, new Response.ErrorListener() {
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
	 * @方法说明:按条件查询储值卡类型数据个数
	 **/
	public void queryCount(StoreTypeCond cond, Listener<JSONObject> listener) {
		JSONObject json = null;
		try {
			json = new JSONObject(new Gson().toJson(cond));
		} catch (JSONException e) {
			// LogUtil.i(storeType.toString());
			e.printStackTrace();
		}
		JsonObjectRequest request = new JsonObjectRequest(1, Constants.HOST + "storeType/queryCount", json, listener, new Response.ErrorListener() {
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