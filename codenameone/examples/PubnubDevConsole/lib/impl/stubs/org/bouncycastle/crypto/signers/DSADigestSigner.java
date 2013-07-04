/**
 * 
 * Basic signers.
 */
package org.bouncycastle.crypto.signers;


public class DSADigestSigner implements org.bouncycastle.crypto.Signer {

	public DSADigestSigner(org.bouncycastle.crypto.DSA signer, org.bouncycastle.crypto.Digest digest) {
	}

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
	 *  Generate a signature for the message we've been loaded with using
	 *  the key we were initialised with.
	 */
	public byte[] generateSignature() {
	}

	public boolean verifySignature(byte[] signature) {
	}

	public void reset() {
	}
}
