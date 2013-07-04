/**
 * 
 * Generators for keys, key pairs and password based encryption algorithms.
 */
package org.bouncycastle.crypto.generators;


/**
 *  Generator for PBE derived keys and ivs as usd by OpenSSL.
 *  <p>
 *  The scheme is a simple extension of PKCS 5 V2.0 Scheme 1 using MD5 with an
 *  iteration count of 1.
 *  <p>
 */
public class OpenSSLPBEParametersGenerator extends org.bouncycastle.crypto.PBEParametersGenerator {

	/**
	 *  Construct a OpenSSL Parameters generator. 
	 */
	public OpenSSLPBEParametersGenerator() {
	}

	/**
	 *  Initialise - note the iteration count for this algorithm is fixed at 1.
	 *  
	 *  @param password password to use.
	 *  @param salt salt to use.
	 */
	public void init(byte[] password, byte[] salt) {
	}

	/**
	 *  Generate a key parameter derived from the password, salt, and iteration
	 *  count we are currently initialised with.
	 * 
	 *  @param keySize the size of the key we want (in bits)
	 *  @return a KeyParameter object.
	 *  @exception IllegalArgumentException if the key length larger than the base hash size.
	 */
	public org.bouncycastle.crypto.CipherParameters generateDerivedParameters(int keySize) {
	}

	/**
	 *  Generate a key with initialisation vector parameter derived from
	 *  the password, salt, and iteration count we are currently initialised
	 *  with.
	 * 
	 *  @param keySize the size of the key we want (in bits)
	 *  @param ivSize the size of the iv we want (in bits)
	 *  @return a ParametersWithIV object.
	 *  @exception IllegalArgumentException if keySize + ivSize is larger than the base hash size.
	 */
	public org.bouncycastle.crypto.CipherParameters generateDerivedParameters(int keySize, int ivSize) {
	}

	/**
	 *  Generate a key parameter for use with a MAC derived from the password,
	 *  salt, and iteration count we are currently initialised with.
	 * 
	 *  @param keySize the size of the key we want (in bits)
	 *  @return a KeyParameter object.
	 *  @exception IllegalArgumentException if the key length larger than the base hash size.
	 */
	public org.bouncycastle.crypto.CipherParameters generateDerivedMacParameters(int keySize) {
	}
}
