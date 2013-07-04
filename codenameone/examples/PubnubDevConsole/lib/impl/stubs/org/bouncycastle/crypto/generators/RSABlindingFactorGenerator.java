/**
 * 
 * Generators for keys, key pairs and password based encryption algorithms.
 */
package org.bouncycastle.crypto.generators;


/**
 *  Generate a random factor suitable for use with RSA blind signatures
 *  as outlined in Chaum's blinding and unblinding as outlined in
 *  "Handbook of Applied Cryptography", page 475.
 */
public class RSABlindingFactorGenerator {

	public RSABlindingFactorGenerator() {
	}

	/**
	 *  Initialise the factor generator
	 * 
	 *  @param param the necessary RSA key parameters.
	 */
	public void init(org.bouncycastle.crypto.CipherParameters param) {
	}

	/**
	 *  Generate a suitable blind factor for the public key the generator was initialised with.
	 * 
	 *  @return a random blind factor
	 */
	public javabc.BigInteger generateBlindingFactor() {
	}
}
