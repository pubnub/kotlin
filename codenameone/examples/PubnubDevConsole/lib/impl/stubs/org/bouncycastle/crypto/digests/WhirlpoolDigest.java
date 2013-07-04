/**
 * 
 * Message digest classes.
 */
package org.bouncycastle.crypto.digests;


/**
 *  Implementation of WhirlpoolDigest, based on Java source published by Barreto
 *  and Rijmen.
 *   
 */
public final class WhirlpoolDigest implements org.bouncycastle.crypto.ExtendedDigest {

	public WhirlpoolDigest() {
	}

	/**
	 *  Copy constructor. This will copy the state of the provided message
	 *  digest.
	 */
	public WhirlpoolDigest(WhirlpoolDigest originalDigest) {
	}

	public String getAlgorithmName() {
	}

	public int getDigestSize() {
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

	public void update(byte in) {
	}

	public void update(byte[] in, int inOff, int len) {
	}

	public int getByteLength() {
	}
}
