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

public class EditProfileActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		
		((EditText)findViewById(R.id.editEmailField)).setText(Globals.loggedUser.getEmail());
		((EditText)findViewById(R.id.editFirstnameField)).setText(Globals.loggedUser.getFirstname());
		((EditText)findViewById(R.id.editLastnameField)).setText(Globals.loggedUser.getLastname());			
	}
	
	
	public void onClickEditSubmit(View view) {
		String oldPassword = ((EditText)findViewById(R.id.editPassword1Field)).getText().toString();
		String newPassword1 = ((EditText)findViewById(R.id.editPassword2Field)).getText().toString();
		String newPassword2 = ((EditText)findViewById(R.id.editPassword3Field)).getText().toString();
		
		String lastname = ((EditText)findViewById(R.id.editLastnameField)).getText().toString();
		String firstname = ((EditText)findViewById(R.id.editFirstnameField)).getText().toString();
		
		if(EncryptionService.encrypt(oldPassword).equals(Globals.loggedUser.getPassword())){
			if(newPassword1.equals(newPassword2)
			&& isPasswordValid(newPassword1)) {
				
				User editedUser = Globals.loggedUser;
				editedUser.setPassword(EncryptionService.encrypt(newPassword1));
				editedUser.setFirstname(firstname);
				editedUser.setLastname(lastname);
				
				URI uri = null;
				try {
					uri = new URI("http://"+Globals.SERVER_URL+"/ws/user");
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				WsToUserManager wsm = new WsToUserManager(uri);
				wsm.sendUserToJsonByPut(editedUser);
				Globals.loggedUser = editedUser;
				setResult(Globals.RESULT_CODE_EDIT_PROFILE_OK);
				Toast toast= Toast.makeText(this,"Les modifications ont été enregistrée.",
				Toast.LENGTH_LONG);
				toast.show();
				finish();
			} else {
				Toast toast= Toast.makeText(this,"Les mots de passes ne correspondent pas ou sont inférieur à 8 caractères.",
				Toast.LENGTH_LONG);
				toast.show();
			}
		} else {
			Toast toast= Toast.makeText(this,"Ancien mot de passe incorrect.",
			Toast.LENGTH_LONG);
			toast.show();
		}
	}
	
	private boolean isPasswordValid(String password){
		if(password.length() >= Globals.PASSWORD_LENGTH){
			return true;
		}
		return false;
	}
}
