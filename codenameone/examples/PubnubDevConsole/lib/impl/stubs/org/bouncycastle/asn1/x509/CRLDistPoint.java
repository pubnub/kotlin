/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


public class CRLDistPoint extends org.bouncycastle.asn1.ASN1Object {

	public CRLDistPoint(DistributionPoint[] points) {
	}

	public static CRLDistPoint getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public static CRLDistPoint getInstance(Object obj) {
	}

	/**
	 *  Return the distribution points making up the sequence.
	 *  
	 *  @return DistributionPoint[]
	 */
	public DistributionPoint[] getDistributionPoints() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  <pre>
	 *  CRLDistPoint ::= SEQUENCE SIZE {1..MAX} OF DistributionPoint
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}

	public String toString() {
	}
}
