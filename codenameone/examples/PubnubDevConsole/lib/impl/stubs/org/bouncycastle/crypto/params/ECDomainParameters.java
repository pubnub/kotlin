/**
 * 
 * Classes for parameter objects for ciphers and generators.
 */
package org.bouncycastle.crypto.params;


public class ECDomainParameters implements org.bouncycastle.math.ec.ECConstants {

	public ECDomainParameters(org.bouncycastle.math.ec.ECCurve curve, org.bouncycastle.math.ec.ECPoint G, javabc.BigInteger n) {
	}

	public ECDomainParameters(org.bouncycastle.math.ec.ECCurve curve, org.bouncycastle.math.ec.ECPoint G, javabc.BigInteger n, javabc.BigInteger h) {
	}

	public ECDomainParameters(org.bouncycastle.math.ec.ECCurve curve, org.bouncycastle.math.ec.ECPoint G, javabc.BigInteger n, javabc.BigInteger h, byte[] seed) {
	}

	public org.bouncycastle.math.ec.ECCurve getCurve() {
	}

	public org.bouncycastle.math.ec.ECPoint getG() {
	}

	public javabc.BigInteger getN() {
	}

	public javabc.BigInteger getH() {
	}

	public byte[] getSeed() {
	}
}
