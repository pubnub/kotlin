/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  PolicyQualifierId, used in the CertificatePolicies
 *  X509V3 extension.
 *  
 *  <pre>
 *     id-qt          OBJECT IDENTIFIER ::=  { id-pkix 2 }
 *     id-qt-cps      OBJECT IDENTIFIER ::=  { id-qt 1 }
 *     id-qt-unotice  OBJECT IDENTIFIER ::=  { id-qt 2 }
 *   PolicyQualifierId ::=
 *        OBJECT IDENTIFIER (id-qt-cps | id-qt-unotice)
 *  </pre>
 */
public class PolicyQualifierId extends org.bouncycastle.asn1.ASN1ObjectIdentifier {

	public static final PolicyQualifierId id_qt_cps;

	public static final PolicyQualifierId id_qt_unotice;
}
