/**
 * 
 * Classes for parameter objects for ciphers and generators.
 */
package org.bouncycastle.crypto.params;


public class DSAParameters implements org.bouncycastle.crypto.CipherParameters {

	public DSAParameters(javabc.BigInteger p, javabc.BigInteger q, javabc.BigInteger g) {
	}

	public DSAParameters(javabc.BigInteger p, javabc.BigInteger q, javabc.BigInteger g, DSAValidationParameters params) {
	}

	public javabc.BigInteger getP() {
	}

	public javabc.BigInteger getQ() {
	}

	public javabc.BigInteger getG() {
	}

	public DSAValidationParameters getValidationParameters() {
	}

	public boolean equals(Object obj) {
	}

	public int hashCode() {
	}
}
