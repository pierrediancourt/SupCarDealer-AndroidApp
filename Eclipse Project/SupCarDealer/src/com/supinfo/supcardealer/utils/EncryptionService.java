package com.supinfo.supcardealer.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionService {
	
	static String salt = "h4k3rZn0tg0nN4h4ck";

	/**
	 * Crypte une chaine de caract�re
	 * @param 	strToEncrypt	Chaine de caract�re � encrypter
	 * @return 	Chaine de caract�re encrypt�e selon la formule : MD5(chaine+salt)
	 */
	public static String encrypt(String strToEncrypt) {
		String text = strToEncrypt + salt;
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		byte[] encryptedText = md.digest(text.getBytes());
		
		return (String) new BigInteger(1,encryptedText).toString(16);
	}
	
}
