/**
 * 
 * Base classes for the lightweight API.
 */
package org.bouncycastle.crypto;


/**
 *  The basic interface that basic Diffie-Hellman implementations
 *  conforms to.
 */
public interface BasicAgreement {

	/**
	 *  initialise the agreement engine.
	 */
	public void init(CipherParameters param);

	/**
	 *  given a public key from a given party calculate the next
	 *  message in the agreement sequence. 
	 */
	public javabc.BigInteger calculateAgreement(CipherParameters pubKey);
}
