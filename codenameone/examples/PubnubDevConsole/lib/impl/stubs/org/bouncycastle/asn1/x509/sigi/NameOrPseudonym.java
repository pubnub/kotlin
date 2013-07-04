package org.bouncycastle.asn1.x509.sigi;


/**
 *  Structure for a name or pseudonym.
 *  
 *  <pre>
 *        NameOrPseudonym ::= CHOICE {
 *             surAndGivenName SEQUENCE {
 *               surName DirectoryString,
 *               givenName SEQUENCE OF DirectoryString 
 *          },
 *             pseudonym DirectoryString 
 *        }
 *  </pre>
 *  
 *  @see org.bouncycastle.asn1.x509.sigi.PersonalData
 *  
 */
public class NameOrPseudonym extends org.bouncycastle.asn1.ASN1Object implements org.bouncycastle.asn1.ASN1Choice {

	/**
	 *  Constructor from DirectoryString.
	 *  <p/>
	 *  The sequence is of type NameOrPseudonym:
	 *  <p/>
	 *  <pre>
	 *        NameOrPseudonym ::= CHOICE {
	 *             surAndGivenName SEQUENCE {
	 *               surName DirectoryString,
	 *               givenName SEQUENCE OF DirectoryString
	 *          },
	 *             pseudonym DirectoryString
	 *        }
	 *  </pre>
	 *  @param pseudonym pseudonym value to use.
	 */
	public NameOrPseudonym(org.bouncycastle.asn1.x500.DirectoryString pseudonym) {
	}

	/**
	 *  Constructor from a given details.
	 * 
	 *  @param pseudonym The pseudonym.
	 */
	public NameOrPseudonym(String pseudonym) {
	}

	/**
	 *  Constructor from a given details.
	 * 
	 *  @param surname   The surname.
	 *  @param givenName A sequence of directory strings making up the givenName
	 */
	public NameOrPseudonym(org.bouncycastle.asn1.x500.DirectoryString surname, org.bouncycastle.asn1.ASN1Sequence givenName) {
	}

	public static NameOrPseudonym getInstance(Object obj) {
	}

	public org.bouncycastle.asn1.x500.DirectoryString getPseudonym() {
	}

	public org.bouncycastle.asn1.x500.DirectoryString getSurname() {
	}

	public org.bouncycastle.asn1.x500.DirectoryString[] getGivenName() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  <p/>
	 *  Returns:
	 *  <p/>
	 *  <pre>
	 *        NameOrPseudonym ::= CHOICE {
	 *             surAndGivenName SEQUENCE {
	 *               surName DirectoryString,
	 *               givenName SEQUENCE OF DirectoryString
	 *          },
	 *             pseudonym DirectoryString
	 *        }
	 *  </pre>
	 * 
	 *  @return a DERObject
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
