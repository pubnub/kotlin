/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


public class Extensions extends org.bouncycastle.asn1.ASN1Object {

	/**
	 *  Base Constructor
	 *  
	 *  @param extensions an array of extensions.
	 */
	public Extensions(Extension[] extensions) {
	}

	public static Extensions getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public static Extensions getInstance(Object obj) {
	}

	/**
	 *  return an Enumeration of the extension field's object ids.
	 */
	public java.util.Enumeration oids() {
	}

	/**
	 *  return the extension represented by the object identifier
	 *  passed in.
	 * 
	 *  @return the extension if it's present, null otherwise.
	 */
	public Extension getExtension(org.bouncycastle.asn1.ASN1ObjectIdentifier oid) {
	}

	/**
	 *  <pre>
	 *      Extensions        ::=   SEQUENCE SIZE (1..MAX) OF Extension
	 * 
	 *      Extension         ::=   SEQUENCE {
	 *         extnId            EXTENSION.&amp;id ({ExtensionSet}),
	 *         critical          BOOLEAN DEFAULT FALSE,
	 *         extnValue         OCTET STRING }
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}

	public boolean equivalent(Extensions other) {
	}

	public org.bouncycastle.asn1.ASN1ObjectIdentifier[] getExtensionOIDs() {
	}

	public org.bouncycastle.asn1.ASN1ObjectIdentifier[] getNonCriticalExtensionOIDs() {
	}

	public org.bouncycastle.asn1.ASN1ObjectIdentifier[] getCriticalExtensionOIDs() {
	}
}
