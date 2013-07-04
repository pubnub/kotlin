package org.json;


/**
 *  A JSONObject is an unordered collection of name/value pairs. Its
 *  external form is a string wrapped in curly braces with colons between the
 *  names and values, and commas between the values and names. The internal form
 *  is an object having <code>get</code> and <code>opt</code> methods for
 *  accessing the values by name, and <code>put</code> methods for adding or
 *  replacing values by name. The values can be any of these types:
 *  <code>Boolean</code>, <code>JSONArray</code>, <code>JSONObject</code>,
 *  <code>Number</code>, <code>String</code>, or the <code>JSONObject.NULL</code>
 *  object. A JSONObject constructor can be used to convert an external form
 *  JSON text into an internal form whose values can be retrieved with the
 *  <code>get</code> and <code>opt</code> methods, or to convert values into a
 *  JSON text using the <code>put</code> and <code>toString</code> methods.
 *  A <code>get</code> method returns a value if one can be found, and throws an
 *  exception if one cannot be found. An <code>opt</code> method returns a
 *  default value instead of throwing an exception, and so is useful for
 *  obtaining optional values.
 *  <p>
 *  The generic <code>get()</code> and <code>opt()</code> methods return an
 *  object, which you can cast or query for type. There are also typed
 *  <code>get</code> and <code>opt</code> methods that do type checking and type
 *  coersion for you.
 *  <p>
 *  The <code>put</code> methods adds values to an object. For example, <pre>
 *      myString = new JSONObject().put("JSON", "Hello, World!").toString();</pre>
 *  produces the string <code>{"JSON": "Hello, World"}</code>.
 *  <p>
 *  The texts produced by the <code>toString</code> methods strictly conform to
 *  the JSON sysntax rules.
 *  The constructors are more forgiving in the texts they will accept:
 *  <ul>
 *  <li>An extra <code>,</code>&nbsp;<small>(comma)</small> may appear just
 *      before the closing brace.</li>
 *  <li>Strings may be quoted with <code>'</code>&nbsp;<small>(single
 *      quote)</small>.</li>
 *  <li>Strings do not need to be quoted at all if they do not begin with a quote
 *      or single quote, and if they do not contain leading or trailing spaces,
 *      and if they do not contain any of these characters:
 *      <code>{ } [ ] / \ : , = ; #</code> and if they do not look like numbers
 *      and if they are not the reserved words <code>true</code>,
 *      <code>false</code>, or <code>null</code>.</li>
 *  <li>Keys can be followed by <code>=</code> or <code>=></code> as well as
 *      by <code>:</code>.</li>
 *  <li>Values can be followed by <code>;</code> <small>(semicolon)</small> as
 *      well as by <code>,</code> <small>(comma)</small>.</li>
 *  <li>Numbers may have the <code>0-</code> <small>(octal)</small> or
 *      <code>0x-</code> <small>(hex)</small> prefix.</li>
 *  <li>Comments written in the slashshlash, slashstar, and hash conventions
 *      will be ignored.</li>
 *  </ul>
 *  @author JSON.org
 *  @version 2
 */
public class JSONObject {

	public static final Boolean TRUE;

	public static final Boolean FALSE;

	/**
	 *  It is sometimes more convenient and less ambiguous to have a
	 *  <code>NULL</code> object than to use Java's <code>null</code> value.
	 *  <code>JSONObject.NULL.equals(null)</code> returns <code>true</code>.
	 *  <code>JSONObject.NULL.toString()</code> returns <code>"null"</code>.
	 */
	public static final Object NULL;

	/**
	 *  Construct an empty JSONObject.
	 */
	public JSONObject() {
	}

	/**
	 *  Construct a JSONObject from a JSONTokener.
	 *  @param x A JSONTokener object containing the source string.
	 *  @throws JSONException If there is a syntax error in the source string.
	 */
	public JSONObject(JSONTokener x) {
	}

	/**
	 *  Construct a JSONObject from a string.
	 *  This is the most commonly used JSONObject constructor.
	 *  @param string    A string beginning
	 *   with <code>{</code>&nbsp;<small>(left brace)</small> and ending
	 *   with <code>}</code>&nbsp;<small>(right brace)</small>.
	 *  @exception JSONException If there is a syntax error in the source string.
	 */
	public JSONObject(String string) {
	}

	/**
	 *  Accumulate values under a key. It is similar to the put method except
	 *  that if there is already an object stored under the key then a
	 *  JSONArray is stored under the key to hold all of the accumulated values.
	 *  If there is already a JSONArray, then the new value is appended to it.
	 *  In contrast, the put method replaces the previous value.
	 *  @param key   A key string.
	 *  @param value An object to be accumulated under the key.
	 *  @return this.
	 *  @throws JSONException If the value is an invalid number
	 *   or if the key is null.
	 */
	public JSONObject accumulate(String key, Object value) {
	}

	/**
	 *  Get the value object associated with a key.
	 * 
	 *  @param key   A key string.
	 *  @return      The object associated with the key.
	 *  @throws   JSONException if the key is not found.
	 */
	public Object get(String key) {
	}

	/**
	 *  Get the boolean value associated with a key.
	 * 
	 *  @param key   A key string.
	 *  @return      The truth.
	 *  @throws   JSONException
	 *   if the value is not a Boolean or the String "true" or "false".
	 */
	public boolean getBoolean(String key) {
	}

	/**
	 *  Get the int value associated with a key. If the number value is too
	 *  large for an int, it will be clipped.
	 * 
	 *  @param key   A key string.
	 *  @return      The integer value.
	 *  @throws   JSONException if the key is not found or if the value cannot
	 *   be converted to an integer.
	 */
	public int getInt(String key) {
	}

	/**
	 *  Get the JSONArray value associated with a key.
	 * 
	 *  @param key   A key string.
	 *  @return      A JSONArray which is the value.
	 *  @throws   JSONException if the key is not found or
	 *   if the value is not a JSONArray.
	 */
	public JSONArray getJSONArray(String key) {
	}

	/**
	 *  Get the JSONObject value associated with a key.
	 * 
	 *  @param key   A key string.
	 *  @return      A JSONObject which is the value.
	 *  @throws   JSONException if the key is not found or
	 *   if the value is not a JSONObject.
	 */
	public JSONObject getJSONObject(String key) {
	}

	/**
	 *  Get the long value associated with a key. If the number value is too
	 *  long for a long, it will be clipped.
	 * 
	 *  @param key   A key string.
	 *  @return      The long value.
	 *  @throws   JSONException if the key is not found or if the value cannot
	 *   be converted to a long.
	 */
	public long getLong(String key) {
	}

	/**
	 *  Get the string associated with a key.
	 * 
	 *  @param key   A key string.
	 *  @return      A string which is the value.
	 *  @throws   JSONException if the key is not found.
	 */
	public String getString(String key) {
	}

	/**
	 *  Determine if the JSONObject contains a specific key.
	 *  @param key   A key string.
	 *  @return      true if the key exists in the JSONObject.
	 */
	public boolean has(String key) {
	}

	/**
	 *  Determine if the value associated with the key is null or if there is
	 *   no value.
	 *  @param key   A key string.
	 *  @return      true if there is no value associated with the key or if
	 *   the value is the JSONObject.NULL object.
	 */
	public boolean isNull(String key) {
	}

	/**
	 *  Get an enumeration of the keys of the JSONObject.
	 * 
	 *  @return An iterator of the keys.
	 */
	public java.util.Enumeration keys() {
	}

	/**
	 *  Get the number of keys stored in the JSONObject.
	 * 
	 *  @return The number of keys in the JSONObject.
	 */
	public int length() {
	}

	/**
	 *  Produce a JSONArray containing the names of the elements of this
	 *  JSONObject.
	 *  @return A JSONArray containing the key strings, or null if the JSONObject
	 *  is empty.
	 */
	public JSONArray names() {
	}

	/**
	 *  Shave off trailing zeros and decimal point, if possible.
	 */
	public static String trimNumber(String s) {
	}

	/**
	 *  Produce a string from a Number.
	 *  @param  n A Number
	 *  @return A String.
	 *  @throws JSONException If n is a non-finite number.
	 */
	public static String numberToString(Object n) {
	}

	/**
	 *  Get an optional value associated with a key.
	 *  @param key   A key string.
	 *  @return      An object which is the value, or null if there is no value.
	 */
	public Object opt(String key) {
	}

	/**
	 *  Get an optional boolean associated with a key.
	 *  It returns false if there is no such key, or if the value is not
	 *  Boolean.TRUE or the String "true".
	 * 
	 *  @param key   A key string.
	 *  @return      The truth.
	 */
	public boolean optBoolean(String key) {
	}

	/**
	 *  Get an optional boolean associated with a key.
	 *  It returns the defaultValue if there is no such key, or if it is not
	 *  a Boolean or the String "true" or "false" (case insensitive).
	 * 
	 *  @param key              A key string.
	 *  @param defaultValue     The default.
	 *  @return      The truth.
	 */
	public boolean optBoolean(String key, boolean defaultValue) {
	}

	/**
	 *  Put a key/value pair in the JSONObject, where the value will be a
	 *  JSONArray which is produced from a Collection.
	 *  @param key 	A key string.
	 *  @param value	A Collection value.
	 *  @return		this.
	 *  @throws JSONException
	 */
	public JSONObject put(String key, java.util.Vector value) {
	}

	/**
	 *  Get an optional int value associated with a key,
	 *  or zero if there is no such key or if the value is not a number.
	 *  If the value is a string, an attempt will be made to evaluate it as
	 *  a number.
	 * 
	 *  @param key   A key string.
	 *  @return      An object which is the value.
	 */
	public int optInt(String key) {
	}

	/**
	 *  Get an optional int value associated with a key,
	 *  or the default if there is no such key or if the value is not a number.
	 *  If the value is a string, an attempt will be made to evaluate it as
	 *  a number.
	 * 
	 *  @param key   A key string.
	 *  @param defaultValue     The default.
	 *  @return      An object which is the value.
	 */
	public int optInt(String key, int defaultValue) {
	}

	/**
	 *  Get an optional JSONArray associated with a key.
	 *  It returns null if there is no such key, or if its value is not a
	 *  JSONArray.
	 * 
	 *  @param key   A key string.
	 *  @return      A JSONArray which is the value.
	 */
	public JSONArray optJSONArray(String key) {
	}

	/**
	 *  Get an optional JSONObject associated with a key.
	 *  It returns null if there is no such key, or if its value is not a
	 *  JSONObject.
	 * 
	 *  @param key   A key string.
	 *  @return      A JSONObject which is the value.
	 */
	public JSONObject optJSONObject(String key) {
	}

	/**
	 *  Get an optional long value associated with a key,
	 *  or zero if there is no such key or if the value is not a number.
	 *  If the value is a string, an attempt will be made to evaluate it as
	 *  a number.
	 * 
	 *  @param key   A key string.
	 *  @return      An object which is the value.
	 */
	public long optLong(String key) {
	}

	/**
	 *  Get an optional long value associated with a key,
	 *  or the default if there is no such key or if the value is not a number.
	 *  If the value is a string, an attempt will be made to evaluate it as
	 *  a number.
	 * 
	 *  @param key   A key string.
	 *  @param defaultValue     The default.
	 *  @return      An object which is the value.
	 */
	public long optLong(String key, long defaultValue) {
	}

	/**
	 *  Get an optional string associated with a key.
	 *  It returns an empty string if there is no such key. If the value is not
	 *  a string and is not null, then it is coverted to a string.
	 * 
	 *  @param key   A key string.
	 *  @return      A string which is the value.
	 */
	public String optString(String key) {
	}

	/**
	 *  Get an optional string associated with a key.
	 *  It returns the defaultValue if there is no such key.
	 * 
	 *  @param key   A key string.
	 *  @param defaultValue     The default.
	 *  @return      A string which is the value.
	 */
	public String optString(String key, String defaultValue) {
	}

	/**
	 *  Put a key/boolean pair in the JSONObject.
	 * 
	 *  @param key   A key string.
	 *  @param value A boolean which is the value.
	 *  @return this.
	 *  @throws JSONException If the key is null.
	 */
	public JSONObject put(String key, boolean value) {
	}

	/**
	 *  Put a key/int pair in the JSONObject.
	 * 
	 *  @param key   A key string.
	 *  @param value An int which is the value.
	 *  @return this.
	 *  @throws JSONException If the key is null.
	 */
	public JSONObject put(String key, int value) {
	}

	/**
	 *  Put a key/long pair in the JSONObject.
	 * 
	 *  @param key   A key string.
	 *  @param value A long which is the value.
	 *  @return this.
	 *  @throws JSONException If the key is null.
	 */
	public JSONObject put(String key, long value) {
	}

	/**
	 *  Put a key/value pair in the JSONObject. If the value is null,
	 *  then the key will be removed from the JSONObject if it is present.
	 *  @param key   A key string.
	 *  @param value An object which is the value. It should be of one of these
	 *   types: Boolean, Double, Integer, JSONArray, JSONObject, Long, String,
	 *   or the JSONObject.NULL object.
	 *  @return this.
	 *  @throws JSONException If the value is non-finite number
	 *   or if the key is null.
	 */
	public JSONObject put(String key, Object value) {
	}

	/**
	 *  Put a key/value pair in the JSONObject, but only if the
	 *  key and the value are both non-null.
	 *  @param key   A key string.
	 *  @param value An object which is the value. It should be of one of these
	 *   types: Boolean, Double, Integer, JSONArray, JSONObject, Long, String,
	 *   or the JSONObject.NULL object.
	 *  @return this.
	 *  @throws JSONException If the value is a non-finite number.
	 */
	public JSONObject putOpt(String key, Object value) {
	}

	/**
	 *  Produce a string in double quotes with backslash sequences in all the
	 *  right places. A backslash will be inserted within </, allowing JSON
	 *  text to be delivered in HTML. In JSON text, a string cannot contain a
	 *  control character or an unescaped quote or backslash.
	 *  @param string A String
	 *  @return  A String correctly formatted for insertion in a JSON text.
	 */
	public static String quote(String string) {
	}

	/**
	 *  Remove a name and its value, if present.
	 *  @param key The name to be removed.
	 *  @return The value that was associated with the name,
	 *  or null if there was no value.
	 */
	public Object remove(String key) {
	}

	/**
	 *  Produce a JSONArray containing the values of the members of this
	 *  JSONObject.
	 *  @param names A JSONArray containing a list of key strings. This
	 *  determines the sequence of the values in the result.
	 *  @return A JSONArray of values.
	 *  @throws JSONException If any of the values are non-finite numbers.
	 */
	public JSONArray toJSONArray(JSONArray names) {
	}

	/**
	 *  Make a JSON text of this JSONObject. For compactness, no whitespace
	 *  is added. If this would not result in a syntactically correct JSON text,
	 *  then null will be returned instead.
	 *  <p>
	 *  Warning: This method assumes that the data structure is acyclical.
	 * 
	 *  @return a printable, displayable, portable, transmittable
	 *   representation of the object, beginning
	 *   with <code>{</code>&nbsp;<small>(left brace)</small> and ending
	 *   with <code>}</code>&nbsp;<small>(right brace)</small>.
	 */
	public String toString() {
	}

	/**
	 *  Make a prettyprinted JSON text of this JSONObject.
	 *  <p>
	 *  Warning: This method assumes that the data structure is acyclical.
	 *  @param indentFactor The number of spaces to add to each level of
	 *   indentation.
	 *  @return a printable, displayable, portable, transmittable
	 *   representation of the object, beginning
	 *   with <code>{</code>&nbsp;<small>(left brace)</small> and ending
	 *   with <code>}</code>&nbsp;<small>(right brace)</small>.
	 *  @throws JSONException If the object contains an invalid number.
	 */
	public String toString(int indentFactor) {
	}

	/**
	 *  Write the contents of the JSONObject as JSON text to a writer.
	 *  For compactness, no whitespace is added.
	 *  <p>
	 *  Warning: This method assumes that the data structure is acyclical.
	 * 
	 *  @return The writer.
	 *  @throws JSONException
	 */
	public java.io.Writer write(java.io.Writer writer) {
	}
}
