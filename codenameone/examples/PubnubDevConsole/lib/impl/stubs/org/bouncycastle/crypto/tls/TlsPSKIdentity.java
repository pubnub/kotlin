/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


public interface TlsPSKIdentity {

	public void skipIdentityHint();

	public void notifyIdentityHint(byte[] psk_identity_hint);

	public byte[] getPSKIdentity();

	public byte[] getPSK();
}
