/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  PKIX RFC-2459 - TBSCertList object.
 *  <pre>
 *  TBSCertList  ::=  SEQUENCE  {
 *       version                 Version OPTIONAL,
 *                                    -- if present, shall be v2
 *       signature               AlgorithmIdentifier,
 *       issuer                  Name,
 *       thisUpdate              Time,
 *       nextUpdate              Time OPTIONAL,
 *       revokedCertificates     SEQUENCE OF SEQUENCE  {
 *            userCertificate         CertificateSerialNumber,
 *            revocationDate          Time,
 *            crlEntryExtensions      Extensions OPTIONAL
 *                                          -- if present, shall be v2
 *                                 }  OPTIONAL,
 *       crlExtensions           [0]  EXPLICIT Extensions OPTIONAL
 *                                          -- if present, shall be v2
 *                                 }
 *  </pre>
 */
public class TBSCertList extends org.bouncycastle.asn1.ASN1Object {

	public TBSCertList(org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	public static TBSCertList getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public static TBSCertList getInstance(Object obj) {
	}

	public int getVersionNumber() {
	}

	public org.bouncycastle.asn1.ASN1Integer getVersion() {
	}

	public AlgorithmIdentifier getSignature() {
	}

	public org.bouncycastle.asn1.x500.X500Name getIssuer() {
	}

	public Time getThisUpdate() {
	}

	public Time getNextUpdate() {
	}

	public TBSCertList.CRLEntry[] getRevokedCertificates() {
	}

	public java.util.Enumeration getRevokedCertificateEnumeration() {
	}

	public Extensions getExtensions() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}

	public static class CRLEntry {


		public static TBSCertList.CRLEntry getInstance(Object o) {
		}

		public org.bouncycastle.asn1.ASN1Integer getUserCertificate() {
		}

		public Time getRevocationDate() {
		}

		public Extensions getExtensions() {
		}

		public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
		}

		public boolean hasExtensions() {
		}
	}
}
