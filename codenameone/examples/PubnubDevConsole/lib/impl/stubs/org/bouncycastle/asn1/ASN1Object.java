/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


public abstract class ASN1Object implements ASN1Encodable {

	public ASN1Object() {
	}

	/**
	 *  Return the default BER or DER encoding for this object.
	 * 
	 *  @return BER/DER byte encoded object.
	 *  @throws java.io.IOException on encoding error.
	 */
	public byte[] getEncoded() {
	}

	/**
	 *  Return either the default for "BER" or a DER encoding if "DER" is specified.
	 * 
	 *  @param encoding name of encoding to use.
	 *  @return byte encoded object.
	 *  @throws IOException on encoding error.
	 */
	public byte[] getEncoded(String encoding) {
	}

	public int hashCode() {
	}

	public boolean equals(Object o) {
	}

	/**
	 *  @deprecated use toASN1Primitive()
	 *  @return the underlying primitive type.
	 */
	public ASN1Primitive toASN1Object() {
	}

	protected static boolean hasEncodedTagValue(Object obj, int tagValue) {
	}

	public abstract ASN1Primitive toASN1Primitive() {
	}
}
