/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


public class DEREnumerated extends ASN1Primitive {

	public DEREnumerated(int value) {
	}

	public DEREnumerated(javabc.BigInteger value) {
	}

	public DEREnumerated(byte[] bytes) {
	}

	/**
	 *  return an integer from the passed in object
	 * 
	 *  @exception IllegalArgumentException if the object cannot be converted.
	 */
	public static ASN1Enumerated getInstance(Object obj) {
	}

	/**
	 *  return an Enumerated from a tagged object.
	 * 
	 *  @param obj the tagged object holding the object we want
	 *  @param explicit true if the object is meant to be explicitly
	 *               tagged false otherwise.
	 *  @exception IllegalArgumentException if the tagged object cannot
	 *                be converted.
	 */
	public static DEREnumerated getInstance(ASN1TaggedObject obj, boolean explicit) {
	}

	public javabc.BigInteger getValue() {
	}

	public int hashCode() {
	}
}
