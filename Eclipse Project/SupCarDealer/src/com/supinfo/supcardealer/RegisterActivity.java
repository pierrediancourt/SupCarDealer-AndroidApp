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

public class RegisterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		setResult(Globals.RESULT_CODE_REGISTER_CANCEL);
	}
	
	public void onClickRegisterSubmit(View v) {
		EditText email;
		EditText firstName;
		EditText lastName;
		EditText password;
		EditText password2;
	    email   = (EditText)findViewById(R.id.registerEmailField);
	    firstName   = (EditText)findViewById(R.id.registerFirstnameField);
	    lastName   = (EditText)findViewById(R.id.registerLastnameField);
	    password   = (EditText)findViewById(R.id.registerPassword1Field);
	    password2   = (EditText)findViewById(R.id.registerPassword2Field);
	    
	    if(email.getText().toString() != null
	    || firstName.getText().toString() != null 
	    || lastName.getText().toString() != null 
	    || password.getText().toString() != null 
	    || password2.getText().toString() != null){
	    	if(isEmailValid(email.getText().toString())) {
	    		URI uri = null;
				try {
					uri = new URI("http://"+Globals.SERVER_URL+"/ws/user/"+ email.getText().toString());
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				WsToUserManager wsm = new WsToUserManager(uri);
				User user = wsm.receiveJsonToUser();
				
				if(user == null)
				{
					if(password.getText().toString().equals(password2.getText().toString())) {
						if(isPasswordValid(password.getText().toString())){
							User newUser = new User();
			    			newUser.setEmail(email.getText().toString());
			    			newUser.setPassword(EncryptionService.encrypt(password.getText().toString()));
			    			newUser.setFirstname(firstName.getText().toString());
			    			newUser.setLastname(lastName.getText().toString());
			    			try {
			    				uri = new URI("http://"+Globals.SERVER_URL+"/ws/user");
			    			} catch (URISyntaxException e) {
			    				e.printStackTrace();
			    			}
				    		wsm.setNewPostUri(uri);
			    			wsm.sendUserToJsonByPost(newUser);
			    			
			    			try {
			    				uri = new URI("http://"+Globals.SERVER_URL+"/ws/user/"+email.getText().toString());
			    			} catch (URISyntaxException e) {
			    				e.printStackTrace();
			    			}
				    		wsm.setNewGetUri(uri);			    			
			    			Globals.loggedUser = wsm.receiveJsonToUser();
			    			
				    		setResult(Globals.RESULT_CODE_REGISTER_OK);
							finish();
				    	} else {
				    		Toast toast= Toast.makeText(this,"Mot de passe invalide (< 8 caractères)",
				    		Toast.LENGTH_LONG);
				    		toast.show();
				    	}
					} else{
						Toast toast1= Toast.makeText(this,"Les mots de passes ne sont pas identiques",
						Toast.LENGTH_LONG);
						toast1.show();
			    	}
				} else {
					Toast toast2= Toast.makeText(this,"Un utilisateur existe déjà avec cet email",
				    Toast.LENGTH_LONG);
					toast2.show();
				}
	    	} else {
	    		Toast toast3= Toast.makeText(this,"Email invalide",
			    Toast.LENGTH_LONG);
	    		toast3.show();
	    	}
		} else{
			Toast toast4= Toast.makeText(this,"Merci de remplir tous les champs",
		    Toast.LENGTH_LONG);
			toast4.show();
		}
	}
	
	private boolean isPasswordValid(String password){
		if(password.length() >= Globals.PASSWORD_LENGTH){
			return true;
		}
		return false;
	}
	
	private boolean isEmailValid(CharSequence target) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	}
}