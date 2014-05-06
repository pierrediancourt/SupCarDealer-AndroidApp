package com.supinfo.supcardealer.globals;

import com.supinfo.supcardealer.entities.User;

public class Globals {
	
	public static User loggedUser = null;

	public static final String EXTRA_CAR = "car";
	public static final String EXTRA_QUOTES = "cars";
	
	public static final String SERVER_URL = "10.15.17.40:8080/SupCarDealer";
	
	public static final int REQUEST_CODE_LOGIN = 0;
	
	public static final int RESULT_CODE_LOGIN_OK = 0;
	public static final int RESULT_CODE_LOGIN_CANCEL = 1;

	
	
	public static final int REQUEST_CODE_REGISTER = 1;
	
	public static final int RESULT_CODE_REGISTER_OK = 0;
	public static final int RESULT_CODE_REGISTER_CANCEL = 1;
	
	
	
	public static final int REQUEST_CODE_EDIT_PROFILE = 1;
	
	public static final int RESULT_CODE_EDIT_PROFILE_OK = 0;
	public static final int RESULT_CODE_EDIT_PROFILE_CANCEL = 1;
	
	
	public static final int REQUEST_CODE_EDIT_CAR = 1;
	
	public static final int RESULT_CODE_EDIT_CAR_OK = 0;
	public static final int RESULT_CODE_EDIT_CAR_CANCEL = 1;
	
	
	
	public static final int PASSWORD_LENGTH = 8;
}
