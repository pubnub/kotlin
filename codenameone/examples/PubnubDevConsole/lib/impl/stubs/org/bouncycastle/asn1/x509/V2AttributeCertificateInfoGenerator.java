/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  Generator for Version 2 AttributeCertificateInfo
 *  <pre>
 *  AttributeCertificateInfo ::= SEQUENCE {
 *        version              AttCertVersion -- version is v2,
 *        holder               Holder,
 *        issuer               AttCertIssuer,
 *        signature            AlgorithmIdentifier,
 *        serialNumber         CertificateSerialNumber,
 *        attrCertValidityPeriod   AttCertValidityPeriod,
 *        attributes           SEQUENCE OF Attribute,
 *        issuerUniqueID       UniqueIdentifier OPTIONAL,
 *        extensions           Extensions OPTIONAL
 *  }
 *  </pre>
 */
public class V2AttributeCertificateInfoGenerator {

	public V2AttributeCertificateInfoGenerator() {
	}

	public void setHolder(Holder holder) {
	}

	public void addAttribute(String oid, org.bouncycastle.asn1.ASN1Encodable value) {
	}

	/**
	 *  @param attribute
	 */
	public void addAttribute(Attribute attribute) {
	}

	public void setSerialNumber(org.bouncycastle.asn1.ASN1Integer serialNumber) {
	}

	public void setSignature(AlgorithmIdentifier signature) {
	}

	public void setIssuer(AttCertIssuer issuer) {
	}

	public void setStartDate(org.bouncycastle.asn1.DERGeneralizedTime startDate) {
	}

	public void setEndDate(org.bouncycastle.asn1.DERGeneralizedTime endDate) {
	}

	public void setIssuerUniqueID(org.bouncycastle.asn1.DERBitString issuerUniqueID) {
	}

	/**
	 *  @deprecated use method taking Extensions
	 *  @param extensions
	 */
	public void setExtensions(X509Extensions extensions) {
	}

	public void setExtensions(Extensions extensions) {
	}

	public AttributeCertificateInfo generateAttributeCertificateInfo() {
	}
}
