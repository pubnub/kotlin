/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


public class DefaultTlsAgreementCredentials implements TlsAgreementCredentials {

	protected Certificate clientCert;

	protected org.bouncycastle.crypto.params.AsymmetricKeyParameter clientPrivateKey;

	protected org.bouncycastle.crypto.BasicAgreement basicAgreement;

	public DefaultTlsAgreementCredentials(Certificate clientCertificate, org.bouncycastle.crypto.params.AsymmetricKeyParameter clientPrivateKey) {
	}

	public Certificate getCertificate() {
	}

	public byte[] generateAgreement(org.bouncycastle.crypto.params.AsymmetricKeyParameter serverPublicKey) {
	}
}
