/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  The ReasonFlags object.
 *  <pre>
 *  ReasonFlags ::= BIT STRING {
 *       unused                  (0),
 *       keyCompromise           (1),
 *       cACompromise            (2),
 *       affiliationChanged      (3),
 *       superseded              (4),
 *       cessationOfOperation    (5),
 *       certificateHold         (6),
 *       privilegeWithdrawn      (7),
 *       aACompromise            (8) }
 *  </pre>
 */
public class ReasonFlags extends org.bouncycastle.asn1.DERBitString {

	/**
	 *  @deprecated use lower case version
	 */
	public static final int UNUSED = 128;

	/**
	 *  @deprecated use lower case version
	 */
	public static final int KEY_COMPROMISE = 64;

	/**
	 *  @deprecated use lower case version
	 */
	public static final int CA_COMPROMISE = 32;

	/**
	 *  @deprecated use lower case version
	 */
	public static final int AFFILIATION_CHANGED = 16;

	/**
	 *  @deprecated use lower case version
	 */
	public static final int SUPERSEDED = 8;

	/**
	 *  @deprecated use lower case version
	 */
	public static final int CESSATION_OF_OPERATION = 4;

	/**
	 *  @deprecated use lower case version
	 */
	public static final int CERTIFICATE_HOLD = 2;

	/**
	 *  @deprecated use lower case version
	 */
	public static final int PRIVILEGE_WITHDRAWN = 1;

	/**
	 *  @deprecated use lower case version
	 */
	public static final int AA_COMPROMISE = 32768;

	public static final int unused = 128;

	public static final int keyCompromise = 64;

	public static final int cACompromise = 32;

	public static final int affiliationChanged = 16;

	public static final int superseded = 8;

	public static final int cessationOfOperation = 4;

	public static final int certificateHold = 2;

	public static final int privilegeWithdrawn = 1;

	public static final int aACompromise = 32768;

	/**
	 *  @param reasons - the bitwise OR of the Key Reason flags giving the
	 *  allowed uses for the key.
	 */
	public ReasonFlags(int reasons) {
	}

	public ReasonFlags(org.bouncycastle.asn1.DERBitString reasons) {
	}
}
