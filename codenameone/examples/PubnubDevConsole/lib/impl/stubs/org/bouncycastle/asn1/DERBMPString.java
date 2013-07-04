/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


/**
 *  DER BMPString object.
 */
public class DERBMPString extends ASN1Primitive implements ASN1String {

	/**
	 *  basic constructor
	 */
	public DERBMPString(String string) {
	}

	/**
	 *  return a BMP String from the given object.
	 * 
	 *  @param obj the object we want converted.
	 *  @exception IllegalArgumentException if the object cannot be converted.
	 */
	public static DERBMPString getInstance(Object obj) {
	}

	/**
	 *  return a BMP String from a tagged object.
	 * 
	 *  @param obj the tagged object holding the object we want
	 *  @param explicit true if the object is meant to be explicitly
	 *               tagged false otherwise.
	 *  @exception IllegalArgumentException if the tagged object cannot
	 *               be converted.
	 */
	public static DERBMPString getInstance(ASN1TaggedObject obj, boolean explicit) {
	}

	public String getString() {
	}

	public String toString() {
	}

	public int hashCode() {
	}

	protected boolean asn1Equals(ASN1Primitive o) {
	}
}
