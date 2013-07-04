/**
 * 
 * Classes for creating MACs and HMACs.
 */
package org.bouncycastle.crypto.macs;


public class BlockCipherMac implements org.bouncycastle.crypto.Mac {

	/**
	 *  create a standard MAC based on a block cipher. This will produce an
	 *  authentication code half the length of the block size of the cipher.
	 * 
	 *  @param cipher the cipher to be used as the basis of the MAC generation.
	 *  @deprecated use CBCBlockCipherMac
	 */
	public BlockCipherMac(org.bouncycastle.crypto.BlockCipher cipher) {
	}

	/**
	 *  create a standard MAC based on a block cipher with the size of the
	 *  MAC been given in bits.
	 *  <p>
	 *  Note: the size of the MAC must be at least 16 bits (FIPS Publication 113),
	 *  and in general should be less than the size of the block cipher as it reduces
	 *  the chance of an exhaustive attack (see Handbook of Applied Cryptography).
	 * 
	 *  @param cipher the cipher to be used as the basis of the MAC generation.
	 *  @param macSizeInBits the size of the MAC in bits, must be a multiple of 8.
	 *  @deprecated use CBCBlockCipherMac
	 */
	public BlockCipherMac(org.bouncycastle.crypto.BlockCipher cipher, int macSizeInBits) {
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
