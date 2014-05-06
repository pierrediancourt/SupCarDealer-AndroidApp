package com.supinfo.supcardealer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.supinfo.supcardealer.adapters.CategoriesAdapter;
import com.supinfo.supcardealer.entities.Car;
import com.supinfo.supcardealer.entities.Category;
import com.supinfo.supcardealer.entities.User;
import com.supinfo.supcardealer.globals.Globals;
import com.supinfo.supcardealer.ws.WsToCarManager;
import com.supinfo.supcardealer.ws.WsToCategoryManager;

public class AddCarActivity extends Activity {
	
	private ArrayList<Category> categories = new ArrayList<Category>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_car);
		
		URI uri = null;
		try {
			uri = new URI("http://"+Globals.SERVER_URL+"/ws/category/all");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		WsToCategoryManager wsm = new WsToCategoryManager(uri);
		categories = wsm.receiveJsonToCategories();
		
		CategoriesAdapter aa_categories = new CategoriesAdapter(this, categories);
		Spinner spinner_categories = (Spinner) findViewById(R.id.categoriesList);
		spinner_categories.setAdapter(aa_categories);
	}
	
	public void onClickAddCar(View v) {
		
		// Récupération des Radio
		RadioGroup rg_gearbox = (RadioGroup) findViewById(R.id.radioGearBox);
		RadioButton rb_gearbox = (RadioButton) findViewById(rg_gearbox.getCheckedRadioButtonId());
		
		RadioGroup rg_conditionnalAir = (RadioGroup) findViewById(R.id.radioClim);
		RadioButton rb_conditionnalAir = (RadioButton) findViewById(rg_conditionnalAir.getCheckedRadioButtonId());
		
		// Récupération de la liste déroulante
		Spinner spinner_categories = (Spinner) findViewById(R.id.categoriesList);
		
		String name = 				((TextView) findViewById(R.id.modele)).getText().toString();
		String baggage = 			((TextView) findViewById(R.id.baggage)).getText().toString();
		String gearbox = 			rb_gearbox.getText().toString();
		boolean conditionalAir = 	Boolean.parseBoolean(rb_conditionnalAir.getText().toString());
		String imageUrl = null;
		try{
			int year = Integer.parseInt(((TextView) findViewById(R.id.year)).getText().toString());
			int doors = Integer.parseInt(((TextView) findViewById(R.id.doors)).getText().toString());
			int seats = Integer.parseInt(((TextView) findViewById(R.id.seats)).getText().toString());
			float kilometers = Float.parseFloat(((TextView) findViewById(R.id.kilometers)).getText().toString());
			float pricePerDay = Float.parseFloat(((TextView) findViewById(R.id.price)).getText().toString());
			int category_id = spinner_categories.getSelectedItemPosition();
			
			User owner = Globals.loggedUser;
			Category category = categories.get(category_id);
			
			Car newCar = new Car(	name,
									year,
									category,
									owner,
									seats,
									baggage,
									doors,
									gearbox,
									conditionalAir,
									kilometers,
									pricePerDay, 
									imageUrl);
			URI uri = null;
			try {
				uri = new URI("http://"+Globals.SERVER_URL+"/ws/car");
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			WsToCarManager wsm = new WsToCarManager(uri);
			wsm.sendCarToJsonByPost(newCar);
			
		} catch(NumberFormatException e) {
			e.printStackTrace();
		}
		finish();
	}
}
