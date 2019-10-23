package com.pubnub.api;


import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.vendor.Base64;
import lombok.extern.java.Log;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okio.Buffer;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Log
public class PubNubUtil {

    private static final String CHARSET = "UTF-8";

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
            return URLEncoder.encode(stringToEncode, CHARSET).replace("+", "%20");
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
            return URLDecoder.decode(stringToEncode, CHARSET);
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

    public static String signSHA256(String key, String data) throws PubNubException, UnsupportedEncodingException {
        Mac sha256HMAC;
        byte[] hmacData;
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(CHARSET), "HmacSHA256");

        try {
            sha256HMAC = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw PubNubException.builder()
                    .pubnubError(PubNubErrorBuilder.PNERROBJ_CRYPTO_ERROR)
                    .errormsg(e.getMessage())
                    .build();
        }

        try {
            sha256HMAC.init(secretKey);
        } catch (InvalidKeyException e) {
            throw PubNubException.builder()
                    .pubnubError(PubNubErrorBuilder.PNERROBJ_CRYPTO_ERROR)
                    .errormsg(e.getMessage())
                    .build();
        }

        hmacData = sha256HMAC.doFinal(data.getBytes(CHARSET));

        return new String(Base64.encode(hmacData, 0), CHARSET)
                .replace('+', '-')
                .replace('/', '_')
                .replace("\n", "");
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

    public static Request signRequest(Request originalRequest, PNConfiguration pnConfiguration, int timestamp) {
        // only sign if we have a secret key in place.
        if (pnConfiguration.getSecretKey() == null) {
            return originalRequest;
        }

        String signature = generateSignature(pnConfiguration, originalRequest, timestamp);

        HttpUrl rebuiltUrl = originalRequest.url().newBuilder()
                .addQueryParameter("timestamp", String.valueOf(timestamp))
                .addQueryParameter("signature", signature)
                .build();

        return originalRequest.newBuilder().url(rebuiltUrl).build();
    }

    private static String generateSignature(PNConfiguration configuration, Request request, int timestamp) {
        boolean isV2Signature;

        StringBuilder signatureBuilder = new StringBuilder();
        String requestURL = request.url().encodedPath();

        Map<String, String> queryParams = new HashMap<>();
        for (String queryKey : request.url().queryParameterNames()) {
            queryParams.put(queryKey, request.url().queryParameter(queryKey));
        }
        queryParams.put("timestamp", String.valueOf(timestamp));
        String encodedQueryString = PubNubUtil.preparePamArguments(queryParams);

        isV2Signature = !(requestURL.startsWith("/publish") && request.method().equalsIgnoreCase("post"));

        if (!isV2Signature) {
            signatureBuilder.append(configuration.getSubscribeKey()).append("\n");
            signatureBuilder.append(configuration.getPublishKey()).append("\n");
            signatureBuilder.append(requestURL).append("\n");
            signatureBuilder.append(encodedQueryString);
        } else {
            signatureBuilder.append(request.method().toUpperCase()).append("\n");
            signatureBuilder.append(configuration.getPublishKey()).append("\n");
            signatureBuilder.append(requestURL).append("\n");
            signatureBuilder.append(encodedQueryString).append("\n");
            signatureBuilder.append(requestBodyToString(request));
        }

        String signature = "";
        try {
            signature = PubNubUtil.signSHA256(configuration.getSecretKey(), signatureBuilder.toString());
            if (isV2Signature) {
                signature = removeTrailingEqualSigns(signature);
                signature = "v2.".concat(signature);
            }
        } catch (PubNubException | UnsupportedEncodingException e) {
            log.warning("signature failed on SignatureInterceptor: " + e.toString());
        }

        return signature;
    }

    public static String removeTrailingEqualSigns(String signature) {
        String cleanSignature = signature;

        while ((cleanSignature.charAt(cleanSignature.length() - 1) == '=')) {
            cleanSignature = cleanSignature.substring(0, cleanSignature.length() - 1);
        }
        return cleanSignature;
    }

    private static String requestBodyToString(final Request request) {
        if (request.body() == null) {
            return "";
        }
        try {
            Buffer buffer = new Buffer();
            request.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
