/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


/**
 *  Base class for an application specific object
 */
public class DERApplicationSpecific extends ASN1Primitive {

	public DERApplicationSpecific(int tag, byte[] octets) {
	}

	public DERApplicationSpecific(int tag, ASN1Encodable object) {
	}

	public DERApplicationSpecific(boolean explicit, int tag, ASN1Encodable object) {
	}

	public DERApplicationSpecific(int tagNo, ASN1EncodableVector vec) {
	}

	public static DERApplicationSpecific getInstance(Object obj) {
	}

	public boolean isConstructed() {
	}

	public byte[] getContents() {
	}

	public int getApplicationTag() {
	}

	/**
	 *  Return the enclosed object assuming explicit tagging.
	 * 
	 *  @return  the resulting object
	 *  @throws IOException if reconstruction fails.
	 */
	public ASN1Primitive getObject() {
	}

	/**
	 *  Return the enclosed object assuming implicit tagging.
	 * 
	 *  @param derTagNo the type tag that should be applied to the object's contents.
	 *  @return  the resulting object
	 *  @throws IOException if reconstruction fails.
	 */
	public ASN1Primitive getObject(int derTagNo) {
	}

	public int hashCode() {
	}
}
