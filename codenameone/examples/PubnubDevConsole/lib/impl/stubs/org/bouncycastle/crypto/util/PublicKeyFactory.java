/**
 * 
 * Some general utility/conversion classes.
 */
package org.bouncycastle.crypto.util;


/**
 *  Factory to create asymmetric public key parameters for asymmetric ciphers from range of
 *  ASN.1 encoded SubjectPublicKeyInfo objects.
 */
public class PublicKeyFactory {

	public PublicKeyFactory() {
	}

	/**
	 *  Create a public key from a SubjectPublicKeyInfo encoding
	 *  
	 *  @param keyInfoData the SubjectPublicKeyInfo encoding
	 *  @return the appropriate key parameter
	 *  @throws IOException on an error decoding the key
	 */
	public static org.bouncycastle.crypto.params.AsymmetricKeyParameter createKey(byte[] keyInfoData) {
	}

	/**
	 *  Create a public key from a SubjectPublicKeyInfo encoding read from a stream
	 *  
	 *  @param inStr the stream to read the SubjectPublicKeyInfo encoding from
	 *  @return the appropriate key parameter
	 *  @throws IOException on an error decoding the key
	 */
	public static org.bouncycastle.crypto.params.AsymmetricKeyParameter createKey(java.io.InputStream inStr) {
	}

	/**
	 *  Create a public key from the passed in SubjectPublicKeyInfo
	 *  
	 *  @param keyInfo the SubjectPublicKeyInfo containing the key data
	 *  @return the appropriate key parameter
	 *  @throws IOException on an error decoding the key
	 */
	public static org.bouncycastle.crypto.params.AsymmetricKeyParameter createKey(org.bouncycastle.asn1.x509.SubjectPublicKeyInfo keyInfo) {
	}
}
