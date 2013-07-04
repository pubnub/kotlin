/**
 * 
 * Message digest classes.
 */
package org.bouncycastle.crypto.digests;


/**
 *  implementation of RIPEMD256.
 *  <p>
 *  <b>note:</b> this algorithm offers the same level of security as RIPEMD128.
 */
public class RIPEMD256Digest extends GeneralDigest {

	/**
	 *  Standard constructor
	 */
	public RIPEMD256Digest() {
	}

	/**
	 *  Copy constructor.  This will copy the state of the provided
	 *  message digest.
	 */
	public RIPEMD256Digest(RIPEMD256Digest t) {
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
