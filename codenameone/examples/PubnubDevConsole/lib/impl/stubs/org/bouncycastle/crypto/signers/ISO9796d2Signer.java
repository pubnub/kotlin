/**
 * 
 * Basic signers.
 */
package org.bouncycastle.crypto.signers;


/**
 *  ISO9796-2 - mechanism using a hash function with recovery (scheme 1)
 */
public class ISO9796d2Signer implements org.bouncycastle.crypto.SignerWithRecovery {

	public static final int TRAILER_IMPLICIT = 188;

	public static final int TRAILER_RIPEMD160 = 12748;

	public static final int TRAILER_RIPEMD128 = 13004;

	public static final int TRAILER_SHA1 = 13260;

	public static final int TRAILER_SHA256 = 13516;

	public static final int TRAILER_SHA512 = 13772;

	public static final int TRAILER_SHA384 = 14028;

	public static final int TRAILER_WHIRLPOOL = 14284;

	/**
	 *  Generate a signer for the with either implicit or explicit trailers
	 *  for ISO9796-2.
	 *  
	 *  @param cipher base cipher to use for signature creation/verification
	 *  @param digest digest to use.
	 *  @param implicit whether or not the trailer is implicit or gives the hash.
	 */
	public ISO9796d2Signer(org.bouncycastle.crypto.AsymmetricBlockCipher cipher, org.bouncycastle.crypto.Digest digest, boolean implicit) {
	}

	/**
	 *  Constructor for a signer with an explicit digest trailer.
	 *  
	 *  @param cipher cipher to use.
	 *  @param digest digest to sign with.
	 */
	public ISO9796d2Signer(org.bouncycastle.crypto.AsymmetricBlockCipher cipher, org.bouncycastle.crypto.Digest digest) {
	}

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
	 *  @return true on full message recovery, false otherwise.
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
