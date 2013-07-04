/**
 * 
 * Classes for parameter objects for ciphers and generators.
 */
package org.bouncycastle.crypto.params;


public class IESWithCipherParameters extends IESParameters {

	/**
	 *  @param derivation the derivation parameter for the KDF function.
	 *  @param encoding the encoding parameter for the KDF function.
	 *  @param macKeySize the size of the MAC key (in bits).
	 *  @param cipherKeySize the size of the associated Cipher key (in bits).
	 */
	public IESWithCipherParameters(byte[] derivation, byte[] encoding, int macKeySize, int cipherKeySize) {
	}

	public int getCipherKeySize() {
	}
}
