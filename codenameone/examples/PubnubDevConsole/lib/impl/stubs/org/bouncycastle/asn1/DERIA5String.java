/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


/**
 *  DER IA5String object - this is an ascii string.
 */
public class DERIA5String extends ASN1Primitive implements ASN1String {

	/**
	 *  basic constructor - without validation.
	 */
	public DERIA5String(String string) {
	}

	/**
	 *  Constructor with optional validation.
	 * 
	 *  @param string the base string to wrap.
	 *  @param validate whether or not to check the string.
	 *  @throws IllegalArgumentException if validate is true and the string
	 *  contains characters that should not be in an IA5String.
	 */
	public DERIA5String(String string, boolean validate) {
	}

	/**
	 *  return a IA5 string from the passed in object
	 * 
	 *  @exception IllegalArgumentException if the object cannot be converted.
	 */
	public static DERIA5String getInstance(Object obj) {
	}

	/**
	 *  return an IA5 String from a tagged object.
	 * 
	 *  @param obj the tagged object holding the object we want
	 *  @param explicit true if the object is meant to be explicitly
	 *               tagged false otherwise.
	 *  @exception IllegalArgumentException if the tagged object cannot
	 *                be converted.
	 */
	public static DERIA5String getInstance(ASN1TaggedObject obj, boolean explicit) {
	}

	public String getString() {
	}

	public String toString() {
	}

	public byte[] getOctets() {
	}

	public int hashCode() {
	}

	/**
	 *  return true if the passed in String can be represented without
	 *  loss as an IA5String, false otherwise.
	 * 
	 *  @return true if in printable set, false otherwise.
	 */
	public static boolean isIA5String(String str) {
	}
}
