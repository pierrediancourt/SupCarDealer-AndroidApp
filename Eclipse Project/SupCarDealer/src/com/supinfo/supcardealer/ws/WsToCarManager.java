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
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.supinfo.supcardealer.entities.Car;


public class WsToCarManager {

	private HttpClient httpClient;
	private HttpGet httpGet;
	private HttpPost httpPost;
	private HttpPut httpPut;
	
	public WsToCarManager(URI uri){
		httpClient = new DefaultHttpClient();
		httpGet = new HttpGet();
		httpGet.setURI(uri);
		httpPost = new HttpPost();
		httpPost.setURI(uri);
		httpPut = new HttpPut();
		httpPut.setURI(uri);
	}
	
	private class ReceiveJsonToCars extends AsyncTask<Object, Object, Object> {
		// /ws/car/all
		@Override
		protected Object doInBackground(Object... params) {
			ArrayList<Car> listCar = null;
			try {
			   HttpResponse response = httpClient.execute(httpGet);
			   String responseString = EntityUtils.toString(response.getEntity());
			   JSONObject jsonObject;
			   jsonObject = new JSONObject(responseString);
			   listCar = convertJsonToCars(jsonObject);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return listCar;
		}
	}
	
//	private class ReceiveJsonToCar extends AsyncTask<Object, Object, Object> {
//
//		@Override
//		protected Object doInBackground(Object... params) {
//			Car car = null;
//			try {
//			   HttpResponse response = httpClient.execute(httpGet);
//			   String responseString = EntityUtils.toString(response.getEntity());
//			   JSONObject jsonObject;
//			   jsonObject = new JSONObject(responseString);
//			   car = convertJsonToCar(jsonObject);
//			} catch (ClientProtocolException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//			return car;
//		}
//	}
	
//	public Car receiveJsonToCar(){
//	try {
//		return (Car) new ReceiveJsonToCar().execute().get();
//	} catch (InterruptedException e) {
//		e.printStackTrace();
//	} catch (ExecutionException e) {
//		e.printStackTrace();
//	}
//	return null;
//}
	
//	private Car convertJsonToCar(JSONObject jsonCar){
//	Car car = new Car();
//	try {
//		JSONObject jsonCarObject = jsonCar.getJSONObject("car");
//			try {
//				car.setName(jsonCarObject.getString("name"));
////				car.setDoors(jsonCarObject.getInt("doors"));
////				car.setYear(jsonCarObject.getInt("year"));
////				car.setSeats(jsonCarObject.getInt("seats"));
////				car.setBaggage(jsonCarObject.getString("baggage"));
////				car.setKilometers(jsonCarObject.getInt("kilometers"));
////				car.setGearbox(jsonCarObject.getString("gearbox"));
////				car.setConditionalAir(jsonCarObject.getBoolean("conditionalAir"));
////				car.setPricePerDay(jsonCarObject.getInt("pricePerDay"));  // /!\ gaffe au type   
////				car.setImageUrl(jsonCarObject.getString("imageUrl"));
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//	} catch (JSONException e1) {
//		e1.printStackTrace();
//	}
//	return car;
//}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Car> receiveJsonToCars(){
		try {
			return (ArrayList<Car>) new ReceiveJsonToCars().execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	private class SendCarToJsonByPost extends AsyncTask<Object, Object, Object> {
		//  /ws/car
		@Override
		protected Object doInBackground(Object... car) {
			JSONObject jsonCar = convertCarToJson((Car)car[0], false);
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

	public void sendCarToJsonByPost(Car car){
		new SendCarToJsonByPost().execute(car);
	}
	
	private class SendCarToJsonByPut extends AsyncTask<Object, Object, Object> {
		//  /ws/car
		@Override
		protected Object doInBackground(Object... car) {
			JSONObject jsonCar = convertCarToJson((Car)car[0], true);
			StringEntity se = null;
			try {
				try {
					se = new StringEntity(jsonCar.toString());
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

	public void sendCarToJsonByPut(Car car){
		new SendCarToJsonByPut().execute(car);
	}
	
	private JSONObject convertCarToJson(Car car, boolean update){
		JSONObject jsonCar = new JSONObject();
		try {
			jsonCar.put("doors",car.getDoors());
			jsonCar.put("year",car.getYear());
			jsonCar.put("name",car.getName());
			jsonCar.put("category",WsToCategoryManager.convertCategoryToJson(car.getCategory()));
			jsonCar.put("owner",WsToUserManager.convertUserToJson(car.getOwner(), true));
			jsonCar.put("seats",car.getSeats());
			jsonCar.put("baggage",car.getBaggage());
			jsonCar.put("gearbox",car.getGearbox());
			jsonCar.put("conditionalAir",car.isConditionalAir());
			jsonCar.put("kilometers",car.getKilometers());
			jsonCar.put("pricePerDay",car.getPricePerDay());
			jsonCar.put("imageUrl",car.getImageUrl());
			if(update){jsonCar.put("id",car.getId());}
		} catch (JSONException e) {
			e.printStackTrace();
		}		
		return jsonCar;
	}
	
	public static ArrayList<Car> convertJsonToCars(JSONObject jsonCar){
		ArrayList<Car> listCar = new ArrayList<Car>();
		try {
			JSONArray jsonCarArray = jsonCar.getJSONArray("car");
			for (int i=0 ; i<jsonCarArray.length(); i++){
				Car car = new Car();
				try {
					car.setName(jsonCarArray.getJSONObject(i).getString("name"));
					car.setDoors(jsonCarArray.getJSONObject(i).getInt("doors"));
					car.setYear(jsonCarArray.getJSONObject(i).getInt("year"));
					car.setSeats(jsonCarArray.getJSONObject(i).getInt("seats"));
					car.setBaggage(jsonCarArray.getJSONObject(i).getString("baggage"));
					car.setKilometers((float)jsonCarArray.getJSONObject(i).getDouble("kilometers"));
					car.setGearbox(jsonCarArray.getJSONObject(i).getString("gearbox"));
					car.setConditionalAir(jsonCarArray.getJSONObject(i).getBoolean("conditionalAir"));
					car.setPricePerDay((float)jsonCarArray.getJSONObject(i).getDouble("pricePerDay"));  // /!\ gaffe au type   
					car.setImageUrl(jsonCarArray.getJSONObject(i).getString("imageUrl"));
					car.setId((long)jsonCarArray.getJSONObject(i).getInt("id"));
					car.setCategory(WsToCategoryManager.convertJsonToCategory(jsonCarArray.getJSONObject(i).getJSONObject("category")));
					car.setOwner(WsToUserManager.convertJsonToUser(jsonCarArray.getJSONObject(i).getJSONObject("owner")));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				listCar.add(car);
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return listCar;
	}

}
