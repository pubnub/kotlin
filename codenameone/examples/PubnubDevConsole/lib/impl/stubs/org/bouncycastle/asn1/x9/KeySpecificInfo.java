/**
 * 
 * Support classes useful for encoding and supporting X9.62 elliptic curve.
 */
package org.bouncycastle.asn1.x9;


/**
 *  ASN.1 def for Diffie-Hellman key exchange KeySpecificInfo structure. See
 *  RFC 2631, or X9.42, for further details.
 */
public class KeySpecificInfo extends org.bouncycastle.asn1.ASN1Object {

	public KeySpecificInfo(org.bouncycastle.asn1.ASN1ObjectIdentifier algorithm, org.bouncycastle.asn1.ASN1OctetString counter) {
	}

	public KeySpecificInfo(org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	public org.bouncycastle.asn1.ASN1ObjectIdentifier getAlgorithm() {
	}

	public org.bouncycastle.asn1.ASN1OctetString getCounter() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  <pre>
	 *   KeySpecificInfo ::= SEQUENCE {
	 *       algorithm OBJECT IDENTIFIER,
	 *       counter OCTET STRING SIZE (4..4)
	 *   }
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
