/**
 * 
 * Math support for Elliptic Curve.
 */
package org.bouncycastle.math.ec;


/**
 *  base class for an elliptic curve
 */
public abstract class ECCurve {

	public ECCurve() {
	}

	public abstract int getFieldSize() {
	}

	public abstract ECFieldElement fromBigInteger(javabc.BigInteger x) {
	}

	public abstract ECPoint createPoint(javabc.BigInteger x, javabc.BigInteger y, boolean withCompression) {
	}

	public abstract ECPoint decodePoint(byte[] encoded) {
	}

	public abstract ECPoint getInfinity() {
	}

	public ECFieldElement getA() {
	}

	public ECFieldElement getB() {
	}

	/**
	 *  Elliptic curve over Fp
	 */
	public static class Fp {


		public ECCurve.Fp(javabc.BigInteger q, javabc.BigInteger a, javabc.BigInteger b) {
		}

		public javabc.BigInteger getQ() {
		}

		public int getFieldSize() {
		}

		public ECFieldElement fromBigInteger(javabc.BigInteger x) {
		}

		public ECPoint createPoint(javabc.BigInteger x, javabc.BigInteger y, boolean withCompression) {
		}

		/**
		 *  Decode a point on this curve from its ASN.1 encoding. The different
		 *  encodings are taken account of, including point compression for
		 *  <code>F<sub>p</sub></code> (X9.62 s 4.2.1 pg 17).
		 *  @return The decoded point.
		 */
		public ECPoint decodePoint(byte[] encoded) {
		}

		public ECPoint getInfinity() {
		}

		public boolean equals(Object anObject) {
		}

		public int hashCode() {
		}
	}

	/**
	 *  Elliptic curves over F2m. The Weierstrass equation is given by
	 *  <code>y<sup>2</sup> + xy = x<sup>3</sup> + ax<sup>2</sup> + b</code>.
	 */
	public static class F2m {


		/**
		 *  Constructor for Trinomial Polynomial Basis (TPB).
		 *  @param m  The exponent <code>m</code> of
		 *  <code>F<sub>2<sup>m</sup></sub></code>.
		 *  @param k The integer <code>k</code> where <code>x<sup>m</sup> +
		 *  x<sup>k</sup> + 1</code> represents the reduction
		 *  polynomial <code>f(z)</code>.
		 *  @param a The coefficient <code>a</code> in the Weierstrass equation
		 *  for non-supersingular elliptic curves over
		 *  <code>F<sub>2<sup>m</sup></sub></code>.
		 *  @param b The coefficient <code>b</code> in the Weierstrass equation
		 *  for non-supersingular elliptic curves over
		 *  <code>F<sub>2<sup>m</sup></sub></code>.
		 */
		public ECCurve.F2m(int m, int k, javabc.BigInteger a, javabc.BigInteger b) {
		}

		/**
		 *  Constructor for Trinomial Polynomial Basis (TPB).
		 *  @param m  The exponent <code>m</code> of
		 *  <code>F<sub>2<sup>m</sup></sub></code>.
		 *  @param k The integer <code>k</code> where <code>x<sup>m</sup> +
		 *  x<sup>k</sup> + 1</code> represents the reduction
		 *  polynomial <code>f(z)</code>.
		 *  @param a The coefficient <code>a</code> in the Weierstrass equation
		 *  for non-supersingular elliptic curves over
		 *  <code>F<sub>2<sup>m</sup></sub></code>.
		 *  @param b The coefficient <code>b</code> in the Weierstrass equation
		 *  for non-supersingular elliptic curves over
		 *  <code>F<sub>2<sup>m</sup></sub></code>.
		 *  @param n The order of the main subgroup of the elliptic curve.
		 *  @param h The cofactor of the elliptic curve, i.e.
		 *  <code>#E<sub>a</sub>(F<sub>2<sup>m</sup></sub>) = h * n</code>.
		 */
		public ECCurve.F2m(int m, int k, javabc.BigInteger a, javabc.BigInteger b, javabc.BigInteger n, javabc.BigInteger h) {
		}

		/**
		 *  Constructor for Pentanomial Polynomial Basis (PPB).
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
		 *  represents the reduction polynomial <code>f(z)</code>.
		 *  @param a The coefficient <code>a</code> in the Weierstrass equation
		 *  for non-supersingular elliptic curves over
		 *  <code>F<sub>2<sup>m</sup></sub></code>.
		 *  @param b The coefficient <code>b</code> in the Weierstrass equation
		 *  for non-supersingular elliptic curves over
		 *  <code>F<sub>2<sup>m</sup></sub></code>.
		 */
		public ECCurve.F2m(int m, int k1, int k2, int k3, javabc.BigInteger a, javabc.BigInteger b) {
		}

		/**
		 *  Constructor for Pentanomial Polynomial Basis (PPB).
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
		 *  represents the reduction polynomial <code>f(z)</code>.
		 *  @param a The coefficient <code>a</code> in the Weierstrass equation
		 *  for non-supersingular elliptic curves over
		 *  <code>F<sub>2<sup>m</sup></sub></code>.
		 *  @param b The coefficient <code>b</code> in the Weierstrass equation
		 *  for non-supersingular elliptic curves over
		 *  <code>F<sub>2<sup>m</sup></sub></code>.
		 *  @param n The order of the main subgroup of the elliptic curve.
		 *  @param h The cofactor of the elliptic curve, i.e.
		 *  <code>#E<sub>a</sub>(F<sub>2<sup>m</sup></sub>) = h * n</code>.
		 */
		public ECCurve.F2m(int m, int k1, int k2, int k3, javabc.BigInteger a, javabc.BigInteger b, javabc.BigInteger n, javabc.BigInteger h) {
		}

		public int getFieldSize() {
		}

		public ECFieldElement fromBigInteger(javabc.BigInteger x) {
		}

		public ECPoint createPoint(javabc.BigInteger x, javabc.BigInteger y, boolean withCompression) {
		}

		public ECPoint decodePoint(byte[] encoded) {
		}

		public ECPoint getInfinity() {
		}

		/**
		 *  Returns true if this is a Koblitz curve (ABC curve).
		 *  @return true if this is a Koblitz curve (ABC curve), false otherwise
		 */
		public boolean isKoblitz() {
		}

		public boolean equals(Object anObject) {
		}

		public int hashCode() {
		}

		public int getM() {
		}

		/**
		 *  Return true if curve uses a Trinomial basis.
		 *  
		 *  @return true if curve Trinomial, false otherwise.
		 */
		public boolean isTrinomial() {
		}

		public int getK1() {
		}

		public int getK2() {
		}

		public int getK3() {
		}

		public javabc.BigInteger getN() {
		}

		public javabc.BigInteger getH() {
		}
	}
}
