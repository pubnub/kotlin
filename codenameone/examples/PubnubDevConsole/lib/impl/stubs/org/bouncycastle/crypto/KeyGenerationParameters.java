/**
 * 
 * Base classes for the lightweight API.
 */
package org.bouncycastle.crypto;


/**
 *  The base class for parameters to key generators.
 */
public class KeyGenerationParameters {

	/**
	 *  initialise the generator with a source of randomness
	 *  and a strength (in bits).
	 * 
	 *  @param random the random byte source.
	 *  @param strength the size, in bits, of the keys we want to produce.
	 */
	public KeyGenerationParameters(javabc.SecureRandom random, int strength) {
	}

	/**
	 *  return the random source associated with this
	 *  generator.
	 * 
	 *  @return the generators random source.
	 */
	public javabc.SecureRandom getRandom() {
	}

	/**
	 *  return the bit strength for keys produced by this generator,
	 * 
	 *  @return the strength of the keys this generator produces (in bits).
	 */
	public int getStrength() {
	}
}
