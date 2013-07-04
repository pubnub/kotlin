/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


/**
 *  BER TaggedObject - in ASN.1 notation this is any object preceded by
 *  a [n] where n is some number - these are assumed to follow the construction
 *  rules (as with sequences).
 */
public class BERTaggedObject extends ASN1TaggedObject {

	/**
	 *  @param tagNo the tag number for this object.
	 *  @param obj the tagged object.
	 */
	public BERTaggedObject(int tagNo, ASN1Encodable obj) {
	}

	/**
	 *  @param explicit true if an explicitly tagged object.
	 *  @param tagNo the tag number for this object.
	 *  @param obj the tagged object.
	 */
	public BERTaggedObject(boolean explicit, int tagNo, ASN1Encodable obj) {
	}

	/**
	 *  create an implicitly tagged object that contains a zero
	 *  length sequence.
	 */
	public BERTaggedObject(int tagNo) {
	}
}
