/**
 * 
 * Basic cipher classes.
 */
package org.bouncycastle.crypto.engines;


/**
 *  an implementation of the AES Key Wrapper from the NIST Key Wrap
 *  Specification as described in RFC 3394.
 *  <p>
 *  For further details see: <a href="http://www.ietf.org/rfc/rfc3394.txt">http://www.ietf.org/rfc/rfc3394.txt</a>
 *  and  <a href="http://csrc.nist.gov/encryption/kms/key-wrap.pdf">http://csrc.nist.gov/encryption/kms/key-wrap.pdf</a>.
 */
public class RFC3394WrapEngine implements org.bouncycastle.crypto.Wrapper {

	public RFC3394WrapEngine(org.bouncycastle.crypto.BlockCipher engine) {
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
