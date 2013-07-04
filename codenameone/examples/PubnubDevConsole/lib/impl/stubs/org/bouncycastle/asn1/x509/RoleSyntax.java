/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  Implementation of the RoleSyntax object as specified by the RFC3281.
 *  
 *  <pre>
 *  RoleSyntax ::= SEQUENCE {
 *                  roleAuthority  [0] GeneralNames OPTIONAL,
 *                  roleName       [1] GeneralName
 *            } 
 *  </pre>
 */
public class RoleSyntax extends org.bouncycastle.asn1.ASN1Object {

	/**
	 *  Constructor.
	 *  @param roleAuthority the role authority of this RoleSyntax.
	 *  @param roleName    the role name of this RoleSyntax.
	 */
	public RoleSyntax(GeneralNames roleAuthority, GeneralName roleName) {
	}

	/**
	 *  Constructor. Invoking this constructor is the same as invoking
	 *  <code>new RoleSyntax(null, roleName)</code>.
	 *  @param roleName    the role name of this RoleSyntax.
	 */
	public RoleSyntax(GeneralName roleName) {
	}

	/**
	 *  Utility constructor. Takes a <code>String</code> argument representing
	 *  the role name, builds a <code>GeneralName</code> to hold the role name
	 *  and calls the constructor that takes a <code>GeneralName</code>.
	 *  @param roleName
	 */
	public RoleSyntax(String roleName) {
	}

	/**
	 *  RoleSyntax factory method.
	 *  @param obj the object used to construct an instance of <code>
	 *  RoleSyntax</code>. It must be an instance of <code>RoleSyntax
	 *  </code> or <code>ASN1Sequence</code>.
	 *  @return the instance of <code>RoleSyntax</code> built from the
	 *  supplied object.
	 *  @throws java.lang.IllegalArgumentException if the object passed
	 *  to the factory is not an instance of <code>RoleSyntax</code> or
	 *  <code>ASN1Sequence</code>.
	 */
	public static RoleSyntax getInstance(Object obj) {
	}

	/**
	 *  Gets the role authority of this RoleSyntax.
	 *  @return    an instance of <code>GeneralNames</code> holding the
	 *  role authority of this RoleSyntax.
	 */
	public GeneralNames getRoleAuthority() {
	}

	/**
	 *  Gets the role name of this RoleSyntax.
	 *  @return    an instance of <code>GeneralName</code> holding the
	 *  role name of this RoleSyntax.
	 */
	public GeneralName getRoleName() {
	}

	/**
	 *  Gets the role name as a <code>java.lang.String</code> object.
	 *  @return    the role name of this RoleSyntax represented as a 
	 *  <code>java.lang.String</code> object.
	 */
	public String getRoleNameAsString() {
	}

	/**
	 *  Gets the role authority as a <code>String[]</code> object.
	 *  @return the role authority of this RoleSyntax represented as a
	 *  <code>String[]</code> array.
	 */
	public String[] getRoleAuthorityAsString() {
	}

	/**
	 *  Implementation of the method <code>toASN1Object</code> as
	 *  required by the superclass <code>ASN1Encodable</code>.
	 *  
	 *  <pre>
	 *  RoleSyntax ::= SEQUENCE {
	 *                  roleAuthority  [0] GeneralNames OPTIONAL,
	 *                  roleName       [1] GeneralName
	 *            } 
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}

	public String toString() {
	}
}
