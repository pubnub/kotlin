package com.pubnub.api;

import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.vendor.Base64;
import lombok.extern.java.Log;
import okhttp3.HttpUrl;
import okhttp3.Request;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Log
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

    public static String joinLong(List<Long> val, String delim) {
        StringBuilder builder = new StringBuilder();
        for (Long l : val) {
            builder.append(Long.toString(l).toLowerCase());
            builder.append(",");
        }

        return builder.toString().substring(0, builder.toString().length() - 1);

    }

    /**
     * Returns encoded String
     *
     * @param stringToEncode , input string
     * @return , encoded string
     */
    public static String pamEncode(String stringToEncode) {
        /* !'()*~ */

        String encoded = urlEncode(stringToEncode);
        if (encoded != null) {
            encoded = encoded
                    .replace("*", "%2A")
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
     * @param stringToEncode , input string
     * @return , encoded string
     */
    public static String urlEncode(String stringToEncode) {
        try {
            return URLEncoder.encode(stringToEncode, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Returns decoded String
     *
     * @param stringToEncode , input string
     * @return , decoded string
     */
    public static String urlDecode(String stringToEncode) {
        try {
            return URLDecoder.decode(stringToEncode, "UTF-8");
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


            stringifiedArguments =
                    stringifiedArguments.concat(pamKey).concat("=").concat(pamEncode(pamArgs.get(pamKey)));

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
            return string.substring(0, pos).concat(replacement).concat(string.substring(pos + toReplace.length(),
                    string.length()));
        } else {
            return string;
        }
    }

    public static Request requestSigner(Request originalRequest, PNConfiguration pnConfiguration, int timestamp) {
        // only sign if we have a secret key in place.
        if (pnConfiguration.getSecretKey() == null) {
            return originalRequest;
        }

        HttpUrl url = originalRequest.url();
        String requestURL = url.encodedPath();
        Map<String, String> queryParams = new HashMap<>();
        String signature = "";

        for (String queryKey : url.queryParameterNames()) {
            queryParams.put(queryKey, url.queryParameter(queryKey));
        }

        queryParams.put("timestamp", String.valueOf(timestamp));

        String signInput = pnConfiguration.getSubscribeKey() + "\n" + pnConfiguration.getPublishKey() + "\n";

        if (requestURL.startsWith("/v1/auth/audit")) {
            signInput += "audit" + "\n";
        } else if (requestURL.startsWith("/v1/auth/grant")) {
            signInput += "grant" + "\n";
        } else {
            signInput += requestURL + "\n";
        }

        signInput += PubNubUtil.preparePamArguments(queryParams);

        try {
            signature = PubNubUtil.signSHA256(pnConfiguration.getSecretKey(), signInput);
        } catch (PubNubException e) {
            log.warning("signature failed on SignatureInterceptor: " + e.toString());
        }

        HttpUrl rebuiltUrl = url.newBuilder()
                .addQueryParameter("timestamp", String.valueOf(timestamp))
                .addQueryParameter("signature", signature)
                .build();

        return originalRequest.newBuilder().url(rebuiltUrl).build();
    }

}
