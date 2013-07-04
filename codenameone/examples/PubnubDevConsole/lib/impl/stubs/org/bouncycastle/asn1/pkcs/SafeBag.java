/**
 * 
 * Support classes useful for encoding and supporting the various RSA PKCS documents.
 */
package org.bouncycastle.asn1.pkcs;


public class SafeBag extends org.bouncycastle.asn1.ASN1Object {

	public SafeBag(org.bouncycastle.asn1.ASN1ObjectIdentifier oid, org.bouncycastle.asn1.ASN1Encodable obj) {
	}

	public SafeBag(org.bouncycastle.asn1.ASN1ObjectIdentifier oid, org.bouncycastle.asn1.ASN1Encodable obj, org.bouncycastle.asn1.ASN1Set bagAttributes) {
	}

	public static SafeBag getInstance(Object obj) {
	}

	public org.bouncycastle.asn1.ASN1ObjectIdentifier getBagId() {
	}

	public org.bouncycastle.asn1.ASN1Encodable getBagValue() {
	}

	public org.bouncycastle.asn1.ASN1Set getBagAttributes() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
