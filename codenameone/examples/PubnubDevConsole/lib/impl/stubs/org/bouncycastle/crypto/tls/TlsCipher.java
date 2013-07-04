/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


public interface TlsCipher {

	public byte[] encodePlaintext(short type, byte[] plaintext, int offset, int len);

	public byte[] decodeCiphertext(short type, byte[] ciphertext, int offset, int len);
}
