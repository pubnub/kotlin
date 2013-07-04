/**
 * 
 * Classes for creating MACs and HMACs.
 */
package org.bouncycastle.crypto.macs;


/**
 *  CMAC - as specified at www.nuee.nagoya-u.ac.jp/labs/tiwata/omac/omac.html
 *  <p>
 *  CMAC is analogous to OMAC1 - see also en.wikipedia.org/wiki/CMAC
 *  </p><p>
 *  CMAC is a NIST recomendation - see 
 *  csrc.nist.gov/CryptoToolkit/modes/800-38_Series_Publications/SP800-38B.pdf
 *  </p><p>
 *  CMAC/OMAC1 is a blockcipher-based message authentication code designed and
 *  analyzed by Tetsu Iwata and Kaoru Kurosawa.
 *  </p><p>
 *  CMAC/OMAC1 is a simple variant of the CBC MAC (Cipher Block Chaining Message 
 *  Authentication Code). OMAC stands for One-Key CBC MAC.
 *  </p><p>
 *  It supports 128- or 64-bits block ciphers, with any key size, and returns
 *  a MAC with dimension less or equal to the block size of the underlying 
 *  cipher.
 *  </p>
 */
public class CMac implements org.bouncycastle.crypto.Mac {

	/**
	 *  create a standard MAC based on a CBC block cipher (64 or 128 bit block).
	 *  This will produce an authentication code the length of the block size
	 *  of the cipher.
	 * 
	 *  @param cipher the cipher to be used as the basis of the MAC generation.
	 */
	public CMac(org.bouncycastle.crypto.BlockCipher cipher) {
	}

	/**
	 *  create a standard MAC based on a block cipher with the size of the
	 *  MAC been given in bits.
	 *  <p/>
	 *  Note: the size of the MAC must be at least 24 bits (FIPS Publication 81),
	 *  or 16 bits if being used as a data authenticator (FIPS Publication 113),
	 *  and in general should be less than the size of the block cipher as it reduces
	 *  the chance of an exhaustive attack (see Handbook of Applied Cryptography).
	 * 
	 *  @param cipher        the cipher to be used as the basis of the MAC generation.
	 *  @param macSizeInBits the size of the MAC in bits, must be a multiple of 8 and <= 128.
	 */
	public CMac(org.bouncycastle.crypto.BlockCipher cipher, int macSizeInBits) {
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
