/**
 * 
 * Generators for keys, key pairs and password based encryption algorithms.
 */
package org.bouncycastle.crypto.generators;


public class DHParametersGenerator {

	public DHParametersGenerator() {
	}

	/**
	 *  Initialise the parameters generator.
	 *  
	 *  @param size bit length for the prime p
	 *  @param certainty level of certainty for the prime number tests
	 *  @param random  a source of randomness
	 */
	public void init(int size, int certainty, javabc.SecureRandom random) {
	}

	/**
	 *  which generates the p and g values from the given parameters,
	 *  returning the DHParameters object.
	 *  <p>
	 *  Note: can take a while...
	 */
	public org.bouncycastle.crypto.params.DHParameters generateParameters() {
	}
}
