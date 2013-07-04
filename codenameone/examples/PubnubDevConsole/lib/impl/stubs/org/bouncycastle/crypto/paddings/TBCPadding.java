/**
 * 
 * Paddings for symmetric ciphers.
 */
package org.bouncycastle.crypto.paddings;


/**
 *  A padder that adds Trailing-Bit-Compliment padding to a block.
 *  <p>
 *  This padding pads the block out with the compliment of the last bit
 *  of the plain text.
 *  </p>
 */
public class TBCPadding implements BlockCipherPadding {

	public TBCPadding() {
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
	 *  <p>
	 *  Note: this assumes that the last block of plain text is always 
	 *  passed to it inside in. i.e. if inOff is zero, indicating the
	 *  entire block is to be overwritten with padding the value of in
	 *  should be the same as the last block of plain text.
	 *  </p>
	 */
	public int addPadding(byte[] in, int inOff) {
	}

	/**
	 *  return the number of pad bytes present in the block.
	 */
	public int padCount(byte[] in) {
	}
}
