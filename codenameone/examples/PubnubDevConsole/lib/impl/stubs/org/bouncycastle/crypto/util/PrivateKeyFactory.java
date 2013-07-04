/**
 * 
 * Some general utility/conversion classes.
 */
package org.bouncycastle.crypto.util;


/**
 *  Factory for creating private key objects from PKCS8 PrivateKeyInfo objects.
 */
public class PrivateKeyFactory {

	public PrivateKeyFactory() {
	}

	/**
	 *  Create a private key parameter from a PKCS8 PrivateKeyInfo encoding.
	 *  
	 *  @param privateKeyInfoData the PrivateKeyInfo encoding
	 *  @return a suitable private key parameter
	 *  @throws IOException on an error decoding the key
	 */
	public static org.bouncycastle.crypto.params.AsymmetricKeyParameter createKey(byte[] privateKeyInfoData) {
	}

	/**
	 *  Create a private key parameter from a PKCS8 PrivateKeyInfo encoding read from a
	 *  stream.
	 *  
	 *  @param inStr the stream to read the PrivateKeyInfo encoding from
	 *  @return a suitable private key parameter
	 *  @throws IOException on an error decoding the key
	 */
	public static org.bouncycastle.crypto.params.AsymmetricKeyParameter createKey(java.io.InputStream inStr) {
	}

	/**
	 *  Create a private key parameter from the passed in PKCS8 PrivateKeyInfo object.
	 *  
	 *  @param keyInfo the PrivateKeyInfo object containing the key material
	 *  @return a suitable private key parameter
	 *  @throws IOException on an error decoding the key
	 */
	public static org.bouncycastle.crypto.params.AsymmetricKeyParameter createKey(org.bouncycastle.asn1.pkcs.PrivateKeyInfo keyInfo) {
	}
}
