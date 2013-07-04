/**
 * 
 * Modes for symmetric ciphers.
 */
package org.bouncycastle.crypto.modes;


/**
 *  Implements the Segmented Integer Counter (SIC) mode on top of a simple
 *  block cipher. This mode is also known as CTR mode.
 */
public class SICBlockCipher implements org.bouncycastle.crypto.BlockCipher {

	/**
	 *  Basic constructor.
	 * 
	 *  @param c the block cipher to be used.
	 */
	public SICBlockCipher(org.bouncycastle.crypto.BlockCipher c) {
	}

	/**
	 *  return the underlying block cipher that we are wrapping.
	 * 
	 *  @return the underlying block cipher that we are wrapping.
	 */
	public org.bouncycastle.crypto.BlockCipher getUnderlyingCipher() {
	}

	public void init(boolean forEncryption, org.bouncycastle.crypto.CipherParameters params) {
	}

	public String getAlgorithmName() {
	}

	public int getBlockSize() {
	}

	public int processBlock(byte[] in, int inOff, byte[] out, int outOff) {
	}

	public void reset() {
	}
}
