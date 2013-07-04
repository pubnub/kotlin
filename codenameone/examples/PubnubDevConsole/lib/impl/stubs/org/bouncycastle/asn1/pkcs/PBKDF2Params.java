/**
 * 
 * Support classes useful for encoding and supporting the various RSA PKCS documents.
 */
package org.bouncycastle.asn1.pkcs;


public class PBKDF2Params extends org.bouncycastle.asn1.ASN1Object {

	public PBKDF2Params(byte[] salt, int iterationCount) {
	}

	public PBKDF2Params(byte[] salt, int iterationCount, int keyLength) {
	}

	public static PBKDF2Params getInstance(Object obj) {
	}

	public byte[] getSalt() {
	}

	public javabc.BigInteger getIterationCount() {
	}

	public javabc.BigInteger getKeyLength() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
