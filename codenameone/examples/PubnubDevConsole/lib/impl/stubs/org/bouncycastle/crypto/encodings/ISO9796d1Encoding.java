/**
 * 
 * Block encodings for asymmetric ciphers.
 */
package org.bouncycastle.crypto.encodings;


/**
 *  ISO 9796-1 padding. Note in the light of recent results you should
 *  only use this with RSA (rather than the "simpler" Rabin keys) and you
 *  should never use it with anything other than a hash (ie. even if the
 *  message is small don't sign the message, sign it's hash) or some "random"
 *  value. See your favorite search engine for details.
 */
public class ISO9796d1Encoding implements org.bouncycastle.crypto.AsymmetricBlockCipher {

	public ISO9796d1Encoding(org.bouncycastle.crypto.AsymmetricBlockCipher cipher) {
	}

	public org.bouncycastle.crypto.AsymmetricBlockCipher getUnderlyingCipher() {
	}

	public void init(boolean forEncryption, org.bouncycastle.crypto.CipherParameters param) {
	}

	/**
	 *  return the input block size. The largest message we can process
	 *  is (key_size_in_bits + 3)/16, which in our world comes to
	 *  key_size_in_bytes / 2.
	 */
	public int getInputBlockSize() {
	}

	/**
	 *  return the maximum possible size for the output.
	 */
	public int getOutputBlockSize() {
	}

	/**
	 *  set the number of bits in the next message to be treated as
	 *  pad bits.
	 */
	public void setPadBits(int padBits) {
	}

	/**
	 *  retrieve the number of pad bits in the last decoded message.
	 */
	public int getPadBits() {
	}

	public byte[] processBlock(byte[] in, int inOff, int inLen) {
	}
}
