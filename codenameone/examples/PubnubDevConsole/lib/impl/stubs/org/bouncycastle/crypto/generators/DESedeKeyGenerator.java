/**
 * 
 * Generators for keys, key pairs and password based encryption algorithms.
 */
package org.bouncycastle.crypto.generators;


public class DESedeKeyGenerator extends DESKeyGenerator {

	public DESedeKeyGenerator() {
	}

	/**
	 *  initialise the key generator - if strength is set to zero
	 *  the key generated will be 192 bits in size, otherwise
	 *  strength can be 128 or 192 (or 112 or 168 if you don't count
	 *  parity bits), depending on whether you wish to do 2-key or 3-key
	 *  triple DES.
	 * 
	 *  @param param the parameters to be used for key generation
	 */
	public void init(org.bouncycastle.crypto.KeyGenerationParameters param) {
	}

	public byte[] generateKey() {
	}
}
