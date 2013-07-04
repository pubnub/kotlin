/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


public class V2Form extends org.bouncycastle.asn1.ASN1Object {

	public V2Form(GeneralNames issuerName) {
	}

	public V2Form(org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	public static V2Form getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public static V2Form getInstance(Object obj) {
	}

	public GeneralNames getIssuerName() {
	}

	public IssuerSerial getBaseCertificateID() {
	}

	public ObjectDigestInfo getObjectDigestInfo() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  <pre>
	 *   V2Form ::= SEQUENCE {
	 *        issuerName            GeneralNames  OPTIONAL,
	 *        baseCertificateID     [0] IssuerSerial  OPTIONAL,
	 *        objectDigestInfo      [1] ObjectDigestInfo  OPTIONAL
	 *          -- issuerName MUST be present in this profile
	 *          -- baseCertificateID and objectDigestInfo MUST NOT
	 *          -- be present in this profile
	 *   }
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
