package javabc;


/**
 *  An implementation of SecureRandom specifically for the light-weight API, JDK
 *  1.0, and the J2ME. Random generation is based on the traditional SHA1 with
 *  counter. Calling setSeed will always increase the entropy of the hash.
 *  <p>
 *  <b>Do not use this class without calling setSeed at least once</b>! There
 *  are some example seed generators in the org.bouncycastle.prng package.
 */
public class SecureRandom extends java.util.Random {

	protected org.bouncycastle.crypto.prng.RandomGenerator generator;

	public SecureRandom() {
	}

	public SecureRandom(byte[] inSeed) {
	}

	protected SecureRandom(org.bouncycastle.crypto.prng.RandomGenerator generator) {
	}

	public static SecureRandom getInstance(String algorithm) {
	}

	public static SecureRandom getInstance(String algorithm, String provider) {
	}

	public static byte[] getSeed(int numBytes) {
	}

	public byte[] generateSeed(int numBytes) {
	}

	public void setSeed(byte[] inSeed) {
	}

	public void nextBytes(byte[] bytes) {
	}

	public void setSeed(long rSeed) {
	}

	public int nextInt() {
	}

	protected final int next(int numBits) {
	}
}
