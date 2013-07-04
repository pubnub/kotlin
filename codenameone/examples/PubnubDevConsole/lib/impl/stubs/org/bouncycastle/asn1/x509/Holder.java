/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  The Holder object.
 *  <p>
 *  For an v2 attribute certificate this is:
 *  
 *  <pre>
 *             Holder ::= SEQUENCE {
 *                   baseCertificateID   [0] IssuerSerial OPTIONAL,
 *                            -- the issuer and serial number of
 *                            -- the holder's Public Key Certificate
 *                   entityName          [1] GeneralNames OPTIONAL,
 *                            -- the name of the claimant or role
 *                   objectDigestInfo    [2] ObjectDigestInfo OPTIONAL
 *                            -- used to directly authenticate the holder,
 *                            -- for example, an executable
 *             }
 *  </pre>
 *  
 *  <p>
 *  For an v1 attribute certificate this is:
 *  
 *  <pre>
 *          subject CHOICE {
 *           baseCertificateID [0] IssuerSerial,
 *           -- associated with a Public Key Certificate
 *           subjectName [1] GeneralNames },
 *           -- associated with a name
 *  </pre>
 */
public class Holder extends org.bouncycastle.asn1.ASN1Object {

	public static final int V1_CERTIFICATE_HOLDER = 0;

	public static final int V2_CERTIFICATE_HOLDER = 1;

	public Holder(IssuerSerial baseCertificateID) {
	}

	/**
	 *  Constructs a holder from a IssuerSerial for a V1 or V2 certificate.
	 *  .
	 *  @param baseCertificateID The IssuerSerial.
	 *  @param version The version of the attribute certificate. 
	 */
	public Holder(IssuerSerial baseCertificateID, int version) {
	}

	/**
	 *  Constructs a holder with an entityName for V2 attribute certificates.
	 *  
	 *  @param entityName The entity or subject name.
	 */
	public Holder(GeneralNames entityName) {
	}

	/**
	 *  Constructs a holder with an entityName for V2 attribute certificates or
	 *  with a subjectName for V1 attribute certificates.
	 *  
	 *  @param entityName The entity or subject name.
	 *  @param version The version of the attribute certificate. 
	 */
	public Holder(GeneralNames entityName, int version) {
	}

	/**
	 *  Constructs a holder from an object digest info.
	 *  
	 *  @param objectDigestInfo The object digest info object.
	 */
	public Holder(ObjectDigestInfo objectDigestInfo) {
	}

	public static Holder getInstance(Object obj) {
	}

	/**
	 *  Returns 1 for V2 attribute certificates or 0 for V1 attribute
	 *  certificates. 
	 *  @return The version of the attribute certificate.
	 */
	public int getVersion() {
	}

	public IssuerSerial getBaseCertificateID() {
	}

	/**
	 *  Returns the entityName for an V2 attribute certificate or the subjectName
	 *  for an V1 attribute certificate.
	 *  
	 *  @return The entityname or subjectname.
	 */
	public GeneralNames getEntityName() {
	}

	public ObjectDigestInfo getObjectDigestInfo() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
