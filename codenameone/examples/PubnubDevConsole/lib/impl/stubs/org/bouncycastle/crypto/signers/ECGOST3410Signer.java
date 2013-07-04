/**
 * 
 * Basic signers.
 */
package org.bouncycastle.crypto.signers;


/**
 *  GOST R 34.10-2001 Signature Algorithm
 */
public class ECGOST3410Signer implements org.bouncycastle.crypto.DSA {

	public ECGOST3410Signer() {
	}

	public void init(boolean forSigning, org.bouncycastle.crypto.CipherParameters param) {
	}

	/**
	 *  generate a signature for the given message using the key we were
	 *  initialised with. For conventional GOST3410 the message should be a GOST3411
	 *  hash of the message of interest.
	 * 
	 *  @param message the message that will be verified later.
	 */
	public javabc.BigInteger[] generateSignature(byte[] message) {
	}

	/**
	 *  return true if the value r and s represent a GOST3410 signature for
	 *  the passed in message (for standard GOST3410 the message should be
	 *  a GOST3411 hash of the real message to be verified).
	 */
	public boolean verifySignature(byte[] message, javabc.BigInteger r, javabc.BigInteger s) {
	}
}
