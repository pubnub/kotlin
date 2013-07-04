/**
 * 
 * Math support for Elliptic Curve.
 */
package org.bouncycastle.math.ec;


/**
 *  base class for points on elliptic curves.
 */
public abstract class ECPoint {

	protected boolean withCompression;

	protected ECMultiplier multiplier;

	protected PreCompInfo preCompInfo;

	protected ECPoint(ECCurve curve, ECFieldElement x, ECFieldElement y) {
	}

	public ECCurve getCurve() {
	}

	public ECFieldElement getX() {
	}

	public ECFieldElement getY() {
	}

	public boolean isInfinity() {
	}

	public boolean isCompressed() {
	}

	public boolean equals(Object other) {
	}

	public int hashCode() {
	}

	public abstract byte[] getEncoded() {
	}

	public abstract ECPoint add(ECPoint b) {
	}

	public abstract ECPoint subtract(ECPoint b) {
	}

	public abstract ECPoint negate() {
	}

	public abstract ECPoint twice() {
	}

	/**
	 *  Multiplies this <code>ECPoint</code> by the given number.
	 *  @param k The multiplicator.
	 *  @return <code>k * this</code>.
	 */
	public ECPoint multiply(javabc.BigInteger k) {
	}

	/**
	 *  Elliptic curve points over Fp
	 */
	public static class Fp {


		/**
		 *  Create a point which encodes with point compression.
		 *  
		 *  @param curve the curve to use
		 *  @param x affine x co-ordinate
		 *  @param y affine y co-ordinate
		 */
		public ECPoint.Fp(ECCurve curve, ECFieldElement x, ECFieldElement y) {
		}

		/**
		 *  Create a point that encodes with or without point compresion.
		 *  
		 *  @param curve the curve to use
		 *  @param x affine x co-ordinate
		 *  @param y affine y co-ordinate
		 *  @param withCompression if true encode with point compression
		 */
		public ECPoint.Fp(ECCurve curve, ECFieldElement x, ECFieldElement y, boolean withCompression) {
		}

		/**
		 *  return the field element encoded with point compression. (S 4.3.6)
		 */
		public byte[] getEncoded() {
		}

		public ECPoint add(ECPoint b) {
		}

		public ECPoint twice() {
		}

		public ECPoint subtract(ECPoint b) {
		}

		public ECPoint negate() {
		}
	}

	/**
	 *  Elliptic curve points over F2m
	 */
	public static class F2m {


		/**
		 *  @param curve base curve
		 *  @param x x point
		 *  @param y y point
		 */
		public ECPoint.F2m(ECCurve curve, ECFieldElement x, ECFieldElement y) {
		}

		/**
		 *  @param curve base curve
		 *  @param x x point
		 *  @param y y point
		 *  @param withCompression true if encode with point compression.
		 */
		public ECPoint.F2m(ECCurve curve, ECFieldElement x, ECFieldElement y, boolean withCompression) {
		}

		public byte[] getEncoded() {
		}

		public ECPoint add(ECPoint b) {
		}

		/**
		 *  Adds another <code>ECPoints.F2m</code> to <code>this</code> without
		 *  checking if both points are on the same curve. Used by multiplication
		 *  algorithms, because there all points are a multiple of the same point
		 *  and hence the checks can be omitted.
		 *  @param b The other <code>ECPoints.F2m</code> to add to
		 *  <code>this</code>.
		 *  @return <code>this + b</code>
		 */
		public ECPoint.F2m addSimple(ECPoint.F2m b) {
		}

		public ECPoint subtract(ECPoint b) {
		}

		/**
		 *  Subtracts another <code>ECPoints.F2m</code> from <code>this</code>
		 *  without checking if both points are on the same curve. Used by
		 *  multiplication algorithms, because there all points are a multiple
		 *  of the same point and hence the checks can be omitted.
		 *  @param b The other <code>ECPoints.F2m</code> to subtract from
		 *  <code>this</code>.
		 *  @return <code>this - b</code>
		 */
		public ECPoint.F2m subtractSimple(ECPoint.F2m b) {
		}

		public ECPoint twice() {
		}

		public ECPoint negate() {
		}
	}
}
