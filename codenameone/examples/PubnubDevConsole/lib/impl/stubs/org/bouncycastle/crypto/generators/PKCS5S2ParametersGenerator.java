/**
 * 
 * Generators for keys, key pairs and password based encryption algorithms.
 */
package org.bouncycastle.crypto.generators;


/**
 *  Generator for PBE derived keys and ivs as defined by PKCS 5 V2.0 Scheme 2.
 *  This generator uses a SHA-1 HMac as the calculation function.
 *  <p>
 *  The document this implementation is based on can be found at
 *  <a href=http://www.rsasecurity.com/rsalabs/pkcs/pkcs-5/index.html>
 *  RSA's PKCS5 Page</a>
 */
public class PKCS5S2ParametersGenerator extends org.bouncycastle.crypto.PBEParametersGenerator {

	/**
	 *  construct a PKCS5 Scheme 2 Parameters generator.
	 */
	public PKCS5S2ParametersGenerator() {
	}

	public PKCS5S2ParametersGenerator(org.bouncycastle.crypto.Digest digest) {
	}

	/**
	 *  Generate a key parameter derived from the password, salt, and iteration
	 *  count we are currently initialised with.
	 * 
	 *  @param keySize the size of the key we want (in bits)
	 *  @return a KeyParameter object.
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
	 */
	public org.bouncycastle.crypto.CipherParameters generateDerivedParameters(int keySize, int ivSize) {
	}

	/**
	 *  Generate a key parameter for use with a MAC derived from the password,
	 *  salt, and iteration count we are currently initialised with.
	 * 
	 *  @param keySize the size of the key we want (in bits)
	 *  @return a KeyParameter object.
	 */
	public org.bouncycastle.crypto.CipherParameters generateDerivedMacParameters(int keySize) {
	}
}
