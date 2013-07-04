/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  The default converter for X509 DN entries when going from their
 *  string value to ASN.1 strings.
 */
public class X509DefaultEntryConverter extends X509NameEntryConverter {

	public X509DefaultEntryConverter() {
	}

	/**
	 *  Apply default coversion for the given value depending on the oid
	 *  and the character range of the value.
	 *  
	 *  @param oid the object identifier for the DN entry
	 *  @param value the value associated with it
	 *  @return the ASN.1 equivalent for the string value.
	 */
	public org.bouncycastle.asn1.ASN1Primitive getConvertedValue(org.bouncycastle.asn1.ASN1ObjectIdentifier oid, String value) {
	}
}
