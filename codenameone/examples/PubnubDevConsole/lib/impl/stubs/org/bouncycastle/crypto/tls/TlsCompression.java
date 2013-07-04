/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


public interface TlsCompression {

	public java.io.OutputStream compress(java.io.OutputStream output);

	public java.io.OutputStream decompress(java.io.OutputStream output);
}
