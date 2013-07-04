/**
 * 
 * Classes for creating MACs and HMACs.
 */
package org.bouncycastle.crypto.macs;


/**
 *  DES based CBC Block Cipher MAC according to ISO9797, algorithm 3 (ANSI X9.19 Retail MAC)
 * 
 *  This could as well be derived from CBCBlockCipherMac, but then the property mac in the base
 *  class must be changed to protected  
 */
public class ISO9797Alg3Mac implements org.bouncycastle.crypto.Mac {

	/**
	 *  create a Retail-MAC based on a CBC block cipher. This will produce an
	 *  authentication code of the length of the block size of the cipher.
	 * 
	 *  @param cipher the cipher to be used as the basis of the MAC generation. This must
	 *  be DESEngine.
	 */
	public ISO9797Alg3Mac(org.bouncycastle.crypto.BlockCipher cipher) {
	}

	/**
	 *  create a Retail-MAC based on a CBC block cipher. This will produce an
	 *  authentication code of the length of the block size of the cipher.
	 * 
	 *  @param cipher the cipher to be used as the basis of the MAC generation.
	 *  @param padding the padding to be used to complete the last block.
	 */
	public ISO9797Alg3Mac(org.bouncycastle.crypto.BlockCipher cipher, org.bouncycastle.crypto.paddings.BlockCipherPadding padding) {
	}

	/**
	 *  create a Retail-MAC based on a block cipher with the size of the
	 *  MAC been given in bits. This class uses single DES CBC mode as the basis for the
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
	public ISO9797Alg3Mac(org.bouncycastle.crypto.BlockCipher cipher, int macSizeInBits) {
	}

	/**
	 *  create a standard MAC based on a block cipher with the size of the
	 *  MAC been given in bits. This class uses single DES CBC mode as the basis for the
	 *  MAC generation. The final block is decrypted and then encrypted using the
	 *  middle and right part of the key.
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
	public ISO9797Alg3Mac(org.bouncycastle.crypto.BlockCipher cipher, int macSizeInBits, org.bouncycastle.crypto.paddings.BlockCipherPadding padding) {
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
