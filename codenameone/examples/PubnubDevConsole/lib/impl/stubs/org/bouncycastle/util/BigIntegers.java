package org.bouncycastle.util;


/**
 *  BigInteger utilities.
 */
public final class BigIntegers {

	public BigIntegers() {
	}

	/**
	 *  Return the passed in value as an unsigned byte array.
	 *  
	 *  @param value value to be converted.
	 *  @return a byte array without a leading zero byte if present in the signed encoding.
	 */
	public static byte[] asUnsignedByteArray(javabc.BigInteger value) {
	}

	/**
	 *  Return a random BigInteger not less than 'min' and not greater than 'max'
	 *  
	 *  @param min the least value that may be generated
	 *  @param max the greatest value that may be generated
	 *  @param random the source of randomness
	 *  @return a random BigInteger value in the range [min,max]
	 */
	public static javabc.BigInteger createRandomInRange(javabc.BigInteger min, javabc.BigInteger max, javabc.SecureRandom random) {
	}
}
