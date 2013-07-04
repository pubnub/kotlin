/**
 * 
 * Support classes useful for encoding and supporting the various RSA PKCS documents.
 */
package org.bouncycastle.asn1.pkcs;


public class CRLBag extends org.bouncycastle.asn1.ASN1Object {

	public CRLBag(org.bouncycastle.asn1.ASN1ObjectIdentifier crlId, org.bouncycastle.asn1.ASN1Encodable crlValue) {
	}

	public static CRLBag getInstance(Object o) {
	}

	public org.bouncycastle.asn1.ASN1ObjectIdentifier getcrlId() {
	}

	public org.bouncycastle.asn1.ASN1Encodable getCRLValue() {
	}

	/**
	 *  <pre>
	 *      CRLBag ::= SEQUENCE {
	 *      crlId  BAG-TYPE.&id ({CRLTypes}),
	 *      crlValue  [0] EXPLICIT BAG-TYPE.&Type ({CRLTypes}{@crlId})
	 *      }
	 * 
	 *      x509CRL BAG-TYPE ::= {OCTET STRING IDENTIFIED BY {certTypes 1}
	 *      -- DER-encoded X.509 CRL stored in OCTET STRING
	 * 
	 *      CRLTypes BAG-TYPE ::= {
	 *      x509CRL,
	 *      ... -- For future extensions
	 *      }
	 *        </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
