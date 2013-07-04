/**
 * 
 * Support classes useful for encoding and supporting the various RSA PKCS documents.
 */
package org.bouncycastle.asn1.pkcs;


public class ContentInfo extends org.bouncycastle.asn1.ASN1Object {

	public ContentInfo(org.bouncycastle.asn1.ASN1ObjectIdentifier contentType, org.bouncycastle.asn1.ASN1Encodable content) {
	}

	public static ContentInfo getInstance(Object obj) {
	}

	public org.bouncycastle.asn1.ASN1ObjectIdentifier getContentType() {
	}

	public org.bouncycastle.asn1.ASN1Encodable getContent() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  <pre>
	 *  ContentInfo ::= SEQUENCE {
	 *           contentType ContentType,
	 *           content
	 *           [0] EXPLICIT ANY DEFINED BY contentType OPTIONAL }
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
