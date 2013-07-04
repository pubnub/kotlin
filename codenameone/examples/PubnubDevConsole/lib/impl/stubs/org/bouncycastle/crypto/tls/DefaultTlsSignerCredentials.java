/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


public class DefaultTlsSignerCredentials implements TlsSignerCredentials {

	protected TlsClientContext context;

	protected Certificate clientCert;

	protected org.bouncycastle.crypto.params.AsymmetricKeyParameter clientPrivateKey;

	protected TlsSigner clientSigner;

	public DefaultTlsSignerCredentials(TlsClientContext context, Certificate clientCertificate, org.bouncycastle.crypto.params.AsymmetricKeyParameter clientPrivateKey) {
	}

	public Certificate getCertificate() {
	}

	public byte[] generateCertificateSignature(byte[] md5andsha1) {
	}
}
