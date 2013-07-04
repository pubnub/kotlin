/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


public interface TlsClient {

	public void init(TlsClientContext context);

	public ProtocolVersion getClientVersion();

	public int[] getCipherSuites();

	public short[] getCompressionMethods();

	public java.util.Hashtable getClientExtensions();

	public void notifyServerVersion(ProtocolVersion selectedVersion);

	public void notifySessionID(byte[] sessionID);

	public void notifySelectedCipherSuite(int selectedCipherSuite);

	public void notifySelectedCompressionMethod(short selectedCompressionMethod);

	public void notifySecureRenegotiation(boolean secureNegotiation);

	public void processServerExtensions(java.util.Hashtable serverExtensions);

	public TlsKeyExchange getKeyExchange();

	public TlsAuthentication getAuthentication();

	public TlsCompression getCompression();

	public TlsCipher getCipher();
}
