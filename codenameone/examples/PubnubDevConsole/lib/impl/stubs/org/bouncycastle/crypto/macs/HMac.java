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
public class HMac implements org.bouncycastle.crypto.Mac {

	/**
	 *  Base constructor for one of the standard digest algorithms that the 
	 *  byteLength of the algorithm is know for.
	 *  
	 *  @param digest the digest.
	 */
	public HMac(org.bouncycastle.crypto.Digest digest) {
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
