/**
 * 
 * Basic key agreement classes.
 */
package org.bouncycastle.crypto.agreement;


/**
 *  a Diffie-Hellman key exchange engine.
 *  <p>
 *  note: This uses MTI/A0 key agreement in order to make the key agreement
 *  secure against passive attacks. If you're doing Diffie-Hellman and both
 *  parties have long term public keys you should look at using this. For
 *  further information have a look at RFC 2631.
 *  <p>
 *  It's possible to extend this to more than two parties as well, for the moment
 *  that is left as an exercise for the reader.
 */
public class DHAgreement {

	public DHAgreement() {
	}

	public void init(org.bouncycastle.crypto.CipherParameters param) {
	}

	/**
	 *  calculate our initial message.
	 */
	public javabc.BigInteger calculateMessage() {
	}

	/**
	 *  given a message from a given party and the corresponding public key,
	 *  calculate the next message in the agreement sequence. In this case
	 *  this will represent the shared secret.
	 */
	public javabc.BigInteger calculateAgreement(org.bouncycastle.crypto.params.DHPublicKeyParameters pub, javabc.BigInteger message) {
	}
}
