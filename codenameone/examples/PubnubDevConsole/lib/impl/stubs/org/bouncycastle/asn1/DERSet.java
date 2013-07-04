/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


/**
 *  A DER encoded set object
 */
public class DERSet extends ASN1Set {

	/**
	 *  create an empty set
	 */
	public DERSet() {
	}

	/**
	 *  @param obj - a single object that makes up the set.
	 */
	public DERSet(ASN1Encodable obj) {
	}

	/**
	 *  @param v - a vector of objects making up the set.
	 */
	public DERSet(ASN1EncodableVector v) {
	}

	/**
	 *  create a set from an array of objects.
	 */
	public DERSet(ASN1Encodable[] a) {
	}
}
