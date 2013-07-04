/**
 * 
 * Support classes useful for encoding and supporting X9.62 elliptic curve.
 */
package org.bouncycastle.asn1.x9;


public class DHValidationParms extends org.bouncycastle.asn1.ASN1Object {

	public DHValidationParms(org.bouncycastle.asn1.DERBitString seed, org.bouncycastle.asn1.ASN1Integer pgenCounter) {
	}

	public static DHValidationParms getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public static DHValidationParms getInstance(Object obj) {
	}

	public org.bouncycastle.asn1.DERBitString getSeed() {
	}

	public org.bouncycastle.asn1.ASN1Integer getPgenCounter() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
