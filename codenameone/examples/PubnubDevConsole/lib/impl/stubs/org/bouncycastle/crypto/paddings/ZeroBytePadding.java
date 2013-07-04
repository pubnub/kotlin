/**
 * 
 * Paddings for symmetric ciphers.
 */
package org.bouncycastle.crypto.paddings;


/**
 *  A padder that adds NULL byte padding to a block.
 */
public class ZeroBytePadding implements BlockCipherPadding {

	public ZeroBytePadding() {
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
