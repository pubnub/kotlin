/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  <pre>
 *      RDNSequence ::= SEQUENCE OF RelativeDistinguishedName
 * 
 *      RelativeDistinguishedName ::= SET SIZE (1..MAX) OF AttributeTypeAndValue
 * 
 *      AttributeTypeAndValue ::= SEQUENCE {
 *                                    type  OBJECT IDENTIFIER,
 *                                    value ANY }
 *  </pre>
 *  @deprecated use org.bouncycastle.asn1.x500.X500Name.
 */
public class X509Name extends org.bouncycastle.asn1.ASN1Object {

	/**
	 *  country code - StringType(SIZE(2))
	 *  @deprecated use a X500NameStyle
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier C;

	/**
	 *  organization - StringType(SIZE(1..64))
	 *  @deprecated use a X500NameStyle
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier O;

	/**
	 *  organizational unit name - StringType(SIZE(1..64))
	 *  @deprecated use a X500NameStyle
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier OU;

	/**
	 *  Title
	 *  @deprecated use a X500NameStyle
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier T;

	/**
	 *  common name - StringType(SIZE(1..64))
	 *  @deprecated use a X500NameStyle
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier CN;

	/**
	 *  device serial number name - StringType(SIZE(1..64))
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier SN;

	/**
	 *  street - StringType(SIZE(1..64))
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier STREET;

	/**
	 *  device serial number name - StringType(SIZE(1..64))
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier SERIALNUMBER;

	/**
	 *  locality name - StringType(SIZE(1..64))
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier L;

	/**
	 *  state, or province name - StringType(SIZE(1..64))
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier ST;

	/**
	 *  Naming attributes of type X520name
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier SURNAME;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier GIVENNAME;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier INITIALS;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier GENERATION;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier UNIQUE_IDENTIFIER;

	/**
	 *  businessCategory - DirectoryString(SIZE(1..128)
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier BUSINESS_CATEGORY;

	/**
	 *  postalCode - DirectoryString(SIZE(1..40)
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier POSTAL_CODE;

	/**
	 *  dnQualifier - DirectoryString(SIZE(1..64)
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier DN_QUALIFIER;

	/**
	 *  RFC 3039 Pseudonym - DirectoryString(SIZE(1..64)
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier PSEUDONYM;

	/**
	 *  RFC 3039 DateOfBirth - GeneralizedTime - YYYYMMDD000000Z
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier DATE_OF_BIRTH;

	/**
	 *  RFC 3039 PlaceOfBirth - DirectoryString(SIZE(1..128)
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier PLACE_OF_BIRTH;

	/**
	 *  RFC 3039 Gender - PrintableString (SIZE(1)) -- "M", "F", "m" or "f"
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier GENDER;

	/**
	 *  RFC 3039 CountryOfCitizenship - PrintableString (SIZE (2)) -- ISO 3166
	 *  codes only
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier COUNTRY_OF_CITIZENSHIP;

	/**
	 *  RFC 3039 CountryOfResidence - PrintableString (SIZE (2)) -- ISO 3166
	 *  codes only
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier COUNTRY_OF_RESIDENCE;

	/**
	 *  ISIS-MTT NameAtBirth - DirectoryString(SIZE(1..64)
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier NAME_AT_BIRTH;

	/**
	 *  RFC 3039 PostalAddress - SEQUENCE SIZE (1..6) OF
	 *  DirectoryString(SIZE(1..30))
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier POSTAL_ADDRESS;

	/**
	 *  RFC 2256 dmdName
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier DMD_NAME;

	/**
	 *  id-at-telephoneNumber
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier TELEPHONE_NUMBER;

	/**
	 *  id-at-name
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier NAME;

	/**
	 *  Email address (RSA PKCS#9 extension) - IA5String.
	 *  <p>Note: if you're trying to be ultra orthodox, don't use this! It shouldn't be in here.
	 *  @deprecated use a X500NameStyle
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier EmailAddress;

	/**
	 *  more from PKCS#9
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier UnstructuredName;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier UnstructuredAddress;

	/**
	 *  email address in Verisign certificates
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier E;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier DC;

	/**
	 *  LDAP User id.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier UID;

	/**
	 *  determines whether or not strings should be processed and printed
	 *  from back to front.
	 */
	public static boolean DefaultReverse;

	/**
	 *  default look up table translating OID values into their common symbols following
	 *  the convention in RFC 2253 with a few extras
	 */
	public static final java.util.Hashtable DefaultSymbols;

	/**
	 *  look up table translating OID values into their common symbols following the convention in RFC 2253
	 *  
	 */
	public static final java.util.Hashtable RFC2253Symbols;

	/**
	 *  look up table translating OID values into their common symbols following the convention in RFC 1779
	 *  
	 */
	public static final java.util.Hashtable RFC1779Symbols;

	/**
	 *  look up table translating common symbols into their OIDS.
	 */
	public static final java.util.Hashtable DefaultLookUp;

	/**
	 *  look up table translating OID values into their common symbols
	 *  @deprecated use DefaultSymbols
	 */
	public static final java.util.Hashtable OIDLookUp;

	/**
	 *  look up table translating string values into their OIDS -
	 *  @deprecated use DefaultLookUp
	 */
	public static final java.util.Hashtable SymbolLookUp;

	protected X509Name() {
	}

	/**
	 *  Constructor from ASN1Sequence
	 * 
	 *  the principal will be a list of constructed sets, each containing an (OID, String) pair.
	 */
	public X509Name(org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	/**
	 *  constructor from a table of attributes.
	 *  <p>
	 *  it's is assumed the table contains OID/String pairs, and the contents
	 *  of the table are copied into an internal table as part of the
	 *  construction process.
	 *  <p>
	 *  <b>Note:</b> if the name you are trying to generate should be
	 *  following a specific ordering, you should use the constructor
	 *  with the ordering specified below.
	 *  @deprecated use an ordered constructor! The hashtable ordering is rarely correct
	 */
	public X509Name(java.util.Hashtable attributes) {
	}

	/**
	 *  Constructor from a table of attributes with ordering.
	 *  <p>
	 *  it's is assumed the table contains OID/String pairs, and the contents
	 *  of the table are copied into an internal table as part of the
	 *  construction process. The ordering vector should contain the OIDs
	 *  in the order they are meant to be encoded or printed in toString.
	 */
	public X509Name(java.util.Vector ordering, java.util.Hashtable attributes) {
	}

	/**
	 *  Constructor from a table of attributes with ordering.
	 *  <p>
	 *  it's is assumed the table contains OID/String pairs, and the contents
	 *  of the table are copied into an internal table as part of the
	 *  construction process. The ordering vector should contain the OIDs
	 *  in the order they are meant to be encoded or printed in toString.
	 *  <p>
	 *  The passed in converter will be used to convert the strings into their
	 *  ASN.1 counterparts.
	 */
	public X509Name(java.util.Vector ordering, java.util.Hashtable attributes, X509NameEntryConverter converter) {
	}

	/**
	 *  Takes two vectors one of the oids and the other of the values.
	 */
	public X509Name(java.util.Vector oids, java.util.Vector values) {
	}

	/**
	 *  Takes two vectors one of the oids and the other of the values.
	 *  <p>
	 *  The passed in converter will be used to convert the strings into their
	 *  ASN.1 counterparts.
	 */
	public X509Name(java.util.Vector oids, java.util.Vector values, X509NameEntryConverter converter) {
	}

	/**
	 *  Takes an X509 dir name as a string of the format "C=AU, ST=Victoria", or
	 *  some such, converting it into an ordered set of name attributes.
	 */
	public X509Name(String dirName) {
	}

	/**
	 *  Takes an X509 dir name as a string of the format "C=AU, ST=Victoria", or
	 *  some such, converting it into an ordered set of name attributes with each
	 *  string value being converted to its associated ASN.1 type using the passed
	 *  in converter.
	 */
	public X509Name(String dirName, X509NameEntryConverter converter) {
	}

	/**
	 *  Takes an X509 dir name as a string of the format "C=AU, ST=Victoria", or
	 *  some such, converting it into an ordered set of name attributes. If reverse
	 *  is true, create the encoded version of the sequence starting from the
	 *  last element in the string.
	 */
	public X509Name(boolean reverse, String dirName) {
	}

	/**
	 *  Takes an X509 dir name as a string of the format "C=AU, ST=Victoria", or
	 *  some such, converting it into an ordered set of name attributes with each
	 *  string value being converted to its associated ASN.1 type using the passed
	 *  in converter. If reverse is true the ASN.1 sequence representing the DN will
	 *  be built by starting at the end of the string, rather than the start.
	 */
	public X509Name(boolean reverse, String dirName, X509NameEntryConverter converter) {
	}

	/**
	 *  Takes an X509 dir name as a string of the format "C=AU, ST=Victoria", or
	 *  some such, converting it into an ordered set of name attributes. lookUp
	 *  should provide a table of lookups, indexed by lowercase only strings and
	 *  yielding a ASN1ObjectIdentifier, other than that OID. and numeric oids
	 *  will be processed automatically.
	 *  <br>
	 *  If reverse is true, create the encoded version of the sequence
	 *  starting from the last element in the string.
	 *  @param reverse true if we should start scanning from the end (RFC 2553).
	 *  @param lookUp table of names and their oids.
	 *  @param dirName the X.500 string to be parsed.
	 */
	public X509Name(boolean reverse, java.util.Hashtable lookUp, String dirName) {
	}

	/**
	 *  Takes an X509 dir name as a string of the format "C=AU, ST=Victoria", or
	 *  some such, converting it into an ordered set of name attributes. lookUp
	 *  should provide a table of lookups, indexed by lowercase only strings and
	 *  yielding a ASN1ObjectIdentifier, other than that OID. and numeric oids
	 *  will be processed automatically. The passed in converter is used to convert the
	 *  string values to the right of each equals sign to their ASN.1 counterparts.
	 *  <br>
	 *  @param reverse true if we should start scanning from the end, false otherwise.
	 *  @param lookUp table of names and oids.
	 *  @param dirName the string dirName
	 *  @param converter the converter to convert string values into their ASN.1 equivalents
	 */
	public X509Name(boolean reverse, java.util.Hashtable lookUp, String dirName, X509NameEntryConverter converter) {
	}

	/**
	 *  Return a X509Name based on the passed in tagged object.
	 *  
	 *  @param obj tag object holding name.
	 *  @param explicit true if explicitly tagged false otherwise.
	 *  @return the X509Name
	 */
	public static X509Name getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public static X509Name getInstance(Object obj) {
	}

	/**
	 *  return a vector of the oids in the name, in the order they were found.
	 */
	public java.util.Vector getOIDs() {
	}

	/**
	 *  return a vector of the values found in the name, in the order they
	 *  were found.
	 */
	public java.util.Vector getValues() {
	}

	/**
	 *  return a vector of the values found in the name, in the order they
	 *  were found, with the DN label corresponding to passed in oid.
	 */
	public java.util.Vector getValues(org.bouncycastle.asn1.ASN1ObjectIdentifier oid) {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}

	/**
	 *  @param inOrder if true the order of both X509 names must be the same,
	 *  as well as the values associated with each element.
	 */
	public boolean equals(Object obj, boolean inOrder) {
	}

	public int hashCode() {
	}

	/**
	 *  test for equality - note: case is ignored.
	 */
	public boolean equals(Object obj) {
	}

	/**
	 *  convert the structure to a string - if reverse is true the
	 *  oids and values are listed out starting with the last element
	 *  in the sequence (ala RFC 2253), otherwise the string will begin
	 *  with the first element of the structure. If no string definition
	 *  for the oid is found in oidSymbols the string value of the oid is
	 *  added. Two standard symbol tables are provided DefaultSymbols, and
	 *  RFC2253Symbols as part of this class.
	 * 
	 *  @param reverse if true start at the end of the sequence and work back.
	 *  @param oidSymbols look up table strings for oids.
	 */
	public String toString(boolean reverse, java.util.Hashtable oidSymbols) {
	}

	public String toString() {
	}
}
