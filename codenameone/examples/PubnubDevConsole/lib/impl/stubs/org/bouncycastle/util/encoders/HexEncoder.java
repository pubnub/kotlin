/**
 * 
 * Classes for producing and reading Base64 and Hex strings.
 */
package org.bouncycastle.util.encoders;


public class HexEncoder implements Encoder {

	protected final byte[] encodingTable;

	protected final byte[] decodingTable;

	public HexEncoder() {
	}

	protected void initialiseDecodingTable() {
	}

	/**
	 *  encode the input data producing a Hex output stream.
	 * 
	 *  @return the number of bytes produced.
	 */
	public int encode(byte[] data, int off, int length, java.io.OutputStream out) {
	}

	/**
	 *  decode the Hex encoded byte data writing it to the given output stream,
	 *  whitespace characters will be ignored.
	 * 
	 *  @return the number of bytes produced.
	 */
	public int decode(byte[] data, int off, int length, java.io.OutputStream out) {
	}

	/**
	 *  decode the Hex encoded String data writing it to the given output stream,
	 *  whitespace characters will be ignored.
	 * 
	 *  @return the number of bytes produced.
	 */
	public int decode(String data, java.io.OutputStream out) {
	}
}
