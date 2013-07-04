/**
 * 
 * Generators for keys, key pairs and password based encryption algorithms.
 */
package org.bouncycastle.crypto.generators;


/**
 *  Generator for PBE derived keys and ivs as defined by PKCS 12 V1.0.
 *  <p>
 *  The document this implementation is based on can be found at
 *  <a href=http://www.rsasecurity.com/rsalabs/pkcs/pkcs-12/index.html>
 *  RSA's PKCS12 Page</a>
 */
public class PKCS12ParametersGenerator extends org.bouncycastle.crypto.PBEParametersGenerator {

	public static final int KEY_MATERIAL = 1;

	public static final int IV_MATERIAL = 2;

	public static final int MAC_MATERIAL = 3;

	/**
	 *  Construct a PKCS 12 Parameters generator. This constructor will
	 *  accept any digest which also implements ExtendedDigest.
	 * 
	 *  @param digest the digest to be used as the source of derived keys.
	 *  @exception IllegalArgumentException if an unknown digest is passed in.
	 */
	public PKCS12ParametersGenerator(org.bouncycastle.crypto.Digest digest) {
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
