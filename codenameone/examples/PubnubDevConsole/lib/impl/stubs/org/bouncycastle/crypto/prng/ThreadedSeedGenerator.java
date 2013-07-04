/**
 * 
 * Lightweight psuedo-random number generators.
 */
package org.bouncycastle.crypto.prng;


/**
 *  A thread based seed generator - one source of randomness.
 *  <p>
 *  Based on an idea from Marcus Lippert.
 *  </p>
 */
public class ThreadedSeedGenerator {

	public ThreadedSeedGenerator() {
	}

	/**
	 *  Generate seed bytes. Set fast to false for best quality.
	 *  <p>
	 *  If fast is set to true, the code should be round about 8 times faster when
	 *  generating a long sequence of random bytes. 20 bytes of random values using
	 *  the fast mode take less than half a second on a Nokia e70. If fast is set to false,
	 *  it takes round about 2500 ms.
	 *  </p>
	 *  @param numBytes the number of bytes to generate
	 *  @param fast true if fast mode should be used
	 */
	public byte[] generateSeed(int numBytes, boolean fast) {
	}
}
