/**
 * 
 * Basic cipher classes.
 */
package org.bouncycastle.crypto.engines;


/**
 *  This does your basic RSA Chaum's blinding and unblinding as outlined in
 *  "Handbook of Applied Cryptography", page 475. You need to use this if you are
 *  trying to get another party to generate signatures without them being aware
 *  of the message they are signing.
 */
public class RSABlindingEngine implements org.bouncycastle.crypto.AsymmetricBlockCipher {

	public RSABlindingEngine() {
	}

	/**
	 *  Initialise the blinding engine.
	 * 
	 *  @param forEncryption true if we are encrypting (blinding), false otherwise.
	 *  @param param         the necessary RSA key parameters.
	 */
	public void init(boolean forEncryption, org.bouncycastle.crypto.CipherParameters param) {
	}

	/**
	 *  Return the maximum size for an input block to this engine.
	 *  For RSA this is always one byte less than the key size on
	 *  encryption, and the same length as the key size on decryption.
	 * 
	 *  @return maximum size for an input block.
	 */
	public int getInputBlockSize() {
	}

	/**
	 *  Return the maximum size for an output block to this engine.
	 *  For RSA this is always one byte less than the key size on
	 *  decryption, and the same length as the key size on encryption.
	 * 
	 *  @return maximum size for an output block.
	 */
	public int getOutputBlockSize() {
	}

	/**
	 *  Process a single block using the RSA blinding algorithm.
	 * 
	 *  @param in    the input array.
	 *  @param inOff the offset into the input buffer where the data starts.
	 *  @param inLen the length of the data to be processed.
	 *  @return the result of the RSA process.
	 *  @throws DataLengthException the input block is too large.
	 */
	public byte[] processBlock(byte[] in, int inOff, int inLen) {
	}
}
