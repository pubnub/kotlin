/**
 * 
 * Support classes useful for encoding and supporting the various RSA PKCS documents.
 */
package org.bouncycastle.asn1.pkcs;


/**
 *  a PKCS#7 signer info object.
 */
public class SignerInfo extends org.bouncycastle.asn1.ASN1Object {

	public SignerInfo(org.bouncycastle.asn1.ASN1Integer version, IssuerAndSerialNumber issuerAndSerialNumber, org.bouncycastle.asn1.x509.AlgorithmIdentifier digAlgorithm, org.bouncycastle.asn1.ASN1Set authenticatedAttributes, org.bouncycastle.asn1.x509.AlgorithmIdentifier digEncryptionAlgorithm, org.bouncycastle.asn1.ASN1OctetString encryptedDigest, org.bouncycastle.asn1.ASN1Set unauthenticatedAttributes) {
	}

	public SignerInfo(org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	public static SignerInfo getInstance(Object o) {
	}

	public org.bouncycastle.asn1.ASN1Integer getVersion() {
	}

	public IssuerAndSerialNumber getIssuerAndSerialNumber() {
	}

	public org.bouncycastle.asn1.ASN1Set getAuthenticatedAttributes() {
	}

	public org.bouncycastle.asn1.x509.AlgorithmIdentifier getDigestAlgorithm() {
	}

	public org.bouncycastle.asn1.ASN1OctetString getEncryptedDigest() {
	}

	public org.bouncycastle.asn1.x509.AlgorithmIdentifier getDigestEncryptionAlgorithm() {
	}

	public org.bouncycastle.asn1.ASN1Set getUnauthenticatedAttributes() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  <pre>
	 *   SignerInfo ::= SEQUENCE {
	 *       version Version,
	 *       issuerAndSerialNumber IssuerAndSerialNumber,
	 *       digestAlgorithm DigestAlgorithmIdentifier,
	 *       authenticatedAttributes [0] IMPLICIT Attributes OPTIONAL,
	 *       digestEncryptionAlgorithm DigestEncryptionAlgorithmIdentifier,
	 *       encryptedDigest EncryptedDigest,
	 *       unauthenticatedAttributes [1] IMPLICIT Attributes OPTIONAL
	 *   }
	 * 
	 *   EncryptedDigest ::= OCTET STRING
	 * 
	 *   DigestAlgorithmIdentifier ::= AlgorithmIdentifier
	 * 
	 *   DigestEncryptionAlgorithmIdentifier ::= AlgorithmIdentifier
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
