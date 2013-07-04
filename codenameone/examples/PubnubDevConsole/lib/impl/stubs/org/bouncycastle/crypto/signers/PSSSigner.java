/**
 * 
 * Basic signers.
 */
package org.bouncycastle.crypto.signers;


/**
 *  RSA-PSS as described in PKCS# 1 v 2.1.
 *  <p>
 *  Note: the usual value for the salt length is the number of
 *  bytes in the hash function.
 */
public class PSSSigner implements org.bouncycastle.crypto.Signer {

	public static final byte TRAILER_IMPLICIT = -68;

	/**
	 *  basic constructor
	 * 
	 *  @param cipher the asymmetric cipher to use.
	 *  @param digest the digest to use.
	 *  @param sLen the length of the salt to use (in bytes).
	 */
	public PSSSigner(org.bouncycastle.crypto.AsymmetricBlockCipher cipher, org.bouncycastle.crypto.Digest digest, int sLen) {
	}

	public PSSSigner(org.bouncycastle.crypto.AsymmetricBlockCipher cipher, org.bouncycastle.crypto.Digest contentDigest, org.bouncycastle.crypto.Digest mgfDigest, int sLen) {
	}

	public PSSSigner(org.bouncycastle.crypto.AsymmetricBlockCipher cipher, org.bouncycastle.crypto.Digest digest, int sLen, byte trailer) {
	}

	public PSSSigner(org.bouncycastle.crypto.AsymmetricBlockCipher cipher, org.bouncycastle.crypto.Digest contentDigest, org.bouncycastle.crypto.Digest mgfDigest, int sLen, byte trailer) {
	}

	public void init(boolean forSigning, org.bouncycastle.crypto.CipherParameters param) {
	}

	/**
	 *  update the internal digest with the byte b
	 */
	public void update(byte b) {
	}

	/**
	 *  update the internal digest with the byte array in
	 */
	public void update(byte[] in, int off, int len) {
	}

	/**
	 *  reset the internal state
	 */
	public void reset() {
	}

	/**
	 *  generate a signature for the message we've been loaded with using
	 *  the key we were initialised with.
	 */
	public byte[] generateSignature() {
	}

	/**
	 *  return true if the internal state represents the signature described
	 *  in the passed in array.
	 */
	public boolean verifySignature(byte[] signature) {
	}
}
