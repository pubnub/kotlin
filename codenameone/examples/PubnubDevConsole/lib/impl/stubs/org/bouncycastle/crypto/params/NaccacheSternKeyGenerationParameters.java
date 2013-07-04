/**
 * 
 * Classes for parameter objects for ciphers and generators.
 */
package org.bouncycastle.crypto.params;


/**
 *  Parameters for NaccacheStern public private key generation. For details on
 *  this cipher, please see
 *  
 *  http://www.gemplus.com/smart/rd/publications/pdf/NS98pkcs.pdf
 */
public class NaccacheSternKeyGenerationParameters extends org.bouncycastle.crypto.KeyGenerationParameters {

	/**
	 *  Parameters for generating a NaccacheStern KeyPair.
	 *  
	 *  @param random
	 *             The source of randomness
	 *  @param strength
	 *             The desired strength of the Key in Bits
	 *  @param certainty
	 *             the probability that the generated primes are not really prime
	 *             as integer: 2^(-certainty) is then the probability
	 *  @param cntSmallPrimes
	 *             How many small key factors are desired
	 */
	public NaccacheSternKeyGenerationParameters(javabc.SecureRandom random, int strength, int certainty, int cntSmallPrimes) {
	}

	/**
	 *  Parameters for a NaccacheStern KeyPair.
	 *  
	 *  @param random
	 *             The source of randomness
	 *  @param strength
	 *             The desired strength of the Key in Bits
	 *  @param certainty
	 *             the probability that the generated primes are not really prime
	 *             as integer: 2^(-certainty) is then the probability
	 *  @param cntSmallPrimes
	 *             How many small key factors are desired
	 *  @param debug
	 *             Turn debugging on or off (reveals secret information, use with
	 *             caution)
	 */
	public NaccacheSternKeyGenerationParameters(javabc.SecureRandom random, int strength, int certainty, int cntSmallPrimes, boolean debug) {
	}

	/**
	 *  @return Returns the certainty.
	 */
	public int getCertainty() {
	}

	/**
	 *  @return Returns the cntSmallPrimes.
	 */
	public int getCntSmallPrimes() {
	}

	public boolean isDebug() {
	}
}
