package org.bouncycastle.crypto.agreement.srp;


/**
 *  Implements the client side SRP-6a protocol. Note that this class is stateful, and therefore NOT threadsafe.
 *  This implementation of SRP is based on the optimized message sequence put forth by Thomas Wu in the paper
 *  "SRP-6: Improvements and Refinements to the Secure Remote Password Protocol, 2002"
 */
public class SRP6Client {

	protected javabc.BigInteger N;

	protected javabc.BigInteger g;

	protected javabc.BigInteger a;

	protected javabc.BigInteger A;

	protected javabc.BigInteger B;

	protected javabc.BigInteger x;

	protected javabc.BigInteger u;

	protected javabc.BigInteger S;

	protected org.bouncycastle.crypto.Digest digest;

	protected javabc.SecureRandom random;

	public SRP6Client() {
	}

	/**
	 *  Initialises the client to begin new authentication attempt
	 *  @param N The safe prime associated with the client's verifier
	 *  @param g The group parameter associated with the client's verifier
	 *  @param digest The digest algorithm associated with the client's verifier
	 *  @param random For key generation
	 */
	public void init(javabc.BigInteger N, javabc.BigInteger g, org.bouncycastle.crypto.Digest digest, javabc.SecureRandom random) {
	}

	/**
	 *  Generates client's credentials given the client's salt, identity and password
	 *  @param salt The salt used in the client's verifier.
	 *  @param identity The user's identity (eg. username)
	 *  @param password The user's password
	 *  @return Client's public value to send to server
	 */
	public javabc.BigInteger generateClientCredentials(byte[] salt, byte[] identity, byte[] password) {
	}

	/**
	 *  Generates client's verification message given the server's credentials
	 *  @param serverB The server's credentials
	 *  @return Client's verification message for the server
	 *  @throws CryptoException If server's credentials are invalid
	 */
	public javabc.BigInteger calculateSecret(javabc.BigInteger serverB) {
	}

	protected javabc.BigInteger selectPrivateValue() {
	}
}
