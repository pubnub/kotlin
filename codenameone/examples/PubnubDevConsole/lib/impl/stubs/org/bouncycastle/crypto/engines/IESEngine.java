/**
 * 
 * Basic cipher classes.
 */
package org.bouncycastle.crypto.engines;


/**
 *  support class for constructing integrated encryption ciphers
 *  for doing basic message exchanges on top of key agreement ciphers
 */
public class IESEngine {

	/**
	 *  set up for use with stream mode, where the key derivation function
	 *  is used to provide a stream of bytes to xor with the message.
	 * 
	 *  @param agree the key agreement used as the basis for the encryption
	 *  @param kdf the key derivation function used for byte generation
	 *  @param mac the message authentication code generator for the message
	 */
	public IESEngine(org.bouncycastle.crypto.BasicAgreement agree, org.bouncycastle.crypto.DerivationFunction kdf, org.bouncycastle.crypto.Mac mac) {
	}

	/**
	 *  set up for use in conjunction with a block cipher to handle the
	 *  message.
	 * 
	 *  @param agree the key agreement used as the basis for the encryption
	 *  @param kdf the key derivation function used for byte generation
	 *  @param mac the message authentication code generator for the message
	 *  @param cipher the cipher to used for encrypting the message
	 */
	public IESEngine(org.bouncycastle.crypto.BasicAgreement agree, org.bouncycastle.crypto.DerivationFunction kdf, org.bouncycastle.crypto.Mac mac, org.bouncycastle.crypto.BufferedBlockCipher cipher) {
	}

	/**
	 *  Initialise the encryptor.
	 * 
	 *  @param forEncryption whether or not this is encryption/decryption.
	 *  @param privParam our private key parameters
	 *  @param pubParam the recipient's/sender's public key parameters
	 *  @param param encoding and derivation parameters.
	 */
	public void init(boolean forEncryption, org.bouncycastle.crypto.CipherParameters privParam, org.bouncycastle.crypto.CipherParameters pubParam, org.bouncycastle.crypto.CipherParameters param) {
	}

	public byte[] processBlock(byte[] in, int inOff, int inLen) {
	}
}
