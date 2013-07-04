/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


public class BERSequence extends ASN1Sequence {

	/**
	 *  create an empty sequence
	 */
	public BERSequence() {
	}

	/**
	 *  create a sequence containing one object
	 */
	public BERSequence(ASN1Encodable obj) {
	}

	/**
	 *  create a sequence containing a vector of objects.
	 */
	public BERSequence(ASN1EncodableVector v) {
	}

	/**
	 *  create a sequence containing an array of objects.
	 */
	public BERSequence(ASN1Encodable[] array) {
	}
}
