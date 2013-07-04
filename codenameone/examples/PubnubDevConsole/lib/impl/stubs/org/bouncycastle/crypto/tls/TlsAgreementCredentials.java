/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


public interface TlsAgreementCredentials extends TlsCredentials {

	public byte[] generateAgreement(org.bouncycastle.crypto.params.AsymmetricKeyParameter serverPublicKey);
}
