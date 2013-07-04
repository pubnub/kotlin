/**
 * 
 * Support classes useful for encoding and supporting X9.62 elliptic curve.
 */
package org.bouncycastle.asn1.x9;


/**
 *  ASN.1 def for Elliptic-Curve Curve structure. See
 *  X9.62, for further details.
 */
public class X9Curve extends org.bouncycastle.asn1.ASN1Object implements X9ObjectIdentifiers {

	public X9Curve(org.bouncycastle.math.ec.ECCurve curve) {
	}

	public X9Curve(org.bouncycastle.math.ec.ECCurve curve, byte[] seed) {
	}

	public X9Curve(X9FieldID fieldID, org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	public org.bouncycastle.math.ec.ECCurve getCurve() {
	}

	public byte[] getSeed() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  <pre>
	 *   Curve ::= SEQUENCE {
	 *       a               FieldElement,
	 *       b               FieldElement,
	 *       seed            BIT STRING      OPTIONAL
	 *   }
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
