/**
 * 
 * Support classes useful for encoding and supporting X9.62 elliptic curve.
 */
package org.bouncycastle.asn1.x9;


/**
 *  class for describing an ECPoint as a DER object.
 */
public class X9ECPoint extends org.bouncycastle.asn1.ASN1Object {

	public X9ECPoint(org.bouncycastle.math.ec.ECPoint p) {
	}

	public X9ECPoint(org.bouncycastle.math.ec.ECCurve c, org.bouncycastle.asn1.ASN1OctetString s) {
	}

	public org.bouncycastle.math.ec.ECPoint getPoint() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  <pre>
	 *   ECPoint ::= OCTET STRING
	 *  </pre>
	 *  <p>
	 *  Octet string produced using ECPoint.getEncoded().
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
