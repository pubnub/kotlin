package org.bouncycastle.crypto.agreement.srp;


/**
 *  Implements the server side SRP-6a protocol. Note that this class is stateful, and therefore NOT threadsafe.
 *  This implementation of SRP is based on the optimized message sequence put forth by Thomas Wu in the paper
 *  "SRP-6: Improvements and Refinements to the Secure Remote Password Protocol, 2002"
 */
public class SRP6Server {

	protected javabc.BigInteger N;

	protected javabc.BigInteger g;

	protected javabc.BigInteger v;

	protected javabc.SecureRandom random;

	protected org.bouncycastle.crypto.Digest digest;

	protected javabc.BigInteger A;

	protected javabc.BigInteger b;

	protected javabc.BigInteger B;

	protected javabc.BigInteger u;

	protected javabc.BigInteger S;

	public SRP6Server() {
	}

	/**
	 *  Initialises the server to accept a new client authentication attempt
	 *  @param N The safe prime associated with the client's verifier
	 *  @param g The group parameter associated with the client's verifier
	 *  @param v The client's verifier
	 *  @param digest The digest algorithm associated with the client's verifier
	 *  @param random For key generation
	 */
	public void init(javabc.BigInteger N, javabc.BigInteger g, javabc.BigInteger v, org.bouncycastle.crypto.Digest digest, javabc.SecureRandom random) {
	}

	/**
	 *  Generates the server's credentials that are to be sent to the client.
	 *  @return The server's public value to the client
	 */
	public javabc.BigInteger generateServerCredentials() {
	}

	/**
	 *  Processes the client's credentials. If valid the shared secret is generated and returned.
	 *  @param clientA The client's credentials
	 *  @return A shared secret BigInteger
	 *  @throws CryptoException If client's credentials are invalid
	 */
	public javabc.BigInteger calculateSecret(javabc.BigInteger clientA) {
	}

	protected javabc.BigInteger selectPrivateValue() {
	}
}
