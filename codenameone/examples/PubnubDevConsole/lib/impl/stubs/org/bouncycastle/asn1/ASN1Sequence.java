/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


public abstract class ASN1Sequence extends ASN1Primitive {

	protected java.util.Vector seq;

	/**
	 *  create an empty sequence
	 */
	protected ASN1Sequence() {
	}

	/**
	 *  create a sequence containing one object
	 */
	protected ASN1Sequence(ASN1Encodable obj) {
	}

	/**
	 *  create a sequence containing a vector of objects.
	 */
	protected ASN1Sequence(ASN1EncodableVector v) {
	}

	/**
	 *  create a sequence containing a vector of objects.
	 */
	protected ASN1Sequence(ASN1Encodable[] array) {
	}

	/**
	 *  return an ASN1Sequence from the given object.
	 * 
	 *  @param obj the object we want converted.
	 *  @exception IllegalArgumentException if the object cannot be converted.
	 */
	public static ASN1Sequence getInstance(Object obj) {
	}

	/**
	 *  Return an ASN1 sequence from a tagged object. There is a special
	 *  case here, if an object appears to have been explicitly tagged on 
	 *  reading but we were expecting it to be implicitly tagged in the 
	 *  normal course of events it indicates that we lost the surrounding
	 *  sequence - so we need to add it back (this will happen if the tagged
	 *  object is a sequence that contains other sequences). If you are
	 *  dealing with implicitly tagged sequences you really <b>should</b>
	 *  be using this method.
	 * 
	 *  @param obj the tagged object.
	 *  @param explicit true if the object is meant to be explicitly tagged,
	 *           false otherwise.
	 *  @exception IllegalArgumentException if the tagged object cannot
	 *           be converted.
	 */
	public static ASN1Sequence getInstance(ASN1TaggedObject obj, boolean explicit) {
	}

	public ASN1Encodable[] toArray() {
	}

	public java.util.Enumeration getObjects() {
	}

	public ASN1SequenceParser parser() {
	}

	/**
	 *  return the object at the sequence position indicated by index.
	 * 
	 *  @param index the sequence number (starting at zero) of the object
	 *  @return the object at the sequence position indicated by index.
	 */
	public ASN1Encodable getObjectAt(int index) {
	}

	/**
	 *  return the number of objects in this sequence.
	 * 
	 *  @return the number of objects in this sequence.
	 */
	public int size() {
	}

	public int hashCode() {
	}

	public String toString() {
	}
}
