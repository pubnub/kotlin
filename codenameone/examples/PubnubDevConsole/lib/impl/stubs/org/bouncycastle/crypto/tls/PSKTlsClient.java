/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


public class PSKTlsClient implements TlsClient {

	protected TlsCipherFactory cipherFactory;

	protected TlsPSKIdentity pskIdentity;

	protected TlsClientContext context;

	protected int selectedCompressionMethod;

	protected int selectedCipherSuite;

	public PSKTlsClient(TlsPSKIdentity pskIdentity) {
	}

	public PSKTlsClient(TlsCipherFactory cipherFactory, TlsPSKIdentity pskIdentity) {
	}

	public ProtocolVersion getClientVersion() {
	}

	public void init(TlsClientContext context) {
	}

	public int[] getCipherSuites() {
	}

	public java.util.Hashtable getClientExtensions() {
	}

	public short[] getCompressionMethods() {
	}

	public void notifyServerVersion(ProtocolVersion serverVersion) {
	}

	public void notifySessionID(byte[] sessionID) {
	}

	public void notifySelectedCipherSuite(int selectedCipherSuite) {
	}

	public void notifySelectedCompressionMethod(short selectedCompressionMethod) {
	}

	public void notifySecureRenegotiation(boolean secureRenegotiation) {
	}

	public void processServerExtensions(java.util.Hashtable serverExtensions) {
	}

	public TlsKeyExchange getKeyExchange() {
	}

	public TlsAuthentication getAuthentication() {
	}

	public TlsCompression getCompression() {
	}

	public TlsCipher getCipher() {
	}

	protected TlsKeyExchange createPSKKeyExchange(int keyExchange) {
	}
}
