/**
 * 
 * Classes for parameter objects for ciphers and generators.
 */
package org.bouncycastle.crypto.params;


public class ParametersWithRandom implements org.bouncycastle.crypto.CipherParameters {

	public ParametersWithRandom(org.bouncycastle.crypto.CipherParameters parameters, javabc.SecureRandom random) {
	}

	public ParametersWithRandom(org.bouncycastle.crypto.CipherParameters parameters) {
	}

	public javabc.SecureRandom getRandom() {
	}

	public org.bouncycastle.crypto.CipherParameters getParameters() {
	}
}
