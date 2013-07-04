/**
 * 
 * Basic key agreement classes.
 */
package org.bouncycastle.crypto.agreement;


/**
 *  a Diffie-Hellman key agreement class.
 *  <p>
 *  note: This is only the basic algorithm, it doesn't take advantage of
 *  long term public keys if they are available. See the DHAgreement class
 *  for a "better" implementation.
 */
public class DHBasicAgreement implements org.bouncycastle.crypto.BasicAgreement {

	public DHBasicAgreement() {
	}

	public void init(org.bouncycastle.crypto.CipherParameters param) {
	}

	/**
	 *  given a short term public key from a given party calculate the next
	 *  message in the agreement sequence. 
	 */
	public javabc.BigInteger calculateAgreement(org.bouncycastle.crypto.CipherParameters pubKey) {
	}
}
