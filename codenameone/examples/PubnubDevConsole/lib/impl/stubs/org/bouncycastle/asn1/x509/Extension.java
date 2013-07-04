/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  an object for the elements in the X.509 V3 extension block.
 */
public class Extension {

	/**
	 *  Subject Directory Attributes
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier subjectDirectoryAttributes;

	/**
	 *  Subject Key Identifier 
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier subjectKeyIdentifier;

	/**
	 *  Key Usage 
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier keyUsage;

	/**
	 *  Private Key Usage Period 
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier privateKeyUsagePeriod;

	/**
	 *  Subject Alternative Name 
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier subjectAlternativeName;

	/**
	 *  Issuer Alternative Name 
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier issuerAlternativeName;

	/**
	 *  Basic Constraints 
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier basicConstraints;

	/**
	 *  CRL Number 
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier cRLNumber;

	/**
	 *  Reason code 
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier reasonCode;

	/**
	 *  Hold Instruction Code 
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier instructionCode;

	/**
	 *  Invalidity Date 
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier invalidityDate;

	/**
	 *  Delta CRL indicator 
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier deltaCRLIndicator;

	/**
	 *  Issuing Distribution Point 
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier issuingDistributionPoint;

	/**
	 *  Certificate Issuer 
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier certificateIssuer;

	/**
	 *  Name Constraints 
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier nameConstraints;

	/**
	 *  CRL Distribution Points 
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier cRLDistributionPoints;

	/**
	 *  Certificate Policies 
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier certificatePolicies;

	/**
	 *  Policy Mappings 
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier policyMappings;

	/**
	 *  Authority Key Identifier 
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier authorityKeyIdentifier;

	/**
	 *  Policy Constraints 
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier policyConstraints;

	/**
	 *  Extended Key Usage 
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier extendedKeyUsage;

	/**
	 *  Freshest CRL
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier freshestCRL;

	/**
	 *  Inhibit Any Policy
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier inhibitAnyPolicy;

	/**
	 *  Authority Info Access
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier authorityInfoAccess;

	/**
	 *  Subject Info Access
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier subjectInfoAccess;

	/**
	 *  Logo Type
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier logoType;

	/**
	 *  BiometricInfo
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier biometricInfo;

	/**
	 *  QCStatements
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier qCStatements;

	/**
	 *  Audit identity extension in attribute certificates.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier auditIdentity;

	/**
	 *  NoRevAvail extension in attribute certificates.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier noRevAvail;

	/**
	 *  TargetInformation extension in attribute certificates.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier targetInformation;

	public Extension(org.bouncycastle.asn1.ASN1ObjectIdentifier extnId, org.bouncycastle.asn1.ASN1Boolean critical, org.bouncycastle.asn1.ASN1OctetString value) {
	}

	public Extension(org.bouncycastle.asn1.ASN1ObjectIdentifier extnId, boolean critical, byte[] value) {
	}

	public Extension(org.bouncycastle.asn1.ASN1ObjectIdentifier extnId, boolean critical, org.bouncycastle.asn1.ASN1OctetString value) {
	}

	public org.bouncycastle.asn1.ASN1ObjectIdentifier getExtnId() {
	}

	public boolean isCritical() {
	}

	public org.bouncycastle.asn1.ASN1OctetString getExtnValue() {
	}

	public org.bouncycastle.asn1.ASN1Encodable getParsedValue() {
	}

	public int hashCode() {
	}

	public boolean equals(Object o) {
	}
}
