/**
 * 
 * Classes for producing and reading Base64 and Hex strings.
 */
package org.bouncycastle.util.encoders;


/**
 *  Convert binary data to and from UrlBase64 encoding.  This is identical to
 *  Base64 encoding, except that the padding character is "." and the other 
 *  non-alphanumeric characters are "-" and "_" instead of "+" and "/".
 *  <p>
 *  The purpose of UrlBase64 encoding is to provide a compact encoding of binary
 *  data that is safe for use as an URL parameter. Base64 encoding does not
 *  produce encoded values that are safe for use in URLs, since "/" can be 
 *  interpreted as a path delimiter; "+" is the encoded form of a space; and
 *  "=" is used to separate a name from the corresponding value in an URL 
 *  parameter.
 */
public class UrlBase64 {

	public UrlBase64() {
	}

	/**
	 *  Encode the input data producing a URL safe base 64 encoded byte array.
	 * 
	 *  @return a byte array containing the URL safe base 64 encoded data.
	 */
	public static byte[] encode(byte[] data) {
	}

	/**
	 *  Encode the byte data writing it to the given output stream.
	 * 
	 *  @return the number of bytes produced.
	 */
	public static int encode(byte[] data, java.io.OutputStream out) {
	}

	/**
	 *  Decode the URL safe base 64 encoded input data - white space will be ignored.
	 * 
	 *  @return a byte array representing the decoded data.
	 */
	public static byte[] decode(byte[] data) {
	}

	/**
	 *  decode the URL safe base 64 encoded byte data writing it to the given output stream,
	 *  whitespace characters will be ignored.
	 * 
	 *  @return the number of bytes produced.
	 */
	public static int decode(byte[] data, java.io.OutputStream out) {
	}

	/**
	 *  decode the URL safe base 64 encoded String data - whitespace will be ignored.
	 * 
	 *  @return a byte array representing the decoded data.
	 */
	public static byte[] decode(String data) {
	}

	/**
	 *  Decode the URL safe base 64 encoded String data writing it to the given output stream,
	 *  whitespace characters will be ignored.
	 * 
	 *  @return the number of bytes produced.
	 */
	public static int decode(String data, java.io.OutputStream out) {
	}
}
