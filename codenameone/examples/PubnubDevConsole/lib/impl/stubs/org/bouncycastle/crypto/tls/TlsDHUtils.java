/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


public class TlsDHUtils {

	public TlsDHUtils() {
	}

	public static byte[] calculateDHBasicAgreement(org.bouncycastle.crypto.params.DHPublicKeyParameters publicKey, org.bouncycastle.crypto.params.DHPrivateKeyParameters privateKey) {
	}

	public static org.bouncycastle.crypto.AsymmetricCipherKeyPair generateDHKeyPair(javabc.SecureRandom random, org.bouncycastle.crypto.params.DHParameters dhParams) {
	}

	public static org.bouncycastle.crypto.params.DHPrivateKeyParameters generateEphemeralClientKeyExchange(javabc.SecureRandom random, org.bouncycastle.crypto.params.DHParameters dhParams, java.io.OutputStream os) {
	}

	public static org.bouncycastle.crypto.params.DHPublicKeyParameters validateDHPublicKey(org.bouncycastle.crypto.params.DHPublicKeyParameters key) {
	}
}
