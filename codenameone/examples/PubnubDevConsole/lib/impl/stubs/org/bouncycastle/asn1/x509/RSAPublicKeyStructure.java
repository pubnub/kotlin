/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  @deprecated use org.bouncycastle.asn1.pkcs.RSAPublicKey
 */
public class RSAPublicKeyStructure extends org.bouncycastle.asn1.ASN1Object {

	public RSAPublicKeyStructure(javabc.BigInteger modulus, javabc.BigInteger publicExponent) {
	}

	public RSAPublicKeyStructure(org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	public static RSAPublicKeyStructure getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public static RSAPublicKeyStructure getInstance(Object obj) {
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
