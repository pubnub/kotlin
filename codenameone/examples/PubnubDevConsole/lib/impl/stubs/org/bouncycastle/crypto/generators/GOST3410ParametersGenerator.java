/**
 * 
 * Generators for keys, key pairs and password based encryption algorithms.
 */
package org.bouncycastle.crypto.generators;


/**
 *  generate suitable parameters for GOST3410.
 */
public class GOST3410ParametersGenerator {

	public GOST3410ParametersGenerator() {
	}

	/**
	 *  initialise the key generator.
	 * 
	 *  @param size size of the key
	 *  @param typeproc type procedure A,B = 1;  A',B' - else
	 *  @param random random byte source.
	 */
	public void init(int size, int typeproc, javabc.SecureRandom random) {
	}

	/**
	 *  which generates the p , q and a values from the given parameters,
	 *  returning the GOST3410Parameters object.
	 */
	public org.bouncycastle.crypto.params.GOST3410Parameters generateParameters() {
	}
}
