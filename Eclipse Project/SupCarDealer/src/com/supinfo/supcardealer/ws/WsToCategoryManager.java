package com.supinfo.supcardealer.ws;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.supinfo.supcardealer.entities.Category;


public class WsToCategoryManager {

	private static HttpClient httpClient;
	private static HttpGet httpGet;
	private static HttpPost httpPost;
	
	public WsToCategoryManager(URI uri){
		httpClient = new DefaultHttpClient();
		httpGet = new HttpGet();
		httpGet.setURI(uri);
		httpPost = new HttpPost();
		httpPost.setURI(uri);
	}
	
	private class ReceiveJsonToCategories extends AsyncTask<Object, Object, Object> {

		@Override
		protected Object doInBackground(Object... params) {
			ArrayList<Category> listCategories = null;
			try {
			   HttpResponse response = WsToCategoryManager.httpClient.execute(WsToCategoryManager.httpGet);
			   String responseString = EntityUtils.toString(response.getEntity());
			   JSONObject jsonObject = null;
			   jsonObject = new JSONObject(responseString);
			   listCategories = convertJsonToCategories(jsonObject);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return listCategories;
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Category> receiveJsonToCategories(){
		try {
			return (ArrayList<Category>) new ReceiveJsonToCategories().execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private class SendCategoryToJsonByPost extends AsyncTask<Object, Object, Object> {
		//  /ws/car
		@Override
		protected Object doInBackground(Object... category) {
			JSONObject jsonCar = convertCategoryToJson((Category)category[0]);
			StringEntity se = null;
			try {
				try {
					se = new StringEntity(jsonCar.toString());
					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				httpPost.setEntity(se);
				httpClient.execute(httpPost);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public void sendCategoryToJsonByPost(Category category){
		new SendCategoryToJsonByPost().execute(category);
	}
	
	public static JSONObject convertCategoryToJson(Category category){
		JSONObject jsonCategory = new JSONObject();
		try {
			jsonCategory.put("name",category.getName());
			jsonCategory.put("id",category.getId());
		} catch (JSONException e) {
			e.printStackTrace();
		}		
		return jsonCategory;
	}
	
	private ArrayList<Category> convertJsonToCategories(JSONObject jsonCategory){
		ArrayList<Category> listCategories = new ArrayList<Category>();
		try {
			JSONArray jsonCategoryArray = jsonCategory.getJSONArray("category");
			for (int i=0 ; i<jsonCategoryArray.length(); i++){
				Category category = new Category();
				try {
					category.setId(jsonCategoryArray.getJSONObject(i).getLong("id"));
					category.setName(jsonCategoryArray.getJSONObject(i).getString("name"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				listCategories.add(category);
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return listCategories;
	}
	
	public static Category convertJsonToCategory(JSONObject jsonCategory){
		Category category = new Category();

		try {
			category.setName(jsonCategory.getString("name"));
			return category;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
