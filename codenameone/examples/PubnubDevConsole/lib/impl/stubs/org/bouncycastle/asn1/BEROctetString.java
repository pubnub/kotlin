/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


public class BEROctetString extends ASN1OctetString {

	/**
	 *  @param string the octets making up the octet string.
	 */
	public BEROctetString(byte[] string) {
	}

	public BEROctetString(ASN1OctetString[] octs) {
	}

	public byte[] getOctets() {
	}

	/**
	 *  return the DER octets that make up this string.
	 */
	public java.util.Enumeration getObjects() {
	}

	public void encode(ASN1OutputStream out) {
	}
}
