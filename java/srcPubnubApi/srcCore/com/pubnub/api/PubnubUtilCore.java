package com.pubnub.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author PubnubCore
 */
class PubnubUtilCore {

    static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String escapeJava(String a) {
//        return StringEscapeUtils.escapeJava(a);
        StringBuilder result = new StringBuilder(a);
        int len = a.length();
        for (int i=0, j=0; i < len; i++, j++) {
            final char in = a.charAt(i);
            final char escaped = escapeJavaChar(in);
            if (escaped != 0) {
                result.insert(j++, '\\');
                result.setCharAt(j, escaped);
            } else {
                //int point = a.codePointAt(i);
                int point = a.charAt(i);
                j = unicodeEscape(result, point, j);
                if (point > '\uffff') {
                    // multi-char
                    i++;
                }
            }
        }
        return result.toString();
    }

    /**
     * @return 0 if the char need not be escaped,
     *  else the character that should replace `in`
     *  after being escaped with a backslash.
     */
    private static char escapeJavaChar(char in) {
        if (in == '"' || in == '\\') {
            return in;
        }

        // JAVA_CTRL_CHARS_ESCAPE
        if (in == '\b') {
            return 'b';
        } else if (in == '\n') {
            return 'n';
        } else if (in == '\t') {
            return 't';
        } else if (in == '\f') {
            return 'f';
        } else if (in == '\r') {
            return 'r';
        }

        return 0;
    }

    /**
     * Given a char, and its location in a StringBuilder,
     *  replace it with an escaped unicode if necessary
     * @return The new index into StringBuilder
     */
    private static int unicodeEscape(StringBuilder result, int in, int index) {
        // utf8 within [32, 127] are fine
        if (in >= 32 && in <= 127) {
            return index;
        }

        if (in > '\uffff') {
            final char[] surrogatePair = Character.toChars(in);
            final String first = hex(surrogatePair[0]);
            final String second = hex(surrogatePair[1]);

            // ensure we have capacity to replace 2 chars
            // (IE a surrogate pair) with \\u + first + \\u + second
            result.ensureCapacity(result.capacity() + 2
                    + first.length() + second.length());
            result.delete(index, index + 2);
            result.insert(index++, '\\');
            result.insert(index++, 'u');
            result.insert(index, first);
            index += first.length();

            result.insert(index++, '\\');
            result.insert(index++, 'u');
            result.insert(index, second);
            index += second.length() - 1;
        } else {
            // ensure we have capacity to replace 1 char with 6
            result.ensureCapacity(result.capacity() + 5);
            result.deleteCharAt(index);
            result.insert(index++, '\\');
            result.insert(index++, 'u');
            result.insert(index++, HEX_DIGITS[in >> 12 & 15]);
            result.insert(index++, HEX_DIGITS[in >> 8 & 15]);
            result.insert(index++, HEX_DIGITS[in >> 4 & 15]);
            result.insert(index, HEX_DIGITS[in & 15]);
        }

        return index;
    }
    

    private static String hex(int codepoint) {
        return Integer.toHexString(codepoint).toUpperCase();
    }

    
    static void addToHash(Hashtable h, String name, Object object) {
        if (object != null) {
            h.put(name, object);
        }
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

    /**
     * Returns string keys in a hashtable as array of string
     *
     * @param ht
     *            , Hashtable
     * @return , string array with hash keys string
     */
    public static synchronized String[] hashtableKeysToArray(Hashtable ht) {
        return hashtableKeysToArray(ht, null);
    }

    public static synchronized String[] hashtableKeysToArray(Hashtable ht, String exclude) {
        Vector v = new Vector();
        String[] sa = null;
        int count = 0;

        Enumeration e = ht.keys();
        while (e.hasMoreElements()) {
            String s = (String) e.nextElement();

            if (exclude != null && s.indexOf(exclude) != -1) {
                continue;
            }

            v.addElement(s);
            count++;
        }

        sa = new String[count];
        v.copyInto(sa);
        return sa;

    }

    /**
     * Returns string keys in a hashtable as delimited string
     *
     * @param ht
     *            , Hashtable
     * @param delimiter
     *            , String
     * @param exclude
     *            , exclude channel if present as substring
     * @return , string array with hash keys string
     */
    public static synchronized String hashTableKeysToDelimitedString(Hashtable ht, String delimiter, String exclude) {

        StringBuffer sb = new StringBuffer();
        boolean first = true;
        Enumeration e = ht.keys();

        while (e.hasMoreElements()) {

            String s = (String) e.nextElement();

            if (exclude != null) {
                if (s.indexOf(exclude) != -1) {
                    continue;
                }
            }
            if (first) {
                sb.append(s);
                first = false;
            } else {
                sb.append(delimiter).append(s);
            }
        }
        return sb.toString();
    }

    public static synchronized String hashTableKeysToSortedSuffixString(Hashtable ht, String delimiter,
            String lastSuffix) {

        StringBuffer sb = new StringBuffer();
        StringBuffer sbPresence = new StringBuffer();
        boolean first = true;
        boolean firstPresence = true;
        Enumeration e = ht.keys();

        while (e.hasMoreElements()) {

            String s = (String) e.nextElement();

            if (s.endsWith(lastSuffix)) {
                if (firstPresence) {
                    sbPresence.append(s);
                    firstPresence = false;
                } else {
                    sbPresence.append(delimiter).append(s);
                }
            } else {
                if (first) {
                    sb.append(s);
                    first = false;
                } else {
                    sb.append(delimiter).append(s);
                }
            }
        }

        if (sb.length() > 0 && sbPresence.length() > 0) {
            return sb.toString() + delimiter + sbPresence.toString();
        } else if (sb.length() > 0 && sbPresence.length() == 0) {
            return sb.toString();
        } else if (sb.length() == 0 && sbPresence.length() > 0) {
            return sbPresence.toString();
        } else {
            return "";
        }
    }

    /**
     * Returns string keys in a hashtable as delimited string
     *
     * @param ht
     *            , Hashtable
     * @param delimiter
     *            , String
     * @return , string array with hash keys string
     */
    public static String hashTableKeysToDelimitedString(Hashtable ht, String delimiter) {
        return hashTableKeysToDelimitedString(ht, delimiter, null);
    }

    static Hashtable hashtableClone(Hashtable ht) {
        if (ht == null)
            return null;

        Hashtable htresp = new Hashtable();
        Enumeration e = ht.keys();

        while (e.hasMoreElements()) {
            Object element = e.nextElement();
            htresp.put(element, ht.get(element));
        }
        return htresp;
    }

    static Hashtable hashtableClone(Hashtable ht1, Hashtable ht2) {
        if (ht1 == null && ht2 == null)
            return null;

        Hashtable htresp = new Hashtable();

        if (ht1 != null) {
            Enumeration e = ht1.keys();
            while (e.hasMoreElements()) {
                Object element = e.nextElement();
                htresp.put(element, ht1.get(element));
            }
        }
        if (ht2 != null) {
            Enumeration e = ht2.keys();
            while (e.hasMoreElements()) {
                Object element = e.nextElement();
                htresp.put(element, ht2.get(element));
            }
        }
        return htresp;
    }

    static Hashtable hashtableMerge(Hashtable dst, Hashtable src) {
        if (dst == null)
            return src;
        if (src == null)
            return dst;

        Enumeration e = src.keys();

        while (e.hasMoreElements()) {
            Object element = e.nextElement();
            dst.put(element, src.get(element));
        }
        return dst;
    }

    /**
     * Parse Json, change json string to string
     *
     * @param obj
     *            JSON data in string format
     *
     * @return JSONArray or JSONObject or String
     */
    static Object parseJSON(Object obj, boolean enc) {

        if (obj instanceof String && enc) {
            String s = (String)obj;
            if (s.endsWith("\"") && s.startsWith("\"") &&
                    s.length() > 1)
                obj = ((String) obj).substring(1, ((String) obj).length() - 1);
        }

        return obj;
    }

    static boolean isEmptyString(String s) {
        return (s == null || s.length() == 0);
    }
}
