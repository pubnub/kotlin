/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  Policy qualifiers, used in the X509V3 CertificatePolicies
 *  extension.
 *  
 *  <pre>
 *    PolicyQualifierInfo ::= SEQUENCE {
 *        policyQualifierId  PolicyQualifierId,
 *        qualifier          ANY DEFINED BY policyQualifierId }
 *  </pre>
 */
public class PolicyQualifierInfo extends org.bouncycastle.asn1.ASN1Object {

	/**
	 *  Creates a new <code>PolicyQualifierInfo</code> instance.
	 * 
	 *  @param policyQualifierId a <code>PolicyQualifierId</code> value
	 *  @param qualifier the qualifier, defined by the above field.
	 */
	public PolicyQualifierInfo(org.bouncycastle.asn1.ASN1ObjectIdentifier policyQualifierId, org.bouncycastle.asn1.ASN1Encodable qualifier) {
	}

	/**
	 *  Creates a new <code>PolicyQualifierInfo</code> containing a
	 *  cPSuri qualifier.
	 * 
	 *  @param cps the CPS (certification practice statement) uri as a
	 *  <code>String</code>.
	 */
	public PolicyQualifierInfo(String cps) {
	}

	/**
	 *  Creates a new <code>PolicyQualifierInfo</code> instance.
	 * 
	 *  @param as <code>PolicyQualifierInfo</code> X509 structure
	 *  encoded as an ASN1Sequence. 
	 */
	public PolicyQualifierInfo(org.bouncycastle.asn1.ASN1Sequence as) {
	}

	public static PolicyQualifierInfo getInstance(Object obj) {
	}

	public org.bouncycastle.asn1.ASN1ObjectIdentifier getPolicyQualifierId() {
	}

	public org.bouncycastle.asn1.ASN1Encodable getQualifier() {
	}

	/**
	 *  Returns a DER-encodable representation of this instance. 
	 * 
	 *  @return a <code>ASN1Primitive</code> value
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
