/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


public class DefaultTlsCipherFactory implements TlsCipherFactory {

	public DefaultTlsCipherFactory() {
	}

	public TlsCipher createCipher(TlsClientContext context, int encryptionAlgorithm, int digestAlgorithm) {
	}

	protected TlsCipher createAESCipher(TlsClientContext context, int cipherKeySize, int digestAlgorithm) {
	}

	protected TlsCipher createDESedeCipher(TlsClientContext context, int cipherKeySize, int digestAlgorithm) {
	}

	protected org.bouncycastle.crypto.BlockCipher createAESBlockCipher() {
	}

	protected org.bouncycastle.crypto.BlockCipher createDESedeBlockCipher() {
	}

	protected org.bouncycastle.crypto.Digest createDigest(int digestAlgorithm) {
	}
}
