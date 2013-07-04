package org.bouncycastle.asn1.x509.sigi;


/**
 *  Object Identifiers of SigI specifciation (German Signature Law
 *  Interoperability specification).
 */
public interface SigIObjectIdentifiers {

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_sigi;

	/**
	 *  Key purpose IDs for German SigI (Signature Interoperability
	 *  Specification)
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_sigi_kp;

	/**
	 *  Certificate policy IDs for German SigI (Signature Interoperability
	 *  Specification)
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_sigi_cp;

	/**
	 *  Other Name IDs for German SigI (Signature Interoperability Specification)
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_sigi_on;

	/**
	 *  To be used for for the generation of directory service certificates.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_sigi_kp_directoryService;

	/**
	 *  ID for PersonalData
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_sigi_on_personalData;

	/**
	 *  Certificate is conform to german signature law.
	 */
	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_sigi_cp_sigconform;
}
