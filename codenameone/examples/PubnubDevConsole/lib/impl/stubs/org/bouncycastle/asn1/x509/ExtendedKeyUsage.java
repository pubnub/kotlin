/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  The extendedKeyUsage object.
 *  <pre>
 *       extendedKeyUsage ::= SEQUENCE SIZE (1..MAX) OF KeyPurposeId
 *  </pre>
 */
public class ExtendedKeyUsage extends org.bouncycastle.asn1.ASN1Object {

	public ExtendedKeyUsage(KeyPurposeId usage) {
	}

	public ExtendedKeyUsage(org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	public ExtendedKeyUsage(java.util.Vector usages) {
	}

	public static ExtendedKeyUsage getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public static ExtendedKeyUsage getInstance(Object obj) {
	}

	public boolean hasKeyPurposeId(KeyPurposeId keyPurposeId) {
	}

	/**
	 *  Returns all extended key usages.
	 *  The returned vector contains ASN1ObjectIdentifiers.
	 *  @return A vector with all key purposes.
	 */
	public java.util.Vector getUsages() {
	}

	public int size() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
