/**
 * 
 * Modes for symmetric ciphers.
 */
package org.bouncycastle.crypto.modes;


/**
 *  implements a Cipher-FeedBack (CFB) mode on top of a simple cipher.
 */
public class CFBBlockCipher implements org.bouncycastle.crypto.BlockCipher {

	/**
	 *  Basic constructor.
	 * 
	 *  @param cipher the block cipher to be used as the basis of the
	 *  feedback mode.
	 *  @param bitBlockSize the block size in bits (note: a multiple of 8)
	 */
	public CFBBlockCipher(org.bouncycastle.crypto.BlockCipher cipher, int bitBlockSize) {
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
	 *  An IV which is too short is handled in FIPS compliant fashion.
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
	 *  @return the name of the underlying algorithm followed by "/CFB"
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
	 *  Do the appropriate processing for CFB mode encryption.
	 * 
	 *  @param in the array containing the data to be encrypted.
	 *  @param inOff offset into the in array the data starts at.
	 *  @param out the array the encrypted data will be copied into.
	 *  @param outOff the offset into the out array the output will start at.
	 *  @exception DataLengthException if there isn't enough data in in, or
	 *  space in out.
	 *  @exception IllegalStateException if the cipher isn't initialised.
	 *  @return the number of bytes processed and produced.
	 */
	public int encryptBlock(byte[] in, int inOff, byte[] out, int outOff) {
	}

	/**
	 *  Do the appropriate processing for CFB mode decryption.
	 * 
	 *  @param in the array containing the data to be decrypted.
	 *  @param inOff offset into the in array the data starts at.
	 *  @param out the array the encrypted data will be copied into.
	 *  @param outOff the offset into the out array the output will start at.
	 *  @exception DataLengthException if there isn't enough data in in, or
	 *  space in out.
	 *  @exception IllegalStateException if the cipher isn't initialised.
	 *  @return the number of bytes processed and produced.
	 */
	public int decryptBlock(byte[] in, int inOff, byte[] out, int outOff) {
	}

	/**
	 *  reset the chaining vector back to the IV and reset the underlying
	 *  cipher.
	 */
	public void reset() {
	}
}
