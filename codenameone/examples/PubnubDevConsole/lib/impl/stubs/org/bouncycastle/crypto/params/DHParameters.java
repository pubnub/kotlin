/**
 * 
 * Classes for parameter objects for ciphers and generators.
 */
package org.bouncycastle.crypto.params;


public class DHParameters implements org.bouncycastle.crypto.CipherParameters {

	public DHParameters(javabc.BigInteger p, javabc.BigInteger g) {
	}

	public DHParameters(javabc.BigInteger p, javabc.BigInteger g, javabc.BigInteger q) {
	}

	public DHParameters(javabc.BigInteger p, javabc.BigInteger g, javabc.BigInteger q, int l) {
	}

	public DHParameters(javabc.BigInteger p, javabc.BigInteger g, javabc.BigInteger q, int m, int l) {
	}

	public DHParameters(javabc.BigInteger p, javabc.BigInteger g, javabc.BigInteger q, javabc.BigInteger j, DHValidationParameters validation) {
	}

	public DHParameters(javabc.BigInteger p, javabc.BigInteger g, javabc.BigInteger q, int m, int l, javabc.BigInteger j, DHValidationParameters validation) {
	}

	public javabc.BigInteger getP() {
	}

	public javabc.BigInteger getG() {
	}

	public javabc.BigInteger getQ() {
	}

	/**
	 *  Return the subgroup factor J.
	 * 
	 *  @return subgroup factor
	 */
	public javabc.BigInteger getJ() {
	}

	/**
	 *  Return the minimum length of the private value.
	 * 
	 *  @return the minimum length of the private value in bits.
	 */
	public int getM() {
	}

	/**
	 *  Return the private value length in bits - if set, zero otherwise
	 * 
	 *  @return the private value length in bits, zero otherwise.
	 */
	public int getL() {
	}

	public DHValidationParameters getValidationParameters() {
	}

	public boolean equals(Object obj) {
	}

	public int hashCode() {
	}
}
