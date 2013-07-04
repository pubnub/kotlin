/**
 * 
 * Support classes useful for encoding and processing messages based around RFC3739
 */
package org.bouncycastle.asn1.x509.qualified;


/**
 *  The SemanticsInformation object.
 *  <pre>
 *        SemanticsInformation ::= SEQUENCE {
 *          semanticsIdentifier        OBJECT IDENTIFIER   OPTIONAL,
 *          nameRegistrationAuthorities NameRegistrationAuthorities
 *                                                          OPTIONAL }
 *          (WITH COMPONENTS {..., semanticsIdentifier PRESENT}|
 *           WITH COMPONENTS {..., nameRegistrationAuthorities PRESENT})
 * 
 *      NameRegistrationAuthorities ::=  SEQUENCE SIZE (1..MAX) OF
 *          GeneralName
 *  </pre>
 */
public class SemanticsInformation extends org.bouncycastle.asn1.ASN1Object {

	public SemanticsInformation(org.bouncycastle.asn1.ASN1ObjectIdentifier semanticsIdentifier, org.bouncycastle.asn1.x509.GeneralName[] generalNames) {
	}

	public SemanticsInformation(org.bouncycastle.asn1.ASN1ObjectIdentifier semanticsIdentifier) {
	}

	public SemanticsInformation(org.bouncycastle.asn1.x509.GeneralName[] generalNames) {
	}

	public static SemanticsInformation getInstance(Object obj) {
	}

	public org.bouncycastle.asn1.ASN1ObjectIdentifier getSemanticsIdentifier() {
	}

	public org.bouncycastle.asn1.x509.GeneralName[] getNameRegistrationAuthorities() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
