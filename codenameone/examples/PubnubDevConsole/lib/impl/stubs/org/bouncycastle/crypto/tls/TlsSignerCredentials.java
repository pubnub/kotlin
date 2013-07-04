/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


public interface TlsSignerCredentials extends TlsCredentials {

	public byte[] generateCertificateSignature(byte[] md5andsha1);
}
