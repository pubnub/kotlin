/**
 * 
 * Support classes useful for encoding and processing messages based around RFC3739
 */
package org.bouncycastle.asn1.x509.qualified;


/**
 *  The BiometricData object.
 *  <pre>
 *  BiometricData  ::=  SEQUENCE {
 *        typeOfBiometricData  TypeOfBiometricData,
 *        hashAlgorithm        AlgorithmIdentifier,
 *        biometricDataHash    OCTET STRING,
 *        sourceDataUri        IA5String OPTIONAL  }
 *  </pre>
 */
public class BiometricData extends org.bouncycastle.asn1.ASN1Object {

	public BiometricData(TypeOfBiometricData typeOfBiometricData, org.bouncycastle.asn1.x509.AlgorithmIdentifier hashAlgorithm, org.bouncycastle.asn1.ASN1OctetString biometricDataHash, org.bouncycastle.asn1.DERIA5String sourceDataUri) {
	}

	public BiometricData(TypeOfBiometricData typeOfBiometricData, org.bouncycastle.asn1.x509.AlgorithmIdentifier hashAlgorithm, org.bouncycastle.asn1.ASN1OctetString biometricDataHash) {
	}

	public static BiometricData getInstance(Object obj) {
	}

	public TypeOfBiometricData getTypeOfBiometricData() {
	}

	public org.bouncycastle.asn1.x509.AlgorithmIdentifier getHashAlgorithm() {
	}

	public org.bouncycastle.asn1.ASN1OctetString getBiometricDataHash() {
	}

	public org.bouncycastle.asn1.DERIA5String getSourceDataUri() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
