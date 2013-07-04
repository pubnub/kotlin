/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  The SubjectKeyIdentifier object.
 *  <pre>
 *  SubjectKeyIdentifier::= OCTET STRING
 *  </pre>
 */
public class SubjectKeyIdentifier extends org.bouncycastle.asn1.ASN1Object {

	public SubjectKeyIdentifier(byte[] keyid) {
	}

	protected SubjectKeyIdentifier(org.bouncycastle.asn1.ASN1OctetString keyid) {
	}

	/**
	 *  Calculates the keyidentifier using a SHA1 hash over the BIT STRING
	 *  from SubjectPublicKeyInfo as defined in RFC3280.
	 * 
	 *  @param spki the subject public key info.
	 *  @deprecated
	 */
	public SubjectKeyIdentifier(SubjectPublicKeyInfo spki) {
	}

	public static SubjectKeyIdentifier getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public static SubjectKeyIdentifier getInstance(Object obj) {
	}

	public byte[] getKeyIdentifier() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}

	/**
	 *  Return a RFC 3280 type 1 key identifier. As in:
	 *  <pre>
	 *  (1) The keyIdentifier is composed of the 160-bit SHA-1 hash of the
	 *  value of the BIT STRING subjectPublicKey (excluding the tag,
	 *  length, and number of unused bits).
	 *  </pre>
	 *  @param keyInfo the key info object containing the subjectPublicKey field.
	 *  @return the key identifier.
	 *  @deprecated use org.bouncycastle.cert.X509ExtensionUtils.createSubjectKeyIdentifier
	 */
	public static SubjectKeyIdentifier createSHA1KeyIdentifier(SubjectPublicKeyInfo keyInfo) {
	}

	/**
	 *  Return a RFC 3280 type 2 key identifier. As in:
	 *  <pre>
	 *  (2) The keyIdentifier is composed of a four bit type field with
	 *  the value 0100 followed by the least significant 60 bits of the
	 *  SHA-1 hash of the value of the BIT STRING subjectPublicKey.
	 *  </pre>
	 *  @param keyInfo the key info object containing the subjectPublicKey field.
	 *  @return the key identifier.
	 *  @deprecated use org.bouncycastle.cert.X509ExtensionUtils.createTruncatedSubjectKeyIdentifier
	 */
	public static SubjectKeyIdentifier createTruncatedSHA1KeyIdentifier(SubjectPublicKeyInfo keyInfo) {
	}
}
