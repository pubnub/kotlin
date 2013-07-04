/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  The AuthorityInformationAccess object.
 *  <pre>
 *  id-pe-authorityInfoAccess OBJECT IDENTIFIER ::= { id-pe 1 }
 * 
 *  AuthorityInfoAccessSyntax  ::=
 *       SEQUENCE SIZE (1..MAX) OF AccessDescription
 *  AccessDescription  ::=  SEQUENCE {
 *        accessMethod          OBJECT IDENTIFIER,
 *        accessLocation        GeneralName  }
 * 
 *  id-ad OBJECT IDENTIFIER ::= { id-pkix 48 }
 *  id-ad-caIssuers OBJECT IDENTIFIER ::= { id-ad 2 }
 *  id-ad-ocsp OBJECT IDENTIFIER ::= { id-ad 1 }
 *  </pre>
 */
public class AuthorityInformationAccess extends org.bouncycastle.asn1.ASN1Object {

	/**
	 *  create an AuthorityInformationAccess with the oid and location provided.
	 */
	public AuthorityInformationAccess(org.bouncycastle.asn1.ASN1ObjectIdentifier oid, GeneralName location) {
	}

	public static AuthorityInformationAccess getInstance(Object obj) {
	}

	/**
	 *  
	 *  @return the access descriptions contained in this object.
	 */
	public AccessDescription[] getAccessDescriptions() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}

	public String toString() {
	}
}
