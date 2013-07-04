/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  PolicyMappings V3 extension, described in RFC3280.
 *  <pre>
 *     PolicyMappings ::= SEQUENCE SIZE (1..MAX) OF SEQUENCE {
 *       issuerDomainPolicy      CertPolicyId,
 *       subjectDomainPolicy     CertPolicyId }
 *  </pre>
 * 
 *  @see <a href="http://www.faqs.org/rfc/rfc3280.txt">RFC 3280, section 4.2.1.6</a>
 */
public class PolicyMappings extends org.bouncycastle.asn1.ASN1Object {

	/**
	 *  Creates a new <code>PolicyMappings</code> instance.
	 * 
	 *  @param mappings a <code>HashMap</code> value that maps
	 *                  <code>String</code> oids
	 *                  to other <code>String</code> oids.
	 */
	public PolicyMappings(java.util.Hashtable mappings) {
	}

	public static PolicyMappings getInstance(Object obj) {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
