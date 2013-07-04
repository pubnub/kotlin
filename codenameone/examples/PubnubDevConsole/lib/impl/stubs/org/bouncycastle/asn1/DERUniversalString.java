/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


/**
 *  DER UniversalString object.
 */
public class DERUniversalString extends ASN1Primitive implements ASN1String {

	/**
	 *  basic constructor - byte encoded string.
	 */
	public DERUniversalString(byte[] string) {
	}

	/**
	 *  return a Universal String from the passed in object.
	 * 
	 *  @exception IllegalArgumentException if the object cannot be converted.
	 */
	public static DERUniversalString getInstance(Object obj) {
	}

	/**
	 *  return a Universal String from a tagged object.
	 * 
	 *  @param obj the tagged object holding the object we want
	 *  @param explicit true if the object is meant to be explicitly
	 *               tagged false otherwise.
	 *  @exception IllegalArgumentException if the tagged object cannot
	 *                be converted.
	 */
	public static DERUniversalString getInstance(ASN1TaggedObject obj, boolean explicit) {
	}

	public String getString() {
	}

	public String toString() {
	}

	public byte[] getOctets() {
	}

	public int hashCode() {
	}
}
