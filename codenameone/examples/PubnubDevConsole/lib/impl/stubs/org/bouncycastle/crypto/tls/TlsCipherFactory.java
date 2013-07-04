/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


public interface TlsCipherFactory {

	/**
	 *  See enumeration classes EncryptionAlgorithm and DigestAlgorithm for appropriate argument values
	 */
	public TlsCipher createCipher(TlsClientContext context, int encryptionAlgorithm, int digestAlgorithm);
}
