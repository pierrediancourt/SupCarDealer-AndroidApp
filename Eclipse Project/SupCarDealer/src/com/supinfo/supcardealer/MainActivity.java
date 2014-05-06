package com.supinfo.supcardealer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.supinfo.supcardealer.globals.Globals;

public class MainActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
	}
	
	
	//=========================================================================================
	//------------------------ Clic sur les boutons du menu d'accueil -------------------------
	//=========================================================================================
	public void onClickRegister(View v) {
		Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
		startActivityForResult(intent, Globals.REQUEST_CODE_REGISTER);
	}
	
	public void onClickLogin(View v) {
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		startActivityForResult(intent, Globals.REQUEST_CODE_LOGIN);
	}

	public void onClickProfile(View v) {
		Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
		startActivity(intent);
	}
	
	public void onClickAddCar(View v) {
		Intent intent = new Intent(MainActivity.this, AddCarActivity.class);
		startActivity(intent);
	}
	
	public void onClickCars(View v) {
		Intent intent = new Intent(MainActivity.this, CarsActivity.class);
		startActivity(intent);
	}

	public void onClickAbout(View v) {
		Intent intent = new Intent(MainActivity.this, AboutActivity.class);
		startActivity(intent);
	}
	
	public void onClickLogout(View v) {
		findViewById(R.id.profile).setVisibility(View.GONE);
		findViewById(R.id.addCar).setVisibility(View.GONE);
		findViewById(R.id.logout).setVisibility(View.GONE);
		findViewById(R.id.register).setVisibility(View.VISIBLE);
		findViewById(R.id.login).setVisibility(View.VISIBLE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode) {
			case Globals.REQUEST_CODE_LOGIN:
				if(resultCode == Globals.RESULT_CODE_LOGIN_OK) {
					findViewById(R.id.profile).setVisibility(View.VISIBLE);
					findViewById(R.id.addCar).setVisibility(View.VISIBLE);
					findViewById(R.id.logout).setVisibility(View.VISIBLE);
					findViewById(R.id.register).setVisibility(View.GONE);
					findViewById(R.id.login).setVisibility(View.GONE);
				}
				break;
			case Globals.REQUEST_CODE_REGISTER:
				if(resultCode == Globals.RESULT_CODE_REGISTER_OK) {
					findViewById(R.id.profile).setVisibility(View.VISIBLE);
					findViewById(R.id.addCar).setVisibility(View.VISIBLE);
					findViewById(R.id.logout).setVisibility(View.VISIBLE);
					findViewById(R.id.register).setVisibility(View.GONE);
					findViewById(R.id.login).setVisibility(View.GONE);
				} 
				break;
		}
	}
}
