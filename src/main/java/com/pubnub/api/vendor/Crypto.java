package com.pubnub.api.vendor;

import com.google.gson.Gson;
import com.pubnub.api.PubNubError;
import com.pubnub.api.PubNubException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;


public class Crypto {

    byte[] keyBytes = null;
    byte[] ivBytes = null;
    String initializationVector = "0123456789012345";
    String cipherKey;
    boolean INIT = false;

    public Crypto(String cipherKey) {
        this.cipherKey = cipherKey;
    }

    public Crypto(String cipherKey, String customInitializationVector) {
        if (customInitializationVector != null) {
            this.initializationVector = customInitializationVector;
        }

        this.cipherKey = cipherKey;
    }

    public void initCiphers() throws PubNubException {
        if (INIT)
            return;
        try {

            keyBytes = new String(hexEncode(sha256(this.cipherKey.getBytes("UTF-8"))), "UTF-8")
                    .substring(0, 32)
                    .toLowerCase().getBytes("UTF-8");
            ivBytes = initializationVector.getBytes("UTF-8");
            INIT = true;
        } catch (UnsupportedEncodingException e) {
            throw newCryptoError(11, e);
        }
    }

    public static byte[] hexEncode(byte[] input) throws PubNubException {
        StringBuffer result = new StringBuffer();
        for (byte byt : input)
            result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        try {
            return result.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw newCryptoError(12, e);
        }
    }

    public static PubNubException newCryptoError(int code, Exception exception) {
        PubNubException pubNubException = new PubNubException();
        pubNubException.setPubnubError(PubNubError.CRYPTO_ERROR);
        pubNubException.setErrorMessage(exception.getClass().getSimpleName() + " " + exception.getMessage() + " " + code);
        return pubNubException;
    }

    public String encrypt(String input) throws PubNubException {
        try {
            initCiphers();
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = null;
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
            return new String(Base64.encode(cipher.doFinal(input.getBytes("UTF-8")), 0), Charset.forName("UTF-8"));
        } catch (Exception e) {
            throw newCryptoError(0, e);
        }

    }

    /**
     * Decrypt
     *
     * @param cipher_text
     * @return String
     * @throws PubNubException
     */
    public String decrypt(String cipher_text) throws PubNubException {
        try {
            initCiphers();
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
            return new String(cipher.doFinal(Base64.decode(cipher_text, 0)), "UTF-8");
        } catch (Exception e) {
            throw newCryptoError(0, e);
        }
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * Get MD5
     *
     * @param input
     * @return byte[]
     * @throws PubNubException
     */
    public static byte[] md5(String input) throws PubNubException {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            byte[] hashedBytes = digest.digest(input.getBytes("UTF-8"));
            return hashedBytes;
        } catch (Exception e) {
            throw newCryptoError(0, e);
        }
    }

    /**
     * Get SHA256
     *
     * @param input
     * @return byte[]
     * @throws PubNubException
     */
    public static byte[] sha256(byte[] input) throws PubNubException {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(input);
            return hashedBytes;
        } catch (Exception e) {
            throw newCryptoError(0, e);
        }
    }

}
