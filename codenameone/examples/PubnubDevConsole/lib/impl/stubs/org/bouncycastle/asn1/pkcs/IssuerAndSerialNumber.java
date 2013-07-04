/**
 * 
 * Support classes useful for encoding and supporting the various RSA PKCS documents.
 */
package org.bouncycastle.asn1.pkcs;


public class IssuerAndSerialNumber extends org.bouncycastle.asn1.ASN1Object {

	public IssuerAndSerialNumber(org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	public IssuerAndSerialNumber(org.bouncycastle.asn1.x509.X509Name name, javabc.BigInteger certSerialNumber) {
	}

	public IssuerAndSerialNumber(org.bouncycastle.asn1.x509.X509Name name, org.bouncycastle.asn1.ASN1Integer certSerialNumber) {
	}

	public IssuerAndSerialNumber(org.bouncycastle.asn1.x500.X500Name name, javabc.BigInteger certSerialNumber) {
	}

	public static IssuerAndSerialNumber getInstance(Object obj) {
	}

	public org.bouncycastle.asn1.x500.X500Name getName() {
	}

	public org.bouncycastle.asn1.ASN1Integer getCertificateSerialNumber() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
