/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


public class DERObjectIdentifier extends ASN1Primitive {

	public DERObjectIdentifier(String identifier) {
	}

	/**
	 *  return an OID from the passed in object
	 * 
	 *  @exception IllegalArgumentException if the object cannot be converted.
	 */
	public static ASN1ObjectIdentifier getInstance(Object obj) {
	}

	/**
	 *  return an Object Identifier from a tagged object.
	 * 
	 *  @param obj the tagged object holding the object we want
	 *  @param explicit true if the object is meant to be explicitly
	 *               tagged false otherwise.
	 *  @exception IllegalArgumentException if the tagged object cannot
	 *                be converted.
	 */
	public static ASN1ObjectIdentifier getInstance(ASN1TaggedObject obj, boolean explicit) {
	}

	public String getId() {
	}

	protected byte[] getBody() {
	}

	public int hashCode() {
	}

	public String toString() {
	}
}
