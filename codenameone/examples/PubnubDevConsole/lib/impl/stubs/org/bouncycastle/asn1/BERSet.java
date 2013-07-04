/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


public class BERSet extends ASN1Set {

	/**
	 *  create an empty sequence
	 */
	public BERSet() {
	}

	/**
	 *  @param obj - a single object that makes up the set.
	 */
	public BERSet(ASN1Encodable obj) {
	}

	/**
	 *  @param v - a vector of objects making up the set.
	 */
	public BERSet(ASN1EncodableVector v) {
	}

	/**
	 *  create a set from an array of objects.
	 */
	public BERSet(ASN1Encodable[] a) {
	}
}
