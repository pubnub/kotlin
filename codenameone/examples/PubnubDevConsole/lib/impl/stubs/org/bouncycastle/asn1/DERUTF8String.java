/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


/**
 *  DER UTF8String object.
 */
public class DERUTF8String extends ASN1Primitive implements ASN1String {

	/**
	 *  basic constructor
	 */
	public DERUTF8String(String string) {
	}

	/**
	 *  return an UTF8 string from the passed in object.
	 *  
	 *  @exception IllegalArgumentException
	 *                 if the object cannot be converted.
	 */
	public static DERUTF8String getInstance(Object obj) {
	}

	/**
	 *  return an UTF8 String from a tagged object.
	 *  
	 *  @param obj
	 *             the tagged object holding the object we want
	 *  @param explicit
	 *             true if the object is meant to be explicitly tagged false
	 *             otherwise.
	 *  @exception IllegalArgumentException
	 *                 if the tagged object cannot be converted.
	 */
	public static DERUTF8String getInstance(ASN1TaggedObject obj, boolean explicit) {
	}

	public String getString() {
	}

	public String toString() {
	}

	public int hashCode() {
	}
}
