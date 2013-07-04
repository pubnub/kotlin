/**
 * 
 * Classes for doing "enhanced" I/O with Digests and MACs.
 */
package org.bouncycastle.crypto.io;


public class SignerInputStream extends javabc.FilterInputStream {

	protected org.bouncycastle.crypto.Signer signer;

	public SignerInputStream(java.io.InputStream stream, org.bouncycastle.crypto.Signer signer) {
	}

	public int read() {
	}

	public int read(byte[] b, int off, int len) {
	}

	public org.bouncycastle.crypto.Signer getSigner() {
	}
}
