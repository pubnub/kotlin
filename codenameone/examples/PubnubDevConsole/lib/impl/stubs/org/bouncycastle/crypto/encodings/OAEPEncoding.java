/**
 * 
 * Block encodings for asymmetric ciphers.
 */
package org.bouncycastle.crypto.encodings;


/**
 *  Optimal Asymmetric Encryption Padding (OAEP) - see PKCS 1 V 2.
 */
public class OAEPEncoding implements org.bouncycastle.crypto.AsymmetricBlockCipher {

	public OAEPEncoding(org.bouncycastle.crypto.AsymmetricBlockCipher cipher) {
	}

	public OAEPEncoding(org.bouncycastle.crypto.AsymmetricBlockCipher cipher, org.bouncycastle.crypto.Digest hash) {
	}

	public OAEPEncoding(org.bouncycastle.crypto.AsymmetricBlockCipher cipher, org.bouncycastle.crypto.Digest hash, byte[] encodingParams) {
	}

	public OAEPEncoding(org.bouncycastle.crypto.AsymmetricBlockCipher cipher, org.bouncycastle.crypto.Digest hash, org.bouncycastle.crypto.Digest mgf1Hash, byte[] encodingParams) {
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

	public byte[] encodeBlock(byte[] in, int inOff, int inLen) {
	}

	/**
	 *  @exception InvalidCipherTextException if the decrypted block turns out to
	 *  be badly formatted.
	 */
	public byte[] decodeBlock(byte[] in, int inOff, int inLen) {
	}
}
