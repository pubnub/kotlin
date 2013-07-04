/**
 * 
 * Support classes useful for encoding and supporting the various RSA PKCS documents.
 */
package org.bouncycastle.asn1.pkcs;


public class RSAPublicKey extends org.bouncycastle.asn1.ASN1Object {

	public RSAPublicKey(javabc.BigInteger modulus, javabc.BigInteger publicExponent) {
	}

	public static RSAPublicKey getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public static RSAPublicKey getInstance(Object obj) {
	}

	public javabc.BigInteger getModulus() {
	}

	public javabc.BigInteger getPublicExponent() {
	}

	/**
	 *  This outputs the key in PKCS1v2 format.
	 *  <pre>
	 *       RSAPublicKey ::= SEQUENCE {
	 *                           modulus INTEGER, -- n
	 *                           publicExponent INTEGER, -- e
	 *                       }
	 *  </pre>
	 *  <p>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
