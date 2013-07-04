/**
 * 
 * Support classes useful for encoding and supporting the various RSA PKCS documents.
 */
package org.bouncycastle.asn1.pkcs;


public class EncryptedPrivateKeyInfo extends org.bouncycastle.asn1.ASN1Object {

	public EncryptedPrivateKeyInfo(org.bouncycastle.asn1.x509.AlgorithmIdentifier algId, byte[] encoding) {
	}

	public static EncryptedPrivateKeyInfo getInstance(Object obj) {
	}

	public org.bouncycastle.asn1.x509.AlgorithmIdentifier getEncryptionAlgorithm() {
	}

	public byte[] getEncryptedData() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  <pre>
	 *  EncryptedPrivateKeyInfo ::= SEQUENCE {
	 *       encryptionAlgorithm AlgorithmIdentifier {{KeyEncryptionAlgorithms}},
	 *       encryptedData EncryptedData
	 *  }
	 * 
	 *  EncryptedData ::= OCTET STRING
	 * 
	 *  KeyEncryptionAlgorithms ALGORITHM-IDENTIFIER ::= {
	 *           ... -- For local profiles
	 *  }
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
