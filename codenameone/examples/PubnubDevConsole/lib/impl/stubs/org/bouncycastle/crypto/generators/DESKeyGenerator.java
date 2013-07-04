/**
 * 
 * Generators for keys, key pairs and password based encryption algorithms.
 */
package org.bouncycastle.crypto.generators;


public class DESKeyGenerator extends org.bouncycastle.crypto.CipherKeyGenerator {

	public DESKeyGenerator() {
	}

	/**
	 *  initialise the key generator - if strength is set to zero
	 *  the key generated will be 64 bits in size, otherwise
	 *  strength can be 64 or 56 bits (if you don't count the parity bits).
	 * 
	 *  @param param the parameters to be used for key generation
	 */
	public void init(org.bouncycastle.crypto.KeyGenerationParameters param) {
	}

	public byte[] generateKey() {
	}
}
