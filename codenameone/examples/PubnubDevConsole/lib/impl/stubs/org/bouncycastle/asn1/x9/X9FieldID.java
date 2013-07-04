/**
 * 
 * Support classes useful for encoding and supporting X9.62 elliptic curve.
 */
package org.bouncycastle.asn1.x9;


/**
 *  ASN.1 def for Elliptic-Curve Field ID structure. See
 *  X9.62, for further details.
 */
public class X9FieldID extends org.bouncycastle.asn1.ASN1Object implements X9ObjectIdentifiers {

	/**
	 *  Constructor for elliptic curves over prime fields
	 *  <code>F<sub>2</sub></code>.
	 *  @param primeP The prime <code>p</code> defining the prime field.
	 */
	public X9FieldID(javabc.BigInteger primeP) {
	}

	/**
	 *  Constructor for elliptic curves over binary fields
	 *  <code>F<sub>2<sup>m</sup></sub></code>.
	 *  @param m  The exponent <code>m</code> of
	 *  <code>F<sub>2<sup>m</sup></sub></code>.
	 *  @param k1 The integer <code>k1</code> where <code>x<sup>m</sup> +
	 *  x<sup>k3</sup> + x<sup>k2</sup> + x<sup>k1</sup> + 1</code>
	 *  represents the reduction polynomial <code>f(z)</code>.
	 *  @param k2 The integer <code>k2</code> where <code>x<sup>m</sup> +
	 *  x<sup>k3</sup> + x<sup>k2</sup> + x<sup>k1</sup> + 1</code>
	 *  represents the reduction polynomial <code>f(z)</code>.
	 *  @param k3 The integer <code>k3</code> where <code>x<sup>m</sup> +
	 *  x<sup>k3</sup> + x<sup>k2</sup> + x<sup>k1</sup> + 1</code>
	 *  represents the reduction polynomial <code>f(z)</code>..
	 */
	public X9FieldID(int m, int k1, int k2, int k3) {
	}

	public X9FieldID(org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	public org.bouncycastle.asn1.ASN1ObjectIdentifier getIdentifier() {
	}

	public org.bouncycastle.asn1.ASN1Primitive getParameters() {
	}

	/**
	 *  Produce a DER encoding of the following structure.
	 *  <pre>
	 *   FieldID ::= SEQUENCE {
	 *       fieldType       FIELD-ID.&amp;id({IOSet}),
	 *       parameters      FIELD-ID.&amp;Type({IOSet}{&#64;fieldType})
	 *   }
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
