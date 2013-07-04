/**
 * 
 * Classes for support of the SEC standard for Elliptic Curve.
 */
package org.bouncycastle.asn1.sec;


/**
 *  the elliptic curve private key object from SEC 1
 *  @deprecated use ECPrivateKey
 */
public class ECPrivateKeyStructure extends org.bouncycastle.asn1.ASN1Object {

	public ECPrivateKeyStructure(org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	public ECPrivateKeyStructure(javabc.BigInteger key) {
	}

	public ECPrivateKeyStructure(javabc.BigInteger key, org.bouncycastle.asn1.ASN1Encodable parameters) {
	}

	public ECPrivateKeyStructure(javabc.BigInteger key, org.bouncycastle.asn1.DERBitString publicKey, org.bouncycastle.asn1.ASN1Encodable parameters) {
	}

	public javabc.BigInteger getKey() {
	}

	public org.bouncycastle.asn1.DERBitString getPublicKey() {
	}

	public org.bouncycastle.asn1.ASN1Primitive getParameters() {
	}

	/**
	 *  ECPrivateKey ::= SEQUENCE {
	 *      version INTEGER { ecPrivkeyVer1(1) } (ecPrivkeyVer1),
	 *      privateKey OCTET STRING,
	 *      parameters [0] Parameters OPTIONAL,
	 *      publicKey [1] BIT STRING OPTIONAL }
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
