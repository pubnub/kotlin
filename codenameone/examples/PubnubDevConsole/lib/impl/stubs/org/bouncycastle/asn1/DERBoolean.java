/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


public class DERBoolean extends ASN1Primitive {

	public static final ASN1Boolean FALSE;

	public static final ASN1Boolean TRUE;

	public DERBoolean(boolean value) {
	}

	/**
	 *  return a boolean from the passed in object.
	 * 
	 *  @exception IllegalArgumentException if the object cannot be converted.
	 */
	public static ASN1Boolean getInstance(Object obj) {
	}

	/**
	 *  return a DERBoolean from the passed in boolean.
	 */
	public static ASN1Boolean getInstance(boolean value) {
	}

	/**
	 *  return a Boolean from a tagged object.
	 * 
	 *  @param obj the tagged object holding the object we want
	 *  @param explicit true if the object is meant to be explicitly
	 *               tagged false otherwise.
	 *  @exception IllegalArgumentException if the tagged object cannot
	 *                be converted.
	 */
	public static DERBoolean getInstance(ASN1TaggedObject obj, boolean explicit) {
	}

	public boolean isTrue() {
	}

	protected boolean asn1Equals(ASN1Primitive o) {
	}

	public int hashCode() {
	}

	public String toString() {
	}
}
