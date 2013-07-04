/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  This class helps to support crossCerfificatePairs in a LDAP directory
 *  according RFC 2587
 *  
 *  <pre>
 *      crossCertificatePairATTRIBUTE::={
 *        WITH SYNTAX   CertificatePair
 *        EQUALITY MATCHING RULE certificatePairExactMatch
 *        ID joint-iso-ccitt(2) ds(5) attributeType(4) crossCertificatePair(40)}
 *  </pre>
 *  
 *  <blockquote> The forward elements of the crossCertificatePair attribute of a
 *  CA's directory entry shall be used to store all, except self-issued
 *  certificates issued to this CA. Optionally, the reverse elements of the
 *  crossCertificatePair attribute, of a CA's directory entry may contain a
 *  subset of certificates issued by this CA to other CAs. When both the forward
 *  and the reverse elements are present in a single attribute value, issuer name
 *  in one certificate shall match the subject name in the other and vice versa,
 *  and the subject public key in one certificate shall be capable of verifying
 *  the digital signature on the other certificate and vice versa.
 *  
 *  When a reverse element is present, the forward element value and the reverse
 *  element value need not be stored in the same attribute value; in other words,
 *  they can be stored in either a single attribute value or two attribute
 *  values. </blockquote>
 *  
 *  <pre>
 *        CertificatePair ::= SEQUENCE {
 *          forward        [0]    Certificate OPTIONAL,
 *          reverse        [1]    Certificate OPTIONAL,
 *          -- at least one of the pair shall be present -- } 
 *  </pre>
 */
public class CertificatePair extends org.bouncycastle.asn1.ASN1Object {

	/**
	 *  Constructor from a given details.
	 * 
	 *  @param forward Certificates issued to this CA.
	 *  @param reverse Certificates issued by this CA to other CAs.
	 */
	public CertificatePair(X509CertificateStructure forward, X509CertificateStructure reverse) {
	}

	public static CertificatePair getInstance(Object obj) {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  <p/>
	 *  Returns:
	 *  <p/>
	 *  <pre>
	 *        CertificatePair ::= SEQUENCE {
	 *          forward        [0]    Certificate OPTIONAL,
	 *          reverse        [1]    Certificate OPTIONAL,
	 *          -- at least one of the pair shall be present -- }
	 *  </pre>
	 * 
	 *  @return a ASN1Primitive
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}

	/**
	 *  @return Returns the forward.
	 */
	public X509CertificateStructure getForward() {
	}

	/**
	 *  @return Returns the reverse.
	 */
	public X509CertificateStructure getReverse() {
	}
}
