/**
 * 
 * Modes for symmetric ciphers.
 */
package org.bouncycastle.crypto.modes;


/**
 *  Implements OpenPGP's rather strange version of Cipher-FeedBack (CFB) mode on top of a simple cipher. For further info see <a href="http://www.ietf.org/rfc/rfc2440.html">RFC 2440</a>.
 */
public class PGPCFBBlockCipher implements org.bouncycastle.crypto.BlockCipher {

	/**
	 *  Basic constructor.
	 * 
	 *  @param cipher the block cipher to be used as the basis of the
	 *  feedback mode.
	 *  @param inlineIv if true this is for PGP CFB with a prepended iv.
	 */
	public PGPCFBBlockCipher(org.bouncycastle.crypto.BlockCipher cipher, boolean inlineIv) {
	}

	/**
	 *  return the underlying block cipher that we are wrapping.
	 * 
	 *  @return the underlying block cipher that we are wrapping.
	 */
	public org.bouncycastle.crypto.BlockCipher getUnderlyingCipher() {
	}

	/**
	 *  return the algorithm name and mode.
	 * 
	 *  @return the name of the underlying algorithm followed by "/PGPCFB"
	 *  and the block size in bits.
	 */
	public String getAlgorithmName() {
	}

	/**
	 *  return the block size we are operating at.
	 * 
	 *  @return the block size we are operating at (in bytes).
	 */
	public int getBlockSize() {
	}

	/**
	 *  Process one block of input from the array in and write it to
	 *  the out array.
	 * 
	 *  @param in the array containing the input data.
	 *  @param inOff offset into the in array the data starts at.
	 *  @param out the array the output data will be copied into.
	 *  @param outOff the offset into the out array the output will start at.
	 *  @exception DataLengthException if there isn't enough data in in, or
	 *  space in out.
	 *  @exception IllegalStateException if the cipher isn't initialised.
	 *  @return the number of bytes processed and produced.
	 */
	public int processBlock(byte[] in, int inOff, byte[] out, int outOff) {
	}

	/**
	 *  reset the chaining vector back to the IV and reset the underlying
	 *  cipher.
	 */
	public void reset() {
	}

	/**
	 *  Initialise the cipher and, possibly, the initialisation vector (IV).
	 *  If an IV isn't passed as part of the parameter, the IV will be all zeros.
	 *  An IV which is too short is handled in FIPS compliant fashion.
	 * 
	 *  @param forEncryption if true the cipher is initialised for
	 *   encryption, if false for decryption.
	 *  @param params the key and other data required by the cipher.
	 *  @exception IllegalArgumentException if the params argument is
	 *  inappropriate.
	 */
	public void init(boolean forEncryption, org.bouncycastle.crypto.CipherParameters params) {
	}
}
