/**
 * 
 * Basic signers.
 */
package org.bouncycastle.crypto.signers;


/**
 *  The Digital Signature Algorithm - as described in "Handbook of Applied
 *  Cryptography", pages 452 - 453.
 */
public class DSASigner implements org.bouncycastle.crypto.DSA {

	public DSASigner() {
	}

	public void init(boolean forSigning, org.bouncycastle.crypto.CipherParameters param) {
	}

	/**
	 *  generate a signature for the given message using the key we were
	 *  initialised with. For conventional DSA the message should be a SHA-1
	 *  hash of the message of interest.
	 * 
	 *  @param message the message that will be verified later.
	 */
	public javabc.BigInteger[] generateSignature(byte[] message) {
	}

	/**
	 *  return true if the value r and s represent a DSA signature for
	 *  the passed in message for standard DSA the message should be a
	 *  SHA-1 hash of the real message to be verified.
	 */
	public boolean verifySignature(byte[] message, javabc.BigInteger r, javabc.BigInteger s) {
	}
}
