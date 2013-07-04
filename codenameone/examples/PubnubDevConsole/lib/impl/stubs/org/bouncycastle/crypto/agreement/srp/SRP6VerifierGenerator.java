package org.bouncycastle.crypto.agreement.srp;


/**
 *  Generates new SRP verifier for user
 */
public class SRP6VerifierGenerator {

	protected javabc.BigInteger N;

	protected javabc.BigInteger g;

	protected org.bouncycastle.crypto.Digest digest;

	public SRP6VerifierGenerator() {
	}

	/**
	 *  Initialises generator to create new verifiers
	 *  @param N The safe prime to use (see DHParametersGenerator)
	 *  @param g The group parameter to use (see DHParametersGenerator)
	 *  @param digest The digest to use. The same digest type will need to be used later for the actual authentication
	 *  attempt. Also note that the final session key size is dependent on the chosen digest.
	 */
	public void init(javabc.BigInteger N, javabc.BigInteger g, org.bouncycastle.crypto.Digest digest) {
	}

	/**
	 *  Creates a new SRP verifier
	 *  @param salt The salt to use, generally should be large and random
	 *  @param identity The user's identifying information (eg. username)
	 *  @param password The user's password
	 *  @return A new verifier for use in future SRP authentication
	 */
	public javabc.BigInteger generateVerifier(byte[] salt, byte[] identity, byte[] password) {
	}
}
