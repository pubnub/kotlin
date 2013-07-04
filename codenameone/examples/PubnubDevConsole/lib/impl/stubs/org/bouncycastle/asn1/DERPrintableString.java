/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


/**
 *  DER PrintableString object.
 */
public class DERPrintableString extends ASN1Primitive implements ASN1String {

	/**
	 *  basic constructor - this does not validate the string
	 */
	public DERPrintableString(String string) {
	}

	/**
	 *  Constructor with optional validation.
	 * 
	 *  @param string the base string to wrap.
	 *  @param validate whether or not to check the string.
	 *  @throws IllegalArgumentException if validate is true and the string
	 *  contains characters that should not be in a PrintableString.
	 */
	public DERPrintableString(String string, boolean validate) {
	}

	/**
	 *  return a printable string from the passed in object.
	 *  
	 *  @exception IllegalArgumentException if the object cannot be converted.
	 */
	public static DERPrintableString getInstance(Object obj) {
	}

	/**
	 *  return a Printable String from a tagged object.
	 * 
	 *  @param obj the tagged object holding the object we want
	 *  @param explicit true if the object is meant to be explicitly
	 *               tagged false otherwise.
	 *  @exception IllegalArgumentException if the tagged object cannot
	 *                be converted.
	 */
	public static DERPrintableString getInstance(ASN1TaggedObject obj, boolean explicit) {
	}

	public String getString() {
	}

	public byte[] getOctets() {
	}

	public int hashCode() {
	}

	public String toString() {
	}

	/**
	 *  return true if the passed in String can be represented without
	 *  loss as a PrintableString, false otherwise.
	 * 
	 *  @return true if in printable set, false otherwise.
	 */
	public static boolean isPrintableString(String str) {
	}
}
