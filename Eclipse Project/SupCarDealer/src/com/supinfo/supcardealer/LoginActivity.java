package com.supinfo.supcardealer;

import java.net.URI;
import java.net.URISyntaxException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.supinfo.supcardealer.entities.User;
import com.supinfo.supcardealer.globals.Globals;
import com.supinfo.supcardealer.utils.EncryptionService;
import com.supinfo.supcardealer.ws.WsToUserManager;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setResult(Globals.RESULT_CODE_LOGIN_CANCEL);
	}

	//Quand on clique sur le bouton Submit
	public void onClickLoginSubmit(View v) {
		//TODO:penser à afficher les boutons masqués par défaut, et masquer les boutons Register, Login
		EditText email = (EditText)findViewById(R.id.loginEmailField);
		EditText password = (EditText)findViewById(R.id.loginPasswordField);
		
		URI uri = null;
		try {
			uri = new URI("http://"+Globals.SERVER_URL+"/ws/user/"+ email.getText().toString());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		WsToUserManager wsm = new WsToUserManager(uri);
		User user = wsm.receiveJsonToUser();
		
		if(user != null)
		{
			if(EncryptionService.encrypt(password.getText().toString()).equals(user.getPassword())) {
				
				Globals.loggedUser = user;
				setResult(Globals.RESULT_CODE_LOGIN_OK);
				finish();
			} else {
				Toast toast= Toast.makeText(this,"Mot de passe incorrect.",
				Toast.LENGTH_LONG);
				toast.show();
			}
		} else {
			Toast toast= Toast.makeText(this,"L'utilisateur n'existe pas.",
			Toast.LENGTH_LONG);
			toast.show();
		}
	}
}
