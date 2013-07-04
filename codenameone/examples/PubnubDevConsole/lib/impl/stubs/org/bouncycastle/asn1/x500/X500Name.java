package org.bouncycastle.asn1.x500;


/**
 *  <pre>
 *      Name ::= CHOICE {
 *                        RDNSequence }
 * 
 *      RDNSequence ::= SEQUENCE OF RelativeDistinguishedName
 * 
 *      RelativeDistinguishedName ::= SET SIZE (1..MAX) OF AttributeTypeAndValue
 * 
 *      AttributeTypeAndValue ::= SEQUENCE {
 *                                    type  OBJECT IDENTIFIER,
 *                                    value ANY }
 *  </pre>
 */
public class X500Name extends org.bouncycastle.asn1.ASN1Object implements org.bouncycastle.asn1.ASN1Choice {

	public X500Name(X500NameStyle style, X500Name name) {
	}

	public X500Name(RDN[] rDNs) {
	}

	public X500Name(X500NameStyle style, RDN[] rDNs) {
	}

	public X500Name(String dirName) {
	}

	public X500Name(X500NameStyle style, String dirName) {
	}

	/**
	 *  Return a X500Name based on the passed in tagged object.
	 *  
	 *  @param obj tag object holding name.
	 *  @param explicit true if explicitly tagged false otherwise.
	 *  @return the X500Name
	 */
	public static X500Name getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public static X500Name getInstance(Object obj) {
	}

	public static X500Name getInstance(X500NameStyle style, Object obj) {
	}

	/**
	 *  return an array of RDNs in structure order.
	 * 
	 *  @return an array of RDN objects.
	 */
	public RDN[] getRDNs() {
	}

	/**
	 *  return an array of OIDs contained in the attribute type of each RDN in structure order.
	 * 
	 *  @return an array, possibly zero length, of ASN1ObjectIdentifiers objects.
	 */
	public org.bouncycastle.asn1.ASN1ObjectIdentifier[] getAttributeTypes() {
	}

	/**
	 *  return an array of RDNs containing the attribute type given by OID in structure order.
	 * 
	 *  @param attributeType the type OID we are looking for.
	 *  @return an array, possibly zero length, of RDN objects.
	 */
	public RDN[] getRDNs(org.bouncycastle.asn1.ASN1ObjectIdentifier attributeType) {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}

	public int hashCode() {
	}

	/**
	 *  test for equality - note: case is ignored.
	 */
	public boolean equals(Object obj) {
	}

	public String toString() {
	}

	/**
	 *  Set the default style for X500Name construction.
	 * 
	 *  @param style  an X500NameStyle
	 */
	public static void setDefaultStyle(X500NameStyle style) {
	}

	/**
	 *  Return the current default style.
	 * 
	 *  @return default style for X500Name construction.
	 */
	public static X500NameStyle getDefaultStyle() {
	}
}
