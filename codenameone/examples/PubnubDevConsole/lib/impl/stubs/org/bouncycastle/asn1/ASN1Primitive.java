/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


public abstract class ASN1Primitive extends ASN1Object {

	/**
	 *  Create a base ASN.1 object from a byte stream.
	 * 
	 *  @param data the byte stream to parse.
	 *  @return the base ASN.1 object represented by the byte stream.
	 *  @exception IOException if there is a problem parsing the data.
	 */
	public static ASN1Primitive fromByteArray(byte[] data) {
	}

	public final boolean equals(Object o) {
	}

	public ASN1Primitive toASN1Primitive() {
	}

	public abstract int hashCode() {
	}
}
