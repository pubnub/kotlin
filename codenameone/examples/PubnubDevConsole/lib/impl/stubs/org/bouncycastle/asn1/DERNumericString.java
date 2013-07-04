/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


/**
 *  DER NumericString object - this is an ascii string of characters {0,1,2,3,4,5,6,7,8,9, }.
 */
public class DERNumericString extends ASN1Primitive implements ASN1String {

	/**
	 *  basic constructor -  without validation..
	 */
	public DERNumericString(String string) {
	}

	/**
	 *  Constructor with optional validation.
	 * 
	 *  @param string the base string to wrap.
	 *  @param validate whether or not to check the string.
	 *  @throws IllegalArgumentException if validate is true and the string
	 *  contains characters that should not be in a NumericString.
	 */
	public DERNumericString(String string, boolean validate) {
	}

	/**
	 *  return a Numeric string from the passed in object
	 * 
	 *  @exception IllegalArgumentException if the object cannot be converted.
	 */
	public static DERNumericString getInstance(Object obj) {
	}

	/**
	 *  return an Numeric String from a tagged object.
	 * 
	 *  @param obj the tagged object holding the object we want
	 *  @param explicit true if the object is meant to be explicitly
	 *               tagged false otherwise.
	 *  @exception IllegalArgumentException if the tagged object cannot
	 *                be converted.
	 */
	public static DERNumericString getInstance(ASN1TaggedObject obj, boolean explicit) {
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
	 *  Return true if the string can be represented as a NumericString ('0'..'9', ' ')
	 * 
	 *  @param str string to validate.
	 *  @return true if numeric, fale otherwise.
	 */
	public static boolean isNumericString(String str) {
	}
}
