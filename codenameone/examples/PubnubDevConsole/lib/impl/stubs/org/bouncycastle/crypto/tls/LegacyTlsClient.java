/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


/**
 *  A temporary class to use LegacyTlsAuthentication
 *  
 *  @deprecated
 */
public class LegacyTlsClient extends DefaultTlsClient {

	/**
	 * @deprecated 
	 */
	protected CertificateVerifyer verifyer;

	/**
	 *  @deprecated
	 */
	public LegacyTlsClient(CertificateVerifyer verifyer) {
	}

	public TlsAuthentication getAuthentication() {
	}
}
