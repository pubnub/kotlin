/**
 * 
 * Classes for parameter objects for ciphers and generators.
 */
package org.bouncycastle.crypto.params;


/**
 *  Cipher parameters with a fixed salt value associated with them.
 */
public class ParametersWithSalt implements org.bouncycastle.crypto.CipherParameters {

	public ParametersWithSalt(org.bouncycastle.crypto.CipherParameters parameters, byte[] salt) {
	}

	public ParametersWithSalt(org.bouncycastle.crypto.CipherParameters parameters, byte[] salt, int saltOff, int saltLen) {
	}

	public byte[] getSalt() {
	}

	public org.bouncycastle.crypto.CipherParameters getParameters() {
	}
}
