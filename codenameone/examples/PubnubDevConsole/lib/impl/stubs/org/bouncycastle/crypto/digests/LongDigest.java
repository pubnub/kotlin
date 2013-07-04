/**
 * 
 * Message digest classes.
 */
package org.bouncycastle.crypto.digests;


/**
 *  Base class for SHA-384 and SHA-512.
 */
public abstract class LongDigest implements org.bouncycastle.crypto.ExtendedDigest {

	protected long H1;

	protected long H2;

	protected long H3;

	protected long H4;

	protected long H5;

	protected long H6;

	protected long H7;

	protected long H8;

	/**
	 *  Constructor for variable length word
	 */
	protected LongDigest() {
	}

	/**
	 *  Copy constructor.  We are using copy constructors in place
	 *  of the Object.clone() interface as this interface is not
	 *  supported by J2ME.
	 */
	protected LongDigest(LongDigest t) {
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

	protected void processWord(byte[] in, int inOff) {
	}

	protected void processLength(long lowW, long hiW) {
	}

	protected void processBlock() {
	}
}
