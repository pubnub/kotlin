/**
 * 
 * Some general utility/conversion classes.
 */
package org.bouncycastle.crypto.util;


public abstract class Pack {

	public Pack() {
	}

	public static int bigEndianToInt(byte[] bs, int off) {
	}

	public static void bigEndianToInt(byte[] bs, int off, int[] ns) {
	}

	public static void intToBigEndian(int n, byte[] bs, int off) {
	}

	public static void intToBigEndian(int[] ns, byte[] bs, int off) {
	}

	public static long bigEndianToLong(byte[] bs, int off) {
	}

	public static void longToBigEndian(long n, byte[] bs, int off) {
	}

	public static int littleEndianToInt(byte[] bs, int off) {
	}

	public static void littleEndianToInt(byte[] bs, int off, int[] ns) {
	}

	public static void intToLittleEndian(int n, byte[] bs, int off) {
	}

	public static void intToLittleEndian(int[] ns, byte[] bs, int off) {
	}

	public static long littleEndianToLong(byte[] bs, int off) {
	}

	public static void longToLittleEndian(long n, byte[] bs, int off) {
	}
}
