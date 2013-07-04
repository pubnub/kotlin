/**
 * 
 * Base classes for the lightweight API.
 */
package org.bouncycastle.crypto;


/**
 *  The base class for symmetric, or secret, cipher key generators.
 */
public class CipherKeyGenerator {

	protected javabc.SecureRandom random;

	protected int strength;

	public CipherKeyGenerator() {
	}

	/**
	 *  initialise the key generator.
	 * 
	 *  @param param the parameters to be used for key generation
	 */
	public void init(KeyGenerationParameters param) {
	}

	/**
	 *  generate a secret key.
	 * 
	 *  @return a byte array containing the key value.
	 */
	public byte[] generateKey() {
	}
}
