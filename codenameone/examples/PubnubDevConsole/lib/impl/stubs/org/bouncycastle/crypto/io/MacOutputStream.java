/**
 * 
 * Classes for doing "enhanced" I/O with Digests and MACs.
 */
package org.bouncycastle.crypto.io;


public class MacOutputStream extends java.io.OutputStream {

	protected org.bouncycastle.crypto.Mac mac;

	public MacOutputStream(org.bouncycastle.crypto.Mac mac) {
	}

	public void write(int b) {
	}

	public void write(byte[] b, int off, int len) {
	}

	public byte[] getMac() {
	}
}
