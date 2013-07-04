/**
 * 
 * Support classes useful for encoding and supporting X9.62 elliptic curve.
 */
package org.bouncycastle.asn1.x9;


/**
 *  ASN.1 def for Elliptic-Curve ECParameters structure. See
 *  X9.62, for further details.
 */
public class X9ECParameters extends org.bouncycastle.asn1.ASN1Object implements X9ObjectIdentifiers {

	public X9ECParameters(org.bouncycastle.math.ec.ECCurve curve, org.bouncycastle.math.ec.ECPoint g, javabc.BigInteger n) {
	}

	public X9ECParameters(org.bouncycastle.math.ec.ECCurve curve, org.bouncycastle.math.ec.ECPoint g, javabc.BigInteger n, javabc.BigInteger h) {
	}

	public X9ECParameters(org.bouncycastle.math.ec.ECCurve curve, org.bouncycastle.math.ec.ECPoint g, javabc.BigInteger n, javabc.BigInteger h, byte[] seed) {
	}

	public static X9ECParameters getInstance(Object obj) {
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

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  <pre>
	 *   ECParameters ::= SEQUENCE {
	 *       version         INTEGER { ecpVer1(1) } (ecpVer1),
	 *       fieldID         FieldID {{FieldTypes}},
	 *       curve           X9Curve,
	 *       base            X9ECPoint,
	 *       order           INTEGER,
	 *       cofactor        INTEGER OPTIONAL
	 *   }
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
