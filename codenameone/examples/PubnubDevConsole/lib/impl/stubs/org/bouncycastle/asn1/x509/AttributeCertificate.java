/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


public class AttributeCertificate extends org.bouncycastle.asn1.ASN1Object {

	public AttributeCertificate(AttributeCertificateInfo acinfo, AlgorithmIdentifier signatureAlgorithm, org.bouncycastle.asn1.DERBitString signatureValue) {
	}

	public AttributeCertificate(org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	/**
	 *  @param obj
	 *  @return an AttributeCertificate object
	 */
	public static AttributeCertificate getInstance(Object obj) {
	}

	public AttributeCertificateInfo getAcinfo() {
	}

	public AlgorithmIdentifier getSignatureAlgorithm() {
	}

	public org.bouncycastle.asn1.DERBitString getSignatureValue() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  <pre>
	 *   AttributeCertificate ::= SEQUENCE {
	 *        acinfo               AttributeCertificateInfo,
	 *        signatureAlgorithm   AlgorithmIdentifier,
	 *        signatureValue       BIT STRING
	 *   }
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
