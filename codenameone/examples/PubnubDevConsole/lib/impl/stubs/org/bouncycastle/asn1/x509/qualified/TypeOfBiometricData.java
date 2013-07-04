/**
 * 
 * Support classes useful for encoding and processing messages based around RFC3739
 */
package org.bouncycastle.asn1.x509.qualified;


/**
 *  The TypeOfBiometricData object.
 *  <pre>
 *  TypeOfBiometricData ::= CHOICE {
 *    predefinedBiometricType   PredefinedBiometricType,
 *    biometricDataOid          OBJECT IDENTIFIER }
 * 
 *  PredefinedBiometricType ::= INTEGER {
 *    picture(0),handwritten-signature(1)}
 *    (picture|handwritten-signature)
 *  </pre>
 */
public class TypeOfBiometricData extends org.bouncycastle.asn1.ASN1Object implements org.bouncycastle.asn1.ASN1Choice {

	public static final int PICTURE = 0;

	public static final int HANDWRITTEN_SIGNATURE = 1;

	public TypeOfBiometricData(int predefinedBiometricType) {
	}

	public TypeOfBiometricData(org.bouncycastle.asn1.ASN1ObjectIdentifier BiometricDataID) {
	}

	public static TypeOfBiometricData getInstance(Object obj) {
	}

	public boolean isPredefined() {
	}

	public int getPredefinedBiometricType() {
	}

	public org.bouncycastle.asn1.ASN1ObjectIdentifier getBiometricDataOid() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
