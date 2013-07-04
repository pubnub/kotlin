/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  The DistributionPoint object.
 *  <pre>
 *  DistributionPoint ::= SEQUENCE {
 *       distributionPoint [0] DistributionPointName OPTIONAL,
 *       reasons           [1] ReasonFlags OPTIONAL,
 *       cRLIssuer         [2] GeneralNames OPTIONAL
 *  }
 *  </pre>
 */
public class DistributionPoint extends org.bouncycastle.asn1.ASN1Object {

	public DistributionPoint(org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	public DistributionPoint(DistributionPointName distributionPoint, ReasonFlags reasons, GeneralNames cRLIssuer) {
	}

	public static DistributionPoint getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public static DistributionPoint getInstance(Object obj) {
	}

	public DistributionPointName getDistributionPoint() {
	}

	public ReasonFlags getReasons() {
	}

	public GeneralNames getCRLIssuer() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}

	public String toString() {
	}
}
