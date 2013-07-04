/**
 * 
 * Classes for producing and reading Base64 and Hex strings.
 */
package org.bouncycastle.util.encoders;


/**
 *  Converters for going from hex to binary and back. Note: this class assumes ASCII processing.
 */
public class HexTranslator implements Translator {

	public HexTranslator() {
	}

	/**
	 *  size of the output block on encoding produced by getDecodedBlockSize()
	 *  bytes.
	 */
	public int getEncodedBlockSize() {
	}

	public int encode(byte[] in, int inOff, int length, byte[] out, int outOff) {
	}

	/**
	 *  size of the output block on decoding produced by getEncodedBlockSize()
	 *  bytes.
	 */
	public int getDecodedBlockSize() {
	}

	public int decode(byte[] in, int inOff, int length, byte[] out, int outOff) {
	}
}
