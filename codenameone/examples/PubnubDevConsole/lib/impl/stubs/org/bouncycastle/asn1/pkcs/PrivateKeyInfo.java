/**
 * 
 * Support classes useful for encoding and supporting the various RSA PKCS documents.
 */
package org.bouncycastle.asn1.pkcs;


public class PrivateKeyInfo extends org.bouncycastle.asn1.ASN1Object {

	public PrivateKeyInfo(org.bouncycastle.asn1.x509.AlgorithmIdentifier algId, org.bouncycastle.asn1.ASN1Encodable privateKey) {
	}

	public PrivateKeyInfo(org.bouncycastle.asn1.x509.AlgorithmIdentifier algId, org.bouncycastle.asn1.ASN1Encodable privateKey, org.bouncycastle.asn1.ASN1Set attributes) {
	}

	public PrivateKeyInfo(org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	public static PrivateKeyInfo getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public static PrivateKeyInfo getInstance(Object obj) {
	}

	public org.bouncycastle.asn1.x509.AlgorithmIdentifier getPrivateKeyAlgorithm() {
	}

	/**
	 *  @deprecated use getPrivateKeyAlgorithm()
	 */
	public org.bouncycastle.asn1.x509.AlgorithmIdentifier getAlgorithmId() {
	}

	public org.bouncycastle.asn1.ASN1Encodable parsePrivateKey() {
	}

	/**
	 *  @deprecated use parsePrivateKey()
	 */
	public org.bouncycastle.asn1.ASN1Primitive getPrivateKey() {
	}

	public org.bouncycastle.asn1.ASN1Set getAttributes() {
	}

	/**
	 *  write out an RSA private key with its associated information
	 *  as described in PKCS8.
	 *  <pre>
	 *       PrivateKeyInfo ::= SEQUENCE {
	 *                               version Version,
	 *                               privateKeyAlgorithm AlgorithmIdentifier {{PrivateKeyAlgorithms}},
	 *                               privateKey PrivateKey,
	 *                               attributes [0] IMPLICIT Attributes OPTIONAL 
	 *                           }
	 *       Version ::= INTEGER {v1(0)} (v1,...)
	 * 
	 *       PrivateKey ::= OCTET STRING
	 * 
	 *       Attributes ::= SET OF Attribute
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
