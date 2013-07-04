/**
 * 
 * Classes for doing "enhanced" I/O with Digests and MACs.
 */
package org.bouncycastle.crypto.io;


public class CipherOutputStream extends javabc.FilterOutputStream {

	/**
	 *  Constructs a CipherOutputStream from an OutputStream and a
	 *  BufferedBlockCipher.
	 */
	public CipherOutputStream(java.io.OutputStream os, org.bouncycastle.crypto.BufferedBlockCipher cipher) {
	}

	/**
	 *  Constructs a CipherOutputStream from an OutputStream and a
	 *  BufferedBlockCipher.
	 */
	public CipherOutputStream(java.io.OutputStream os, org.bouncycastle.crypto.StreamCipher cipher) {
	}

	/**
	 *  Writes the specified byte to this output stream.
	 * 
	 *  @param b the <code>byte</code>.
	 *  @exception java.io.IOException if an I/O error occurs.
	 */
	public void write(int b) {
	}

	/**
	 *  Writes <code>b.length</code> bytes from the specified byte array
	 *  to this output stream.
	 *  <p>
	 *  The <code>write</code> method of
	 *  <code>CipherOutputStream</code> calls the <code>write</code>
	 *  method of three arguments with the three arguments
	 *  <code>b</code>, <code>0</code>, and <code>b.length</code>.
	 * 
	 *  @param b the data.
	 *  @exception java.io.IOException if an I/O error occurs.
	 *  @see #write(byte[], int, int)
	 */
	public void write(byte[] b) {
	}

	/**
	 *  Writes <code>len</code> bytes from the specified byte array
	 *  starting at offset <code>off</code> to this output stream.
	 * 
	 *  @param b the data.
	 *  @param off the start offset in the data.
	 *  @param len the number of bytes to write.
	 *  @exception java.io.IOException if an I/O error occurs.
	 */
	public void write(byte[] b, int off, int len) {
	}

	/**
	 *  Flushes this output stream by forcing any buffered output bytes
	 *  that have already been processed by the encapsulated cipher object
	 *  to be written out.
	 * 
	 *  <p>
	 *  Any bytes buffered by the encapsulated cipher
	 *  and waiting to be processed by it will not be written out. For example,
	 *  if the encapsulated cipher is a block cipher, and the total number of
	 *  bytes written using one of the <code>write</code> methods is less than
	 *  the cipher's block size, no bytes will be written out.
	 * 
	 *  @exception java.io.IOException if an I/O error occurs.
	 */
	public void flush() {
	}

	/**
	 *  Closes this output stream and releases any system resources
	 *  associated with this stream.
	 *  <p>
	 *  This method invokes the <code>doFinal</code> method of the encapsulated
	 *  cipher object, which causes any bytes buffered by the encapsulated
	 *  cipher to be processed. The result is written out by calling the
	 *  <code>flush</code> method of this output stream.
	 *  <p>
	 *  This method resets the encapsulated cipher object to its initial state
	 *  and calls the <code>close</code> method of the underlying output
	 *  stream.
	 * 
	 *  @exception java.io.IOException if an I/O error occurs.
	 */
	public void close() {
	}
}
