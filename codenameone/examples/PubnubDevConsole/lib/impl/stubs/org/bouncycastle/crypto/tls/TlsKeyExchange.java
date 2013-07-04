/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


/**
 *  A generic interface for key exchange implementations in TLS 1.0.
 */
public interface TlsKeyExchange {

	public void skipServerCertificate();

	public void processServerCertificate(Certificate serverCertificate);

	public void skipServerKeyExchange();

	public void processServerKeyExchange(java.io.InputStream is);

	public void validateCertificateRequest(CertificateRequest certificateRequest);

	public void skipClientCredentials();

	public void processClientCredentials(TlsCredentials clientCredentials);

	public void generateClientKeyExchange(java.io.OutputStream os);

	public byte[] generatePremasterSecret();
}
