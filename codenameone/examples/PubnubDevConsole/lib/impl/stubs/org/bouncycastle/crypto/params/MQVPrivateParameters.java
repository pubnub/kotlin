/**
 * 
 * Classes for parameter objects for ciphers and generators.
 */
package org.bouncycastle.crypto.params;


public class MQVPrivateParameters implements org.bouncycastle.crypto.CipherParameters {

	public MQVPrivateParameters(ECPrivateKeyParameters staticPrivateKey, ECPrivateKeyParameters ephemeralPrivateKey) {
	}

	public MQVPrivateParameters(ECPrivateKeyParameters staticPrivateKey, ECPrivateKeyParameters ephemeralPrivateKey, ECPublicKeyParameters ephemeralPublicKey) {
	}

	public ECPrivateKeyParameters getStaticPrivateKey() {
	}

	public ECPrivateKeyParameters getEphemeralPrivateKey() {
	}

	public ECPublicKeyParameters getEphemeralPublicKey() {
	}
}
