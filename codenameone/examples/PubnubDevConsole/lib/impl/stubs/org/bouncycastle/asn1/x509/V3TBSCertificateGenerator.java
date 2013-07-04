/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  Generator for Version 3 TBSCertificateStructures.
 *  <pre>
 *  TBSCertificate ::= SEQUENCE {
 *       version          [ 0 ]  Version DEFAULT v1(0),
 *       serialNumber            CertificateSerialNumber,
 *       signature               AlgorithmIdentifier,
 *       issuer                  Name,
 *       validity                Validity,
 *       subject                 Name,
 *       subjectPublicKeyInfo    SubjectPublicKeyInfo,
 *       issuerUniqueID    [ 1 ] IMPLICIT UniqueIdentifier OPTIONAL,
 *       subjectUniqueID   [ 2 ] IMPLICIT UniqueIdentifier OPTIONAL,
 *       extensions        [ 3 ] Extensions OPTIONAL
 *       }
 *  </pre>
 */
public class V3TBSCertificateGenerator {

	public V3TBSCertificateGenerator() {
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

	public void setStartDate(org.bouncycastle.asn1.DERUTCTime startDate) {
	}

	public void setStartDate(Time startDate) {
	}

	public void setEndDate(org.bouncycastle.asn1.DERUTCTime endDate) {
	}

	public void setEndDate(Time endDate) {
	}

	/**
	 *  @deprecated use X500Name method
	 */
	public void setSubject(X509Name subject) {
	}

	public void setSubject(org.bouncycastle.asn1.x500.X500Name subject) {
	}

	public void setIssuerUniqueID(org.bouncycastle.asn1.DERBitString uniqueID) {
	}

	public void setSubjectUniqueID(org.bouncycastle.asn1.DERBitString uniqueID) {
	}

	public void setSubjectPublicKeyInfo(SubjectPublicKeyInfo pubKeyInfo) {
	}

	/**
	 *  @deprecated use method taking Extensions
	 *  @param extensions
	 */
	public void setExtensions(X509Extensions extensions) {
	}

	public void setExtensions(Extensions extensions) {
	}

	public TBSCertificate generateTBSCertificate() {
	}
}
