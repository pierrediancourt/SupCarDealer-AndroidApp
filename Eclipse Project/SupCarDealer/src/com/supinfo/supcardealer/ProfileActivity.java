package com.supinfo.supcardealer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.supinfo.supcardealer.globals.Globals;

public class ProfileActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		((TextView)findViewById(R.id.profileEmailValue)).setText(Globals.loggedUser.getEmail());
		((TextView)findViewById(R.id.profileFirstnameValue)).setText(Globals.loggedUser.getFirstname());
		((TextView)findViewById(R.id.profileLastnameValue)).setText(Globals.loggedUser.getLastname());		
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
     	if(resultCode == Globals.RESULT_CODE_EDIT_PROFILE_OK){
	        Intent refresh = new Intent(this, ProfileActivity.class);
	        startActivity(refresh);
	        this.finish();
     	}
    }

	//Quand on clique sur le bouton "Editer le profil"
	public void onClickProfileEdit(View v) {
		Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
		startActivityForResult(intent, Globals.REQUEST_CODE_EDIT_PROFILE);
	}
}
