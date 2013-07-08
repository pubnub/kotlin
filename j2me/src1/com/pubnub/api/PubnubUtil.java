package com.pubnub.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import org.json.me.*;

public class PubnubUtil extends PubnubUtilCore {


    private static String replace( String str, String pattern, String replace ) {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();

        while ( (e = str.indexOf( pattern, s ) ) >= 0 ) {
            result.append(str.substring( s, e ) );
            result.append( replace );
            s = e+pattern.length();
        }
        result.append( str.substring( s ) );
        return result.toString();
    }

    /**
     * Returns encoded String
     *
     * @param sUrl
     *            , input string
     * @return , encoded string
     */
    public static String urlEncode(String sUrl) {
        try {
            return replace(encode(sUrl, "UTF-8"),"+", "%20");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }


    public static String encode(String s, String enc)
    throws UnsupportedEncodingException {

        boolean needToChange = false;
        boolean wroteUnencodedChar = false;
        int maxBytesPerChar = 10;
        StringBuffer out = new StringBuffer(s.length());
        ByteArrayOutputStream buf = new ByteArrayOutputStream(maxBytesPerChar);

        OutputStreamWriter writer = new OutputStreamWriter(buf, enc);

        for (int i = 0; i < s.length(); i++) {
            int c = (int) s.charAt(i);
            if (dontNeedEncoding(c)) {
                if (c == ' ') {
                    c = '+';
                    needToChange = true;
                }
                out.append((char)c);
                wroteUnencodedChar = true;
            } else {
                try {
                    if (wroteUnencodedChar) {
                        writer = new OutputStreamWriter(buf, enc);
                        wroteUnencodedChar = false;
                    }
                    writer.write(c);
                    if (c >= 0xD800 && c <= 0xDBFF) {

                        if ( (i+1) < s.length()) {
                            int d = (int) s.charAt(i+1);
                            if (d >= 0xDC00 && d <= 0xDFFF) {
                                writer.write(d);
                                i++;
                            }
                        }
                    }
                    writer.flush();
                } catch (IOException e) {
                    buf.reset();
                    continue;
                }
                byte[] ba = buf.toByteArray();
                for (int j = 0; j < ba.length; j++) {
                    out.append('%');
                    char ch = CCharacter.forDigit((ba[j] >> 4) & 0xF, 16);
                    out.append(ch);
                    ch = CCharacter.forDigit(ba[j] & 0xF, 16);
                    out.append(ch);
                }
                buf.reset();
                needToChange = true;
            }
        }

        return (needToChange? out.toString() : s);
    }

    static class CCharacter {
        public static char forDigit(int digit, int radix) {
            if ((digit >= radix) || (digit < 0)) {
                return '\0';
            }
            if ((radix < Character.MIN_RADIX) || (radix > Character.MAX_RADIX)) {
                return '\0';
            }
            if (digit < 10) {
                return (char)('0' + digit);
            }
            return (char)('a' - 10 + digit);
        }
    }
    public static boolean dontNeedEncoding(int ch) {
        int len = _dontNeedEncoding.length();
        boolean en = false;
        for (int i =0; i< len; i++) {
            if (_dontNeedEncoding.charAt(i) == ch) {
                en = true;
                break;
            }
        }

        return en;
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
        return str;
    }
    private static String _dontNeedEncoding = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ -_.*";
}
