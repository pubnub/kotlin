/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


public class DSAParameter extends org.bouncycastle.asn1.ASN1Object {

	public DSAParameter(javabc.BigInteger p, javabc.BigInteger q, javabc.BigInteger g) {
	}

	public DSAParameter(org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	public static DSAParameter getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public static DSAParameter getInstance(Object obj) {
	}

	public javabc.BigInteger getP() {
	}

	public javabc.BigInteger getQ() {
	}

	public javabc.BigInteger getG() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
