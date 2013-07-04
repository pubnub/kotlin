/**
 * 
 * Classes for producing and reading Base64 and Hex strings.
 */
package org.bouncycastle.util.encoders;


/**
 *  Encode and decode byte arrays (typically from binary to 7-bit ASCII 
 *  encodings).
 */
public interface Encoder {

	public int encode(byte[] data, int off, int length, java.io.OutputStream out);

	public int decode(byte[] data, int off, int length, java.io.OutputStream out);

	public int decode(String data, java.io.OutputStream out);
}
