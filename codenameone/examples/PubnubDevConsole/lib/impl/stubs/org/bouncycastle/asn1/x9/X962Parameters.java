/**
 * 
 * Support classes useful for encoding and supporting X9.62 elliptic curve.
 */
package org.bouncycastle.asn1.x9;


public class X962Parameters extends org.bouncycastle.asn1.ASN1Object implements org.bouncycastle.asn1.ASN1Choice {

	public X962Parameters(X9ECParameters ecParameters) {
	}

	public X962Parameters(org.bouncycastle.asn1.ASN1ObjectIdentifier namedCurve) {
	}

	public X962Parameters(org.bouncycastle.asn1.ASN1Primitive obj) {
	}

	public static X962Parameters getInstance(Object obj) {
	}

	public static X962Parameters getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public boolean isNamedCurve() {
	}

	public boolean isImplicitlyCA() {
	}

	public org.bouncycastle.asn1.ASN1Primitive getParameters() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  <pre>
	 *  Parameters ::= CHOICE {
	 *     ecParameters ECParameters,
	 *     namedCurve   CURVES.&id({CurveNames}),
	 *     implicitlyCA NULL
	 *  }
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
