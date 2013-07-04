/**
 * 
 * Support classes useful for encoding and processing messages based around RFC3739
 */
package org.bouncycastle.asn1.x509.qualified;


/**
 *  The QCStatement object.
 *  <pre>
 *  QCStatement ::= SEQUENCE {
 *    statementId        OBJECT IDENTIFIER,
 *    statementInfo      ANY DEFINED BY statementId OPTIONAL} 
 *  </pre>
 */
public class QCStatement extends org.bouncycastle.asn1.ASN1Object {

	public QCStatement(org.bouncycastle.asn1.ASN1ObjectIdentifier qcStatementId) {
	}

	public QCStatement(org.bouncycastle.asn1.ASN1ObjectIdentifier qcStatementId, org.bouncycastle.asn1.ASN1Encodable qcStatementInfo) {
	}

	public static QCStatement getInstance(Object obj) {
	}

	public org.bouncycastle.asn1.ASN1ObjectIdentifier getStatementId() {
	}

	public org.bouncycastle.asn1.ASN1Encodable getStatementInfo() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
