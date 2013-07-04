/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  CertPolicyId, used in the CertificatePolicies and PolicyMappings
 *  X509V3 Extensions.
 * 
 *  <pre>
 *      CertPolicyId ::= OBJECT IDENTIFIER
 *  </pre>
 */
public class CertPolicyId extends org.bouncycastle.asn1.ASN1ObjectIdentifier {

	public CertPolicyId(String id) {
	}
}
