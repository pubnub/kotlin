/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


public class IssuerSerial extends org.bouncycastle.asn1.ASN1Object {

	public IssuerSerial(org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	public IssuerSerial(GeneralNames issuer, org.bouncycastle.asn1.ASN1Integer serial) {
	}

	public static IssuerSerial getInstance(Object obj) {
	}

	public static IssuerSerial getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public GeneralNames getIssuer() {
	}

	public org.bouncycastle.asn1.ASN1Integer getSerial() {
	}

	public org.bouncycastle.asn1.DERBitString getIssuerUID() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  <pre>
	 *   IssuerSerial  ::=  SEQUENCE {
	 *        issuer         GeneralNames,
	 *        serial         CertificateSerialNumber,
	 *        issuerUID      UniqueIdentifier OPTIONAL
	 *   }
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
