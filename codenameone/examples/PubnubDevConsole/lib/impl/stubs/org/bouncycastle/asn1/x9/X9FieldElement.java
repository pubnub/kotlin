/**
 * 
 * Support classes useful for encoding and supporting X9.62 elliptic curve.
 */
package org.bouncycastle.asn1.x9;


/**
 *  class for processing an FieldElement as a DER object.
 */
public class X9FieldElement extends org.bouncycastle.asn1.ASN1Object {

	protected org.bouncycastle.math.ec.ECFieldElement f;

	public X9FieldElement(org.bouncycastle.math.ec.ECFieldElement f) {
	}

	public X9FieldElement(javabc.BigInteger p, org.bouncycastle.asn1.ASN1OctetString s) {
	}

	public X9FieldElement(int m, int k1, int k2, int k3, org.bouncycastle.asn1.ASN1OctetString s) {
	}

	public org.bouncycastle.math.ec.ECFieldElement getValue() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  <pre>
	 *   FieldElement ::= OCTET STRING
	 *  </pre>
	 *  <p>
	 *  <ol>
	 *  <li> if <i>q</i> is an odd prime then the field element is
	 *  processed as an Integer and converted to an octet string
	 *  according to x 9.62 4.3.1.</li>
	 *  <li> if <i>q</i> is 2<sup>m</sup> then the bit string
	 *  contained in the field element is converted into an octet
	 *  string with the same ordering padded at the front if necessary.
	 *  </li>
	 *  </ol>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
