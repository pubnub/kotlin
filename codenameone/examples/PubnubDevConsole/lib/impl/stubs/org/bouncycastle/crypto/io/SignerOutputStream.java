/**
 * 
 * Classes for doing "enhanced" I/O with Digests and MACs.
 */
package org.bouncycastle.crypto.io;


public class SignerOutputStream extends java.io.OutputStream {

	protected org.bouncycastle.crypto.Signer signer;

	public SignerOutputStream(org.bouncycastle.crypto.Signer Signer) {
	}

	public void write(int b) {
	}

	public void write(byte[] b, int off, int len) {
	}

	public org.bouncycastle.crypto.Signer getSigner() {
	}
}
