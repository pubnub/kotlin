/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


/**
 *  A temporary class to wrap old CertificateVerifyer stuff for new TlsAuthentication
 *  
 *  @deprecated
 */
public class LegacyTlsAuthentication implements TlsAuthentication {

	protected CertificateVerifyer verifyer;

	public LegacyTlsAuthentication(CertificateVerifyer verifyer) {
	}

	public void notifyServerCertificate(Certificate serverCertificate) {
	}

	public TlsCredentials getClientCredentials(CertificateRequest certificateRequest) {
	}
}
