/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


/**
 *  Some helper fuctions for MicroTLS.
 */
public class TlsUtils {

	public TlsUtils() {
	}

	protected static void writeUint8(short i, java.io.OutputStream os) {
	}

	protected static void writeUint8(short i, byte[] buf, int offset) {
	}

	protected static void writeUint16(int i, java.io.OutputStream os) {
	}

	protected static void writeUint16(int i, byte[] buf, int offset) {
	}

	protected static void writeUint24(int i, java.io.OutputStream os) {
	}

	protected static void writeUint24(int i, byte[] buf, int offset) {
	}

	protected static void writeUint32(long i, java.io.OutputStream os) {
	}

	protected static void writeUint32(long i, byte[] buf, int offset) {
	}

	protected static void writeUint64(long i, java.io.OutputStream os) {
	}

	protected static void writeUint64(long i, byte[] buf, int offset) {
	}

	protected static void writeOpaque8(byte[] buf, java.io.OutputStream os) {
	}

	protected static void writeOpaque16(byte[] buf, java.io.OutputStream os) {
	}

	protected static void writeOpaque24(byte[] buf, java.io.OutputStream os) {
	}

	protected static void writeUint8Array(short[] uints, java.io.OutputStream os) {
	}

	protected static void writeUint16Array(int[] uints, java.io.OutputStream os) {
	}

	protected static short readUint8(java.io.InputStream is) {
	}

	protected static int readUint16(java.io.InputStream is) {
	}

	protected static int readUint24(java.io.InputStream is) {
	}

	protected static long readUint32(java.io.InputStream is) {
	}

	protected static void readFully(byte[] buf, java.io.InputStream is) {
	}

	protected static byte[] readOpaque8(java.io.InputStream is) {
	}

	protected static byte[] readOpaque16(java.io.InputStream is) {
	}

	protected static void writeGMTUnixTime(byte[] buf, int offset) {
	}

	protected static byte[] PRF(byte[] secret, String asciiLabel, byte[] seed, int size) {
	}
}
