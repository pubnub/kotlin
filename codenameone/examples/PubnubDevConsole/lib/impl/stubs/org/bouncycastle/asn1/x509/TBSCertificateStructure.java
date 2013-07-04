/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  The TBSCertificate object.
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
 *  <p>
 *  Note: issuerUniqueID and subjectUniqueID are both deprecated by the IETF. This class
 *  will parse them, but you really shouldn't be creating new ones. 
 */
public class TBSCertificateStructure extends org.bouncycastle.asn1.ASN1Object {

	public TBSCertificateStructure(org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	public static TBSCertificateStructure getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public static TBSCertificateStructure getInstance(Object obj) {
	}

	public int getVersion() {
	}

	public org.bouncycastle.asn1.ASN1Integer getVersionNumber() {
	}

	public org.bouncycastle.asn1.ASN1Integer getSerialNumber() {
	}

	public AlgorithmIdentifier getSignature() {
	}

	public org.bouncycastle.asn1.x500.X500Name getIssuer() {
	}

	public Time getStartDate() {
	}

	public Time getEndDate() {
	}

	public org.bouncycastle.asn1.x500.X500Name getSubject() {
	}

	public SubjectPublicKeyInfo getSubjectPublicKeyInfo() {
	}

	public org.bouncycastle.asn1.DERBitString getIssuerUniqueId() {
	}

	public org.bouncycastle.asn1.DERBitString getSubjectUniqueId() {
	}

	public X509Extensions getExtensions() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
