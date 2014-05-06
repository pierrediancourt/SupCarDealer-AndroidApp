package com.supinfo.supcardealer.ws;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.supinfo.supcardealer.entities.User;


public class WsToUserManager {

	private static HttpClient httpClient;
	private static HttpGet httpGet;
	private HttpPost httpPost;
	private HttpPut httpPut;
	
	public WsToUserManager(URI uri){
		httpClient = new DefaultHttpClient();
		httpGet = new HttpGet();
		httpGet.setURI(uri);
		httpPost = new HttpPost();
		httpPost.setURI(uri);
		httpPut = new HttpPut();
		httpPut.setURI(uri);
	}
	
	public void setNewGetUri(URI uri){
		httpGet.setURI(uri);
	}
	
	public void setNewPostUri(URI uri){
		httpPost.setURI(uri);
	}
	
	public void setNewPutUri(URI uri){
		httpPut.setURI(uri);
	}
	
//	private class ReceiveJsonToUsers extends AsyncTask<Object, Object, Object> {
//
//		@Override
//		protected Object doInBackground(Object... params) {
//			ArrayList<User> listUsers = null;
//			try {
//			   HttpResponse response = WsToUserManager.httpClient.execute(WsToUserManager.httpGet);
//			   String responseString = EntityUtils.toString(response.getEntity());
//			   JSONObject jsonObject = null;
//			   jsonObject = new JSONObject(responseString);
//			   listUsers = convertJsonToUsers(jsonObject);
//			} catch (ClientProtocolException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//			return listUsers;
//		}
//	}
	
//	@SuppressWarnings("unchecked")
//	public ArrayList<User> receiveJsonToUsers(){
//		try {
//			return (ArrayList<User>) new ReceiveJsonToUsers().execute().get();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
//	@SuppressWarnings("unchecked")
//	private ArrayList<User> convertJsonToUsers(JSONObject jsonUser){
//		ArrayList<User> listUser = new ArrayList<User>();
//		try {
//			JSONArray jsonUserArray = jsonUser.getJSONArray("user");
//			for (int i=0 ; i<jsonUserArray.length(); i++){
//				User user = new User();
//				try {
//					user.setEmail(jsonUserArray.getJSONObject(i).getString("email"));
//					user.setCars((List<Car>) jsonUserArray.getJSONObject(i).getJSONArray("cars")); // /!\ gaffe au type
//					user.setPassword(jsonUserArray.getJSONObject(i).getString("password"));
//					user.setFirstname(jsonUserArray.getJSONObject(i).getString("firstname"));
//					user.setLastname(jsonUserArray.getJSONObject(i).getString("lastname"));
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//				listUser.add(user);
//			}
//		} catch (JSONException e1) {
//			e1.printStackTrace();
//		}
//		return listUser;
//	}
	
	private class SendUserToJsonByPost extends AsyncTask<Object, Object, Object> {
		@Override
		protected Object doInBackground(Object... user) {
			JSONObject jsonUser = convertUserToJson((User)user[0], false);
			StringEntity se = null;
			try {
				try {
					se = new StringEntity(jsonUser.toString());
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

	public void sendUserToJsonByPost(User user){
		new SendUserToJsonByPost().execute(user);
	}
	
	private class SendUserToJsonByPut extends AsyncTask<Object, Object, Object> {
		@Override
		protected Object doInBackground(Object... user) {
			JSONObject jsonUser = convertUserToJson((User)user[0], true);
			StringEntity se = null;
			try {
				try {
					se = new StringEntity(jsonUser.toString());
					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				httpPut.setEntity(se);
				httpClient.execute(httpPut);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	public void sendUserToJsonByPut(User user){
		new SendUserToJsonByPut().execute(user);
	}
	
	public static JSONObject convertUserToJson(User user, boolean update){
		JSONObject jsonUser = new JSONObject();
		try {
			jsonUser.put("email",user.getEmail());
			jsonUser.put("cars",user.getCars());
			jsonUser.put("password",user.getPassword());
			jsonUser.put("firstname",user.getFirstname());
			jsonUser.put("lastname",user.getLastname());
			if(update){jsonUser.put("id",user.getId());}
		} catch (JSONException e) {
			e.printStackTrace();
		}		
		return jsonUser;
	}
	
	private class ReceiveJsonToUser extends AsyncTask<Object, Object, Object> {

		@Override
		protected Object doInBackground(Object... params) {
			User user = null;
			try {
			   HttpResponse response = httpClient.execute(httpGet);
			   if (response.getStatusLine().getStatusCode() != 200)
			   {
				   return null;
			   }
//			   int code = response.getStatusLine().getStatusCode(); 
//			   String message = response.getStatusLine().getReasonPhrase();
//			   Log.d("probleme", "code : "+code+" message : "+message);
			   
			   String responseString = EntityUtils.toString(response.getEntity());
			   JSONObject jsonObject;
			   jsonObject = new JSONObject(responseString);
			   user = convertJsonToUser(jsonObject);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return user;
		}
	}
	
	public User receiveJsonToUser(){
		try {
			return (User) new ReceiveJsonToUser().execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static User convertJsonToUser(JSONObject jsonUser){
		User user = new User();
		try {
			user.setId(jsonUser.getLong("id"));
			user.setFirstname(jsonUser.getString("firstname"));
			user.setLastname(jsonUser.getString("lastname"));
			user.setEmail(jsonUser.getString("email"));
			user.setPassword(jsonUser.getString("password"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return user;
	}
	
}
