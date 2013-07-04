/**
 * 
 * Classes for parameter objects for ciphers and generators.
 */
package org.bouncycastle.crypto.params;


/**
 *  parameters for using an integrated cipher in stream mode.
 */
public class IESParameters implements org.bouncycastle.crypto.CipherParameters {

	/**
	 *  @param derivation the derivation parameter for the KDF function.
	 *  @param encoding the encoding parameter for the KDF function.
	 *  @param macKeySize the size of the MAC key (in bits).
	 */
	public IESParameters(byte[] derivation, byte[] encoding, int macKeySize) {
	}

	public byte[] getDerivationV() {
	}

	public byte[] getEncodingV() {
	}

	public int getMacKeySize() {
	}
}
