/**
 * 
 * Generators for keys, key pairs and password based encryption algorithms.
 */
package org.bouncycastle.crypto.generators;


public class ECKeyPairGenerator implements org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator, org.bouncycastle.math.ec.ECConstants {

	public ECKeyPairGenerator() {
	}

	public void init(org.bouncycastle.crypto.KeyGenerationParameters param) {
	}

	/**
	 *  Given the domain parameters this routine generates an EC key
	 *  pair in accordance with X9.62 section 5.2.1 pages 26, 27.
	 */
	public org.bouncycastle.crypto.AsymmetricCipherKeyPair generateKeyPair() {
	}
}
