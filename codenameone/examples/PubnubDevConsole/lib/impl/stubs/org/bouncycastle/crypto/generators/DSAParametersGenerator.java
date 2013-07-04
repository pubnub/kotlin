/**
 * 
 * Generators for keys, key pairs and password based encryption algorithms.
 */
package org.bouncycastle.crypto.generators;


/**
 *  generate suitable parameters for DSA, in line with FIPS 186-2.
 */
public class DSAParametersGenerator {

	public DSAParametersGenerator() {
	}

	/**
	 *  initialise the key generator.
	 * 
	 *  @param size size of the key (range 2^512 -> 2^1024 - 64 bit increments)
	 *  @param certainty measure of robustness of prime (for FIPS 186-2 compliance this should be at least 80).
	 *  @param random random byte source.
	 */
	public void init(int size, int certainty, javabc.SecureRandom random) {
	}

	/**
	 *  which generates the p and g values from the given parameters,
	 *  returning the DSAParameters object.
	 *  <p>
	 *  Note: can take a while...
	 */
	public org.bouncycastle.crypto.params.DSAParameters generateParameters() {
	}
}
