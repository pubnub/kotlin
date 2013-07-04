/**
 * 
 * Message digest classes.
 */
package org.bouncycastle.crypto.digests;


/**
 *  Wrapper class that reduces the output length of a particular digest to
 *  only the first n bytes of the digest function.
 */
public class ShortenedDigest implements org.bouncycastle.crypto.ExtendedDigest {

	/**
	 *  Base constructor.
	 *  
	 *  @param baseDigest underlying digest to use.
	 *  @param length length in bytes of the output of doFinal.
	 *  @exception IllegalArgumentException if baseDigest is null, or length is greater than baseDigest.getDigestSize().
	 */
	public ShortenedDigest(org.bouncycastle.crypto.ExtendedDigest baseDigest, int length) {
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

	public int getByteLength() {
	}
}
