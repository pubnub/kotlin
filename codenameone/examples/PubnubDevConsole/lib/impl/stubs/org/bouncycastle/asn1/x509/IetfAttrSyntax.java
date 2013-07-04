/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  Implementation of <code>IetfAttrSyntax</code> as specified by RFC3281.
 */
public class IetfAttrSyntax extends org.bouncycastle.asn1.ASN1Object {

	public static final int VALUE_OCTETS = 1;

	public static final int VALUE_OID = 2;

	public static final int VALUE_UTF8 = 3;

	public static IetfAttrSyntax getInstance(Object obj) {
	}

	public GeneralNames getPolicyAuthority() {
	}

	public int getValueType() {
	}

	public Object[] getValues() {
	}

	/**
	 *  
	 *  <pre>
	 *  
	 *   IetfAttrSyntax ::= SEQUENCE {
	 *     policyAuthority [0] GeneralNames OPTIONAL,
	 *     values SEQUENCE OF CHOICE {
	 *       octets OCTET STRING,
	 *       oid OBJECT IDENTIFIER,
	 *       string UTF8String
	 *     }
	 *   }
	 *   
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
