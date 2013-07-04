/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


/**
 *  A NULL CipherSuite in java, this should only be used during handshake.
 */
public class TlsNullCipher implements TlsCipher {

	public TlsNullCipher() {
	}

	public byte[] encodePlaintext(short type, byte[] plaintext, int offset, int len) {
	}

	public byte[] decodeCiphertext(short type, byte[] ciphertext, int offset, int len) {
	}

	protected byte[] copyData(byte[] text, int offset, int len) {
	}
}
