/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  <code>UserNotice</code> class, used in
 *  <code>CertificatePolicies</code> X509 extensions (in policy
 *  qualifiers).
 *  <pre>
 *  UserNotice ::= SEQUENCE {
 *       noticeRef        NoticeReference OPTIONAL,
 *       explicitText     DisplayText OPTIONAL}
 * 
 *  </pre>
 *  
 *  @see PolicyQualifierId
 *  @see PolicyInformation
 */
public class UserNotice extends org.bouncycastle.asn1.ASN1Object {

	/**
	 *  Creates a new <code>UserNotice</code> instance.
	 * 
	 *  @param noticeRef a <code>NoticeReference</code> value
	 *  @param explicitText a <code>DisplayText</code> value
	 */
	public UserNotice(NoticeReference noticeRef, DisplayText explicitText) {
	}

	/**
	 *  Creates a new <code>UserNotice</code> instance.
	 * 
	 *  @param noticeRef a <code>NoticeReference</code> value
	 *  @param str the explicitText field as a String. 
	 */
	public UserNotice(NoticeReference noticeRef, String str) {
	}

	public static UserNotice getInstance(Object obj) {
	}

	public NoticeReference getNoticeRef() {
	}

	public DisplayText getExplicitText() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
