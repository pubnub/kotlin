/**
 * 
 * Classes for producing and reading Base64 and Hex strings.
 */
package org.bouncycastle.util.encoders;


public class Hex {

	public Hex() {
	}

	/**
	 *  encode the input data producing a Hex encoded byte array.
	 * 
	 *  @return a byte array containing the Hex encoded data.
	 */
	public static byte[] encode(byte[] data) {
	}

	/**
	 *  encode the input data producing a Hex encoded byte array.
	 * 
	 *  @return a byte array containing the Hex encoded data.
	 */
	public static byte[] encode(byte[] data, int off, int length) {
	}

	/**
	 *  Hex encode the byte data writing it to the given output stream.
	 * 
	 *  @return the number of bytes produced.
	 */
	public static int encode(byte[] data, java.io.OutputStream out) {
	}

	/**
	 *  Hex encode the byte data writing it to the given output stream.
	 * 
	 *  @return the number of bytes produced.
	 */
	public static int encode(byte[] data, int off, int length, java.io.OutputStream out) {
	}

	/**
	 *  decode the Hex encoded input data. It is assumed the input data is valid.
	 * 
	 *  @return a byte array representing the decoded data.
	 */
	public static byte[] decode(byte[] data) {
	}

	/**
	 *  decode the Hex encoded String data - whitespace will be ignored.
	 * 
	 *  @return a byte array representing the decoded data.
	 */
	public static byte[] decode(String data) {
	}

	/**
	 *  decode the Hex encoded String data writing it to the given output stream,
	 *  whitespace characters will be ignored.
	 * 
	 *  @return the number of bytes produced.
	 */
	public static int decode(String data, java.io.OutputStream out) {
	}
}
