/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  an X509Certificate structure.
 *  <pre>
 *   Certificate ::= SEQUENCE {
 *       tbsCertificate          TBSCertificate,
 *       signatureAlgorithm      AlgorithmIdentifier,
 *       signature               BIT STRING
 *   }
 *  </pre>
 */
public class Certificate extends org.bouncycastle.asn1.ASN1Object {

	public static Certificate getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public static Certificate getInstance(Object obj) {
	}

	public TBSCertificate getTBSCertificate() {
	}

	public org.bouncycastle.asn1.ASN1Integer getVersion() {
	}

	public int getVersionNumber() {
	}

	public org.bouncycastle.asn1.ASN1Integer getSerialNumber() {
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

	public AlgorithmIdentifier getSignatureAlgorithm() {
	}

	public org.bouncycastle.asn1.DERBitString getSignature() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
