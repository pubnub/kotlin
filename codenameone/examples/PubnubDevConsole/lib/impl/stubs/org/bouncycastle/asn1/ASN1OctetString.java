/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


public abstract class ASN1OctetString extends ASN1Primitive implements ASN1OctetStringParser {

	/**
	 *  @param string the octets making up the octet string.
	 */
	public ASN1OctetString(byte[] string) {
	}

	/**
	 *  return an Octet String from a tagged object.
	 * 
	 *  @param obj the tagged object holding the object we want.
	 *  @param explicit true if the object is meant to be explicitly
	 *               tagged false otherwise.
	 *  @exception IllegalArgumentException if the tagged object cannot
	 *               be converted.
	 */
	public static ASN1OctetString getInstance(ASN1TaggedObject obj, boolean explicit) {
	}

	/**
	 *  return an Octet String from the given object.
	 * 
	 *  @param obj the object we want converted.
	 *  @exception IllegalArgumentException if the object cannot be converted.
	 */
	public static ASN1OctetString getInstance(Object obj) {
	}

	public java.io.InputStream getOctetStream() {
	}

	public ASN1OctetStringParser parser() {
	}

	public byte[] getOctets() {
	}

	public int hashCode() {
	}

	public ASN1Primitive getLoadedObject() {
	}

	public String toString() {
	}
}
