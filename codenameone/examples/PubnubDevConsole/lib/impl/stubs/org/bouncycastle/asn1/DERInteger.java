/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


public class DERInteger extends ASN1Primitive {

	public DERInteger(int value) {
	}

	public DERInteger(javabc.BigInteger value) {
	}

	public DERInteger(byte[] bytes) {
	}

	/**
	 *  return an integer from the passed in object
	 * 
	 *  @exception IllegalArgumentException if the object cannot be converted.
	 */
	public static ASN1Integer getInstance(Object obj) {
	}

	/**
	 *  return an Integer from a tagged object.
	 * 
	 *  @param obj the tagged object holding the object we want
	 *  @param explicit true if the object is meant to be explicitly
	 *               tagged false otherwise.
	 *  @exception IllegalArgumentException if the tagged object cannot
	 *                be converted.
	 */
	public static ASN1Integer getInstance(ASN1TaggedObject obj, boolean explicit) {
	}

	public javabc.BigInteger getValue() {
	}

	/**
	 *  in some cases positive values get crammed into a space,
	 *  that's not quite big enough...
	 */
	public javabc.BigInteger getPositiveValue() {
	}

	public int hashCode() {
	}

	public String toString() {
	}
}
