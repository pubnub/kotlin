/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


public class GeneralNames extends org.bouncycastle.asn1.ASN1Object {

	/**
	 *  Construct a GeneralNames object containing one GeneralName.
	 *  
	 *  @param name the name to be contained.
	 */
	public GeneralNames(GeneralName name) {
	}

	public GeneralNames(GeneralName[] names) {
	}

	public static GeneralNames getInstance(Object obj) {
	}

	public static GeneralNames getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public GeneralName[] getNames() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  <pre>
	 *  GeneralNames ::= SEQUENCE SIZE {1..MAX} OF GeneralName
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}

	public String toString() {
	}
}
