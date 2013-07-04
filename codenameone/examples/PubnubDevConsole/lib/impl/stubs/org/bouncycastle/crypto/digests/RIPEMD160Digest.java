/**
 * 
 * Message digest classes.
 */
package org.bouncycastle.crypto.digests;


/**
 *  implementation of RIPEMD see,
 *  http://www.esat.kuleuven.ac.be/~bosselae/ripemd160.html
 */
public class RIPEMD160Digest extends GeneralDigest {

	/**
	 *  Standard constructor
	 */
	public RIPEMD160Digest() {
	}

	/**
	 *  Copy constructor.  This will copy the state of the provided
	 *  message digest.
	 */
	public RIPEMD160Digest(RIPEMD160Digest t) {
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
