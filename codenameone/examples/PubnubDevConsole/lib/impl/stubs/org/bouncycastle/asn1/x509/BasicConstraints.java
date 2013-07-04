/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


public class BasicConstraints extends org.bouncycastle.asn1.ASN1Object {

	public BasicConstraints(boolean cA) {
	}

	/**
	 *  create a cA=true object for the given path length constraint.
	 *  
	 *  @param pathLenConstraint
	 */
	public BasicConstraints(int pathLenConstraint) {
	}

	public static BasicConstraints getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public static BasicConstraints getInstance(Object obj) {
	}

	public boolean isCA() {
	}

	public javabc.BigInteger getPathLenConstraint() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  <pre>
	 *  BasicConstraints := SEQUENCE {
	 *     cA                  BOOLEAN DEFAULT FALSE,
	 *     pathLenConstraint   INTEGER (0..MAX) OPTIONAL
	 *  }
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}

	public String toString() {
	}
}
