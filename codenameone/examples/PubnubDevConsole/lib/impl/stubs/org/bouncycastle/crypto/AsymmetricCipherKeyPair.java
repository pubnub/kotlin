/**
 * 
 * Base classes for the lightweight API.
 */
package org.bouncycastle.crypto;


/**
 *  a holding class for public/private parameter pairs.
 */
public class AsymmetricCipherKeyPair {

	/**
	 *  basic constructor.
	 * 
	 *  @param publicParam a public key parameters object.
	 *  @param privateParam the corresponding private key parameters.
	 */
	public AsymmetricCipherKeyPair(CipherParameters publicParam, CipherParameters privateParam) {
	}

	/**
	 *  return the public key parameters.
	 * 
	 *  @return the public key parameters.
	 */
	public CipherParameters getPublic() {
	}

	/**
	 *  return the private key parameters.
	 * 
	 *  @return the private key parameters.
	 */
	public CipherParameters getPrivate() {
	}
}
