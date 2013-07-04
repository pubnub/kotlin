/**
 * 
 * Basic signers.
 */
package org.bouncycastle.crypto.signers;


public class GenericSigner implements org.bouncycastle.crypto.Signer {

	public GenericSigner(org.bouncycastle.crypto.AsymmetricBlockCipher engine, org.bouncycastle.crypto.Digest digest) {
	}

	/**
	 *  initialise the signer for signing or verification.
	 * 
	 *  @param forSigning
	 *             true if for signing, false otherwise
	 *  @param parameters
	 *             necessary parameters.
	 */
	public void init(boolean forSigning, org.bouncycastle.crypto.CipherParameters parameters) {
	}

	/**
	 *  update the internal digest with the byte b
	 */
	public void update(byte input) {
	}

	/**
	 *  update the internal digest with the byte array in
	 */
	public void update(byte[] input, int inOff, int length) {
	}

	/**
	 *  Generate a signature for the message we've been loaded with using the key
	 *  we were initialised with.
	 */
	public byte[] generateSignature() {
	}

	/**
	 *  return true if the internal state represents the signature described in
	 *  the passed in array.
	 */
	public boolean verifySignature(byte[] signature) {
	}

	public void reset() {
	}
}
