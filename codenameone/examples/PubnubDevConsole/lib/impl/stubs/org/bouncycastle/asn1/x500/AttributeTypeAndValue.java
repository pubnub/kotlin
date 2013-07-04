package org.bouncycastle.asn1.x500;


public class AttributeTypeAndValue extends org.bouncycastle.asn1.ASN1Object {

	public AttributeTypeAndValue(org.bouncycastle.asn1.ASN1ObjectIdentifier type, org.bouncycastle.asn1.ASN1Encodable value) {
	}

	public static AttributeTypeAndValue getInstance(Object o) {
	}

	public org.bouncycastle.asn1.ASN1ObjectIdentifier getType() {
	}

	public org.bouncycastle.asn1.ASN1Encodable getValue() {
	}

	/**
	 *  <pre>
	 *  AttributeTypeAndValue ::= SEQUENCE {
	 *            type         OBJECT IDENTIFIER,
	 *            value        ANY DEFINED BY type }
	 *  </pre>
	 *  @return a basic ASN.1 object representation.
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
