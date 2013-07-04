/**
 * 
 * Modes for symmetric ciphers.
 */
package org.bouncycastle.crypto.modes;


/**
 *  implements Cipher-Block-Chaining (CBC) mode on top of a simple cipher.
 */
public class CBCBlockCipher implements org.bouncycastle.crypto.BlockCipher {

	/**
	 *  Basic constructor.
	 * 
	 *  @param cipher the block cipher to be used as the basis of chaining.
	 */
	public CBCBlockCipher(org.bouncycastle.crypto.BlockCipher cipher) {
	}

	/**
	 *  return the underlying block cipher that we are wrapping.
	 * 
	 *  @return the underlying block cipher that we are wrapping.
	 */
	public org.bouncycastle.crypto.BlockCipher getUnderlyingCipher() {
	}

	/**
	 *  Initialise the cipher and, possibly, the initialisation vector (IV).
	 *  If an IV isn't passed as part of the parameter, the IV will be all zeros.
	 * 
	 *  @param encrypting if true the cipher is initialised for
	 *   encryption, if false for decryption.
	 *  @param params the key and other data required by the cipher.
	 *  @exception IllegalArgumentException if the params argument is
	 *  inappropriate.
	 */
	public void init(boolean encrypting, org.bouncycastle.crypto.CipherParameters params) {
	}

	/**
	 *  return the algorithm name and mode.
	 * 
	 *  @return the name of the underlying algorithm followed by "/CBC".
	 */
	public String getAlgorithmName() {
	}

	/**
	 *  return the block size of the underlying cipher.
	 * 
	 *  @return the block size of the underlying cipher.
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
}
