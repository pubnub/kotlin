/**
 * 
 * Basic cipher classes.
 */
package org.bouncycastle.crypto.engines;


/**
 *  an implementation of the RFC 3211 Key Wrap
 *  Specification.
 */
public class RFC3211WrapEngine implements org.bouncycastle.crypto.Wrapper {

	public RFC3211WrapEngine(org.bouncycastle.crypto.BlockCipher engine) {
	}

	public void init(boolean forWrapping, org.bouncycastle.crypto.CipherParameters param) {
	}

	public String getAlgorithmName() {
	}

	public byte[] wrap(byte[] in, int inOff, int inLen) {
	}

	public byte[] unwrap(byte[] in, int inOff, int inLen) {
	}
}
