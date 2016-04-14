package com.pubnub.api.core;

import com.pubnub.api.core.utils.Base64;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;



@Slf4j
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

    public void InitCiphers() throws PubnubException {
        if (INIT)
            return;
        try {

            keyBytes = new String(hexEncode(sha256(this.cipherKey.getBytes("UTF-8"))), "UTF-8")
                    .substring(0, 32)
                    .toLowerCase().getBytes("UTF-8");
            ivBytes = initializationVector.getBytes("UTF-8");
            INIT = true;
        } catch (UnsupportedEncodingException e) {
            throw new PubnubException(newCryptoError(11, e.toString()));
        }
    }

    public static byte[] hexEncode(byte[] input) throws PubnubException {
        StringBuffer result = new StringBuffer();
        for (byte byt : input)
            result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        try {
            return result.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new PubnubException(newCryptoError(12, e.toString()));
        }
    }

    private static PubnubError newCryptoError(int code, String message) {
        return PubnubError.getErrorObject(PubnubError.PNERROBJ_CRYPTO_ERROR, code, message);
    }

    public String encrypt(String input) throws PubnubException {
        try {
            InitCiphers();
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = null;
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
            return new String(Base64.encode(cipher.doFinal(input.getBytes("UTF-8")), 0));
        } catch (NoSuchAlgorithmException e) {
            throw new PubnubException(e.toString());
        } catch (NoSuchPaddingException e) {
            throw new PubnubException(e.toString());
        } catch (InvalidKeyException e) {
            throw new PubnubException(e.toString());
        } catch (InvalidAlgorithmParameterException e) {
            throw new PubnubException(e.toString());
        } catch (UnsupportedEncodingException e) {
            throw new PubnubException(e.toString());
        } catch (IllegalBlockSizeException e) {
            throw new PubnubException(e.toString());
        } catch (BadPaddingException e) {
            throw new PubnubException(e.toString());
        }

    }

    /**
     * Decrypt
     *
     * @param cipher_text
     * @return String
     * @throws PubnubException
     */
    public String decrypt(String cipher_text) throws PubnubException {
        try {
            InitCiphers();
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
            return new String(cipher.doFinal(Base64.decode(cipher_text, 0)), "UTF-8");
        } catch (IllegalArgumentException e) {
            throw new PubnubException(e.toString());
        } catch (UnsupportedEncodingException e) {
            throw new PubnubException(e.toString());
        } catch (IllegalBlockSizeException e) {
            throw new PubnubException(e.toString());
        } catch (BadPaddingException e) {
            throw new PubnubException(e.toString());
        } catch (InvalidKeyException e) {
            throw new PubnubException(e.toString());
        } catch (InvalidAlgorithmParameterException e) {
            throw new PubnubException(e.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new PubnubException(e.toString());
        } catch (NoSuchPaddingException e) {
            throw new PubnubException(e.toString());
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