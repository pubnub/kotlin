/**
 * 
 * Math support for Elliptic Curve.
 */
package org.bouncycastle.math.ec;


public abstract class ECFieldElement implements ECConstants {

	public ECFieldElement() {
	}

	public abstract javabc.BigInteger toBigInteger() {
	}

	public abstract String getFieldName() {
	}

	public abstract int getFieldSize() {
	}

	public abstract ECFieldElement add(ECFieldElement b) {
	}

	public abstract ECFieldElement subtract(ECFieldElement b) {
	}

	public abstract ECFieldElement multiply(ECFieldElement b) {
	}

	public abstract ECFieldElement divide(ECFieldElement b) {
	}

	public abstract ECFieldElement negate() {
	}

	public abstract ECFieldElement square() {
	}

	public abstract ECFieldElement invert() {
	}

	public abstract ECFieldElement sqrt() {
	}

	public String toString() {
	}

	public static class Fp {


		public ECFieldElement.Fp(javabc.BigInteger q, javabc.BigInteger x) {
		}

		public javabc.BigInteger toBigInteger() {
		}

		/**
		 *  return the field name for this field.
		 * 
		 *  @return the string "Fp".
		 */
		public String getFieldName() {
		}

		public int getFieldSize() {
		}

		public javabc.BigInteger getQ() {
		}

		public ECFieldElement add(ECFieldElement b) {
		}

		public ECFieldElement subtract(ECFieldElement b) {
		}

		public ECFieldElement multiply(ECFieldElement b) {
		}

		public ECFieldElement divide(ECFieldElement b) {
		}

		public ECFieldElement negate() {
		}

		public ECFieldElement square() {
		}

		public ECFieldElement invert() {
		}

		/**
		 *  return a sqrt root - the routine verifies that the calculation
		 *  returns the right value - if none exists it returns null.
		 */
		public ECFieldElement sqrt() {
		}

		public boolean equals(Object other) {
		}

		public int hashCode() {
		}
	}

	/**
	 *  Class representing the Elements of the finite field
	 *  <code>F<sub>2<sup>m</sup></sub></code> in polynomial basis (PB)
	 *  representation. Both trinomial (TPB) and pentanomial (PPB) polynomial
	 *  basis representations are supported. Gaussian normal basis (GNB)
	 *  representation is not supported.
	 */
	public static class F2m {


		/**
		 *  Indicates gaussian normal basis representation (GNB). Number chosen
		 *  according to X9.62. GNB is not implemented at present.
		 */
		public static final int GNB = 1;

		/**
		 *  Indicates trinomial basis representation (TPB). Number chosen
		 *  according to X9.62.
		 */
		public static final int TPB = 2;

		/**
		 *  Indicates pentanomial basis representation (PPB). Number chosen
		 *  according to X9.62.
		 */
		public static final int PPB = 3;

		/**
		 *  Constructor for PPB.
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
		 *  @param x The BigInteger representing the value of the field element.
		 */
		public ECFieldElement.F2m(int m, int k1, int k2, int k3, javabc.BigInteger x) {
		}

		/**
		 *  Constructor for TPB.
		 *  @param m  The exponent <code>m</code> of
		 *  <code>F<sub>2<sup>m</sup></sub></code>.
		 *  @param k The integer <code>k</code> where <code>x<sup>m</sup> +
		 *  x<sup>k</sup> + 1</code> represents the reduction
		 *  polynomial <code>f(z)</code>.
		 *  @param x The BigInteger representing the value of the field element.
		 */
		public ECFieldElement.F2m(int m, int k, javabc.BigInteger x) {
		}

		public javabc.BigInteger toBigInteger() {
		}

		public String getFieldName() {
		}

		public int getFieldSize() {
		}

		/**
		 *  Checks, if the ECFieldElements <code>a</code> and <code>b</code>
		 *  are elements of the same field <code>F<sub>2<sup>m</sup></sub></code>
		 *  (having the same representation).
		 *  @param a field element.
		 *  @param b field element to be compared.
		 *  @throws IllegalArgumentException if <code>a</code> and <code>b</code>
		 *  are not elements of the same field
		 *  <code>F<sub>2<sup>m</sup></sub></code> (having the same
		 *  representation). 
		 */
		public static void checkFieldElements(ECFieldElement a, ECFieldElement b) {
		}

		public ECFieldElement add(ECFieldElement b) {
		}

		public ECFieldElement subtract(ECFieldElement b) {
		}

		public ECFieldElement multiply(ECFieldElement b) {
		}

		public ECFieldElement divide(ECFieldElement b) {
		}

		public ECFieldElement negate() {
		}

		public ECFieldElement square() {
		}

		public ECFieldElement invert() {
		}

		public ECFieldElement sqrt() {
		}

		/**
		 *  @return the representation of the field
		 *  <code>F<sub>2<sup>m</sup></sub></code>, either of
		 *  TPB (trinomial
		 *  basis representation) or
		 *  PPB (pentanomial
		 *  basis representation).
		 */
		public int getRepresentation() {
		}

		/**
		 *  @return the degree <code>m</code> of the reduction polynomial
		 *  <code>f(z)</code>.
		 */
		public int getM() {
		}

		/**
		 *  @return TPB: The integer <code>k</code> where <code>x<sup>m</sup> +
		 *  x<sup>k</sup> + 1</code> represents the reduction polynomial
		 *  <code>f(z)</code>.<br>
		 *  PPB: The integer <code>k1</code> where <code>x<sup>m</sup> +
		 *  x<sup>k3</sup> + x<sup>k2</sup> + x<sup>k1</sup> + 1</code>
		 *  represents the reduction polynomial <code>f(z)</code>.<br>
		 */
		public int getK1() {
		}

		/**
		 *  @return TPB: Always returns <code>0</code><br>
		 *  PPB: The integer <code>k2</code> where <code>x<sup>m</sup> +
		 *  x<sup>k3</sup> + x<sup>k2</sup> + x<sup>k1</sup> + 1</code>
		 *  represents the reduction polynomial <code>f(z)</code>.<br>
		 */
		public int getK2() {
		}

		/**
		 *  @return TPB: Always set to <code>0</code><br>
		 *  PPB: The integer <code>k3</code> where <code>x<sup>m</sup> +
		 *  x<sup>k3</sup> + x<sup>k2</sup> + x<sup>k1</sup> + 1</code>
		 *  represents the reduction polynomial <code>f(z)</code>.<br>
		 */
		public int getK3() {
		}

		public boolean equals(Object anObject) {
		}

		public int hashCode() {
		}
	}
}
