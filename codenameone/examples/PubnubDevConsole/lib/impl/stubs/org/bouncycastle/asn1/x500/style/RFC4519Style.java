package org.bouncycastle.asn1.x500.style;


public class RFC4519Style implements org.bouncycastle.asn1.x500.X500NameStyle {

	public static final org.bouncycastle.asn1.x500.X500NameStyle INSTANCE;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier businessCategory;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier c;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier cn;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier dc;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier description;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier destinationIndicator;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier distinguishedName;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier dnQualifier;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier enhancedSearchGuide;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier facsimileTelephoneNumber;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier generationQualifier;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier givenName;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier houseIdentifier;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier initials;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier internationalISDNNumber;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier l;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier member;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier name;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier o;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier ou;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier owner;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier physicalDeliveryOfficeName;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier postalAddress;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier postalCode;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier postOfficeBox;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier preferredDeliveryMethod;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier registeredAddress;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier roleOccupant;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier searchGuide;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier seeAlso;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier serialNumber;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier sn;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier st;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier street;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier telephoneNumber;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier teletexTerminalIdentifier;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier telexNumber;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier title;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier uid;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier uniqueMember;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier userPassword;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier x121Address;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier x500UniqueIdentifier;

	protected RFC4519Style() {
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
