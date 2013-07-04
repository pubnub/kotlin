/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  This extension may contain further X.500 attributes of the subject. See also
 *  RFC 3039.
 *  
 *  <pre>
 *      SubjectDirectoryAttributes ::= Attributes
 *      Attributes ::= SEQUENCE SIZE (1..MAX) OF Attribute
 *      Attribute ::= SEQUENCE 
 *      {
 *        type AttributeType 
 *        values SET OF AttributeValue 
 *      }
 *      
 *      AttributeType ::= OBJECT IDENTIFIER
 *      AttributeValue ::= ANY DEFINED BY AttributeType
 *  </pre>
 *  
 *  @see org.bouncycastle.asn1.x500.style.BCStyle for AttributeType ObjectIdentifiers.
 */
public class SubjectDirectoryAttributes extends org.bouncycastle.asn1.ASN1Object {

	/**
	 *  Constructor from a vector of attributes.
	 *  
	 *  The vector consists of attributes of type {@link Attribute Attribute}
	 *  
	 *  @param attributes
	 *             The attributes.
	 *  
	 */
	public SubjectDirectoryAttributes(java.util.Vector attributes) {
	}

	public static SubjectDirectoryAttributes getInstance(Object obj) {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  
	 *  Returns:
	 *  
	 *  <pre>
	 *       SubjectDirectoryAttributes ::= Attributes
	 *       Attributes ::= SEQUENCE SIZE (1..MAX) OF Attribute
	 *       Attribute ::= SEQUENCE 
	 *       {
	 *         type AttributeType 
	 *         values SET OF AttributeValue 
	 *       }
	 *       
	 *       AttributeType ::= OBJECT IDENTIFIER
	 *       AttributeValue ::= ANY DEFINED BY AttributeType
	 *  </pre>
	 *  
	 *  @return a ASN1Primitive
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}

	/**
	 *  @return Returns the attributes.
	 */
	public java.util.Vector getAttributes() {
	}
}
