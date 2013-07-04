/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  @deprecated use Extensions
 */
public class X509Extensions extends org.bouncycastle.asn1.ASN1Object {

	/**
	 *  Subject Directory Attributes
	 *  @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier SubjectDirectoryAttributes;

	/**
	 *  Subject Key Identifier
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier SubjectKeyIdentifier;

	/**
	 *  Key Usage
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier KeyUsage;

	/**
	 *  Private Key Usage Period
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier PrivateKeyUsagePeriod;

	/**
	 *  Subject Alternative Name
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier SubjectAlternativeName;

	/**
	 *  Issuer Alternative Name
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier IssuerAlternativeName;

	/**
	 *  Basic Constraints
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier BasicConstraints;

	/**
	 *  CRL Number
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier CRLNumber;

	/**
	 *  Reason code
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier ReasonCode;

	/**
	 *  Hold Instruction Code
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier InstructionCode;

	/**
	 *  Invalidity Date
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier InvalidityDate;

	/**
	 *  Delta CRL indicator
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier DeltaCRLIndicator;

	/**
	 *  Issuing Distribution Point
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier IssuingDistributionPoint;

	/**
	 *  Certificate Issuer
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier CertificateIssuer;

	/**
	 *  Name Constraints
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier NameConstraints;

	/**
	 *  CRL Distribution Points
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier CRLDistributionPoints;

	/**
	 *  Certificate Policies
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier CertificatePolicies;

	/**
	 *  Policy Mappings
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier PolicyMappings;

	/**
	 *  Authority Key Identifier
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier AuthorityKeyIdentifier;

	/**
	 *  Policy Constraints
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier PolicyConstraints;

	/**
	 *  Extended Key Usage
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier ExtendedKeyUsage;

	/**
	 *  Freshest CRL
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier FreshestCRL;

	/**
	 *  Inhibit Any Policy
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier InhibitAnyPolicy;

	/**
	 *  Authority Info Access
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier AuthorityInfoAccess;

	/**
	 *  Subject Info Access
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier SubjectInfoAccess;

	/**
	 *  Logo Type
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier LogoType;

	/**
	 *  BiometricInfo
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier BiometricInfo;

	/**
	 *  QCStatements
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier QCStatements;

	/**
	 *  Audit identity extension in attribute certificates.
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier AuditIdentity;

	/**
	 *  NoRevAvail extension in attribute certificates.
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier NoRevAvail;

	/**
	 *  TargetInformation extension in attribute certificates.
	 *   @deprecated use X509Extension value.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier TargetInformation;

	/**
	 *  Constructor from ASN1Sequence.
	 * 
	 *  the extensions are a list of constructed sequences, either with (OID, OctetString) or (OID, Boolean, OctetString)
	 */
	public X509Extensions(org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	/**
	 *  constructor from a table of extensions.
	 *  <p>
	 *  it's is assumed the table contains OID/String pairs.
	 */
	public X509Extensions(java.util.Hashtable extensions) {
	}

	/**
	 *  Constructor from a table of extensions with ordering.
	 *  <p>
	 *  It's is assumed the table contains OID/String pairs.
	 */
	public X509Extensions(java.util.Vector ordering, java.util.Hashtable extensions) {
	}

	/**
	 *  Constructor from two vectors
	 *  
	 *  @param objectIDs a vector of the object identifiers.
	 *  @param values a vector of the extension values.
	 */
	public X509Extensions(java.util.Vector objectIDs, java.util.Vector values) {
	}

	public static X509Extensions getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public static X509Extensions getInstance(Object obj) {
	}

	/**
	 *  return an Enumeration of the extension field's object ids.
	 */
	public java.util.Enumeration oids() {
	}

	/**
	 *  return the extension represented by the object identifier
	 *  passed in.
	 * 
	 *  @return the extension if it's present, null otherwise.
	 */
	public X509Extension getExtension(org.bouncycastle.asn1.DERObjectIdentifier oid) {
	}

	/**
	 *  @deprecated
	 *  @param oid
	 *  @return
	 */
	public X509Extension getExtension(org.bouncycastle.asn1.ASN1ObjectIdentifier oid) {
	}

	/**
	 *  <pre>
	 *      Extensions        ::=   SEQUENCE SIZE (1..MAX) OF Extension
	 * 
	 *      Extension         ::=   SEQUENCE {
	 *         extnId            EXTENSION.&amp;id ({ExtensionSet}),
	 *         critical          BOOLEAN DEFAULT FALSE,
	 *         extnValue         OCTET STRING }
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}

	public boolean equivalent(X509Extensions other) {
	}

	public org.bouncycastle.asn1.ASN1ObjectIdentifier[] getExtensionOIDs() {
	}

	public org.bouncycastle.asn1.ASN1ObjectIdentifier[] getNonCriticalExtensionOIDs() {
	}

	public org.bouncycastle.asn1.ASN1ObjectIdentifier[] getCriticalExtensionOIDs() {
	}
}
