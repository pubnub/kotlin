/**
 * 
 * Classes for parameter objects for ciphers and generators.
 */
package org.bouncycastle.crypto.params;


public class KeyParameter implements org.bouncycastle.crypto.CipherParameters {

	public KeyParameter(byte[] key) {
	}

	public KeyParameter(byte[] key, int keyOff, int keyLen) {
	}

	public byte[] getKey() {
	}
}
