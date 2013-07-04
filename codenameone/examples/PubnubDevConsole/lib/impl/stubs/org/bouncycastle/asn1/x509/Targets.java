/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  Targets structure used in target information extension for attribute
 *  certificates from RFC 3281.
 *  
 *  <pre>
 *             Targets ::= SEQUENCE OF Target
 *            
 *             Target  ::= CHOICE {
 *               targetName          [0] GeneralName,
 *               targetGroup         [1] GeneralName,
 *               targetCert          [2] TargetCert
 *             }
 *            
 *             TargetCert  ::= SEQUENCE {
 *               targetCertificate    IssuerSerial,
 *               targetName           GeneralName OPTIONAL,
 *               certDigestInfo       ObjectDigestInfo OPTIONAL
 *             }
 *  </pre>
 *  
 *  @see org.bouncycastle.asn1.x509.Target
 *  @see org.bouncycastle.asn1.x509.TargetInformation
 */
public class Targets extends org.bouncycastle.asn1.ASN1Object {

	/**
	 *  Constructor from given targets.
	 *  <p>
	 *  The vector is copied.
	 *  
	 *  @param targets A <code>Vector</code> of {@link Target}s.
	 *  @see Target
	 *  @throws IllegalArgumentException if the vector contains not only Targets.
	 */
	public Targets(Target[] targets) {
	}

	/**
	 *  Creates an instance of a Targets from the given object.
	 *  <p>
	 *  <code>obj</code> can be a Targets or a {@link ASN1Sequence}
	 *  
	 *  @param obj The object.
	 *  @return A Targets instance.
	 *  @throws IllegalArgumentException if the given object cannot be
	 *              interpreted as Target.
	 */
	public static Targets getInstance(Object obj) {
	}

	/**
	 *  Returns the targets in a <code>Vector</code>.
	 *  <p>
	 *  The vector is cloned before it is returned.
	 *  
	 *  @return Returns the targets.
	 */
	public Target[] getTargets() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  
	 *  Returns:
	 *  
	 *  <pre>
	 *             Targets ::= SEQUENCE OF Target
	 *  </pre>
	 *  
	 *  @return a ASN1Primitive
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
