package org.bouncycastle.asn1.x509.sigi;


/**
 *  Contains personal data for the otherName field in the subjectAltNames
 *  extension.
 *  <p/>
 *  <pre>
 *      PersonalData ::= SEQUENCE {
 *        nameOrPseudonym NameOrPseudonym,
 *        nameDistinguisher [0] INTEGER OPTIONAL,
 *        dateOfBirth [1] GeneralizedTime OPTIONAL,
 *        placeOfBirth [2] DirectoryString OPTIONAL,
 *        gender [3] PrintableString OPTIONAL,
 *        postalAddress [4] DirectoryString OPTIONAL
 *        }
 *  </pre>
 * 
 *  @see org.bouncycastle.asn1.x509.sigi.NameOrPseudonym
 *  @see org.bouncycastle.asn1.x509.sigi.SigIObjectIdentifiers
 */
public class PersonalData extends org.bouncycastle.asn1.ASN1Object {

	/**
	 *  Constructor from a given details.
	 * 
	 *  @param nameOrPseudonym   Name or pseudonym.
	 *  @param nameDistinguisher Name distinguisher.
	 *  @param dateOfBirth       Date of birth.
	 *  @param placeOfBirth      Place of birth.
	 *  @param gender            Gender.
	 *  @param postalAddress     Postal Address.
	 */
	public PersonalData(NameOrPseudonym nameOrPseudonym, javabc.BigInteger nameDistinguisher, org.bouncycastle.asn1.DERGeneralizedTime dateOfBirth, org.bouncycastle.asn1.x500.DirectoryString placeOfBirth, String gender, org.bouncycastle.asn1.x500.DirectoryString postalAddress) {
	}

	public static PersonalData getInstance(Object obj) {
	}

	public NameOrPseudonym getNameOrPseudonym() {
	}

	public javabc.BigInteger getNameDistinguisher() {
	}

	public org.bouncycastle.asn1.DERGeneralizedTime getDateOfBirth() {
	}

	public org.bouncycastle.asn1.x500.DirectoryString getPlaceOfBirth() {
	}

	public String getGender() {
	}

	public org.bouncycastle.asn1.x500.DirectoryString getPostalAddress() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  <p/>
	 *  Returns:
	 *  <p/>
	 *  <pre>
	 *      PersonalData ::= SEQUENCE {
	 *        nameOrPseudonym NameOrPseudonym,
	 *        nameDistinguisher [0] INTEGER OPTIONAL,
	 *        dateOfBirth [1] GeneralizedTime OPTIONAL,
	 *        placeOfBirth [2] DirectoryString OPTIONAL,
	 *        gender [3] PrintableString OPTIONAL,
	 *        postalAddress [4] DirectoryString OPTIONAL
	 *        }
	 *  </pre>
	 * 
	 *  @return a DERObject
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
