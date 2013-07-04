/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


/**
 *  A queue for bytes. This file could be more optimized.
 */
public class ByteQueue {

	public ByteQueue() {
	}

	/**
	 *  @return The smallest number which can be written as 2^x which is bigger than i.
	 */
	public static final int nextTwoPow(int i) {
	}

	/**
	 *  Read data from the buffer.
	 *  
	 *  @param buf The buffer where the read data will be copied to.
	 *  @param offset How many bytes to skip at the beginning of buf.
	 *  @param len How many bytes to read at all.
	 *  @param skip How many bytes from our data to skip.
	 */
	public void read(byte[] buf, int offset, int len, int skip) {
	}

	/**
	 *  Add some data to our buffer.
	 *  
	 *  @param data A byte-array to read data from.
	 *  @param offset How many bytes to skip at the beginning of the array.
	 *  @param len How many bytes to read from the array.
	 */
	public void addData(byte[] data, int offset, int len) {
	}

	/**
	 *  Remove some bytes from our data from the beginning.
	 *  
	 *  @param i How many bytes to remove.
	 */
	public void removeData(int i) {
	}

	/**
	 *  @return The number of bytes which are available in this buffer.
	 */
	public int size() {
	}
}
