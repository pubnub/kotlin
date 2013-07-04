/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


public class CertificatePolicies extends org.bouncycastle.asn1.ASN1Object {

	/**
	 *  Construct a CertificatePolicies object containing one PolicyInformation.
	 *  
	 *  @param name the name to be contained.
	 */
	public CertificatePolicies(PolicyInformation name) {
	}

	public CertificatePolicies(PolicyInformation[] policyInformation) {
	}

	public static CertificatePolicies getInstance(Object obj) {
	}

	public static CertificatePolicies getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public PolicyInformation[] getPolicyInformation() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  <pre>
	 *  CertificatePolicies ::= SEQUENCE SIZE {1..MAX} OF PolicyInformation
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}

	public String toString() {
	}
}
