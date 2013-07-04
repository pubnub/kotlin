/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  Generator for Version 2 TBSCertList structures.
 *  <pre>
 *   TBSCertList  ::=  SEQUENCE  {
 *        version                 Version OPTIONAL,
 *                                     -- if present, shall be v2
 *        signature               AlgorithmIdentifier,
 *        issuer                  Name,
 *        thisUpdate              Time,
 *        nextUpdate              Time OPTIONAL,
 *        revokedCertificates     SEQUENCE OF SEQUENCE  {
 *             userCertificate         CertificateSerialNumber,
 *             revocationDate          Time,
 *             crlEntryExtensions      Extensions OPTIONAL
 *                                           -- if present, shall be v2
 *                                  }  OPTIONAL,
 *        crlExtensions           [0]  EXPLICIT Extensions OPTIONAL
 *                                           -- if present, shall be v2
 *                                  }
 *  </pre>
 * 
 *  <b>Note: This class may be subject to change</b>
 */
public class V2TBSCertListGenerator {

	public V2TBSCertListGenerator() {
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

	public void setThisUpdate(org.bouncycastle.asn1.DERUTCTime thisUpdate) {
	}

	public void setNextUpdate(org.bouncycastle.asn1.DERUTCTime nextUpdate) {
	}

	public void setThisUpdate(Time thisUpdate) {
	}

	public void setNextUpdate(Time nextUpdate) {
	}

	public void addCRLEntry(org.bouncycastle.asn1.ASN1Sequence crlEntry) {
	}

	public void addCRLEntry(org.bouncycastle.asn1.ASN1Integer userCertificate, org.bouncycastle.asn1.DERUTCTime revocationDate, int reason) {
	}

	public void addCRLEntry(org.bouncycastle.asn1.ASN1Integer userCertificate, Time revocationDate, int reason) {
	}

	public void addCRLEntry(org.bouncycastle.asn1.ASN1Integer userCertificate, Time revocationDate, int reason, org.bouncycastle.asn1.DERGeneralizedTime invalidityDate) {
	}

	public void addCRLEntry(org.bouncycastle.asn1.ASN1Integer userCertificate, Time revocationDate, Extensions extensions) {
	}

	public void setExtensions(X509Extensions extensions) {
	}

	public void setExtensions(Extensions extensions) {
	}

	public TBSCertList generateTBSCertList() {
	}
}
