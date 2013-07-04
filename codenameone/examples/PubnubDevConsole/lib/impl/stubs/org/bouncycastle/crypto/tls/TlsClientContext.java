/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


public interface TlsClientContext {

	public javabc.SecureRandom getSecureRandom();

	public SecurityParameters getSecurityParameters();

	public ProtocolVersion getClientVersion();

	public ProtocolVersion getServerVersion();

	public Object getUserObject();

	public void setUserObject(Object userObject);
}
