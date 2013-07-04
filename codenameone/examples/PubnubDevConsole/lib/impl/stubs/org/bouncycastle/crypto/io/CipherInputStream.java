/**
 * 
 * Classes for doing "enhanced" I/O with Digests and MACs.
 */
package org.bouncycastle.crypto.io;


/**
 *  A CipherInputStream is composed of an InputStream and a BufferedBlockCipher so
 *  that read() methods return data that are read in from the
 *  underlying InputStream but have been additionally processed by the
 *  Cipher.  The Cipher must be fully initialized before being used by
 *  a CipherInputStream.
 *  <p>
 *  For example, if the Cipher is initialized for decryption, the
 *  CipherInputStream will attempt to read in data and decrypt them,
 *  before returning the decrypted data.
 */
public class CipherInputStream extends javabc.FilterInputStream {

	/**
	 *  Constructs a CipherInputStream from an InputStream and a
	 *  BufferedBlockCipher.
	 */
	public CipherInputStream(java.io.InputStream is, org.bouncycastle.crypto.BufferedBlockCipher cipher) {
	}

	public CipherInputStream(java.io.InputStream is, org.bouncycastle.crypto.StreamCipher cipher) {
	}

	public int read() {
	}

	public int read(byte[] b) {
	}

	public int read(byte[] b, int off, int len) {
	}

	public long skip(long n) {
	}

	public int available() {
	}

	public void close() {
	}

	public boolean markSupported() {
	}
}
