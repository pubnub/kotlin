/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


public class AttributeCertificateInfo extends org.bouncycastle.asn1.ASN1Object {

	public static AttributeCertificateInfo getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public static AttributeCertificateInfo getInstance(Object obj) {
	}

	public org.bouncycastle.asn1.ASN1Integer getVersion() {
	}

	public Holder getHolder() {
	}

	public AttCertIssuer getIssuer() {
	}

	public AlgorithmIdentifier getSignature() {
	}

	public org.bouncycastle.asn1.ASN1Integer getSerialNumber() {
	}

	public AttCertValidityPeriod getAttrCertValidityPeriod() {
	}

	public org.bouncycastle.asn1.ASN1Sequence getAttributes() {
	}

	public org.bouncycastle.asn1.DERBitString getIssuerUniqueID() {
	}

	public Extensions getExtensions() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  <pre>
	 *   AttributeCertificateInfo ::= SEQUENCE {
	 *        version              AttCertVersion -- version is v2,
	 *        holder               Holder,
	 *        issuer               AttCertIssuer,
	 *        signature            AlgorithmIdentifier,
	 *        serialNumber         CertificateSerialNumber,
	 *        attrCertValidityPeriod   AttCertValidityPeriod,
	 *        attributes           SEQUENCE OF Attribute,
	 *        issuerUniqueID       UniqueIdentifier OPTIONAL,
	 *        extensions           Extensions OPTIONAL
	 *   }
	 * 
	 *   AttCertVersion ::= INTEGER { v2(1) }
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
