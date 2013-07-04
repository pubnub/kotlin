/**
 * 
 * Classes for producing and reading Base64 and Hex strings.
 */
package org.bouncycastle.util.encoders;


public class Base64Encoder implements Encoder {

	protected final byte[] encodingTable;

	protected byte padding;

	protected final byte[] decodingTable;

	public Base64Encoder() {
	}

	protected void initialiseDecodingTable() {
	}

	/**
	 *  encode the input data producing a base 64 output stream.
	 * 
	 *  @return the number of bytes produced.
	 */
	public int encode(byte[] data, int off, int length, java.io.OutputStream out) {
	}

	/**
	 *  decode the base 64 encoded byte data writing it to the given output stream,
	 *  whitespace characters will be ignored.
	 * 
	 *  @return the number of bytes produced.
	 */
	public int decode(byte[] data, int off, int length, java.io.OutputStream out) {
	}

	/**
	 *  decode the base 64 encoded String data writing it to the given output stream,
	 *  whitespace characters will be ignored.
	 * 
	 *  @return the number of bytes produced.
	 */
	public int decode(String data, java.io.OutputStream out) {
	}
}
