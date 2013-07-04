/**
 * 
 * Lightweight psuedo-random number generators.
 */
package org.bouncycastle.crypto.prng;


/**
 *  Random generation based on the digest with counter. Calling addSeedMaterial will
 *  always increase the entropy of the hash.
 *  <p>
 *  Internal access to the digest is synchronized so a single one of these can be shared.
 *  </p>
 */
public class DigestRandomGenerator implements RandomGenerator {

	public DigestRandomGenerator(org.bouncycastle.crypto.Digest digest) {
	}

	public void addSeedMaterial(byte[] inSeed) {
	}

	public void addSeedMaterial(long rSeed) {
	}

	public void nextBytes(byte[] bytes) {
	}

	public void nextBytes(byte[] bytes, int start, int len) {
	}
}
