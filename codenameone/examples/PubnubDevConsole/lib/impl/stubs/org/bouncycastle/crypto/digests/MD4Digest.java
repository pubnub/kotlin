/**
 * 
 * Message digest classes.
 */
package org.bouncycastle.crypto.digests;


/**
 *  implementation of MD4 as RFC 1320 by R. Rivest, MIT Laboratory for
 *  Computer Science and RSA Data Security, Inc.
 *  <p>
 *  <b>NOTE</b>: This algorithm is only included for backwards compatability
 *  with legacy applications, it's not secure, don't use it for anything new!
 */
public class MD4Digest extends GeneralDigest {

	/**
	 *  Standard constructor
	 */
	public MD4Digest() {
	}

	/**
	 *  Copy constructor.  This will copy the state of the provided
	 *  message digest.
	 */
	public MD4Digest(MD4Digest t) {
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
