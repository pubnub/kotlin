/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  Generator for X.509 extensions
 *  @deprecated use org.bouncycastle.asn1.x509.ExtensionsGenerator
 */
public class X509ExtensionsGenerator {

	public X509ExtensionsGenerator() {
	}

	/**
	 *  Reset the generator
	 */
	public void reset() {
	}

	/**
	 *  @deprecated use ASN1ObjectIdentifier
	 */
	public void addExtension(org.bouncycastle.asn1.DERObjectIdentifier oid, boolean critical, org.bouncycastle.asn1.ASN1Encodable value) {
	}

	/**
	 *  @deprecated use ASN1ObjectIdentifier
	 */
	public void addExtension(org.bouncycastle.asn1.DERObjectIdentifier oid, boolean critical, byte[] value) {
	}

	/**
	 *  Add an extension with the given oid and the passed in value to be included
	 *  in the OCTET STRING associated with the extension.
	 * 
	 *  @param oid  OID for the extension.
	 *  @param critical  true if critical, false otherwise.
	 *  @param value the ASN.1 object to be included in the extension.
	 */
	public void addExtension(org.bouncycastle.asn1.ASN1ObjectIdentifier oid, boolean critical, org.bouncycastle.asn1.ASN1Encodable value) {
	}

	/**
	 *  Add an extension with the given oid and the passed in byte array to be wrapped in the
	 *  OCTET STRING associated with the extension.
	 * 
	 *  @param oid OID for the extension.
	 *  @param critical true if critical, false otherwise.
	 *  @param value the byte array to be wrapped.
	 */
	public void addExtension(org.bouncycastle.asn1.ASN1ObjectIdentifier oid, boolean critical, byte[] value) {
	}

	/**
	 *  Return true if there are no extension present in this generator.
	 * 
	 *  @return true if empty, false otherwise
	 */
	public boolean isEmpty() {
	}

	/**
	 *  Generate an X509Extensions object based on the current state of the generator.
	 * 
	 *  @return  an X09Extensions object.
	 */
	public X509Extensions generate() {
	}
}
