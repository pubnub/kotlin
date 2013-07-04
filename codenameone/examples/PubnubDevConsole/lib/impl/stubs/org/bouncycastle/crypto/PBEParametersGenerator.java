/**
 * 
 * Base classes for the lightweight API.
 */
package org.bouncycastle.crypto;


/**
 *  super class for all Password Based Encryption (PBE) parameter generator classes.
 */
public abstract class PBEParametersGenerator {

	protected byte[] password;

	protected byte[] salt;

	protected int iterationCount;

	/**
	 *  base constructor.
	 */
	protected PBEParametersGenerator() {
	}

	/**
	 *  initialise the PBE generator.
	 * 
	 *  @param password the password converted into bytes (see below).
	 *  @param salt the salt to be mixed with the password.
	 *  @param iterationCount the number of iterations the "mixing" function
	 *  is to be applied for.
	 */
	public void init(byte[] password, byte[] salt, int iterationCount) {
	}

	/**
	 *  return the password byte array.
	 * 
	 *  @return the password byte array.
	 */
	public byte[] getPassword() {
	}

	/**
	 *  return the salt byte array.
	 * 
	 *  @return the salt byte array.
	 */
	public byte[] getSalt() {
	}

	/**
	 *  return the iteration count.
	 * 
	 *  @return the iteration count.
	 */
	public int getIterationCount() {
	}

	/**
	 *  generate derived parameters for a key of length keySize.
	 * 
	 *  @param keySize the length, in bits, of the key required.
	 *  @return a parameters object representing a key.
	 */
	public abstract CipherParameters generateDerivedParameters(int keySize) {
	}

	/**
	 *  generate derived parameters for a key of length keySize, and
	 *  an initialisation vector (IV) of length ivSize.
	 * 
	 *  @param keySize the length, in bits, of the key required.
	 *  @param ivSize the length, in bits, of the iv required.
	 *  @return a parameters object representing a key and an IV.
	 */
	public abstract CipherParameters generateDerivedParameters(int keySize, int ivSize) {
	}

	/**
	 *  generate derived parameters for a key of length keySize, specifically
	 *  for use with a MAC.
	 * 
	 *  @param keySize the length, in bits, of the key required.
	 *  @return a parameters object representing a key.
	 */
	public abstract CipherParameters generateDerivedMacParameters(int keySize) {
	}

	/**
	 *  converts a password to a byte array according to the scheme in
	 *  PKCS5 (ascii, no padding)
	 * 
	 *  @param password a character array representing the password.
	 *  @return a byte array representing the password.
	 */
	public static byte[] PKCS5PasswordToBytes(char[] password) {
	}

	/**
	 *  converts a password to a byte array according to the scheme in
	 *  PKCS5 (UTF-8, no padding)
	 * 
	 *  @param password a character array representing the password.
	 *  @return a byte array representing the password.
	 */
	public static byte[] PKCS5PasswordToUTF8Bytes(char[] password) {
	}

	/**
	 *  converts a password to a byte array according to the scheme in
	 *  PKCS12 (unicode, big endian, 2 zero pad bytes at the end).
	 * 
	 *  @param password a character array representing the password.
	 *  @return a byte array representing the password.
	 */
	public static byte[] PKCS12PasswordToBytes(char[] password) {
	}
}
