package org.bouncycastle.crypto.agreement.srp;


public class SRP6Util {

	public SRP6Util() {
	}

	public static javabc.BigInteger calculateK(org.bouncycastle.crypto.Digest digest, javabc.BigInteger N, javabc.BigInteger g) {
	}

	public static javabc.BigInteger calculateU(org.bouncycastle.crypto.Digest digest, javabc.BigInteger N, javabc.BigInteger A, javabc.BigInteger B) {
	}

	public static javabc.BigInteger calculateX(org.bouncycastle.crypto.Digest digest, javabc.BigInteger N, byte[] salt, byte[] identity, byte[] password) {
	}

	public static javabc.BigInteger generatePrivateValue(org.bouncycastle.crypto.Digest digest, javabc.BigInteger N, javabc.BigInteger g, javabc.SecureRandom random) {
	}

	public static javabc.BigInteger validatePublicValue(javabc.BigInteger N, javabc.BigInteger val) {
	}
}
