package com.pubnub.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import org.json.me.*;

public class PubnubUtil extends PubnubUtilCore {

    public static String stringReplaceAll(String s, String a, String b) {
        return s.replaceAll(a, b);
    }

    public static String escapeJava(String s) {
        s = s.replaceAll("\"", "\\\\\"");
        return s;
    }
		

    private static String replace(String str, String pattern, String replace) {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();

        while ((e = str.indexOf(pattern, s)) >= 0) {
            result.append(str.substring(s, e));
            result.append(replace);
            s = e + pattern.length();
        }
        result.append(str.substring(s));
        return result.toString();
    }

    /**
     * Returns encoded String
     * 
     * @param sUrl
     *            , input string
     * @return , encoded string
     */
    public static String pamEncode(String sUrl) {
        /* !'()*~ */

        String encoded = urlEncode(sUrl);
        if (encoded != null) {
            encoded = replace(encoded, "*", "%2A");
            encoded = replace(encoded, "!", "%21");
            encoded = replace(encoded, "'", "%27");
            encoded = replace(encoded, "(", "%28");
            encoded = replace(encoded, ")", "%29");
            encoded = replace(encoded, "[", "%5B");
            encoded = replace(encoded, "]", "%5D");
            encoded = replace(encoded, "~", "%7E");
        }
        return encoded;
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
            return replace(encode(sUrl, "UTF-8"), "+", "%20");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String encode(String s, String enc) throws UnsupportedEncodingException {

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
                out.append((char) c);
                wroteUnencodedChar = true;
            } else {
                try {
                    if (wroteUnencodedChar) {
                        writer = new OutputStreamWriter(buf, enc);
                        wroteUnencodedChar = false;
                    }
                    writer.write(c);
                    if (c >= 0xD800 && c <= 0xDBFF) {

                        if ((i + 1) < s.length()) {
                            int d = (int) s.charAt(i + 1);
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

        return (needToChange ? out.toString() : s);
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
                return (char) ('0' + digit);
            }
            return (char) ('a' - 10 + digit);
        }
    }

    public static boolean dontNeedEncoding(int ch) {
        int len = _dontNeedEncoding.length();
        boolean en = false;
        for (int i = 0; i < len; i++) {
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
    

    
    /**
     * Takes source and delimiter string as inputs and returns splitted string
     * in form of tokens in String array
     *
     * @param source
     *            , input String
     * @param delimiter
     *            , delimiter to split on
     * @return String[] , tokens in and array
     */
    public static String[] splitString(String source, String delimiter) {
        System.out.println("[" + delimiter + "] : " + source );
        int delimiterCount = 0;
        int index = 0;
        String tmpStr = source;

        String[] splittedList;

        while ((index = tmpStr.indexOf(delimiter)) != -1) {

            tmpStr = tmpStr.substring(index + delimiter.length());
            delimiterCount++;
        }

        splittedList = new String[delimiterCount + 1];

        int counter = 0;
        tmpStr = source;
        System.out.println("Delimiter Count : " + delimiterCount);
        do {
            int nextIndex = tmpStr.indexOf(delimiter, index + 1);
            
            System.out.println("Next Index : " + nextIndex);
            
            if (nextIndex != -1) {
                System.out.println(tmpStr);
                System.out.println(index);
                System.out.println(index + delimiter.length() + " : " + nextIndex);
                splittedList[counter++] = tmpStr.substring(index + delimiter.length(), nextIndex);
                tmpStr = tmpStr.substring(nextIndex);

            } else {
                splittedList[counter++] = tmpStr.substring(index + delimiter.length());
                tmpStr = tmpStr.substring(index + 1);
            }
        } while ((index = tmpStr.indexOf(delimiter)) != -1);

        return splittedList;
    }

    /**
     * Takes String[] of tokens, and String delimiter as input and returns
     * joined String
     *
     * @param sourceArray
     *            , input tokens in String array
     * @param delimiter
     *            , delimiter to join on
     * @return String , string of tokens joined by delimiter
     */
    public static String joinString(String[] sourceArray, String delimiter) {
        if (sourceArray == null || delimiter == null || sourceArray.length <= 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < sourceArray.length - 1; i++) {
            sb.append(sourceArray[i]).append(delimiter);
        }
        sb.append(sourceArray[sourceArray.length - 1]);

        return sb.toString();
    }
}
