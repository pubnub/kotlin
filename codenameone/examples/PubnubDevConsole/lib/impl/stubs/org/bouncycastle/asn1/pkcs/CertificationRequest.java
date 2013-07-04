/**
 * 
 * Support classes useful for encoding and supporting the various RSA PKCS documents.
 */
package org.bouncycastle.asn1.pkcs;


/**
 *  PKCS10 Certification request object.
 *  <pre>
 *  CertificationRequest ::= SEQUENCE {
 *    certificationRequestInfo  CertificationRequestInfo,
 *    signatureAlgorithm        AlgorithmIdentifier{{ SignatureAlgorithms }},
 *    signature                 BIT STRING
 *  }
 *  </pre>
 */
public class CertificationRequest extends org.bouncycastle.asn1.ASN1Object {

	protected CertificationRequestInfo reqInfo;

	protected org.bouncycastle.asn1.x509.AlgorithmIdentifier sigAlgId;

	protected org.bouncycastle.asn1.DERBitString sigBits;

	protected CertificationRequest() {
	}

	public CertificationRequest(CertificationRequestInfo requestInfo, org.bouncycastle.asn1.x509.AlgorithmIdentifier algorithm, org.bouncycastle.asn1.DERBitString signature) {
	}

	public CertificationRequest(org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	public static CertificationRequest getInstance(Object o) {
	}

	public CertificationRequestInfo getCertificationRequestInfo() {
	}

	public org.bouncycastle.asn1.x509.AlgorithmIdentifier getSignatureAlgorithm() {
	}

	public org.bouncycastle.asn1.DERBitString getSignature() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
