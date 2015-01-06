package com.pubnub.api;


import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

/**
 * PubNub 3.1 Cryptography
 *
 */
abstract class PubnubCryptoCore {

    byte[] keyBytes = null;
    byte[] ivBytes = null;
    String IV = "0123456789012345";
    String CIPHER_KEY;
    boolean INIT = false;
    protected static Logger log = new Logger(Worker.class);

    public PubnubCryptoCore(String CIPHER_KEY) {
        this.CIPHER_KEY = CIPHER_KEY;
    }
    public PubnubCryptoCore(String CIPHER_KEY, String initialization_vector) {
        if (initialization_vector != null) this.IV = initialization_vector;
        this.CIPHER_KEY = CIPHER_KEY;
    }

    public void InitCiphers() throws PubnubException {
    	if (INIT) return;
    	try {
    		
			keyBytes = new String(hexEncode(sha256(this.CIPHER_KEY.getBytes("UTF-8"))),
			        "UTF-8").substring(0, 32).toLowerCase().getBytes("UTF-8");
			ivBytes = IV.getBytes("UTF-8");
			INIT = true;
		} catch (UnsupportedEncodingException e) {
			throw new PubnubException(newCryptoError(11, e.toString()));
		}
    }


    public static byte[] hexEncode(byte[] input) throws PubnubException {
        StringBuffer result = new StringBuffer();
        for (byte byt : input) result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        try {
			return result.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new PubnubException(newCryptoError(12, e.toString()));
		}
    }
    
    private static PubnubError newCryptoError(int code, String message) {
    	return PubnubError.getErrorObject(PubnubError.PNERROBJ_CRYPTO_ERROR, code, message);
    }
    
    public String encrypt(String input) throws PubnubException  {
		try {
			InitCiphers();
	        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		    SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
			Cipher cipher = null;
			cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
			cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
			return new String(Base64Encoder.encode(cipher.doFinal(input.getBytes("UTF-8"))));
		} catch (NoSuchAlgorithmException e) {
			throw new PubnubException(newCryptoError(13, e.toString()));
		} catch (NoSuchPaddingException e) {
			throw new PubnubException(newCryptoError(14, e.toString()));
		} catch (InvalidKeyException e) {
			throw new PubnubException(newCryptoError(15, e.toString()));
		} catch (InvalidAlgorithmParameterException e) {
			throw new PubnubException(newCryptoError(16, e.toString()));
		} catch (UnsupportedEncodingException e) {
			throw new PubnubException(newCryptoError(17, e.toString()));
		} catch (IllegalBlockSizeException e) {
			throw new PubnubException(newCryptoError(18, e.toString()));
		} catch (BadPaddingException e) {
			throw new PubnubException(newCryptoError(19, e.toString()));
		}

    }

    /**
     * Decrypt
     *
     * @param cipher_text
     * @return String
     * @throws PubnubException 
     */
    public String decrypt(String cipher_text) throws PubnubException  {
    	try {
			InitCiphers();
			AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
			SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
			cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
			return new String(cipher.doFinal(Base64Encoder.decode(cipher_text)), "UTF-8");
		} catch (IllegalArgumentException e) {
 			throw new PubnubException(newCryptoError(111, e.toString()));
 		} catch (UnsupportedEncodingException e) {
			throw new PubnubException(newCryptoError(112, e.toString()));
		} catch (IllegalBlockSizeException e) {
			throw new PubnubException(newCryptoError(113, e.toString()));
		} catch (BadPaddingException e) {
			throw new PubnubException(newCryptoError(114, e.toString()));
		} catch (InvalidKeyException e) {
			throw new PubnubException(newCryptoError(115, e.toString()));
		} catch (InvalidAlgorithmParameterException e) {
			throw new PubnubException(newCryptoError(116, e.toString()));
		} catch (NoSuchAlgorithmException e) {
			throw new PubnubException(newCryptoError(117, e.toString()));
		} catch (NoSuchPaddingException e) {
			throw new PubnubException(newCryptoError(118, e.toString()));
		}
    }


    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                                  .digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * Get MD5
     *
     * @param input
     * @return byte[]
     * @throws PubnubException 
     */
    public static byte[] md5(String input) throws PubnubException {
        MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
	        byte[] hashedBytes = digest.digest(input.getBytes("UTF-8"));
	        return hashedBytes;
		} catch (NoSuchAlgorithmException e) {
			throw new PubnubException(newCryptoError(118, e.toString()));
		} catch (UnsupportedEncodingException e) {
			throw new PubnubException(newCryptoError(119, e.toString()));
		}
    }

    /**
     * Get SHA256
     *
     * @param input
     * @return byte[]
     * @throws PubnubException 
     */
    public static byte[] sha256(byte[] input) throws PubnubException {       
	    MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
	        byte[] hashedBytes = digest.digest(input);
	        return hashedBytes;
		} catch (NoSuchAlgorithmException e) {
			throw new PubnubException(newCryptoError(1111, e.toString()));
		}
    }

}
