/**
 * 
 * Message digest classes.
 */
package org.bouncycastle.crypto.digests;


/**
 *  implementation of MD5 as outlined in "Handbook of Applied Cryptography", pages 346 - 347.
 */
public class MD5Digest extends GeneralDigest {

	/**
	 *  Standard constructor
	 */
	public MD5Digest() {
	}

	/**
	 *  Copy constructor.  This will copy the state of the provided
	 *  message digest.
	 */
	public MD5Digest(MD5Digest t) {
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
	 *  reset the chaining variables to the IV values.
	 */
	public void reset() {
	}

	protected void processBlock() {
	}
}
