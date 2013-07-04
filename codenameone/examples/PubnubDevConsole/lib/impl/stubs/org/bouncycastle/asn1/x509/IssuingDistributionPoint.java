/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  <pre>
 *  IssuingDistributionPoint ::= SEQUENCE { 
 *    distributionPoint          [0] DistributionPointName OPTIONAL, 
 *    onlyContainsUserCerts      [1] BOOLEAN DEFAULT FALSE, 
 *    onlyContainsCACerts        [2] BOOLEAN DEFAULT FALSE, 
 *    onlySomeReasons            [3] ReasonFlags OPTIONAL, 
 *    indirectCRL                [4] BOOLEAN DEFAULT FALSE,
 *    onlyContainsAttributeCerts [5] BOOLEAN DEFAULT FALSE }
 *  </pre>
 */
public class IssuingDistributionPoint extends org.bouncycastle.asn1.ASN1Object {

	/**
	 *  Constructor from given details.
	 *  
	 *  @param distributionPoint
	 *             May contain an URI as pointer to most current CRL.
	 *  @param onlyContainsUserCerts Covers revocation information for end certificates.
	 *  @param onlyContainsCACerts Covers revocation information for CA certificates.
	 *  
	 *  @param onlySomeReasons
	 *             Which revocation reasons does this point cover.
	 *  @param indirectCRL
	 *             If <code>true</code> then the CRL contains revocation
	 *             information about certificates ssued by other CAs.
	 *  @param onlyContainsAttributeCerts Covers revocation information for attribute certificates.
	 */
	public IssuingDistributionPoint(DistributionPointName distributionPoint, boolean onlyContainsUserCerts, boolean onlyContainsCACerts, ReasonFlags onlySomeReasons, boolean indirectCRL, boolean onlyContainsAttributeCerts) {
	}

	/**
	 *  Shorthand Constructor from given details.
	 * 
	 *  @param distributionPoint
	 *             May contain an URI as pointer to most current CRL.
	 *  @param indirectCRL
	 *             If <code>true</code> then the CRL contains revocation
	 *             information about certificates ssued by other CAs.
	 *  @param onlyContainsAttributeCerts Covers revocation information for attribute certificates.
	 */
	public IssuingDistributionPoint(DistributionPointName distributionPoint, boolean indirectCRL, boolean onlyContainsAttributeCerts) {
	}

	public static IssuingDistributionPoint getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public static IssuingDistributionPoint getInstance(Object obj) {
	}

	public boolean onlyContainsUserCerts() {
	}

	public boolean onlyContainsCACerts() {
	}

	public boolean isIndirectCRL() {
	}

	public boolean onlyContainsAttributeCerts() {
	}

	/**
	 *  @return Returns the distributionPoint.
	 */
	public DistributionPointName getDistributionPoint() {
	}

	/**
	 *  @return Returns the onlySomeReasons.
	 */
	public ReasonFlags getOnlySomeReasons() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}

	public String toString() {
	}
}
