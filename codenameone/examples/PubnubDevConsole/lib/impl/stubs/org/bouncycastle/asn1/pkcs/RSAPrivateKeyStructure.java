/**
 * 
 * Support classes useful for encoding and supporting the various RSA PKCS documents.
 */
package org.bouncycastle.asn1.pkcs;


/**
 *  @deprecated use RSAPrivateKey
 */
public class RSAPrivateKeyStructure extends org.bouncycastle.asn1.ASN1Object {

	public RSAPrivateKeyStructure(javabc.BigInteger modulus, javabc.BigInteger publicExponent, javabc.BigInteger privateExponent, javabc.BigInteger prime1, javabc.BigInteger prime2, javabc.BigInteger exponent1, javabc.BigInteger exponent2, javabc.BigInteger coefficient) {
	}

	public RSAPrivateKeyStructure(org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	public static RSAPrivateKeyStructure getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public static RSAPrivateKeyStructure getInstance(Object obj) {
	}

	public int getVersion() {
	}

	public javabc.BigInteger getModulus() {
	}

	public javabc.BigInteger getPublicExponent() {
	}

	public javabc.BigInteger getPrivateExponent() {
	}

	public javabc.BigInteger getPrime1() {
	}

	public javabc.BigInteger getPrime2() {
	}

	public javabc.BigInteger getExponent1() {
	}

	public javabc.BigInteger getExponent2() {
	}

	public javabc.BigInteger getCoefficient() {
	}

	/**
	 *  This outputs the key in PKCS1v2 format.
	 *  <pre>
	 *       RSAPrivateKey ::= SEQUENCE {
	 *                           version Version,
	 *                           modulus INTEGER, -- n
	 *                           publicExponent INTEGER, -- e
	 *                           privateExponent INTEGER, -- d
	 *                           prime1 INTEGER, -- p
	 *                           prime2 INTEGER, -- q
	 *                           exponent1 INTEGER, -- d mod (p-1)
	 *                           exponent2 INTEGER, -- d mod (q-1)
	 *                           coefficient INTEGER, -- (inverse of q) mod p
	 *                           otherPrimeInfos OtherPrimeInfos OPTIONAL
	 *                       }
	 * 
	 *       Version ::= INTEGER { two-prime(0), multi(1) }
	 *         (CONSTRAINED BY {-- version must be multi if otherPrimeInfos present --})
	 *  </pre>
	 *  <p>
	 *  This routine is written to output PKCS1 version 2.1, private keys.
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
