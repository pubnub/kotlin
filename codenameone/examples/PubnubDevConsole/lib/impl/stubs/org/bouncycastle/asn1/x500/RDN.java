package org.bouncycastle.asn1.x500;


public class RDN extends org.bouncycastle.asn1.ASN1Object {

	/**
	 *  Create a single valued RDN.
	 * 
	 *  @param oid RDN type.
	 *  @param value RDN value.
	 */
	public RDN(org.bouncycastle.asn1.ASN1ObjectIdentifier oid, org.bouncycastle.asn1.ASN1Encodable value) {
	}

	public RDN(AttributeTypeAndValue attrTAndV) {
	}

	/**
	 *  Create a multi-valued RDN.
	 * 
	 *  @param aAndVs attribute type/value pairs making up the RDN
	 */
	public RDN(AttributeTypeAndValue[] aAndVs) {
	}

	public static RDN getInstance(Object obj) {
	}

	public boolean isMultiValued() {
	}

	/**
	 *  Return the number of AttributeTypeAndValue objects in this RDN,
	 * 
	 *  @return size of RDN, greater than 1 if multi-valued.
	 */
	public int size() {
	}

	public AttributeTypeAndValue getFirst() {
	}

	public AttributeTypeAndValue[] getTypesAndValues() {
	}

	/**
	 *  <pre>
	 *  RelativeDistinguishedName ::=
	 *                      SET OF AttributeTypeAndValue
	 * 
	 *  AttributeTypeAndValue ::= SEQUENCE {
	 *         type     AttributeType,
	 *         value    AttributeValue }
	 *  </pre>
	 *  @return this object as an ASN1Primitive type
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
