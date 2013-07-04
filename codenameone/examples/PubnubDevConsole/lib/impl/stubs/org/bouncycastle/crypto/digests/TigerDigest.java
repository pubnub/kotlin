/**
 * 
 * Message digest classes.
 */
package org.bouncycastle.crypto.digests;


/**
 *  implementation of Tiger based on:
 *  <a href="http://www.cs.technion.ac.il/~biham/Reports/Tiger">
 *   http://www.cs.technion.ac.il/~biham/Reports/Tiger</a>
 */
public class TigerDigest implements org.bouncycastle.crypto.ExtendedDigest {

	/**
	 *  Standard constructor
	 */
	public TigerDigest() {
	}

	/**
	 *  Copy constructor.  This will copy the state of the provided
	 *  message digest.
	 */
	public TigerDigest(TigerDigest t) {
	}

	public String getAlgorithmName() {
	}

	public int getDigestSize() {
	}

	public void update(byte in) {
	}

	public void update(byte[] in, int inOff, int len) {
	}

	public void unpackWord(long r, byte[] out, int outOff) {
	}

	public int doFinal(byte[] out, int outOff) {
	}

	/**
	 *  reset the chaining variables
	 */
	public void reset() {
	}

	public int getByteLength() {
	}
}
