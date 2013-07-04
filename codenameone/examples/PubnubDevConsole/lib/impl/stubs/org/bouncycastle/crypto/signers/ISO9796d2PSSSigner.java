/**
 * 
 * Basic signers.
 */
package org.bouncycastle.crypto.signers;


/**
 *  ISO9796-2 - mechanism using a hash function with recovery (scheme 2 and 3).
 *  <p>
 *  Note: the usual length for the salt is the length of the hash 
 *  function used in bytes.
 */
public class ISO9796d2PSSSigner implements org.bouncycastle.crypto.SignerWithRecovery {

	public static final int TRAILER_IMPLICIT = 188;

	public static final int TRAILER_RIPEMD160 = 12748;

	public static final int TRAILER_RIPEMD128 = 13004;

	public static final int TRAILER_SHA1 = 13260;

	/**
	 *  Generate a signer for the with either implicit or explicit trailers
	 *  for ISO9796-2, scheme 2 or 3.
	 *  
	 *  @param cipher base cipher to use for signature creation/verification
	 *  @param digest digest to use.
	 *  @param saltLength length of salt in bytes.
	 *  @param implicit whether or not the trailer is implicit or gives the hash.
	 */
	public ISO9796d2PSSSigner(org.bouncycastle.crypto.AsymmetricBlockCipher cipher, org.bouncycastle.crypto.Digest digest, int saltLength, boolean implicit) {
	}

	/**
	 *  Constructor for a signer with an explicit digest trailer.
	 *  
	 *  @param cipher cipher to use.
	 *  @param digest digest to sign with.
	 *  @param saltLength length of salt in bytes.
	 */
	public ISO9796d2PSSSigner(org.bouncycastle.crypto.AsymmetricBlockCipher cipher, org.bouncycastle.crypto.Digest digest, int saltLength) {
	}

	/**
	 *  Initialise the signer.
	 *  
	 *  @param forSigning true if for signing, false if for verification.
	 *  @param param parameters for signature generation/verification. If the
	 *  parameters are for generation they should be a ParametersWithRandom,
	 *  a ParametersWithSalt, or just an RSAKeyParameters object. If RSAKeyParameters
	 *  are passed in a SecureRandom will be created.
	 *  @exception IllegalArgumentException if wrong parameter type or a fixed 
	 *  salt is passed in which is the wrong length.
	 */
	public void init(boolean forSigning, org.bouncycastle.crypto.CipherParameters param) {
	}

	public void updateWithRecoveredMessage(byte[] signature) {
	}

	/**
	 *  update the internal digest with the byte b
	 */
	public void update(byte b) {
	}

	/**
	 *  update the internal digest with the byte array in
	 */
	public void update(byte[] in, int off, int len) {
	}

	/**
	 *  reset the internal state
	 */
	public void reset() {
	}

	/**
	 *  generate a signature for the loaded message using the key we were
	 *  initialised with.
	 */
	public byte[] generateSignature() {
	}

	/**
	 *  return true if the signature represents a ISO9796-2 signature
	 *  for the passed in message.
	 */
	public boolean verifySignature(byte[] signature) {
	}

	/**
	 *  Return true if the full message was recoveredMessage.
	 *  
	 *  @return true on full message recovery, false otherwise, or if not sure.
	 *  @see org.bouncycastle.crypto.SignerWithRecovery#hasFullMessage()
	 */
	public boolean hasFullMessage() {
	}

	/**
	 *  Return a reference to the recoveredMessage message.
	 *  
	 *  @return the full/partial recoveredMessage message.
	 *  @see org.bouncycastle.crypto.SignerWithRecovery#getRecoveredMessage()
	 */
	public byte[] getRecoveredMessage() {
	}
}
