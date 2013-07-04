/**
 * 
 * Classes for doing "enhanced" I/O with Digests and MACs.
 */
package org.bouncycastle.crypto.io;


public class MacInputStream extends javabc.FilterInputStream {

	protected org.bouncycastle.crypto.Mac mac;

	public MacInputStream(java.io.InputStream stream, org.bouncycastle.crypto.Mac mac) {
	}

	public int read() {
	}

	public int read(byte[] b, int off, int len) {
	}

	public org.bouncycastle.crypto.Mac getMac() {
	}
}
