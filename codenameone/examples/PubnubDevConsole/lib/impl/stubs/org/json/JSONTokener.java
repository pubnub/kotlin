package org.json;


/**
 *  A JSONTokener takes a source string and extracts characters and tokens from
 *  it. It is used by the JSONObject and JSONArray constructors to parse
 *  JSON source strings.
 *  @author JSON.org
 *  @version 2
 */
public class JSONTokener {

	/**
	 *  Construct a JSONTokener from a string.
	 * 
	 *  @param s     A source string.
	 */
	public JSONTokener(String s) {
	}

	/**
	 *  Back up one character. This provides a sort of lookahead capability,
	 *  so that you can test for a digit or letter before attempting to parse
	 *  the next number or identifier.
	 */
	public void back() {
	}

	/**
	 *  Get the hex value of a character (base16).
	 *  @param c A character between '0' and '9' or between 'A' and 'F' or
	 *  between 'a' and 'f'.
	 *  @return  An int between 0 and 15, or -1 if c was not a hex digit.
	 */
	public static int dehexchar(char c) {
	}

	/**
	 *  Determine if the source string still contains characters that next()
	 *  can consume.
	 *  @return true if not yet at the end of the source.
	 */
	public boolean more() {
	}

	/**
	 *  Get the next character in the source string.
	 * 
	 *  @return The next character, or 0 if past the end of the source string.
	 */
	public char next() {
	}

	/**
	 *  Consume the next character, and check that it matches a specified
	 *  character.
	 *  @param c The character to match.
	 *  @return The character.
	 *  @throws JSONException if the character does not match.
	 */
	public char next(char c) {
	}

	/**
	 *  Get the next n characters.
	 * 
	 *  @param n     The number of characters to take.
	 *  @return      A string of n characters.
	 *  @throws JSONException
	 *    Substring bounds error if there are not
	 *    n characters remaining in the source string.
	 */
	public String next(int n) {
	}

	/**
	 *  Get the next char in the string, skipping whitespace
	 *  and comments (slashslash, slashstar, and hash).
	 *  @throws JSONException
	 *  @return  A character, or 0 if there are no more characters.
	 */
	public char nextClean() {
	}

	/**
	 *  Return the characters up to the next close quote character.
	 *  Backslash processing is done. The formal JSON format does not
	 *  allow strings in single quotes, but an implementation is allowed to
	 *  accept them.
	 *  @param quote The quoting character, either
	 *       <code>"</code>&nbsp;<small>(double quote)</small> or
	 *       <code>'</code>&nbsp;<small>(single quote)</small>.
	 *  @return      A String.
	 *  @throws JSONException Unterminated string.
	 */
	public String nextString(char quote) {
	}

	/**
	 *  Get the text up but not including the specified character or the
	 *  end of line, whichever comes first.
	 *  @param  d A delimiter character.
	 *  @return   A string.
	 */
	public String nextTo(char d) {
	}

	/**
	 *  Get the text up but not including one of the specified delimeter
	 *  characters or the end of line, whichever comes first.
	 *  @param delimiters A set of delimiter characters.
	 *  @return A string, trimmed.
	 */
	public String nextTo(String delimiters) {
	}

	/**
	 *  Get the next value. The value can be a Boolean, Double, Integer,
	 *  JSONArray, JSONObject, Long, or String, or the JSONObject.NULL object.
	 *  @throws JSONException If syntax error.
	 * 
	 *  @return An object.
	 */
	public Object nextValue() {
	}

	/**
	 *  Skip characters until the next character is the requested character.
	 *  If the requested character is not found, no characters are skipped.
	 *  @param to A character to skip to.
	 *  @return The requested character, or zero if the requested character
	 *  is not found.
	 */
	public char skipTo(char to) {
	}

	/**
	 *  Skip characters until past the requested string.
	 *  If it is not found, we are left at the end of the source.
	 *  @param to A string to skip past.
	 */
	public void skipPast(String to) {
	}

	/**
	 *  Make a JSONException to signal a syntax error.
	 * 
	 *  @param message The error message.
	 *  @return  A JSONException object, suitable for throwing
	 */
	public JSONException syntaxError(String message) {
	}

	/**
	 *  Make a printable string of this JSONTokener.
	 * 
	 *  @return " at character [this.myIndex] of [this.mySource]"
	 */
	public String toString() {
	}
}
