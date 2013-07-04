/**
 * 
 * Classes for parameter objects for ciphers and generators.
 */
package org.bouncycastle.crypto.params;


public class ParametersWithIV implements org.bouncycastle.crypto.CipherParameters {

	public ParametersWithIV(org.bouncycastle.crypto.CipherParameters parameters, byte[] iv) {
	}

	public ParametersWithIV(org.bouncycastle.crypto.CipherParameters parameters, byte[] iv, int ivOff, int ivLen) {
	}

	public byte[] getIV() {
	}

	public org.bouncycastle.crypto.CipherParameters getParameters() {
	}
}
