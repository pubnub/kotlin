/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


/**
 *  A representation for a certificate chain as used by a tls server.
 */
public class Certificate {

	public static final Certificate EMPTY_CHAIN;

	/**
	 *  The certificates.
	 */
	protected org.bouncycastle.asn1.x509.X509CertificateStructure[] certs;

	/**
	 *  Private constructor from a cert array.
	 *  
	 *  @param certs The certs the chain should contain.
	 */
	public Certificate(org.bouncycastle.asn1.x509.X509CertificateStructure[] certs) {
	}

	/**
	 *  Parse the ServerCertificate message.
	 *  
	 *  @param is The stream where to parse from.
	 *  @return A Certificate object with the certs, the server has sended.
	 *  @throws IOException If something goes wrong during parsing.
	 */
	protected static Certificate parse(java.io.InputStream is) {
	}

	/**
	 *  Encodes version of the ClientCertificate message
	 *  
	 *  @param os stream to write the message to
	 *  @throws IOException If something goes wrong
	 */
	protected void encode(java.io.OutputStream os) {
	}

	/**
	 *  @return An array which contains the certs, this chain contains.
	 */
	public org.bouncycastle.asn1.x509.X509CertificateStructure[] getCerts() {
	}

	public boolean isEmpty() {
	}
}
