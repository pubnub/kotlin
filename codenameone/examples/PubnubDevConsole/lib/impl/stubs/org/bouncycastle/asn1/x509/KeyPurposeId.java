/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  The KeyPurposeId object.
 *  <pre>
 *      KeyPurposeId ::= OBJECT IDENTIFIER
 * 
 *      id-kp ::= OBJECT IDENTIFIER { iso(1) identified-organization(3) 
 *           dod(6) internet(1) security(5) mechanisms(5) pkix(7) 3}
 * 
 *  </pre>
 */
public class KeyPurposeId extends org.bouncycastle.asn1.ASN1ObjectIdentifier {

	/**
	 *  { 2 5 29 37 0 }
	 */
	public static final KeyPurposeId anyExtendedKeyUsage;

	/**
	 *  { id-kp 1 }
	 */
	public static final KeyPurposeId id_kp_serverAuth;

	/**
	 *  { id-kp 2 }
	 */
	public static final KeyPurposeId id_kp_clientAuth;

	/**
	 *  { id-kp 3 }
	 */
	public static final KeyPurposeId id_kp_codeSigning;

	/**
	 *  { id-kp 4 }
	 */
	public static final KeyPurposeId id_kp_emailProtection;

	/**
	 *  Usage deprecated by RFC4945 - was { id-kp 5 }
	 */
	public static final KeyPurposeId id_kp_ipsecEndSystem;

	/**
	 *  Usage deprecated by RFC4945 - was { id-kp 6 }
	 */
	public static final KeyPurposeId id_kp_ipsecTunnel;

	/**
	 *  Usage deprecated by RFC4945 - was { idkp 7 }
	 */
	public static final KeyPurposeId id_kp_ipsecUser;

	/**
	 *  { id-kp 8 }
	 */
	public static final KeyPurposeId id_kp_timeStamping;

	/**
	 *  { id-kp 9 }
	 */
	public static final KeyPurposeId id_kp_OCSPSigning;

	/**
	 *  { id-kp 10 }
	 */
	public static final KeyPurposeId id_kp_dvcs;

	/**
	 *  { id-kp 11 }
	 */
	public static final KeyPurposeId id_kp_sbgpCertAAServerAuth;

	/**
	 *  { id-kp 12 }
	 */
	public static final KeyPurposeId id_kp_scvp_responder;

	/**
	 *  { id-kp 13 }
	 */
	public static final KeyPurposeId id_kp_eapOverPPP;

	/**
	 *  { id-kp 14 }
	 */
	public static final KeyPurposeId id_kp_eapOverLAN;

	/**
	 *  { id-kp 15 }
	 */
	public static final KeyPurposeId id_kp_scvpServer;

	/**
	 *  { id-kp 16 }
	 */
	public static final KeyPurposeId id_kp_scvpClient;

	/**
	 *  { id-kp 17 }
	 */
	public static final KeyPurposeId id_kp_ipsecIKE;

	/**
	 *  { id-kp 18 }
	 */
	public static final KeyPurposeId id_kp_capwapAC;

	/**
	 *  { id-kp 19 }
	 */
	public static final KeyPurposeId id_kp_capwapWTP;

	/**
	 *  { 1 3 6 1 4 1 311 20 2 2 }
	 */
	public static final KeyPurposeId id_kp_smartcardlogon;

	/**
	 *  Create a KeyPurposeId from an OID string
	 * 
	 *  @param id OID String.  E.g. "1.3.6.1.5.5.7.3.1"
	 */
	public KeyPurposeId(String id) {
	}
}
