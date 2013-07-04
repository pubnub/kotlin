/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


/**
 *  a general class for building up a vector of DER encodable objects -
 *  this will eventually be superceded by ASN1EncodableVector so you should
 *  use that class in preference.
 */
public class DEREncodableVector extends ASN1EncodableVector {

	/**
	 *  @deprecated use ASN1EncodableVector instead.
	 */
	public DEREncodableVector() {
	}
}
