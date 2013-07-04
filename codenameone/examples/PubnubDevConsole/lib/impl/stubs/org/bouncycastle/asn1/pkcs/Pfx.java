/**
 * 
 * Support classes useful for encoding and supporting the various RSA PKCS documents.
 */
package org.bouncycastle.asn1.pkcs;


/**
 *  the infamous Pfx from PKCS12
 */
public class Pfx extends org.bouncycastle.asn1.ASN1Object {

	public Pfx(ContentInfo contentInfo, MacData macData) {
	}

	public static Pfx getInstance(Object obj) {
	}

	public ContentInfo getAuthSafe() {
	}

	public MacData getMacData() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
