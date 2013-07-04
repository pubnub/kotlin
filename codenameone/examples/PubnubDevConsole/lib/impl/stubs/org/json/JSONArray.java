package org.json;


/**
 *  A JSONArray is an ordered sequence of values. Its external text form is a
 *  string wrapped in square brackets with commas separating the values. The
 *  internal form is an object having <code>get</code> and <code>opt</code>
 *  methods for accessing the values by index, and <code>put</code> methods for
 *  adding or replacing values. The values can be any of these types:
 *  <code>Boolean</code>, <code>JSONArray</code>, <code>JSONObject</code>,
 *  <code>Number</code>, <code>String</code>, or the
 *  <code>JSONObject.NULL object</code>.
 *  <p>
 *  The constructor can convert a JSON text into a Java object. The
 *  <code>toString</code> method converts to JSON text.
 *  <p>
 *  A <code>get</code> method returns a value if one can be found, and throws an
 *  exception if one cannot be found. An <code>opt</code> method returns a
 *  default value instead of throwing an exception, and so is useful for
 *  obtaining optional values.
 *  <p>
 *  The generic <code>get()</code> and <code>opt()</code> methods return an
 *  object which you can cast or query for type. There are also typed
 *  <code>get</code> and <code>opt</code> methods that do type checking and type
 *  coersion for you.
 *  <p>
 *  The texts produced by the <code>toString</code> methods strictly conform to
 *  JSON syntax rules. The constructors are more forgiving in the texts they will
 *  accept:
 *  <ul>
 *  <li>An extra <code>,</code>&nbsp;<small>(comma)</small> may appear just
 *      before the closing bracket.</li>
 *  <li>The <code>null</code> value will be inserted when there
 *      is <code>,</code>&nbsp;<small>(comma)</small> elision.</li>
 *  <li>Strings may be quoted with <code>'</code>&nbsp;<small>(single
 *      quote)</small>.</li>
 *  <li>Strings do not need to be quoted at all if they do not begin with a quote
 *      or single quote, and if they do not contain leading or trailing spaces,
 *      and if they do not contain any of these characters:
 *      <code>{ } [ ] / \ : , = ; #</code> and if they do not look like numbers
 *      and if they are not the reserved words <code>true</code>,
 *      <code>false</code>, or <code>null</code>.</li>
 *  <li>Values can be separated by <code>;</code> <small>(semicolon)</small> as
 *      well as by <code>,</code> <small>(comma)</small>.</li>
 *  <li>Numbers may have the <code>0-</code> <small>(octal)</small> or
 *      <code>0x-</code> <small>(hex)</small> prefix.</li>
 *  <li>Comments written in the slashshlash, slashstar, and hash conventions
 *      will be ignored.</li>
 *  </ul>
 * 
 *  @author JSON.org
 *  @version 2
 */
public class JSONArray {

	/**
	 *  Construct an empty JSONArray.
	 */
	public JSONArray() {
	}

	/**
	 *  Construct a JSONArray from a JSONTokener.
	 *  @param x A JSONTokener
	 *  @throws JSONException If there is a syntax error.
	 */
	public JSONArray(JSONTokener x) {
	}

	/**
	 *  Construct a JSONArray from a source sJSON text.
	 *  @param string     A string that begins with
	 *  <code>[</code>&nbsp;<small>(left bracket)</small>
	 *   and ends with <code>]</code>&nbsp;<small>(right bracket)</small>.
	 *   @throws JSONException If there is a syntax error.
	 */
	public JSONArray(String string) {
	}

	/**
	 *  Construct a JSONArray from a Collection.
	 *  @param collection     A Collection.
	 */
	public JSONArray(java.util.Vector collection) {
	}

	/**
	 *  Get the object value associated with an index.
	 *  @param index
	 *   The index must be between 0 and length() - 1.
	 *  @return An object value.
	 *  @throws JSONException If there is no value for the index.
	 */
	public Object get(int index) {
	}

	/**
	 *  Get the boolean value associated with an index.
	 *  The string values "true" and "false" are converted to boolean.
	 * 
	 *  @param index The index must be between 0 and length() - 1.
	 *  @return      The truth.
	 *  @throws JSONException If there is no value for the index or if the
	 *   value is not convertable to boolean.
	 */
	public boolean getBoolean(int index) {
	}

	/**
	 *  Get the JSONArray associated with an index.
	 *  @param index The index must be between 0 and length() - 1.
	 *  @return      A JSONArray value.
	 *  @throws JSONException If there is no value for the index. or if the
	 *  value is not a JSONArray
	 */
	public JSONArray getJSONArray(int index) {
	}

	/**
	 *  Get the JSONObject associated with an index.
	 *  @param index subscript
	 *  @return      A JSONObject value.
	 *  @throws JSONException If there is no value for the index or if the
	 *  value is not a JSONObject
	 */
	public JSONObject getJSONObject(int index) {
	}

	/**
	 *  Get the string associated with an index.
	 *  @param index The index must be between 0 and length() - 1.
	 *  @return      A string value.
	 *  @throws JSONException If there is no value for the index.
	 */
	public String getString(int index) {
	}

	/**
	 *  Determine if the value is null.
	 *  @param index The index must be between 0 and length() - 1.
	 *  @return true if the value at the index is null, or if there is no value.
	 */
	public boolean isNull(int index) {
	}

	/**
	 *  Make a string from the contents of this JSONArray. The
	 *  <code>separator</code> string is inserted between each element.
	 *  Warning: This method assumes that the data structure is acyclical.
	 *  @param separator A string that will be inserted between the elements.
	 *  @return a string.
	 *  @throws JSONException If the array contains an invalid number.
	 */
	public String join(String separator) {
	}

	/**
	 *  Get the number of elements in the JSONArray, included nulls.
	 * 
	 *  @return The length (or size).
	 */
	public int length() {
	}

	/**
	 *  Get the optional object value associated with an index.
	 *  @param index The index must be between 0 and length() - 1.
	 *  @return      An object value, or null if there is no
	 *               object at that index.
	 */
	public Object opt(int index) {
	}

	/**
	 *  Get the optional boolean value associated with an index.
	 *  It returns false if there is no value at that index,
	 *  or if the value is not Boolean.TRUE or the String "true".
	 * 
	 *  @param index The index must be between 0 and length() - 1.
	 *  @return      The truth.
	 */
	public boolean optBoolean(int index) {
	}

	/**
	 *  Get the optional boolean value associated with an index.
	 *  It returns the defaultValue if there is no value at that index or if
	 *  it is not a Boolean or the String "true" or "false" (case insensitive).
	 * 
	 *  @param index The index must be between 0 and length() - 1.
	 *  @param defaultValue     A boolean default.
	 *  @return      The truth.
	 */
	public boolean optBoolean(int index, boolean defaultValue) {
	}

	/**
	 *  Get the optional JSONArray associated with an index.
	 *  @param index subscript
	 *  @return      A JSONArray value, or null if the index has no value,
	 *  or if the value is not a JSONArray.
	 */
	public JSONArray optJSONArray(int index) {
	}

	/**
	 *  Get the optional JSONObject associated with an index.
	 *  Null is returned if the key is not found, or null if the index has
	 *  no value, or if the value is not a JSONObject.
	 * 
	 *  @param index The index must be between 0 and length() - 1.
	 *  @return      A JSONObject value.
	 */
	public JSONObject optJSONObject(int index) {
	}

	/**
	 *  Get the optional string value associated with an index. It returns an
	 *  empty string if there is no value at that index. If the value
	 *  is not a string and is not null, then it is coverted to a string.
	 * 
	 *  @param index The index must be between 0 and length() - 1.
	 *  @return      A String value.
	 */
	public String optString(int index) {
	}

	/**
	 *  Get the optional string associated with an index.
	 *  The defaultValue is returned if the key is not found.
	 * 
	 *  @param index The index must be between 0 and length() - 1.
	 *  @param defaultValue     The default value.
	 *  @return      A String value.
	 */
	public String optString(int index, String defaultValue) {
	}

	/**
	 *  Append a boolean value. This increases the array's length by one.
	 * 
	 *  @param value A boolean value.
	 *  @return this.
	 */
	public JSONArray put(boolean value) {
	}

	/**
	 *  Put a value in the JSONArray, where the value will be a
	 *  JSONArray which is produced from a Collection.
	 *  @param value	A Collection value.
	 *  @return		this.
	 */
	public JSONArray put(java.util.Vector value) {
	}

	/**
	 *  Append an int value. This increases the array's length by one.
	 * 
	 *  @param value An int value.
	 *  @return this.
	 */
	public JSONArray put(int value) {
	}

	/**
	 *  Append an long value. This increases the array's length by one.
	 * 
	 *  @param value A long value.
	 *  @return this.
	 */
	public JSONArray put(long value) {
	}

	/**
	 *  Append an object value. This increases the array's length by one.
	 *  @param value An object value.  The value should be a
	 *   Boolean, Double, Integer, JSONArray, JSONObject, Long, or String, or the
	 *   JSONObject.NULL object.
	 *  @return this.
	 */
	public JSONArray put(Object value) {
	}

	/**
	 *  Put or replace a boolean value in the JSONArray. If the index is greater
	 *  than the length of the JSONArray, then null elements will be added as
	 *  necessary to pad it out.
	 *  @param index The subscript.
	 *  @param value A boolean value.
	 *  @return this.
	 *  @throws JSONException If the index is negative.
	 */
	public JSONArray put(int index, boolean value) {
	}

	/**
	 *  Put a value in the JSONArray, where the value will be a
	 *  JSONArray which is produced from a Collection.
	 *  @param index The subscript.
	 *  @param value	A Collection value.
	 *  @return		this.
	 *  @throws JSONException If the index is negative or if the value is
	 *  not finite.
	 */
	public JSONArray put(int index, java.util.Vector value) {
	}

	/**
	 *  Put or replace an int value. If the index is greater than the length of
	 *   the JSONArray, then null elements will be added as necessary to pad
	 *   it out.
	 *  @param index The subscript.
	 *  @param value An int value.
	 *  @return this.
	 *  @throws JSONException If the index is negative.
	 */
	public JSONArray put(int index, int value) {
	}

	/**
	 *  Put or replace a long value. If the index is greater than the length of
	 *   the JSONArray, then null elements will be added as necessary to pad
	 *   it out.
	 *  @param index The subscript.
	 *  @param value A long value.
	 *  @return this.
	 *  @throws JSONException If the index is negative.
	 */
	public JSONArray put(int index, long value) {
	}

	/**
	 *  Put or replace an object value in the JSONArray. If the index is greater
	 *   than the length of the JSONArray, then null elements will be added as
	 *   necessary to pad it out.
	 *  @param index The subscript.
	 *  @param value The value to put into the array. The value should be a
	 *   Boolean, Double, Integer, JSONArray, JSONObject, Long, or String, or the
	 *   JSONObject.NULL object.
	 *  @return this.
	 *  @throws JSONException If the index is negative or if the the value is
	 *   an invalid number.
	 */
	public JSONArray put(int index, Object value) {
	}

	/**
	 *  Produce a JSONObject by combining a JSONArray of names with the values
	 *  of this JSONArray.
	 *  @param names A JSONArray containing a list of key strings. These will be
	 *  paired with the values.
	 *  @return A JSONObject, or null if there are no names or if this JSONArray
	 *  has no values.
	 *  @throws JSONException If any of the names are null.
	 */
	public JSONObject toJSONObject(JSONArray names) {
	}

	/**
	 *  Make a JSON text of this JSONArray. For compactness, no
	 *  unnecessary whitespace is added. If it is not possible to produce a
	 *  syntactically correct JSON text then null will be returned instead. This
	 *  could occur if the array contains an invalid number.
	 *  <p>
	 *  Warning: This method assumes that the data structure is acyclical.
	 * 
	 *  @return a printable, displayable, transmittable
	 *   representation of the array.
	 */
	public String toString() {
	}

	/**
	 *  Make a prettyprinted JSON text of this JSONArray.
	 *  Warning: This method assumes that the data structure is acyclical.
	 *  @param indentFactor The number of spaces to add to each level of
	 *   indentation.
	 *  @return a printable, displayable, transmittable
	 *   representation of the object, beginning
	 *   with <code>[</code>&nbsp;<small>(left bracket)</small> and ending
	 *   with <code>]</code>&nbsp;<small>(right bracket)</small>.
	 *  @throws JSONException
	 */
	public String toString(int indentFactor) {
	}

	/**
	 *  Write the contents of the JSONArray as JSON text to a writer.
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
