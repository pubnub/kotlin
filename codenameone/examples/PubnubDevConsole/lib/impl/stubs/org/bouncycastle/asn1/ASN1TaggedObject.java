/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


/**
 *  ASN.1 TaggedObject - in ASN.1 notation this is any object preceded by
 *  a [n] where n is some number - these are assumed to follow the construction
 *  rules (as with sequences).
 */
public abstract class ASN1TaggedObject extends ASN1Primitive implements ASN1TaggedObjectParser {

	/**
	 *  Create a tagged object with the style given by the value of explicit.
	 *  <p>
	 *  If the object implements ASN1Choice the tag style will always be changed
	 *  to explicit in accordance with the ASN.1 encoding rules.
	 *  </p>
	 *  @param explicit true if the object is explicitly tagged.
	 *  @param tagNo the tag number for this object.
	 *  @param obj the tagged object.
	 */
	public ASN1TaggedObject(boolean explicit, int tagNo, ASN1Encodable obj) {
	}

	public static ASN1TaggedObject getInstance(ASN1TaggedObject obj, boolean explicit) {
	}

	public static ASN1TaggedObject getInstance(Object obj) {
	}

	public int hashCode() {
	}

	public int getTagNo() {
	}

	/**
	 *  return whether or not the object may be explicitly tagged. 
	 *  <p>
	 *  Note: if the object has been read from an input stream, the only
	 *  time you can be sure if isExplicit is returning the true state of
	 *  affairs is if it returns false. An implicitly tagged object may appear
	 *  to be explicitly tagged, so you need to understand the context under
	 *  which the reading was done as well, see getObject below.
	 */
	public boolean isExplicit() {
	}

	public boolean isEmpty() {
	}

	/**
	 *  return whatever was following the tag.
	 *  <p>
	 *  Note: tagged objects are generally context dependent if you're
	 *  trying to extract a tagged object you should be going via the
	 *  appropriate getInstance method.
	 */
	public ASN1Primitive getObject() {
	}

	/**
	 *  Return the object held in this tagged object as a parser assuming it has
	 *  the type of the passed in tag. If the object doesn't have a parser
	 *  associated with it, the base object is returned.
	 */
	public ASN1Encodable getObjectParser(int tag, boolean isExplicit) {
	}

	public ASN1Primitive getLoadedObject() {
	}

	public String toString() {
	}
}
