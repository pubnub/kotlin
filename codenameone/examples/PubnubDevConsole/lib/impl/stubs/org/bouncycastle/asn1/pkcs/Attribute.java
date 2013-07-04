/**
 * 
 * Support classes useful for encoding and supporting the various RSA PKCS documents.
 */
package org.bouncycastle.asn1.pkcs;


public class Attribute extends org.bouncycastle.asn1.ASN1Object {

	public Attribute(org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	public Attribute(org.bouncycastle.asn1.ASN1ObjectIdentifier attrType, org.bouncycastle.asn1.ASN1Set attrValues) {
	}

	/**
	 *  return an Attribute object from the given object.
	 * 
	 *  @param o the object we want converted.
	 *  @exception IllegalArgumentException if the object cannot be converted.
	 */
	public static Attribute getInstance(Object o) {
	}

	public org.bouncycastle.asn1.ASN1ObjectIdentifier getAttrType() {
	}

	public org.bouncycastle.asn1.ASN1Set getAttrValues() {
	}

	public org.bouncycastle.asn1.ASN1Encodable[] getAttributeValues() {
	}

	/**
	 * 
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  <pre>
	 *  Attribute ::= SEQUENCE {
	 *      attrType OBJECT IDENTIFIER,
	 *      attrValues SET OF AttributeValue
	 *  }
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
