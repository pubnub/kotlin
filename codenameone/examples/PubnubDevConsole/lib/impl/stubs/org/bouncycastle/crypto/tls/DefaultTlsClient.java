/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


public abstract class DefaultTlsClient implements TlsClient {

	protected TlsCipherFactory cipherFactory;

	protected TlsClientContext context;

	protected int selectedCipherSuite;

	protected int selectedCompressionMethod;

	public DefaultTlsClient() {
	}

	public DefaultTlsClient(TlsCipherFactory cipherFactory) {
	}

	public void init(TlsClientContext context) {
	}

	public ProtocolVersion getClientVersion() {
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

	public TlsCompression getCompression() {
	}

	public TlsCipher getCipher() {
	}

	protected TlsKeyExchange createDHKeyExchange(int keyExchange) {
	}

	protected TlsKeyExchange createDHEKeyExchange(int keyExchange) {
	}

	protected TlsKeyExchange createECDHKeyExchange(int keyExchange) {
	}

	protected TlsKeyExchange createECDHEKeyExchange(int keyExchange) {
	}

	protected TlsKeyExchange createRSAKeyExchange() {
	}
}
