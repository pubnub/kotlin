/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


public class AttCertValidityPeriod extends org.bouncycastle.asn1.ASN1Object {

	/**
	 *  @param notBeforeTime
	 *  @param notAfterTime
	 */
	public AttCertValidityPeriod(org.bouncycastle.asn1.DERGeneralizedTime notBeforeTime, org.bouncycastle.asn1.DERGeneralizedTime notAfterTime) {
	}

	public static AttCertValidityPeriod getInstance(Object obj) {
	}

	public org.bouncycastle.asn1.DERGeneralizedTime getNotBeforeTime() {
	}

	public org.bouncycastle.asn1.DERGeneralizedTime getNotAfterTime() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  <pre>
	 *   AttCertValidityPeriod  ::= SEQUENCE {
	 *        notBeforeTime  GeneralizedTime,
	 *        notAfterTime   GeneralizedTime
	 *   } 
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
