/**
 * 
 * Basic signers.
 */
package org.bouncycastle.crypto.signers;


/**
 *  EC-NR as described in IEEE 1363-2000
 */
public class ECNRSigner implements org.bouncycastle.crypto.DSA {

	public ECNRSigner() {
	}

	public void init(boolean forSigning, org.bouncycastle.crypto.CipherParameters param) {
	}

	/**
	 *  generate a signature for the given message using the key we were
	 *  initialised with.  Generally, the order of the curve should be at 
	 *  least as long as the hash of the message of interest, and with 
	 *  ECNR it *must* be at least as long.  
	 * 
	 *  @param digest  the digest to be signed.
	 *  @exception DataLengthException if the digest is longer than the key allows
	 */
	public javabc.BigInteger[] generateSignature(byte[] digest) {
	}

	/**
	 *  return true if the value r and s represent a signature for the 
	 *  message passed in. Generally, the order of the curve should be at 
	 *  least as long as the hash of the message of interest, and with 
	 *  ECNR, it *must* be at least as long.  But just in case the signer
	 *  applied mod(n) to the longer digest, this implementation will
	 *  apply mod(n) during verification.
	 * 
	 *  @param digest  the digest to be verified.
	 *  @param r       the r value of the signature.
	 *  @param s       the s value of the signature.
	 *  @exception DataLengthException if the digest is longer than the key allows
	 */
	public boolean verifySignature(byte[] digest, javabc.BigInteger r, javabc.BigInteger s) {
	}
}
