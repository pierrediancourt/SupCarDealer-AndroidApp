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

import com.supinfo.supcardealer.entities.Rental;


public class WsToRentalManager {

	private static HttpClient httpClient;
	private static HttpGet httpGet;
	private static HttpPost httpPost;
	
	public WsToRentalManager(URI uri){
		httpClient = new DefaultHttpClient();
		httpGet = new HttpGet();
		httpGet.setURI(uri);
		httpPost = new HttpPost();
		httpPost.setURI(uri);
	}
	
	private class ReceiveJsonToRentals extends AsyncTask<Object, Object, Object> {
		//  /ws/rental/all
		@Override
		protected Object doInBackground(Object... params) {
			ArrayList<Rental> listRental = null;
			try {
			   HttpResponse response = WsToRentalManager.httpClient.execute(WsToRentalManager.httpGet);
			   String responseString = EntityUtils.toString(response.getEntity());
			   JSONObject jsonObject;
			   jsonObject = new JSONObject(responseString);
			   listRental = convertJsonToRentals(jsonObject);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return listRental;
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Rental> receiveJsonToRentals(){
		try {
			return (ArrayList<Rental>) new ReceiveJsonToRentals().execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private class SendRentalToJsonByPost extends AsyncTask<Object, Object, Object> {
		//  /ws/rental
		@Override
		protected Object doInBackground(Object... rental) {
			JSONObject jsonRental = convertRentalToJson((Rental)rental[0]);
			StringEntity se = null;
			try {
				try {
					se = new StringEntity(jsonRental.toString());
					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				WsToRentalManager.httpPost.setEntity(se);
				
				WsToRentalManager.httpClient.execute(WsToRentalManager.httpPost);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	public void sendRentalToJsonByPost(Rental rental){
		new SendRentalToJsonByPost().execute(rental);
	}
	
	private JSONObject convertRentalToJson(Rental rental){
		JSONObject jsonRental = new JSONObject();
		try {
			jsonRental.put("rentedCarId",rental.getRentedCarId());
			jsonRental.put("renterId",rental.getRenterId());
			jsonRental.put("startDate",rental.getStartDate());
			jsonRental.put("endDate",rental.getEndDate());
			jsonRental.put("price",rental.getPrice());
		} catch (JSONException e) {
			e.printStackTrace();
		}		
		return jsonRental;
	}
	
	private ArrayList<Rental> convertJsonToRentals(JSONObject jsonRental){
		ArrayList<Rental> listRental = new ArrayList<Rental>();
		try {
			JSONArray jsonRentalArray = jsonRental.getJSONArray("rental");
			for (int i=0 ; i<jsonRentalArray.length(); i++){
				Rental rental = new Rental();
				try {
					rental.setRentedCarId(jsonRentalArray.getJSONObject(i).getLong("rentedCarId"));
					rental.setRenterId(jsonRentalArray.getJSONObject(i).getLong("renterId"));
					rental.setStartDate(jsonRentalArray.getJSONObject(i).getString("startDate"));
					rental.setEndDate(jsonRentalArray.getJSONObject(i).getString("endDate"));
					rental.setPrice(jsonRentalArray.getJSONObject(i).getInt("price")); // /!\ Normalement de type float
				} catch (JSONException e) {
					e.printStackTrace();
				}
				listRental.add(rental);
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return listRental;
	}
}
