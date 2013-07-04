/**
 * 
 * Message digest classes.
 */
package org.bouncycastle.crypto.digests;


/**
 *  implementation of SHA-1 as outlined in "Handbook of Applied Cryptography", pages 346 - 349.
 * 
 *  It is interesting to ponder why the, apart from the extra IV, the other difference here from MD5
 *  is the "endienness" of the word processing!
 */
public class SHA1Digest extends GeneralDigest {

	/**
	 *  Standard constructor
	 */
	public SHA1Digest() {
	}

	/**
	 *  Copy constructor.  This will copy the state of the provided
	 *  message digest.
	 */
	public SHA1Digest(SHA1Digest t) {
	}

	public String getAlgorithmName() {
	}

	public int getDigestSize() {
	}

	protected void processWord(byte[] in, int inOff) {
	}

	protected void processLength(long bitLength) {
	}

	public int doFinal(byte[] out, int outOff) {
	}

	/**
	 *  reset the chaining variables
	 */
	public void reset() {
	}

	protected void processBlock() {
	}
}
