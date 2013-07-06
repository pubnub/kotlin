package com.pubnub.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * PubnubUtil class provides utility methods like urlEncode etc
 * @author Pubnub
 *
 */
public class PubnubUtil extends PubnubUtilCore {

    /**
     * Returns encoded String
     *
     * @param sUrl
     *            , input string
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
     * Convert input String to JSONObject, JSONArray, or String
     *
     * @param str
     *            JSON data in string format
     *
     * @return JSONArray or JSONObject or String
     */
    static Object stringToJSON(String str) {
        try {
            return new JSONArray(str);
        } catch (JSONException e) {
        }
        try {
            return new JSONObject(str);
        } catch (JSONException ex) {
        }
        try {
            return Integer.parseInt(str);
        } catch (Exception ex) {
        }
        try {
            return Double.parseDouble(str);
        } catch (Exception ex) {
        }
        return str;
    }
}
