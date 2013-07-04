/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


public abstract class SRPTlsClient implements TlsClient {

	public static final Integer EXT_SRP;

	protected TlsCipherFactory cipherFactory;

	protected byte[] identity;

	protected byte[] password;

	protected TlsClientContext context;

	protected int selectedCompressionMethod;

	protected int selectedCipherSuite;

	public SRPTlsClient(byte[] identity, byte[] password) {
	}

	public SRPTlsClient(TlsCipherFactory cipherFactory, byte[] identity, byte[] password) {
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

	protected TlsKeyExchange createSRPKeyExchange(int keyExchange) {
	}
}
