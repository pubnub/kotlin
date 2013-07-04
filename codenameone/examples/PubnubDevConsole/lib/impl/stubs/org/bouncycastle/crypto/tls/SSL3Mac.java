/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


/**
 *  HMAC implementation based on original internet draft for HMAC (RFC 2104)
 *  
 *  The difference is that padding is concatentated versus XORed with the key
 *  
 *  H(K + opad, H(K + ipad, text))
 */
public class SSL3Mac implements org.bouncycastle.crypto.Mac {

	/**
	 *  Base constructor for one of the standard digest algorithms that the byteLength of
	 *  the algorithm is know for. Behaviour is undefined for digests other than MD5 or SHA1.
	 *  
	 *  @param digest the digest.
	 */
	public SSL3Mac(org.bouncycastle.crypto.Digest digest) {
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
