package com.supinfo.supcardealer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.supinfo.supcardealer.adapters.CarsAdapter;
import com.supinfo.supcardealer.entities.Car;
import com.supinfo.supcardealer.globals.Globals;
import com.supinfo.supcardealer.ws.WsToCarManager;

public class CarsActivity extends Activity {

	private ListView listViewCars;
	private ArrayList<Car> cars;
	private CarsAdapter carsAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cars);
		
		URI uri = null;
		try {
			uri = new URI("http://"+Globals.SERVER_URL+"/ws/car/all");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		WsToCarManager wsm = new WsToCarManager(uri);
		cars = wsm.receiveJsonToCars();
			
		listViewCars = (ListView)findViewById(R.id.listViewCars);
		listViewCars.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				Intent intent = new Intent(CarsActivity.this, CarActivity.class);
				intent.putExtra(Globals.EXTRA_CAR, cars.get(position));
				startActivity(intent);
			}
		});
		
		carsAdapter = new CarsAdapter(this, cars);
		listViewCars.setAdapter(carsAdapter);
	}
}
