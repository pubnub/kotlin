/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  The AccessDescription object.
 *  <pre>
 *  AccessDescription  ::=  SEQUENCE {
 *        accessMethod          OBJECT IDENTIFIER,
 *        accessLocation        GeneralName  }
 *  </pre>
 */
public class AccessDescription extends org.bouncycastle.asn1.ASN1Object {

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_ad_caIssuers;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_ad_ocsp;

	/**
	 *  create an AccessDescription with the oid and location provided.
	 */
	public AccessDescription(org.bouncycastle.asn1.ASN1ObjectIdentifier oid, GeneralName location) {
	}

	public static AccessDescription getInstance(Object obj) {
	}

	/**
	 *  
	 *  @return the access method.
	 */
	public org.bouncycastle.asn1.ASN1ObjectIdentifier getAccessMethod() {
	}

	/**
	 *  
	 *  @return the access location
	 */
	public GeneralName getAccessLocation() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}

	public String toString() {
	}
}
