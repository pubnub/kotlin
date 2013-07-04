/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


/**
 *  A generic TLS 1.0 / SSLv3 block cipher.
 *  This can be used for AES or 3DES for example.
 */
public class TlsBlockCipher implements TlsCipher {

	protected TlsClientContext context;

	protected org.bouncycastle.crypto.BlockCipher encryptCipher;

	protected org.bouncycastle.crypto.BlockCipher decryptCipher;

	protected TlsMac writeMac;

	protected TlsMac readMac;

	public TlsBlockCipher(TlsClientContext context, org.bouncycastle.crypto.BlockCipher encryptCipher, org.bouncycastle.crypto.BlockCipher decryptCipher, org.bouncycastle.crypto.Digest writeDigest, org.bouncycastle.crypto.Digest readDigest, int cipherKeySize) {
	}

	public TlsMac getWriteMac() {
	}

	public TlsMac getReadMac() {
	}

	protected void initCipher(boolean forEncryption, org.bouncycastle.crypto.BlockCipher cipher, byte[] key_block, int key_size, int key_offset, int iv_offset) {
	}

	public byte[] encodePlaintext(short type, byte[] plaintext, int offset, int len) {
	}

	public byte[] decodeCiphertext(short type, byte[] ciphertext, int offset, int len) {
	}

	protected int chooseExtraPadBlocks(javabc.SecureRandom r, int max) {
	}

	protected int lowestBitSet(int x) {
	}
}
