/**
 * 
 * Support classes useful for encoding and supporting the various RSA PKCS documents.
 */
package org.bouncycastle.asn1.pkcs;


/**
 *  The EncryptedData object.
 *  <pre>
 *       EncryptedData ::= SEQUENCE {
 *            version Version,
 *            encryptedContentInfo EncryptedContentInfo
 *       }
 * 
 * 
 *       EncryptedContentInfo ::= SEQUENCE {
 *           contentType ContentType,
 *           contentEncryptionAlgorithm  ContentEncryptionAlgorithmIdentifier,
 *           encryptedContent [0] IMPLICIT EncryptedContent OPTIONAL
 *     }
 * 
 *     EncryptedContent ::= OCTET STRING
 *  </pre>
 */
public class EncryptedData extends org.bouncycastle.asn1.ASN1Object {

	public EncryptedData(org.bouncycastle.asn1.ASN1ObjectIdentifier contentType, org.bouncycastle.asn1.x509.AlgorithmIdentifier encryptionAlgorithm, org.bouncycastle.asn1.ASN1Encodable content) {
	}

	public static EncryptedData getInstance(Object obj) {
	}

	public org.bouncycastle.asn1.ASN1ObjectIdentifier getContentType() {
	}

	public org.bouncycastle.asn1.x509.AlgorithmIdentifier getEncryptionAlgorithm() {
	}

	public org.bouncycastle.asn1.ASN1OctetString getContent() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
