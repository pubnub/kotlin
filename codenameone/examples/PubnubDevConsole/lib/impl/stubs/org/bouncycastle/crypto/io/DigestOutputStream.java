/**
 * 
 * Classes for doing "enhanced" I/O with Digests and MACs.
 */
package org.bouncycastle.crypto.io;


public class DigestOutputStream extends java.io.OutputStream {

	protected org.bouncycastle.crypto.Digest digest;

	public DigestOutputStream(org.bouncycastle.crypto.Digest Digest) {
	}

	public void write(int b) {
	}

	public void write(byte[] b, int off, int len) {
	}

	public byte[] getDigest() {
	}
}
