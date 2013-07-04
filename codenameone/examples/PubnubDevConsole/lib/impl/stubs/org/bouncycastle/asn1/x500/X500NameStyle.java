package org.bouncycastle.asn1.x500;


/**
 *  It turns out that the number of standard ways the fields in a DN should be 
 *  encoded into their ASN.1 counterparts is rapidly approaching the
 *  number of machines on the internet. By default the X500Name class
 *  will produce UTF8Strings in line with the current recommendations (RFC 3280).
 *  <p>
 */
public interface X500NameStyle {

	/**
	 *  Convert the passed in String value into the appropriate ASN.1
	 *  encoded object.
	 *  
	 *  @param oid the oid associated with the value in the DN.
	 *  @param value the value of the particular DN component.
	 *  @return the ASN.1 equivalent for the value.
	 */
	public org.bouncycastle.asn1.ASN1Encodable stringToValue(org.bouncycastle.asn1.ASN1ObjectIdentifier oid, String value);

	public org.bouncycastle.asn1.ASN1ObjectIdentifier attrNameToOID(String attrName);

	public boolean areEqual(X500Name name1, X500Name name2);

	public RDN[] fromString(String dirName);

	public int calculateHashCode(X500Name name);

	public String toString(X500Name name);
}
