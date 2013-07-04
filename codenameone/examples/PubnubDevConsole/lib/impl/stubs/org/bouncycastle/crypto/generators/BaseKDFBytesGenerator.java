/**
 * 
 * Generators for keys, key pairs and password based encryption algorithms.
 */
package org.bouncycastle.crypto.generators;


/**
 *  Basic KDF generator for derived keys and ivs as defined by IEEE P1363a/ISO 18033
 *  <br>
 *  This implementation is based on ISO 18033/P1363a.
 */
public class BaseKDFBytesGenerator implements org.bouncycastle.crypto.DerivationFunction {

	/**
	 *  Construct a KDF Parameters generator.
	 *  <p>
	 *  @param counterStart value of counter.
	 *  @param digest the digest to be used as the source of derived keys.
	 */
	protected BaseKDFBytesGenerator(int counterStart, org.bouncycastle.crypto.Digest digest) {
	}

	public void init(org.bouncycastle.crypto.DerivationParameters param) {
	}

	/**
	 *  return the underlying digest.
	 */
	public org.bouncycastle.crypto.Digest getDigest() {
	}

	/**
	 *  fill len bytes of the output buffer with bytes generated from
	 *  the derivation function.
	 * 
	 *  @throws IllegalArgumentException if the size of the request will cause an overflow.
	 *  @throws DataLengthException if the out buffer is too small.
	 */
	public int generateBytes(byte[] out, int outOff, int len) {
	}
}
