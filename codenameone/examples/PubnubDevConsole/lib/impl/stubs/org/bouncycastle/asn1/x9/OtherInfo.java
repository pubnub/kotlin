/**
 * 
 * Support classes useful for encoding and supporting X9.62 elliptic curve.
 */
package org.bouncycastle.asn1.x9;


/**
 *  ANS.1 def for Diffie-Hellman key exchange OtherInfo structure. See
 *  RFC 2631, or X9.42, for further details.
 */
public class OtherInfo extends org.bouncycastle.asn1.ASN1Object {

	public OtherInfo(KeySpecificInfo keyInfo, org.bouncycastle.asn1.ASN1OctetString partyAInfo, org.bouncycastle.asn1.ASN1OctetString suppPubInfo) {
	}

	public OtherInfo(org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	public KeySpecificInfo getKeyInfo() {
	}

	public org.bouncycastle.asn1.ASN1OctetString getPartyAInfo() {
	}

	public org.bouncycastle.asn1.ASN1OctetString getSuppPubInfo() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  <pre>
	 *   OtherInfo ::= SEQUENCE {
	 *       keyInfo KeySpecificInfo,
	 *       partyAInfo [0] OCTET STRING OPTIONAL,
	 *       suppPubInfo [2] OCTET STRING
	 *   }
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
