/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


public class NameConstraints extends org.bouncycastle.asn1.ASN1Object {

	/**
	 *  Constructor from a given details.
	 *  
	 *  <p>
	 *  permitted and excluded are Vectors of GeneralSubtree objects.
	 *  
	 *  @param permitted
	 *             Permitted subtrees
	 *  @param excluded
	 *             Excludes subtrees
	 */
	public NameConstraints(java.util.Vector permitted, java.util.Vector excluded) {
	}

	public static NameConstraints getInstance(Object obj) {
	}

	public org.bouncycastle.asn1.ASN1Sequence getPermittedSubtrees() {
	}

	public org.bouncycastle.asn1.ASN1Sequence getExcludedSubtrees() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
