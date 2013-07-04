/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


public interface X509AttributeIdentifiers {

	/**
	 *  @deprecated use id_at_role
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier RoleSyntax;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_pe_ac_auditIdentity;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_pe_aaControls;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_pe_ac_proxying;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_ce_targetInformation;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_aca;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_aca_authenticationInfo;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_aca_accessIdentity;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_aca_chargingIdentity;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_aca_group;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_aca_encAttrs;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_at_role;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_at_clearance;
}
