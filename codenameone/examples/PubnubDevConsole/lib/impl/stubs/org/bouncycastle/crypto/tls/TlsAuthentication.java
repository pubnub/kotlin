/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


public interface TlsAuthentication {

	/**
	 *  Called by the protocol handler to report the server certificate
	 *  Note: this method is responsible for certificate verification and validation
	 *  
	 *  @param serverCertificate the server certificate received
	 *  @throws IOException
	 */
	public void notifyServerCertificate(Certificate serverCertificate);

	/**
	 *  Return client credentials in response to server's certificate request
	 *  
	 *  @param certificateRequest details of the certificate request
	 *  @return a TlsCredentials object or null for no client authentication
	 *  @throws IOException
	 */
	public TlsCredentials getClientCredentials(CertificateRequest certificateRequest);
}
