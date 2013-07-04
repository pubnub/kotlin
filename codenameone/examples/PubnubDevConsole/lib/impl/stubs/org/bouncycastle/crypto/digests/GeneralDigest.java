/**
 * 
 * Message digest classes.
 */
package org.bouncycastle.crypto.digests;


/**
 *  base implementation of MD4 family style digest as outlined in
 *  "Handbook of Applied Cryptography", pages 344 - 347.
 */
public abstract class GeneralDigest implements org.bouncycastle.crypto.ExtendedDigest {

	/**
	 *  Standard constructor
	 */
	protected GeneralDigest() {
	}

	/**
	 *  Copy constructor.  We are using copy constructors in place
	 *  of the Object.clone() interface as this interface is not
	 *  supported by J2ME.
	 */
	protected GeneralDigest(GeneralDigest t) {
	}

	public void update(byte in) {
	}

	public void update(byte[] in, int inOff, int len) {
	}

	public void finish() {
	}

	public void reset() {
	}

	public int getByteLength() {
	}

	protected abstract void processWord(byte[] in, int inOff) {
	}

	protected abstract void processLength(long bitLength) {
	}

	protected abstract void processBlock() {
	}
}
