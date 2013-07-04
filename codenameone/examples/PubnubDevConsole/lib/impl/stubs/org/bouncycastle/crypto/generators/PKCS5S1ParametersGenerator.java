/**
 * 
 * Generators for keys, key pairs and password based encryption algorithms.
 */
package org.bouncycastle.crypto.generators;


/**
 *  Generator for PBE derived keys and ivs as defined by PKCS 5 V2.0 Scheme 1.
 *  Note this generator is limited to the size of the hash produced by the
 *  digest used to drive it.
 *  <p>
 *  The document this implementation is based on can be found at
 *  <a href=http://www.rsasecurity.com/rsalabs/pkcs/pkcs-5/index.html>
 *  RSA's PKCS5 Page</a>
 */
public class PKCS5S1ParametersGenerator extends org.bouncycastle.crypto.PBEParametersGenerator {

	/**
	 *  Construct a PKCS 5 Scheme 1 Parameters generator. 
	 * 
	 *  @param digest the digest to be used as the source of derived keys.
	 */
	public PKCS5S1ParametersGenerator(org.bouncycastle.crypto.Digest digest) {
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
