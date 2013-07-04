/**
 * 
 * Paddings for symmetric ciphers.
 */
package org.bouncycastle.crypto.paddings;


/**
 *  A padder that adds the padding according to the scheme referenced in
 *  ISO 7814-4 - scheme 2 from ISO 9797-1. The first byte is 0x80, rest is 0x00
 */
public class ISO7816d4Padding implements BlockCipherPadding {

	public ISO7816d4Padding() {
	}

	/**
	 *  Initialise the padder.
	 * 
	 *  @param random - a SecureRandom if available.
	 */
	public void init(javabc.SecureRandom random) {
	}

	/**
	 *  Return the name of the algorithm the padder implements.
	 * 
	 *  @return the name of the algorithm the padder implements.
	 */
	public String getPaddingName() {
	}

	/**
	 *  add the pad bytes to the passed in block, returning the
	 *  number of bytes added.
	 */
	public int addPadding(byte[] in, int inOff) {
	}

	/**
	 *  return the number of pad bytes present in the block.
	 */
	public int padCount(byte[] in) {
	}
}
