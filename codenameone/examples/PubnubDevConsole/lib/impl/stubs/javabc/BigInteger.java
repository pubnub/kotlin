package javabc;


public class BigInteger {

	public static final BigInteger ZERO;

	public static final BigInteger ONE;

	public BigInteger(String sval) {
	}

	public BigInteger(String sval, int rdx) {
	}

	public BigInteger(byte[] bval) {
	}

	public BigInteger(int sign, byte[] mag) {
	}

	public BigInteger(int numBits, java.util.Random rnd) {
	}

	public BigInteger(int bitLength, int certainty, java.util.Random rnd) {
	}

	public BigInteger abs() {
	}

	public BigInteger add(BigInteger val) {
	}

	public BigInteger and(BigInteger value) {
	}

	public BigInteger andNot(BigInteger value) {
	}

	public int bitCount() {
	}

	public int bitLength() {
	}

	public int compareTo(Object o) {
	}

	public int compareTo(BigInteger val) {
	}

	public BigInteger divide(BigInteger val) {
	}

	public BigInteger[] divideAndRemainder(BigInteger val) {
	}

	public boolean equals(Object val) {
	}

	public BigInteger gcd(BigInteger val) {
	}

	public int hashCode() {
	}

	public int intValue() {
	}

	public byte byteValue() {
	}

	/**
	 *  return whether or not a BigInteger is probably prime with a
	 *  probability of 1 - (1/2)**certainty.
	 *  <p>
	 *  From Knuth Vol 2, pg 395.
	 */
	public boolean isProbablePrime(int certainty) {
	}

	public long longValue() {
	}

	public BigInteger max(BigInteger val) {
	}

	public BigInteger min(BigInteger val) {
	}

	public BigInteger mod(BigInteger m) {
	}

	public BigInteger modInverse(BigInteger m) {
	}

	public BigInteger modPow(BigInteger exponent, BigInteger m) {
	}

	public BigInteger multiply(BigInteger val) {
	}

	public BigInteger negate() {
	}

	public BigInteger not() {
	}

	public BigInteger pow(int exp) {
	}

	public static BigInteger probablePrime(int bitLength, java.util.Random random) {
	}

	public BigInteger remainder(BigInteger n) {
	}

	public BigInteger shiftLeft(int n) {
	}

	public BigInteger shiftRight(int n) {
	}

	public int signum() {
	}

	public BigInteger subtract(BigInteger val) {
	}

	public byte[] toByteArray() {
	}

	public BigInteger xor(BigInteger val) {
	}

	public BigInteger or(BigInteger value) {
	}

	public BigInteger setBit(int n) {
	}

	public BigInteger clearBit(int n) {
	}

	public BigInteger flipBit(int n) {
	}

	public String toString() {
	}

	public String toString(int rdx) {
	}

	public static BigInteger valueOf(long val) {
	}

	public int getLowestSetBit() {
	}

	public boolean testBit(int n) {
	}
}
