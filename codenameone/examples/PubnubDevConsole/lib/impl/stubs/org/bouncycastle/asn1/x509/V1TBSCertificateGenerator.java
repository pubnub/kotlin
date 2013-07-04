/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  Generator for Version 1 TBSCertificateStructures.
 *  <pre>
 *  TBSCertificate ::= SEQUENCE {
 *       version          [ 0 ]  Version DEFAULT v1(0),
 *       serialNumber            CertificateSerialNumber,
 *       signature               AlgorithmIdentifier,
 *       issuer                  Name,
 *       validity                Validity,
 *       subject                 Name,
 *       subjectPublicKeyInfo    SubjectPublicKeyInfo,
 *       }
 *  </pre>
 */
public class V1TBSCertificateGenerator {

	public V1TBSCertificateGenerator() {
	}

	public void setSerialNumber(org.bouncycastle.asn1.ASN1Integer serialNumber) {
	}

	public void setSignature(AlgorithmIdentifier signature) {
	}

	/**
	 *  @deprecated use X500Name method
	 */
	public void setIssuer(X509Name issuer) {
	}

	public void setIssuer(org.bouncycastle.asn1.x500.X500Name issuer) {
	}

	public void setStartDate(Time startDate) {
	}

	public void setStartDate(org.bouncycastle.asn1.DERUTCTime startDate) {
	}

	public void setEndDate(Time endDate) {
	}

	public void setEndDate(org.bouncycastle.asn1.DERUTCTime endDate) {
	}

	/**
	 *  @deprecated use X500Name method
	 */
	public void setSubject(X509Name subject) {
	}

	public void setSubject(org.bouncycastle.asn1.x500.X500Name subject) {
	}

	public void setSubjectPublicKeyInfo(SubjectPublicKeyInfo pubKeyInfo) {
	}

	public TBSCertificate generateTBSCertificate() {
	}
}
