/**
 * 
 * Modes for symmetric ciphers.
 */
package org.bouncycastle.crypto.modes;


/**
 *  Implements the Counter with Cipher Block Chaining mode (CCM) detailed in
 *  NIST Special Publication 800-38C.
 *  <p>
 *  <b>Note</b>: this mode is a packet mode - it needs all the data up front.
 */
public class CCMBlockCipher implements AEADBlockCipher {

	/**
	 *  Basic constructor.
	 * 
	 *  @param c the block cipher to be used.
	 */
	public CCMBlockCipher(org.bouncycastle.crypto.BlockCipher c) {
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

	public int processByte(byte in, byte[] out, int outOff) {
	}

	public int processBytes(byte[] in, int inOff, int inLen, byte[] out, int outOff) {
	}

	public int doFinal(byte[] out, int outOff) {
	}

	public void reset() {
	}

	/**
	 *  Returns a byte array containing the mac calculated as part of the
	 *  last encrypt or decrypt operation.
	 *  
	 *  @return the last mac calculated.
	 */
	public byte[] getMac() {
	}

	public int getUpdateOutputSize(int len) {
	}

	public int getOutputSize(int len) {
	}

	public byte[] processPacket(byte[] in, int inOff, int inLen) {
	}
}
