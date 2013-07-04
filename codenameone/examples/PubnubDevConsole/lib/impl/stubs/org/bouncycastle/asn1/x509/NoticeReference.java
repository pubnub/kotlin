/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  <code>NoticeReference</code> class, used in
 *  <code>CertificatePolicies</code> X509 V3 extensions
 *  (in policy qualifiers).
 *  
 *  <pre>
 *   NoticeReference ::= SEQUENCE {
 *       organization     DisplayText,
 *       noticeNumbers    SEQUENCE OF INTEGER }
 * 
 *  </pre> 
 *  
 *  @see PolicyQualifierInfo
 *  @see PolicyInformation
 */
public class NoticeReference extends org.bouncycastle.asn1.ASN1Object {

	/**
	 *  Creates a new <code>NoticeReference</code> instance.
	 * 
	 *  @param organization a <code>String</code> value
	 *  @param numbers a <code>Vector</code> value
	 */
	public NoticeReference(String organization, java.util.Vector numbers) {
	}

	/**
	 *  Creates a new <code>NoticeReference</code> instance.
	 * 
	 *  @param organization a <code>String</code> value
	 *  @param noticeNumbers an <code>ASN1EncodableVector</code> value
	 */
	public NoticeReference(String organization, org.bouncycastle.asn1.ASN1EncodableVector noticeNumbers) {
	}

	/**
	 *  Creates a new <code>NoticeReference</code> instance.
	 * 
	 *  @param organization displayText
	 *  @param noticeNumbers an <code>ASN1EncodableVector</code> value
	 */
	public NoticeReference(DisplayText organization, org.bouncycastle.asn1.ASN1EncodableVector noticeNumbers) {
	}

	public static NoticeReference getInstance(Object as) {
	}

	public DisplayText getOrganization() {
	}

	public org.bouncycastle.asn1.ASN1Integer[] getNoticeNumbers() {
	}

	/**
	 *  Describe <code>toASN1Object</code> method here.
	 * 
	 *  @return a <code>ASN1Primitive</code> value
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
