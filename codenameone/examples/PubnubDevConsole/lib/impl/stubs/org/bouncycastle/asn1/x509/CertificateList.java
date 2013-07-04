/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  PKIX RFC-2459
 * 
 *  The X.509 v2 CRL syntax is as follows.  For signature calculation,
 *  the data that is to be signed is ASN.1 DER encoded.
 * 
 *  <pre>
 *  CertificateList  ::=  SEQUENCE  {
 *       tbsCertList          TBSCertList,
 *       signatureAlgorithm   AlgorithmIdentifier,
 *       signatureValue       BIT STRING  }
 *  </pre>
 */
public class CertificateList extends org.bouncycastle.asn1.ASN1Object {

	public CertificateList(org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	public static CertificateList getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public static CertificateList getInstance(Object obj) {
	}

	public TBSCertList getTBSCertList() {
	}

	public TBSCertList.CRLEntry[] getRevokedCertificates() {
	}

	public java.util.Enumeration getRevokedCertificateEnumeration() {
	}

	public AlgorithmIdentifier getSignatureAlgorithm() {
	}

	public org.bouncycastle.asn1.DERBitString getSignature() {
	}

	public int getVersionNumber() {
	}

	public org.bouncycastle.asn1.x500.X500Name getIssuer() {
	}

	public Time getThisUpdate() {
	}

	public Time getNextUpdate() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
