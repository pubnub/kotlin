/**
 * 
 * Classes for parameter objects for ciphers and generators.
 */
package org.bouncycastle.crypto.params;


/**
 *  Public key parameters for NaccacheStern cipher. For details on this cipher,
 *  please see
 *  
 *  http://www.gemplus.com/smart/rd/publications/pdf/NS98pkcs.pdf
 */
public class NaccacheSternKeyParameters extends AsymmetricKeyParameter {

	/**
	 *  @param privateKey
	 */
	public NaccacheSternKeyParameters(boolean privateKey, javabc.BigInteger g, javabc.BigInteger n, int lowerSigmaBound) {
	}

	/**
	 *  @return Returns the g.
	 */
	public javabc.BigInteger getG() {
	}

	/**
	 *  @return Returns the lowerSigmaBound.
	 */
	public int getLowerSigmaBound() {
	}

	/**
	 *  @return Returns the n.
	 */
	public javabc.BigInteger getModulus() {
	}
}
