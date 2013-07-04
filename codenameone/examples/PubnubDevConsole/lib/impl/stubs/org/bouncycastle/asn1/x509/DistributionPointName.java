/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  The DistributionPointName object.
 *  <pre>
 *  DistributionPointName ::= CHOICE {
 *      fullName                 [0] GeneralNames,
 *      nameRelativeToCRLIssuer  [1] RDN
 *  }
 *  </pre>
 */
public class DistributionPointName extends org.bouncycastle.asn1.ASN1Object implements org.bouncycastle.asn1.ASN1Choice {

	public static final int FULL_NAME = 0;

	public static final int NAME_RELATIVE_TO_CRL_ISSUER = 1;

	public DistributionPointName(int type, org.bouncycastle.asn1.ASN1Encodable name) {
	}

	public DistributionPointName(GeneralNames name) {
	}

	public DistributionPointName(org.bouncycastle.asn1.ASN1TaggedObject obj) {
	}

	public static DistributionPointName getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public static DistributionPointName getInstance(Object obj) {
	}

	/**
	 *  Return the tag number applying to the underlying choice.
	 *  
	 *  @return the tag number for this point name.
	 */
	public int getType() {
	}

	/**
	 *  Return the tagged object inside the distribution point name.
	 *  
	 *  @return the underlying choice item.
	 */
	public org.bouncycastle.asn1.ASN1Encodable getName() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}

	public String toString() {
	}
}
