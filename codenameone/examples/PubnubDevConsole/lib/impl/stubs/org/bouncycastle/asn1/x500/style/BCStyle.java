package org.bouncycastle.asn1.x500.style;


public class BCStyle implements org.bouncycastle.asn1.x500.X500NameStyle {

	public static final org.bouncycastle.asn1.x500.X500NameStyle INSTANCE;

	/**
	 *  country code - StringType(SIZE(2))
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier C;

	/**
	 *  organization - StringType(SIZE(1..64))
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier O;

	/**
	 *  organizational unit name - StringType(SIZE(1..64))
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier OU;

	/**
	 *  Title
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier T;

	/**
	 *  common name - StringType(SIZE(1..64))
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

	protected BCStyle() {
	}

	public org.bouncycastle.asn1.ASN1Encodable stringToValue(org.bouncycastle.asn1.ASN1ObjectIdentifier oid, String value) {
	}

	public org.bouncycastle.asn1.ASN1ObjectIdentifier attrNameToOID(String attrName) {
	}

	public boolean areEqual(org.bouncycastle.asn1.x500.X500Name name1, org.bouncycastle.asn1.x500.X500Name name2) {
	}

	protected boolean rdnAreEqual(org.bouncycastle.asn1.x500.RDN rdn1, org.bouncycastle.asn1.x500.RDN rdn2) {
	}

	public org.bouncycastle.asn1.x500.RDN[] fromString(String dirName) {
	}

	public int calculateHashCode(org.bouncycastle.asn1.x500.X500Name name) {
	}

	public String toString(org.bouncycastle.asn1.x500.X500Name name) {
	}
}
