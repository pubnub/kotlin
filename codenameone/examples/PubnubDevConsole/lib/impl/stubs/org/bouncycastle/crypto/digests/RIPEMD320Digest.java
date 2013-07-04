/**
 * 
 * Message digest classes.
 */
package org.bouncycastle.crypto.digests;


/**
 *  implementation of RIPEMD 320.
 *  <p>
 *  <b>Note:</b> this implementation offers the same level of security
 *  as RIPEMD 160.
 */
public class RIPEMD320Digest extends GeneralDigest {

	/**
	 *  Standard constructor
	 */
	public RIPEMD320Digest() {
	}

	/**
	 *  Copy constructor.  This will copy the state of the provided
	 *  message digest.
	 */
	public RIPEMD320Digest(RIPEMD320Digest t) {
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
