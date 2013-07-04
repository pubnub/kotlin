/**
 * 
 * Support classes useful for encoding and supporting the various RSA PKCS documents.
 */
package org.bouncycastle.asn1.pkcs;


/**
 *  a PKCS#7 signed data object.
 */
public class SignedData extends org.bouncycastle.asn1.ASN1Object {

	public SignedData(org.bouncycastle.asn1.ASN1Integer _version, org.bouncycastle.asn1.ASN1Set _digestAlgorithms, ContentInfo _contentInfo, org.bouncycastle.asn1.ASN1Set _certificates, org.bouncycastle.asn1.ASN1Set _crls, org.bouncycastle.asn1.ASN1Set _signerInfos) {
	}

	public SignedData(org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	public static SignedData getInstance(Object o) {
	}

	public org.bouncycastle.asn1.ASN1Integer getVersion() {
	}

	public org.bouncycastle.asn1.ASN1Set getDigestAlgorithms() {
	}

	public ContentInfo getContentInfo() {
	}

	public org.bouncycastle.asn1.ASN1Set getCertificates() {
	}

	public org.bouncycastle.asn1.ASN1Set getCRLs() {
	}

	public org.bouncycastle.asn1.ASN1Set getSignerInfos() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  <pre>
	 *   SignedData ::= SEQUENCE {
	 *       version Version,
	 *       digestAlgorithms DigestAlgorithmIdentifiers,
	 *       contentInfo ContentInfo,
	 *       certificates
	 *           [0] IMPLICIT ExtendedCertificatesAndCertificates
	 *                    OPTIONAL,
	 *       crls
	 *           [1] IMPLICIT CertificateRevocationLists OPTIONAL,
	 *       signerInfos SignerInfos }
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
