package com.supinfo.supcardealer;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.supinfo.supcardealer.entities.Car;
import com.supinfo.supcardealer.entities.Rental;
import com.supinfo.supcardealer.entities.User;
import com.supinfo.supcardealer.globals.Globals;
import com.supinfo.supcardealer.ws.WsToRentalManager;
import com.supinfo.supcardealer.ws.WsToUserManager;

public class CarActivity extends Activity {

	private Car car;
	private Rental newRental;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car);
		
		Bundle bundle = getIntent().getExtras();
		car = (Car)bundle.get(Globals.EXTRA_CAR);
		
		((TextView)findViewById(R.id.modeleValue)).setText(car.getName());
		((TextView)findViewById(R.id.seatsValue)).setText(Integer.toString(car.getSeats()));
		((TextView)findViewById(R.id.baggageValue)).setText(car.getBaggage());
		((TextView)findViewById(R.id.doorsValue)).setText(Integer.toString(car.getDoors()));
		if(car.getGearbox().equals("automatic")) {
			((TextView)findViewById(R.id.gearboxValue)).setText("Automatique");
		} else {
			((TextView)findViewById(R.id.gearboxValue)).setText("Manuelle");
		}
		if(car.isConditionalAir()){
			((TextView)findViewById(R.id.conditionnalAirValue)).setText("Oui");
		} else {
			((TextView)findViewById(R.id.conditionnalAirValue)).setText("Non");
		}
		((TextView)findViewById(R.id.kilometersValue)).setText(Float.toString(car.getKilometers()));
		((TextView)findViewById(R.id.priceValue)).setText(Float.toString(car.getPricePerDay()));
		
		if(Globals.loggedUser != null && car.getOwner().getId() == Globals.loggedUser.getId()) {
			findViewById(R.id.btnCarRent).setVisibility(View.GONE);
			findViewById(R.id.startDate).setVisibility(View.GONE);
			findViewById(R.id.startDateLabel).setVisibility(View.GONE);
			findViewById(R.id.endDate).setVisibility(View.GONE);
			findViewById(R.id.endDateLabel).setVisibility(View.GONE);
		} else {
			findViewById(R.id.btnCarEdit).setVisibility(View.GONE);
		}
	}
	
	//Quand on clique sur le bouton "Louer"
	@SuppressLint("SimpleDateFormat")
	public void onClickCarRent(View v) {

		if(Globals.loggedUser != null)
		{
			//Récupérer l'utilisateur pour avoir son id
			URI uriUser = null;
			try {
				uriUser = new URI("http://"+Globals.SERVER_URL+"/ws/user/"+ Globals.loggedUser.getEmail());
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			WsToUserManager wsmUser = new WsToUserManager(uriUser);
			User user = wsmUser.receiveJsonToUser();
			
			//Checking entered dates
			String rentalStartDate = ((EditText) findViewById(R.id.startDate)).getText().toString();
			String rentalEndDate = ((EditText) findViewById(R.id.endDate)).getText().toString();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date today = new Date(); 
			Date startDate = new Date();
			Date endDate = new Date();
			try{
				startDate = sdf.parse(rentalStartDate);
				endDate = sdf.parse(rentalEndDate);
			} catch(Exception e) { //wrong Date parsing
				Toast toast= Toast.makeText(this,"Les dates doivent être entrées au format jj/mm/aaaa",
			    Toast.LENGTH_LONG);
			    toast.show();
			}
			
			//If the startDate is at least today's date and is earlier than the endDate...
			int days = (int)( (endDate.getTime() - startDate.getTime())/86400000 );
			if( startDate.getTime() >= today.getTime() && days > 0 ) {
				//Car's availability check
				Boolean carIsAvailable = true;
				//Récupérer les rentals
				URI uri = null;
				try {
					uri = new URI("http://"+Globals.SERVER_URL+"/ws/rental/allByCar/"+car.getId());
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				WsToRentalManager wsm = new WsToRentalManager(uri);
				ArrayList<Rental> carRentals = wsm.receiveJsonToRentals();
				
				//Comparer les dates des rentals existant pour cette voiture avec les dates entrées par l'utilsateur
				if(carRentals != null) {
					for(int i=0; i<carRentals.size(); i++) {
						try{ 
							if( !(startDate.getTime() > sdf.parse( carRentals.get(i).getEndDate() ).getTime()
								|| endDate.getTime() < sdf.parse( carRentals.get(i).getStartDate() ).getTime()) ) 
							{
								carIsAvailable = false;
							}
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
				}
				if(carIsAvailable) {
					//Billing (Price calculation)
					float rentalPrice = car.getPricePerDay() * days;
					
					//Adding attributes to the new Rental object
					newRental = new Rental();
					newRental.setRentedCarId(car.getId());
					newRental.setRenterId(user.getId());
					newRental.setStartDate(rentalStartDate);
					newRental.setEndDate(rentalEndDate);
					newRental.setPrice(rentalPrice);
					
					//Adding the new Rental in the Database
					URI uriNewRental = null;
					try {
						uriNewRental = new URI("http://"+Globals.SERVER_URL+"/ws/rental");
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
					WsToRentalManager wsrm = new WsToRentalManager(uriNewRental);
					wsrm.sendRentalToJsonByPost(newRental);
	    			
	    			//All is fine, we can display a success message and redirect
					Toast toast= Toast.makeText(this,"La voiture a bien été louée.",
					Toast.LENGTH_LONG);
					toast.show();
					finish();
				} else {
					Toast toast= Toast.makeText(this,"Nous sommes désolés mais cette voiture est indisponible pour la période sélectionée.",
					Toast.LENGTH_LONG);
					toast.show();
				}
			} else {
				Toast toast= Toast.makeText(this,"Les dates choisies sont invalides.",
				Toast.LENGTH_LONG);
				toast.show();
			}
		} else {
			Toast toast= Toast.makeText(this,"Vous devez être connecté pour louer une voiture.",
			Toast.LENGTH_LONG);
			toast.show();
		}
	}
	
	public void onClickCarEdit(View v) {
		Intent intent = new Intent(CarActivity.this, EditCarActivity.class);
		intent.putExtra(Globals.EXTRA_CAR, car);
		startActivityForResult(intent, Globals.REQUEST_CODE_EDIT_CAR);
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
     	if(resultCode == Globals.RESULT_CODE_EDIT_CAR_OK){
	        this.finish();
     	}
    }
}
