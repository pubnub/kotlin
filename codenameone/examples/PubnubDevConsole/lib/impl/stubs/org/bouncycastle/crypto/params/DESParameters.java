/**
 * 
 * Classes for parameter objects for ciphers and generators.
 */
package org.bouncycastle.crypto.params;


public class DESParameters extends KeyParameter {

	public static final int DES_KEY_LENGTH = 8;

	public DESParameters(byte[] key) {
	}

	/**
	 *  DES has 16 weak keys.  This method will check
	 *  if the given DES key material is weak or semi-weak.
	 *  Key material that is too short is regarded as weak.
	 *  <p>
	 *  See <a href="http://www.counterpane.com/applied.html">"Applied
	 *  Cryptography"</a> by Bruce Schneier for more information.
	 * 
	 *  @return true if the given DES key material is weak or semi-weak,
	 *      false otherwise.
	 */
	public static boolean isWeakKey(byte[] key, int offset) {
	}

	/**
	 *  DES Keys use the LSB as the odd parity bit.  This can
	 *  be used to check for corrupt keys.
	 * 
	 *  @param bytes the byte array to set the parity on.
	 */
	public static void setOddParity(byte[] bytes) {
	}
}
