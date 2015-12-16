package kr.co.dementor.net;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
//import javax.crypto.SecretKey;
//import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidAlgorithmParameterException;

//import org.apache.commons.codec.binary.Base64;
 
public class AES256Cipher {
	 //암호화
	public static String AES_Encode(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
		/*
		char* k5 = "150";
		char* k4 = "@f";
		char* k3 = "e891Ab!7eceD43";
		char* k2 = "#7";
		char* k1 = "d2f43f0a13b";
		*/
		String secretKey = "d2f43f0a13b#7e891Ab!7eceD43@f150";
		
		SecretKeySpec newKey;
		Cipher cipher = null;
		try {
			newKey = new SecretKeySpec(secretKey.getBytes("utf-8"), "AES256");
			cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");			
			cipher.init(Cipher.ENCRYPT_MODE, newKey);
			return Base64.encodeToString(cipher.doFinal(str.getBytes("utf-8")), Base64.DEFAULT);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}
 
	//복호화
	public static String AES_Decode(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
		/*
		char* k5 = "z8";
		char* k4 = "D43$q1";
		char* k3 = "7ece";
		char* k2 = "8T1Ab!";
		char* k1 = "W3gc9f0a13!#7e";
		*/
		String secretKey = "W3gc9f0a13!#7e8T1Ab!7eceD43$q1z8";
		
		SecretKeySpec newKey;
		Cipher cipher = null;
		
		try {
			byte[] b = Base64.decode(str, Base64.DEFAULT);
			newKey = new SecretKeySpec(secretKey.getBytes("utf-8"),"AES256");
			cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
			cipher.init(Cipher.DECRYPT_MODE, newKey);
			return new String(cipher.doFinal(b), "utf-8");
		} catch (Exception e1) {	
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		return null;
	}
}