/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  The AuthorityKeyIdentifier object.
 *  <pre>
 *  id-ce-authorityKeyIdentifier OBJECT IDENTIFIER ::=  { id-ce 35 }
 * 
 *    AuthorityKeyIdentifier ::= SEQUENCE {
 *       keyIdentifier             [0] IMPLICIT KeyIdentifier           OPTIONAL,
 *       authorityCertIssuer       [1] IMPLICIT GeneralNames            OPTIONAL,
 *       authorityCertSerialNumber [2] IMPLICIT CertificateSerialNumber OPTIONAL  }
 * 
 *    KeyIdentifier ::= OCTET STRING
 *  </pre>
 */
public class AuthorityKeyIdentifier extends org.bouncycastle.asn1.ASN1Object {

	protected AuthorityKeyIdentifier(org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	/**
	 * 
	 *  Calulates the keyidentifier using a SHA1 hash over the BIT STRING
	 *  from SubjectPublicKeyInfo as defined in RFC2459.
	 * 
	 *  Example of making a AuthorityKeyIdentifier:
	 *  <pre>
	 *    SubjectPublicKeyInfo apki = new SubjectPublicKeyInfo((ASN1Sequence)new ASN1InputStream(
	 *        publicKey.getEncoded()).readObject());
	 *    AuthorityKeyIdentifier aki = new AuthorityKeyIdentifier(apki);
	 *  </pre>
	 */
	public AuthorityKeyIdentifier(SubjectPublicKeyInfo spki) {
	}

	/**
	 *  create an AuthorityKeyIdentifier with the GeneralNames tag and
	 *  the serial number provided as well.
	 */
	public AuthorityKeyIdentifier(SubjectPublicKeyInfo spki, GeneralNames name, javabc.BigInteger serialNumber) {
	}

	/**
	 *  create an AuthorityKeyIdentifier with the GeneralNames tag and
	 *  the serial number provided.
	 */
	public AuthorityKeyIdentifier(GeneralNames name, javabc.BigInteger serialNumber) {
	}

	/**
	 *  create an AuthorityKeyIdentifier with a precomupted key identifier
	 */
	public AuthorityKeyIdentifier(byte[] keyIdentifier) {
	}

	/**
	 *  create an AuthorityKeyIdentifier with a precomupted key identifier
	 *  and the GeneralNames tag and the serial number provided as well.
	 */
	public AuthorityKeyIdentifier(byte[] keyIdentifier, GeneralNames name, javabc.BigInteger serialNumber) {
	}

	public static AuthorityKeyIdentifier getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public static AuthorityKeyIdentifier getInstance(Object obj) {
	}

	public byte[] getKeyIdentifier() {
	}

	public GeneralNames getAuthorityCertIssuer() {
	}

	public javabc.BigInteger getAuthorityCertSerialNumber() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}

	public String toString() {
	}
}
