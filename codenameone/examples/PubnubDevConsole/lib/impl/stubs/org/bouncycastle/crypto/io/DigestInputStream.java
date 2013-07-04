/**
 * 
 * Classes for doing "enhanced" I/O with Digests and MACs.
 */
package org.bouncycastle.crypto.io;


public class DigestInputStream extends javabc.FilterInputStream {

	protected org.bouncycastle.crypto.Digest digest;

	public DigestInputStream(java.io.InputStream stream, org.bouncycastle.crypto.Digest digest) {
	}

	public int read() {
	}

	public int read(byte[] b, int off, int len) {
	}

	public org.bouncycastle.crypto.Digest getDigest() {
	}
}
