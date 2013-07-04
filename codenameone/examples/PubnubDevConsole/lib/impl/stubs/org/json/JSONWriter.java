package org.json;


/**
 *  JSONWriter provides a quick and convenient way of producing JSON text.
 *  The texts produced strictly conform to JSON syntax rules. No whitespace is
 *  added, so the results are ready for transmission or storage. Each instance of
 *  JSONWriter can produce one JSON text.
 *  <p>
 *  A JSONWriter instance provides a <code>value</code> method for appending
 *  values to the
 *  text, and a <code>key</code>
 *  method for adding keys before values in objects. There are <code>array</code>
 *  and <code>endArray</code> methods that make and bound array values, and
 *  <code>object</code> and <code>endObject</code> methods which make and bound
 *  object values. All of these methods return the JSONWriter instance,
 *  permitting a cascade style. For example, <pre>
 *  new JSONWriter(myWriter)
 *      .object()
 *          .key("JSON")
 *          .value("Hello, World!")
 *      .endObject();</pre> which writes <pre>
 *  {"JSON":"Hello, World!"}</pre>
 *  <p>
 *  The first method called must be <code>array</code> or <code>object</code>.
 *  There are no methods for adding commas or colons. JSONWriter adds them for
 *  you. Objects and arrays can be nested up to 20 levels deep.
 *  <p>
 *  This can sometimes be easier than using a JSONObject to build a string.
 *  @author JSON.org
 *  @version 2
 */
public class JSONWriter {

	/**
	 *  The current mode. Values:
	 *  'a' (array),
	 *  'd' (done),
	 *  'i' (initial),
	 *  'k' (key),
	 *  'o' (object).
	 */
	protected char mode;

	/**
	 *  The writer that will receive the output.
	 */
	protected java.io.Writer writer;

	/**
	 *  Make a fresh JSONWriter. It can be used to build one JSON text.
	 */
	public JSONWriter(java.io.Writer w) {
	}

	/**
	 *  Begin appending a new array. All values until the balancing
	 *  <code>endArray</code> will be appended to this array. The
	 *  <code>endArray</code> method must be called to mark the array's end.
	 *  @return this
	 *  @throws JSONException If the nesting is too deep, or if the object is
	 *  started in the wrong place (for example as a key or after the end of the
	 *  outermost array or object).
	 */
	public JSONWriter array() {
	}

	/**
	 *  End an array. This method most be called to balance calls to
	 *  <code>array</code>.
	 *  @return this
	 *  @throws JSONException If incorrectly nested.
	 */
	public JSONWriter endArray() {
	}

	/**
	 *  End an object. This method most be called to balance calls to
	 *  <code>object</code>.
	 *  @return this
	 *  @throws JSONException If incorrectly nested.
	 */
	public JSONWriter endObject() {
	}

	/**
	 *  Append a key. The key will be associated with the next value. In an
	 *  object, every value must be preceded by a key.
	 *  @param s A key string.
	 *  @return this
	 *  @throws JSONException If the key is out of place. For example, keys
	 *   do not belong in arrays or if the key is null.
	 */
	public JSONWriter key(String s) {
	}

	/**
	 *  Begin appending a new object. All keys and values until the balancing
	 *  <code>endObject</code> will be appended to this object. The
	 *  <code>endObject</code> method must be called to mark the object's end.
	 *  @return this
	 *  @throws JSONException If the nesting is too deep, or if the object is
	 *  started in the wrong place (for example as a key or after the end of the
	 *  outermost array or object).
	 */
	public JSONWriter object() {
	}

	/**
	 *  Append either the value <code>true</code> or the value
	 *  <code>false</code>.
	 *  @param b A boolean.
	 *  @return this
	 *  @throws JSONException
	 */
	public JSONWriter value(boolean b) {
	}

	/**
	 *  Append a long value.
	 *  @param l A long.
	 *  @return this
	 *  @throws JSONException
	 */
	public JSONWriter value(long l) {
	}

	/**
	 *  Append an object value.
	 *  @param o The object to append. It can be null, or a Boolean, Number,
	 *    String, JSONObject, or JSONArray, or an object with a toJSONString()
	 *    method.
	 *  @return this
	 *  @throws JSONException If the value is out of sequence.
	 */
	public JSONWriter value(Object o) {
	}
}
