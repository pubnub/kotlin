/**
 * 
 * Classes for creating MACs and HMACs.
 */
package org.bouncycastle.crypto.macs;


/**
 *  HMAC implementation based on RFC2104
 * 
 *  H(K XOR opad, H(K XOR ipad, text))
 */
public class OldHMac implements org.bouncycastle.crypto.Mac {

	/**
	 *  @deprecated uses incorrect pad for SHA-512 and SHA-384 use HMac.
	 */
	public OldHMac(org.bouncycastle.crypto.Digest digest) {
	}

	public String getAlgorithmName() {
	}

	public org.bouncycastle.crypto.Digest getUnderlyingDigest() {
	}

	public void init(org.bouncycastle.crypto.CipherParameters params) {
	}

	public int getMacSize() {
	}

	public void update(byte in) {
	}

	public void update(byte[] in, int inOff, int len) {
	}

	public int doFinal(byte[] out, int outOff) {
	}

	/**
	 *  Reset the mac generator.
	 */
	public void reset() {
	}
}
