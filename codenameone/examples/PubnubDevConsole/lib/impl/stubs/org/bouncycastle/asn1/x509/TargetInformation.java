/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  Target information extension for attributes certificates according to RFC
 *  3281.
 *  
 *  <pre>
 *            SEQUENCE OF Targets
 *  </pre>
 *  
 */
public class TargetInformation extends org.bouncycastle.asn1.ASN1Object {

	/**
	 *  Constructs a target information from a single targets element. 
	 *  According to RFC 3281 only one targets element must be produced.
	 *  
	 *  @param targets A Targets instance.
	 */
	public TargetInformation(Targets targets) {
	}

	/**
	 *  According to RFC 3281 only one targets element must be produced. If
	 *  multiple targets are given they must be merged in
	 *  into one targets element.
	 * 
	 *  @param targets An array with {@link Targets}.
	 */
	public TargetInformation(Target[] targets) {
	}

	/**
	 *  Creates an instance of a TargetInformation from the given object.
	 *  <p>
	 *  <code>obj</code> can be a TargetInformation or a {@link ASN1Sequence}
	 *  
	 *  @param obj The object.
	 *  @return A TargetInformation instance.
	 *  @throws IllegalArgumentException if the given object cannot be
	 *              interpreted as TargetInformation.
	 */
	public static TargetInformation getInstance(Object obj) {
	}

	/**
	 *  Returns the targets in this target information extension.
	 *  
	 *  @return Returns the targets.
	 */
	public Targets[] getTargetsObjects() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  
	 *  Returns:
	 *  
	 *  <pre>
	 *           SEQUENCE OF Targets
	 *  </pre>
	 *  
	 *  <p>
	 *  According to RFC 3281 only one targets element must be produced. If
	 *  multiple targets are given in the constructor they are merged into one
	 *  targets element. If this was produced from a
	 *  {@link org.bouncycastle.asn1.ASN1Sequence} the encoding is kept.
	 *  
	 *  @return a ASN1Primitive
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
