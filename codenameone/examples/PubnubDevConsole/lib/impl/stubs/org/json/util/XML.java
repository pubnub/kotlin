package org.json.util;


/**
 *  This provides static methods to convert an XML text into a JSONObject,
 *  and to covert a JSONObject into an XML text.
 *  @author JSON.org
 *  @version 2
 */
public class XML {

	/**
	 * The Character '&'. 
	 */
	public static final Character AMP;

	/**
	 * The Character '''. 
	 */
	public static final Character APOS;

	/**
	 * The Character '!'. 
	 */
	public static final Character BANG;

	/**
	 * The Character '='. 
	 */
	public static final Character EQ;

	/**
	 * The Character '>'. 
	 */
	public static final Character GT;

	/**
	 * The Character '<'. 
	 */
	public static final Character LT;

	/**
	 * The Character '?'. 
	 */
	public static final Character QUEST;

	/**
	 * The Character '"'. 
	 */
	public static final Character QUOT;

	/**
	 * The Character '/'. 
	 */
	public static final Character SLASH;

	public XML() {
	}

	/**
	 *  Replace special characters with XML escapes:
	 *  <pre>
	 *  &amp; <small>(ampersand)</small> is replaced by &amp;amp;
	 *  &lt; <small>(less than)</small> is replaced by &amp;lt;
	 *  &gt; <small>(greater than)</small> is replaced by &amp;gt;
	 *  &quot; <small>(double quote)</small> is replaced by &amp;quot;
	 *  </pre>
	 *  @param string The string to be escaped.
	 *  @return The escaped string.
	 */
	public static String escape(String string) {
	}

	/**
	 *  Convert a well-formed (but not necessarily valid) XML string into a
	 *  JSONObject. Some information may be lost in this transformation
	 *  because JSON is a data format and XML is a document format. XML uses
	 *  elements, attributes, and content text, while JSON uses unordered
	 *  collections of name/value pairs and arrays of values. JSON does not
	 *  does not like to distinguish between elements and attributes.
	 *  Sequences of similar elements are represented as JSONArrays. Content
	 *  text may be placed in a "content" member. Comments, prologs, DTDs, and
	 *  <code>&lt;[ [ ]]></code> are ignored.
	 *  @param string The source string.
	 *  @return A JSONObject containing the structured data from the XML string.
	 *  @throws JSONException
	 */
	public static org.json.JSONObject toJSONObject(String string) {
	}

	/**
	 *  Convert a JSONObject into a well-formed, element-normal XML string.
	 *  @param o A JSONObject.
	 *  @return  A string.
	 *  @throws  JSONException
	 */
	public static String toString(Object o) {
	}

	/**
	 *  Convert a JSONObject into a well-formed, element-normal XML string.
	 *  @param o A JSONObject.
	 *  @param tagName The optional name of the enclosing tag.
	 *  @return A string.
	 *  @throws JSONException
	 */
	public static String toString(Object o, String tagName) {
	}
}
