package com.pubnub.api;

import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.vendor.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


public class PubNubUtil {

    private PubNubUtil() {
    }

    public static String joinString(List<String> val, String delim) {
        StringBuilder builder = new StringBuilder();
        for (String l : val) {
            builder.append(l);
            builder.append(",");
        }

        return builder.toString().substring(0, builder.toString().length() - 1);

    }

    /**
     * Returns encoded String
     *
     * @param sUrl , input string
     * @return , encoded string
     */
    public static String pamEncode(String sUrl) {
        /* !'()*~ */

        String encoded = urlEncode(sUrl);
        if (encoded != null) {
            encoded = encoded.
                    replace("*", "%2A")
                    .replace("!", "%21")
                    .replace("'", "%27")
                    .replace("(", "%28")
                    .replace(")", "%29")
                    .replace("[", "%5B")
                    .replace("]", "%5D")
                    .replace("~", "%7E");
        }
        return encoded;
    }


    /**
     * Returns encoded String
     *
     * @param sUrl , input string
     * @return , encoded string
     */
    public static String urlEncode(String sUrl) {
        try {
            return URLEncoder.encode(sUrl, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Returns decoded String
     *
     * @param sUrl
     *            , input string
     * @return , decoded string
     */
    public static String urlDecode(String sUrl) {
        try {
            return URLDecoder.decode(sUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String preparePamArguments(Map<String, String> pamArgs) {
        Set<String> pamKeys = new TreeSet(pamArgs.keySet());
        String stringifiedArguments = "";
        int i = 0;

        for (String pamKey : pamKeys) {
            if (i != 0) {
                stringifiedArguments = stringifiedArguments.concat("&");
            }


            stringifiedArguments = stringifiedArguments.concat(pamKey).concat("=").concat(pamEncode(pamArgs.get(pamKey)));

            i += 1;
        }

        return stringifiedArguments;
    }

    public static String signSHA256(String key, String data) throws PubNubException {
        Mac sha256HMAC;
        byte[] hmacData;
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(Charset.forName("UTF-8")), "HmacSHA256");

        try {
            sha256HMAC = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_CRYPTO_ERROR).errormsg(e.getMessage()).build();
        }

        try {
            sha256HMAC.init(secretKey);
        } catch (InvalidKeyException e) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_CRYPTO_ERROR).errormsg(e.getMessage()).build();
        }

        try {
            hmacData = sha256HMAC.doFinal(data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_CRYPTO_ERROR).errormsg(e.getMessage()).build();
        }

        return new String(Base64.encode(hmacData, 0), Charset.forName("UTF-8")).replace('+', '-').replace('/', '_').replace("\n", "");
    }

    public static String replaceLast(String string, String toReplace, String replacement) {
        int pos = string.lastIndexOf(toReplace);
        if (pos > -1) {
            return string.substring(0, pos).concat(replacement).concat(string.substring(pos + toReplace.length(), string.length()));
        } else {
            return string;
        }
    }

}
