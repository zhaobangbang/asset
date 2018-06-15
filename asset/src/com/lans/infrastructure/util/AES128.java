package com.lans.infrastructure.util;

import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AES128 {
	public static String CIPHER_ALGORITHM = "AES"; // optional value
													// AES/DES/DESede

	public AES128() {
		// TODO Auto-generated constructor stub
	}
	
	public static SecretKeySpec getSecretKey(String password) throws Exception{  
        if(password == null){  
        	password = "";  
        }  
        KeyGenerator kgen = KeyGenerator.getInstance(CIPHER_ALGORITHM);
    	kgen.init(128, new SecureRandom(password.getBytes()));
    	
    	SecretKey secretKey = kgen.generateKey();
    	byte[] enCodeFormat = secretKey.getEncoded();
    	SecretKeySpec key = new SecretKeySpec(enCodeFormat, CIPHER_ALGORITHM);
    	
        return key;  
    }  
	
	public static String encryptThenBase64Encode(String content, String password) {
		byte[] contentBytes = content.getBytes();

		try {
			SecretKeySpec key = getSecretKey(password);

			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] result = cipher.doFinal(contentBytes);

			return new BASE64Encoder().encode(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String decryptAfterBase64Decode(String content, String password) {
		byte[] contentBytes;

		try {
			contentBytes = new BASE64Decoder().decodeBuffer(content);
		} catch (IOException e1) {
			return null;
		}

		try {
			SecretKeySpec key = getSecretKey(password);

			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] result = cipher.doFinal(contentBytes);

			return new String(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static byte[] encrypt(byte[] content, String password) {
        try {
        	SecretKeySpec key = getSecretKey(password);
        	
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] result = cipher.doFinal(content);
			
            return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
        
		return null;
	}
	
	public static byte[] decrypt(byte[] content, String password) {
		try {
			SecretKeySpec key = getSecretKey(password);
        	
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] result = cipher.doFinal(content);
			
            return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
        
		return null;
	}

}
