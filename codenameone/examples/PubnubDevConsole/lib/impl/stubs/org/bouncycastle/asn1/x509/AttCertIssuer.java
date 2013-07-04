/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


public class AttCertIssuer extends org.bouncycastle.asn1.ASN1Object implements org.bouncycastle.asn1.ASN1Choice {

	/**
	 *  Don't use this one if you are trying to be RFC 3281 compliant.
	 *  Use it for v1 attribute certificates only.
	 *  
	 *  @param names our GeneralNames structure
	 */
	public AttCertIssuer(GeneralNames names) {
	}

	public AttCertIssuer(V2Form v2Form) {
	}

	public static AttCertIssuer getInstance(Object obj) {
	}

	public static AttCertIssuer getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public org.bouncycastle.asn1.ASN1Encodable getIssuer() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  <pre>
	 *   AttCertIssuer ::= CHOICE {
	 *        v1Form   GeneralNames,  -- MUST NOT be used in this
	 *                                -- profile
	 *        v2Form   [0] V2Form     -- v2 only
	 *   }
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
