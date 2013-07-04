/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  Target structure used in target information extension for attribute
 *  certificates from RFC 3281.
 *  
 *  <pre>
 *      Target  ::= CHOICE {
 *        targetName          [0] GeneralName,
 *        targetGroup         [1] GeneralName,
 *        targetCert          [2] TargetCert
 *      }
 *  </pre>
 *  
 *  <p>
 *  The targetCert field is currently not supported and must not be used
 *  according to RFC 3281.
 */
public class Target extends org.bouncycastle.asn1.ASN1Object implements org.bouncycastle.asn1.ASN1Choice {

	public static final int targetName = 0;

	public static final int targetGroup = 1;

	/**
	 *  Constructor from given details.
	 *  <p>
	 *  Exactly one of the parameters must be not <code>null</code>.
	 * 
	 *  @param type the choice type to apply to the name.
	 *  @param name the general name.
	 *  @throws IllegalArgumentException if type is invalid.
	 */
	public Target(int type, GeneralName name) {
	}

	/**
	 *  Creates an instance of a Target from the given object.
	 *  <p>
	 *  <code>obj</code> can be a Target or a {@link ASN1TaggedObject}
	 *  
	 *  @param obj The object.
	 *  @return A Target instance.
	 *  @throws IllegalArgumentException if the given object cannot be
	 *              interpreted as Target.
	 */
	public static Target getInstance(Object obj) {
	}

	/**
	 *  @return Returns the targetGroup.
	 */
	public GeneralName getTargetGroup() {
	}

	/**
	 *  @return Returns the targetName.
	 */
	public GeneralName getTargetName() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  
	 *  Returns:
	 *  
	 *  <pre>
	 *      Target  ::= CHOICE {
	 *        targetName          [0] GeneralName,
	 *        targetGroup         [1] GeneralName,
	 *        targetCert          [2] TargetCert
	 *      }
	 *  </pre>
	 *  
	 *  @return a ASN1Primitive
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
