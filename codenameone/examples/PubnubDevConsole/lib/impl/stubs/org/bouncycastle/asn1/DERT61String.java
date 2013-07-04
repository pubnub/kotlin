/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


/**
 *  DER T61String (also the teletex string)
 */
public class DERT61String extends ASN1Primitive implements ASN1String {

	/**
	 *  basic constructor - with string.
	 */
	public DERT61String(String string) {
	}

	/**
	 *  return a T61 string from the passed in object.
	 * 
	 *  @exception IllegalArgumentException if the object cannot be converted.
	 */
	public static DERT61String getInstance(Object obj) {
	}

	/**
	 *  return an T61 String from a tagged object.
	 * 
	 *  @param obj the tagged object holding the object we want
	 *  @param explicit true if the object is meant to be explicitly
	 *               tagged false otherwise.
	 *  @exception IllegalArgumentException if the tagged object cannot
	 *                be converted.
	 */
	public static DERT61String getInstance(ASN1TaggedObject obj, boolean explicit) {
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
