/**
 * 
 * Classes for parameter objects for ciphers and generators.
 */
package org.bouncycastle.crypto.params;


public class ElGamalParameters implements org.bouncycastle.crypto.CipherParameters {

	public ElGamalParameters(javabc.BigInteger p, javabc.BigInteger g) {
	}

	public ElGamalParameters(javabc.BigInteger p, javabc.BigInteger g, int l) {
	}

	public javabc.BigInteger getP() {
	}

	/**
	 *  return the generator - g
	 */
	public javabc.BigInteger getG() {
	}

	/**
	 *  return private value limit - l
	 */
	public int getL() {
	}

	public boolean equals(Object obj) {
	}

	public int hashCode() {
	}
}
