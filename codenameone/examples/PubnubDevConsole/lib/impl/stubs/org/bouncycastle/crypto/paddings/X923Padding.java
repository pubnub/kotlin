/**
 * 
 * Paddings for symmetric ciphers.
 */
package org.bouncycastle.crypto.paddings;


/**
 *  A padder that adds X9.23 padding to a block - if a SecureRandom is
 *  passed in random padding is assumed, otherwise padding with zeros is used.
 */
public class X923Padding implements BlockCipherPadding {

	public X923Padding() {
	}

	/**
	 *  Initialise the padder.
	 * 
	 *  @param random a SecureRandom if one is available.
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
