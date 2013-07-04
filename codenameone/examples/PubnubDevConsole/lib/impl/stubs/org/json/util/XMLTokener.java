package org.json.util;


/**
 *  The XMLTokener extends the JSONTokener to provide additional methods
 *  for the parsing of XML texts.
 *  @author JSON.org
 *  @version 2
 */
public class XMLTokener extends org.json.JSONTokener {

	/**
	 * The table of entity values. It initially contains Character values for
	 *  amp, apos, gt, lt, quot.
	 */
	public static final java.util.Hashtable entity;

	/**
	 *  Construct an XMLTokener from a string.
	 *  @param s A source string.
	 */
	public XMLTokener(String s) {
	}

	/**
	 *  Get the text in the CDATA block.
	 *  @return The string up to the <code>]]&gt;</code>.
	 *  @throws JSONException If the <code>]]&gt;</code> is not found.
	 */
	public String nextCDATA() {
	}

	/**
	 *  Get the next XML outer token, trimming whitespace. There are two kinds
	 *  of tokens: the '<' character which begins a markup tag, and the content
	 *  text between markup tags.
	 * 
	 *  @return  A string, or a '<' Character, or null if there is no more
	 *  source text.
	 *  @throws JSONException
	 */
	public Object nextContent() {
	}

	/**
	 *  Return the next entity. These entities are translated to Characters:
	 *      <code>&amp;  &apos;  &gt;  &lt;  &quot;</code>.
	 *  @param a An ampersand character.
	 *  @return  A Character or an entity String if the entity is not recognized.
	 *  @throws JSONException If missing ';' in XML entity.
	 */
	public Object nextEntity(char a) {
	}

	/**
	 *  Returns the next XML meta token. This is used for skipping over <!...>
	 *  and <?...?> structures.
	 *  @return Syntax characters (<code>< > / = ! ?</code>) are returned as
	 *   Character, and strings and names are returned as Boolean. We don't care
	 *   what the values actually are.
	 *  @throws JSONException If a string is not properly closed or if the XML
	 *   is badly structured.
	 */
	public Object nextMeta() {
	}

	/**
	 *  Get the next XML Token. These tokens are found inside of angle
	 *  brackets. It may be one of these characters: <code>/ > = ! ?</code> or it
	 *  may be a string wrapped in single quotes or double quotes, or it may be a
	 *  name.
	 *  @return a String or a Character.
	 *  @throws JSONException If the XML is not well formed.
	 */
	public Object nextToken() {
	}
}
