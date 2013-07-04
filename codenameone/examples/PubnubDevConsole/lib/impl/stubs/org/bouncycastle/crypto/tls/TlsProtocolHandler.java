/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


/**
 *  An implementation of all high level protocols in TLS 1.0.
 */
public class TlsProtocolHandler {

	public TlsProtocolHandler(java.io.InputStream is, java.io.OutputStream os) {
	}

	public TlsProtocolHandler(java.io.InputStream is, java.io.OutputStream os, javabc.SecureRandom sr) {
	}

	protected void processData(short protocol, byte[] buf, int offset, int len) {
	}

	/**
	 *  Connects to the remote system.
	 *  
	 *  @param verifyer Will be used when a certificate is received to verify that this
	 *             certificate is accepted by the client.
	 *  @throws IOException If handshake was not successful.
	 *  
	 *  @deprecated use version taking TlsClient
	 */
	public void connect(CertificateVerifyer verifyer) {
	}

	/**
	 *  Connects to the remote system using client authentication
	 *  
	 *  @param tlsClient
	 *  @throws IOException If handshake was not successful.
	 */
	public void connect(TlsClient tlsClient) {
	}

	/**
	 *  Read data from the network. The method will return immediately, if there is still
	 *  some data left in the buffer, or block until some application data has been read
	 *  from the network.
	 *  
	 *  @param buf The buffer where the data will be copied to.
	 *  @param offset The position where the data will be placed in the buffer.
	 *  @param len The maximum number of bytes to read.
	 *  @return The number of bytes read.
	 *  @throws IOException If something goes wrong during reading data.
	 */
	protected int readApplicationData(byte[] buf, int offset, int len) {
	}

	/**
	 *  Send some application data to the remote system.
	 *  <p/>
	 *  The method will handle fragmentation internally.
	 *  
	 *  @param buf The buffer with the data.
	 *  @param offset The position in the buffer where the data is placed.
	 *  @param len The length of the data.
	 *  @throws IOException If something goes wrong during sending.
	 */
	protected void writeData(byte[] buf, int offset, int len) {
	}

	/**
	 *  @return An OutputStream which can be used to send data.
	 */
	public java.io.OutputStream getOutputStream() {
	}

	/**
	 *  @return An InputStream which can be used to read data.
	 */
	public java.io.InputStream getInputStream() {
	}

	/**
	 *  Closes this connection.
	 *  
	 *  @throws IOException If something goes wrong during closing.
	 */
	public void close() {
	}

	/**
	 *  Make sure the InputStream is now empty. Fail otherwise.
	 *  
	 *  @param is The InputStream to check.
	 *  @throws IOException If is is not empty.
	 */
	protected void assertEmpty(java.io.ByteArrayInputStream is) {
	}

	protected void flush() {
	}
}
