/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


/**
 *  @deprecated use BEROctetString
 */
public class BERConstructedOctetString extends BEROctetString {

	/**
	 *  @param string the octets making up the octet string.
	 */
	public BERConstructedOctetString(byte[] string) {
	}

	public BERConstructedOctetString(java.util.Vector octs) {
	}

	public BERConstructedOctetString(ASN1Primitive obj) {
	}

	public BERConstructedOctetString(ASN1Encodable obj) {
	}

	public byte[] getOctets() {
	}

	/**
	 *  return the DER octets that make up this string.
	 */
	public java.util.Enumeration getObjects() {
	}

	public static BEROctetString fromSequence(ASN1Sequence seq) {
	}
}
