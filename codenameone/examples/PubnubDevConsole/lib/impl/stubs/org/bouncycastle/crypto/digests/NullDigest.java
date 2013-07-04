/**
 * 
 * Message digest classes.
 */
package org.bouncycastle.crypto.digests;


public class NullDigest implements org.bouncycastle.crypto.Digest {

	public NullDigest() {
	}

	public String getAlgorithmName() {
	}

	public int getDigestSize() {
	}

	public void update(byte in) {
	}

	public void update(byte[] in, int inOff, int len) {
	}

	public int doFinal(byte[] out, int outOff) {
	}

	public void reset() {
	}
}
