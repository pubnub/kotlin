/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


/**
 *  DER TaggedObject - in ASN.1 notation this is any object preceded by
 *  a [n] where n is some number - these are assumed to follow the construction
 *  rules (as with sequences).
 */
public class DERTaggedObject extends ASN1TaggedObject {

	/**
	 *  @param explicit true if an explicitly tagged object.
	 *  @param tagNo the tag number for this object.
	 *  @param obj the tagged object.
	 */
	public DERTaggedObject(boolean explicit, int tagNo, ASN1Encodable obj) {
	}

	public DERTaggedObject(int tagNo, ASN1Encodable encodable) {
	}
}
