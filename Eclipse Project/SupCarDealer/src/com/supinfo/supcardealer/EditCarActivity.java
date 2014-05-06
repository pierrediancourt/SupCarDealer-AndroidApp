package com.supinfo.supcardealer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.supinfo.supcardealer.adapters.CategoriesAdapter;
import com.supinfo.supcardealer.entities.Car;
import com.supinfo.supcardealer.entities.Category;
import com.supinfo.supcardealer.globals.Globals;
import com.supinfo.supcardealer.ws.WsToCarManager;
import com.supinfo.supcardealer.ws.WsToCategoryManager;

public class EditCarActivity extends Activity {
	
	ArrayList<Category> categories = new ArrayList<Category>();
	Car car = new Car();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_car);
		
		URI uri = null;
		
		try {
			uri = new URI("http://"+Globals.SERVER_URL+"/ws/category/all");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		WsToCategoryManager wscategorym = new WsToCategoryManager(uri);
		categories = wscategorym.receiveJsonToCategories();
		
		Bundle bundle = getIntent().getExtras();
		car = (Car)bundle.get(Globals.EXTRA_CAR);; // TODO : Récupérer la voiture sélectionnée
		
		((EditText)findViewById(R.id.modele)).setText(car.getName());
		((EditText)findViewById(R.id.year)).setText(Integer.toString(car.getYear()));
		((EditText)findViewById(R.id.seats)).setText(Integer.toString(car.getSeats()));			
		((EditText)findViewById(R.id.baggage)).setText(car.getBaggage());
		((EditText)findViewById(R.id.doors)).setText(Integer.toString(car.getDoors()));	
		((EditText)findViewById(R.id.kilometers)).setText(Float.toString(car.getKilometers()));	
		((EditText)findViewById(R.id.price)).setText(Float.toString(car.getPricePerDay()));	

		if(car.getGearbox().equals("manual")){
			((RadioButton)findViewById(R.id.automatic)).setChecked(false);
			((RadioButton)findViewById(R.id.manual)).setChecked(true);
		}
		
		if(!car.isConditionalAir()){
			((RadioButton)findViewById(R.id.clim_yes)).setChecked(false);
			((RadioButton)findViewById(R.id.clim_no)).setChecked(true);
		}
		
		Log.d("plop", car.getCategory().getName());
		
		CategoriesAdapter aa_categories = new CategoriesAdapter(this, categories);
		Spinner spinner_categories = (Spinner) findViewById(R.id.categoriesList);
		spinner_categories.setAdapter(aa_categories);
		
		spinner_categories.setSelection(aa_categories.getPositionOf(car.getCategory()));
	}

	public void onClickEditCar(View v) {
		
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
		try{
			int year = Integer.parseInt(((TextView) findViewById(R.id.year)).getText().toString());
			int doors = Integer.parseInt(((TextView) findViewById(R.id.doors)).getText().toString());
			int seats = Integer.parseInt(((TextView) findViewById(R.id.seats)).getText().toString());
			float kilometers = Float.parseFloat(((TextView) findViewById(R.id.kilometers)).getText().toString());
			float pricePerDay = Float.parseFloat(((TextView) findViewById(R.id.price)).getText().toString());
			int category_id = spinner_categories.getSelectedItemPosition();
			
			Category category = categories.get(category_id);
			
			car.setName(name);
			car.setYear(year);
			car.setCategory(category);
			car.setSeats(seats);
			car.setBaggage(baggage);
			car.setDoors(doors);
			car.setGearbox(gearbox);
			car.setConditionalAir(conditionalAir);
			car.setKilometers(kilometers);
			car.setPricePerDay(pricePerDay);
			
			URI uri = null;
			try {
				uri = new URI("http://"+Globals.SERVER_URL+"/ws/car");
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			WsToCarManager wsm = new WsToCarManager(uri);
			wsm.sendCarToJsonByPut(car);
			setResult(Globals.RESULT_CODE_EDIT_CAR_OK);
		} catch(NumberFormatException e) {
			e.printStackTrace();
		}
		finish();
	}
}
