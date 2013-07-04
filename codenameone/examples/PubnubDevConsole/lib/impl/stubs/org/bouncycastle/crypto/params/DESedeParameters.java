/**
 * 
 * Classes for parameter objects for ciphers and generators.
 */
package org.bouncycastle.crypto.params;


public class DESedeParameters extends DESParameters {

	public static final int DES_EDE_KEY_LENGTH = 24;

	public DESedeParameters(byte[] key) {
	}

	/**
	 *  return true if the passed in key is a DES-EDE weak key.
	 * 
	 *  @param key bytes making up the key
	 *  @param offset offset into the byte array the key starts at
	 *  @param length number of bytes making up the key
	 */
	public static boolean isWeakKey(byte[] key, int offset, int length) {
	}

	/**
	 *  return true if the passed in key is a DES-EDE weak key.
	 * 
	 *  @param key bytes making up the key
	 *  @param offset offset into the byte array the key starts at
	 */
	public static boolean isWeakKey(byte[] key, int offset) {
	}
}
