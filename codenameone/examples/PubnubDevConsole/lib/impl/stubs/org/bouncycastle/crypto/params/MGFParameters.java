/**
 * 
 * Classes for parameter objects for ciphers and generators.
 */
package org.bouncycastle.crypto.params;


/**
 *  parameters for mask derivation functions.
 */
public class MGFParameters implements org.bouncycastle.crypto.DerivationParameters {

	public MGFParameters(byte[] seed) {
	}

	public MGFParameters(byte[] seed, int off, int len) {
	}

	public byte[] getSeed() {
	}
}
