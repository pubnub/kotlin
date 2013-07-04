/**
 * 
 * Block encodings for asymmetric ciphers.
 */
package org.bouncycastle.crypto.encodings;


/**
 *  this does your basic PKCS 1 v1.5 padding - whether or not you should be using this
 *  depends on your application - see PKCS1 Version 2 for details.
 */
public class PKCS1Encoding implements org.bouncycastle.crypto.AsymmetricBlockCipher {

	/**
	 *  some providers fail to include the leading zero in PKCS1 encoded blocks. If you need to
	 *  work with one of these set the system property org.bouncycastle.pkcs1.strict to false.
	 *  <p>
	 *  The system property is checked during construction of the encoding object, it is set to 
	 *  true by default.
	 *  </p>
	 */
	public static final String STRICT_LENGTH_ENABLED_PROPERTY = "org.bouncycastle.pkcs1.strict";

	/**
	 *  Basic constructor.
	 *  @param cipher
	 */
	public PKCS1Encoding(org.bouncycastle.crypto.AsymmetricBlockCipher cipher) {
	}

	public org.bouncycastle.crypto.AsymmetricBlockCipher getUnderlyingCipher() {
	}

	public void init(boolean forEncryption, org.bouncycastle.crypto.CipherParameters param) {
	}

	public int getInputBlockSize() {
	}

	public int getOutputBlockSize() {
	}

	public byte[] processBlock(byte[] in, int inOff, int inLen) {
	}
}
