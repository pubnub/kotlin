/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


public class DERBitString extends ASN1Primitive implements ASN1String {

	protected byte[] data;

	protected int padBits;

	protected DERBitString(byte data, int padBits) {
	}

	/**
	 *  @param data the octets making up the bit string.
	 *  @param padBits the number of extra bits at the end of the string.
	 */
	public DERBitString(byte[] data, int padBits) {
	}

	public DERBitString(byte[] data) {
	}

	public DERBitString(ASN1Encodable obj) {
	}

	/**
	 *  return the correct number of pad bits for a bit string defined in
	 *  a 32 bit constant
	 */
	protected static int getPadBits(int bitString) {
	}

	/**
	 *  return the correct number of bytes for a bit string defined in
	 *  a 32 bit constant
	 */
	protected static byte[] getBytes(int bitString) {
	}

	/**
	 *  return a Bit String from the passed in object
	 * 
	 *  @exception IllegalArgumentException if the object cannot be converted.
	 */
	public static DERBitString getInstance(Object obj) {
	}

	/**
	 *  return a Bit String from a tagged object.
	 * 
	 *  @param obj the tagged object holding the object we want
	 *  @param explicit true if the object is meant to be explicitly
	 *               tagged false otherwise.
	 *  @exception IllegalArgumentException if the tagged object cannot
	 *                be converted.
	 */
	public static DERBitString getInstance(ASN1TaggedObject obj, boolean explicit) {
	}

	public byte[] getBytes() {
	}

	public int getPadBits() {
	}

	/**
	 *  @return the value of the bit string as an int (truncating if necessary)
	 */
	public int intValue() {
	}

	public int hashCode() {
	}

	protected boolean asn1Equals(ASN1Primitive o) {
	}

	public String getString() {
	}

	public String toString() {
	}
}
