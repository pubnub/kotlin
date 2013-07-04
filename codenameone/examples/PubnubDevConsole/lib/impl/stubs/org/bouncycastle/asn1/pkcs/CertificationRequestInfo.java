/**
 * 
 * Support classes useful for encoding and supporting the various RSA PKCS documents.
 */
package org.bouncycastle.asn1.pkcs;


/**
 *  PKCS10 CertificationRequestInfo object.
 *  <pre>
 *   CertificationRequestInfo ::= SEQUENCE {
 *    version             INTEGER { v1(0) } (v1,...),
 *    subject             Name,
 *    subjectPKInfo   SubjectPublicKeyInfo{{ PKInfoAlgorithms }},
 *    attributes          [0] Attributes{{ CRIAttributes }}
 *   }
 * 
 *   Attributes { ATTRIBUTE:IOSet } ::= SET OF Attribute{{ IOSet }}
 * 
 *   Attribute { ATTRIBUTE:IOSet } ::= SEQUENCE {
 *     type    ATTRIBUTE.&id({IOSet}),
 *     values  SET SIZE(1..MAX) OF ATTRIBUTE.&Type({IOSet}{\@type})
 *   }
 *  </pre>
 */
public class CertificationRequestInfo extends org.bouncycastle.asn1.ASN1Object {

	public CertificationRequestInfo(org.bouncycastle.asn1.x500.X500Name subject, org.bouncycastle.asn1.x509.SubjectPublicKeyInfo pkInfo, org.bouncycastle.asn1.ASN1Set attributes) {
	}

	/**
	 *  @deprecated use X500Name method.
	 */
	public CertificationRequestInfo(org.bouncycastle.asn1.x509.X509Name subject, org.bouncycastle.asn1.x509.SubjectPublicKeyInfo pkInfo, org.bouncycastle.asn1.ASN1Set attributes) {
	}

	public CertificationRequestInfo(org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	public static CertificationRequestInfo getInstance(Object obj) {
	}

	public org.bouncycastle.asn1.ASN1Integer getVersion() {
	}

	public org.bouncycastle.asn1.x500.X500Name getSubject() {
	}

	public org.bouncycastle.asn1.x509.SubjectPublicKeyInfo getSubjectPublicKeyInfo() {
	}

	public org.bouncycastle.asn1.ASN1Set getAttributes() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
