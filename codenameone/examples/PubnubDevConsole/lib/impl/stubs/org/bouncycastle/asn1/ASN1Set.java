/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


public abstract class ASN1Set extends ASN1Primitive {

	protected ASN1Set() {
	}

	/**
	 *  create a sequence containing one object
	 */
	protected ASN1Set(ASN1Encodable obj) {
	}

	/**
	 *  create a sequence containing a vector of objects.
	 */
	protected ASN1Set(ASN1EncodableVector v, boolean doSort) {
	}

	/**
	 *  create a sequence containing a vector of objects.
	 */
	protected ASN1Set(ASN1Encodable[] array, boolean doSort) {
	}

	/**
	 *  return an ASN1Set from the given object.
	 * 
	 *  @param obj the object we want converted.
	 *  @exception IllegalArgumentException if the object cannot be converted.
	 */
	public static ASN1Set getInstance(Object obj) {
	}

	/**
	 *  Return an ASN1 set from a tagged object. There is a special
	 *  case here, if an object appears to have been explicitly tagged on 
	 *  reading but we were expecting it to be implicitly tagged in the 
	 *  normal course of events it indicates that we lost the surrounding
	 *  set - so we need to add it back (this will happen if the tagged
	 *  object is a sequence that contains other sequences). If you are
	 *  dealing with implicitly tagged sets you really <b>should</b>
	 *  be using this method.
	 * 
	 *  @param obj the tagged object.
	 *  @param explicit true if the object is meant to be explicitly tagged
	 *           false otherwise.
	 *  @exception IllegalArgumentException if the tagged object cannot
	 *           be converted.
	 */
	public static ASN1Set getInstance(ASN1TaggedObject obj, boolean explicit) {
	}

	public java.util.Enumeration getObjects() {
	}

	/**
	 *  return the object at the set position indicated by index.
	 * 
	 *  @param index the set number (starting at zero) of the object
	 *  @return the object at the set position indicated by index.
	 */
	public ASN1Encodable getObjectAt(int index) {
	}

	/**
	 *  return the number of objects in this set.
	 * 
	 *  @return the number of objects in this set.
	 */
	public int size() {
	}

	public ASN1Encodable[] toArray() {
	}

	public ASN1SetParser parser() {
	}

	public int hashCode() {
	}

	protected void sort() {
	}

	public String toString() {
	}
}
