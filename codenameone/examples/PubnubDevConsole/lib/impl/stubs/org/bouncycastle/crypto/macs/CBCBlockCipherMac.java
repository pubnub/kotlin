/**
 * 
 * Classes for creating MACs and HMACs.
 */
package org.bouncycastle.crypto.macs;


/**
 *  standard CBC Block Cipher MAC - if no padding is specified the default of
 *  pad of zeroes is used.
 */
public class CBCBlockCipherMac implements org.bouncycastle.crypto.Mac {

	/**
	 *  create a standard MAC based on a CBC block cipher. This will produce an
	 *  authentication code half the length of the block size of the cipher.
	 * 
	 *  @param cipher the cipher to be used as the basis of the MAC generation.
	 */
	public CBCBlockCipherMac(org.bouncycastle.crypto.BlockCipher cipher) {
	}

	/**
	 *  create a standard MAC based on a CBC block cipher. This will produce an
	 *  authentication code half the length of the block size of the cipher.
	 * 
	 *  @param cipher the cipher to be used as the basis of the MAC generation.
	 *  @param padding the padding to be used to complete the last block.
	 */
	public CBCBlockCipherMac(org.bouncycastle.crypto.BlockCipher cipher, org.bouncycastle.crypto.paddings.BlockCipherPadding padding) {
	}

	/**
	 *  create a standard MAC based on a block cipher with the size of the
	 *  MAC been given in bits. This class uses CBC mode as the basis for the
	 *  MAC generation.
	 *  <p>
	 *  Note: the size of the MAC must be at least 24 bits (FIPS Publication 81),
	 *  or 16 bits if being used as a data authenticator (FIPS Publication 113),
	 *  and in general should be less than the size of the block cipher as it reduces
	 *  the chance of an exhaustive attack (see Handbook of Applied Cryptography).
	 * 
	 *  @param cipher the cipher to be used as the basis of the MAC generation.
	 *  @param macSizeInBits the size of the MAC in bits, must be a multiple of 8.
	 */
	public CBCBlockCipherMac(org.bouncycastle.crypto.BlockCipher cipher, int macSizeInBits) {
	}

	/**
	 *  create a standard MAC based on a block cipher with the size of the
	 *  MAC been given in bits. This class uses CBC mode as the basis for the
	 *  MAC generation.
	 *  <p>
	 *  Note: the size of the MAC must be at least 24 bits (FIPS Publication 81),
	 *  or 16 bits if being used as a data authenticator (FIPS Publication 113),
	 *  and in general should be less than the size of the block cipher as it reduces
	 *  the chance of an exhaustive attack (see Handbook of Applied Cryptography).
	 * 
	 *  @param cipher the cipher to be used as the basis of the MAC generation.
	 *  @param macSizeInBits the size of the MAC in bits, must be a multiple of 8.
	 *  @param padding the padding to be used to complete the last block.
	 */
	public CBCBlockCipherMac(org.bouncycastle.crypto.BlockCipher cipher, int macSizeInBits, org.bouncycastle.crypto.paddings.BlockCipherPadding padding) {
	}

	public String getAlgorithmName() {
	}

	public void init(org.bouncycastle.crypto.CipherParameters params) {
	}

	public int getMacSize() {
	}

	public void update(byte in) {
	}

	public void update(byte[] in, int inOff, int len) {
	}

	public int doFinal(byte[] out, int outOff) {
	}

	/**
	 *  Reset the mac generator.
	 */
	public void reset() {
	}
}
