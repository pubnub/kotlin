/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  Class for containing a restriction object subtrees in NameConstraints. See
 *  RFC 3280.
 *  
 *  <pre>
 *        
 *        GeneralSubtree ::= SEQUENCE 
 *        {
 *          base                    GeneralName,
 *          minimum         [0]     BaseDistance DEFAULT 0,
 *          maximum         [1]     BaseDistance OPTIONAL 
 *        }
 *  </pre>
 *  
 *  @see org.bouncycastle.asn1.x509.NameConstraints
 *  
 */
public class GeneralSubtree extends org.bouncycastle.asn1.ASN1Object {

	/**
	 *  Constructor from a given details.
	 *  
	 *  According RFC 3280, the minimum and maximum fields are not used with any
	 *  name forms, thus minimum MUST be zero, and maximum MUST be absent.
	 *  <p>
	 *  If minimum is <code>null</code>, zero is assumed, if
	 *  maximum is <code>null</code>, maximum is absent.
	 *  
	 *  @param base
	 *             A restriction.
	 *  @param minimum
	 *             Minimum
	 *  
	 *  @param maximum
	 *             Maximum
	 */
	public GeneralSubtree(GeneralName base, javabc.BigInteger minimum, javabc.BigInteger maximum) {
	}

	public GeneralSubtree(GeneralName base) {
	}

	public static GeneralSubtree getInstance(org.bouncycastle.asn1.ASN1TaggedObject o, boolean explicit) {
	}

	public static GeneralSubtree getInstance(Object obj) {
	}

	public GeneralName getBase() {
	}

	public javabc.BigInteger getMinimum() {
	}

	public javabc.BigInteger getMaximum() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  
	 *  Returns:
	 *  
	 *  <pre>
	 *        GeneralSubtree ::= SEQUENCE 
	 *        {
	 *          base                    GeneralName,
	 *          minimum         [0]     BaseDistance DEFAULT 0,
	 *          maximum         [1]     BaseDistance OPTIONAL 
	 *        }
	 *  </pre>
	 *  
	 *  @return a ASN1Primitive
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
