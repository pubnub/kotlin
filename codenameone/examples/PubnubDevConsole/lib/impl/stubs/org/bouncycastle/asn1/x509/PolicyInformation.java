/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


public class PolicyInformation extends org.bouncycastle.asn1.ASN1Object {

	public PolicyInformation(org.bouncycastle.asn1.ASN1ObjectIdentifier policyIdentifier) {
	}

	public PolicyInformation(org.bouncycastle.asn1.ASN1ObjectIdentifier policyIdentifier, org.bouncycastle.asn1.ASN1Sequence policyQualifiers) {
	}

	public static PolicyInformation getInstance(Object obj) {
	}

	public org.bouncycastle.asn1.ASN1ObjectIdentifier getPolicyIdentifier() {
	}

	public org.bouncycastle.asn1.ASN1Sequence getPolicyQualifiers() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
