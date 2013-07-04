/**
 * 
 * Classes for parameter objects for ciphers and generators.
 */
package org.bouncycastle.crypto.params;


/**
 *  Private key parameters for NaccacheStern cipher. For details on this cipher,
 *  please see
 *  
 *  http://www.gemplus.com/smart/rd/publications/pdf/NS98pkcs.pdf
 */
public class NaccacheSternPrivateKeyParameters extends NaccacheSternKeyParameters {

	/**
	 *  Constructs a NaccacheSternPrivateKey
	 *  
	 *  @param g
	 *             the public enryption parameter g
	 *  @param n
	 *             the public modulus n = p*q
	 *  @param lowerSigmaBound
	 *             the public lower sigma bound up to which data can be encrypted
	 *  @param smallPrimes
	 *             the small primes, of which sigma is constructed in the right
	 *             order
	 *  @param phi_n
	 *             the private modulus phi(n) = (p-1)(q-1)
	 */
	public NaccacheSternPrivateKeyParameters(javabc.BigInteger g, javabc.BigInteger n, int lowerSigmaBound, java.util.Vector smallPrimes, javabc.BigInteger phi_n) {
	}

	public javabc.BigInteger getPhi_n() {
	}

	public java.util.Vector getSmallPrimes() {
	}
}
