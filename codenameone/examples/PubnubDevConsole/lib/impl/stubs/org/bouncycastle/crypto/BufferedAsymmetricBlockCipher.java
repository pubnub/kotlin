/**
 * 
 * Base classes for the lightweight API.
 */
package org.bouncycastle.crypto;


/**
 *  a buffer wrapper for an asymmetric block cipher, allowing input
 *  to be accumulated in a piecemeal fashion until final processing.
 */
public class BufferedAsymmetricBlockCipher {

	protected byte[] buf;

	protected int bufOff;

	/**
	 *  base constructor.
	 * 
	 *  @param cipher the cipher this buffering object wraps.
	 */
	public BufferedAsymmetricBlockCipher(AsymmetricBlockCipher cipher) {
	}

	/**
	 *  return the underlying cipher for the buffer.
	 * 
	 *  @return the underlying cipher for the buffer.
	 */
	public AsymmetricBlockCipher getUnderlyingCipher() {
	}

	/**
	 *  return the amount of data sitting in the buffer.
	 * 
	 *  @return the amount of data sitting in the buffer.
	 */
	public int getBufferPosition() {
	}

	/**
	 *  initialise the buffer and the underlying cipher.
	 * 
	 *  @param forEncryption if true the cipher is initialised for
	 *   encryption, if false for decryption.
	 *  @param params the key and other data required by the cipher.
	 */
	public void init(boolean forEncryption, CipherParameters params) {
	}

	/**
	 *  returns the largest size an input block can be.
	 * 
	 *  @return maximum size for an input block.
	 */
	public int getInputBlockSize() {
	}

	/**
	 *  returns the maximum size of the block produced by this cipher.
	 * 
	 *  @return maximum size of the output block produced by the cipher.
	 */
	public int getOutputBlockSize() {
	}

	/**
	 *  add another byte for processing.
	 *  
	 *  @param in the input byte.
	 */
	public void processByte(byte in) {
	}

	/**
	 *  add len bytes to the buffer for processing.
	 * 
	 *  @param in the input data
	 *  @param inOff offset into the in array where the data starts
	 *  @param len the length of the block to be processed.
	 */
	public void processBytes(byte[] in, int inOff, int len) {
	}

	/**
	 *  process the contents of the buffer using the underlying
	 *  cipher.
	 * 
	 *  @return the result of the encryption/decryption process on the
	 *  buffer.
	 *  @exception InvalidCipherTextException if we are given a garbage block.
	 */
	public byte[] doFinal() {
	}

	/**
	 *  Reset the buffer and the underlying cipher.
	 */
	public void reset() {
	}
}
