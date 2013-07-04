/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  <pre>
 *     PrivateKeyUsagePeriod ::= SEQUENCE {
 *       notBefore       [0]     GeneralizedTime OPTIONAL,
 *       notAfter        [1]     GeneralizedTime OPTIONAL }
 *  </pre>
 */
public class PrivateKeyUsagePeriod extends org.bouncycastle.asn1.ASN1Object {

	public static PrivateKeyUsagePeriod getInstance(Object obj) {
	}

	public org.bouncycastle.asn1.DERGeneralizedTime getNotBefore() {
	}

	public org.bouncycastle.asn1.DERGeneralizedTime getNotAfter() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
