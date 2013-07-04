/**
 * 
 * Basic cipher classes.
 */
package org.bouncycastle.crypto.engines;


/**
 *  an implementation of the AES (Rijndael), from FIPS-197.
 *  <p>
 *  For further details see: <a href="http://csrc.nist.gov/encryption/aes/">http://csrc.nist.gov/encryption/aes/</a>.
 * 
 *  This implementation is based on optimizations from Dr. Brian Gladman's paper and C code at
 *  <a href="http://fp.gladman.plus.com/cryptography_technology/rijndael/">http://fp.gladman.plus.com/cryptography_technology/rijndael/</a>
 * 
 *  There are three levels of tradeoff of speed vs memory
 *  Because java has no preprocessor, they are written as three separate classes from which to choose
 * 
 *  The fastest uses 8Kbytes of static tables to precompute round calculations, 4 256 word tables for encryption
 *  and 4 for decryption.
 * 
 *  The middle performance version uses only one 256 word table for each, for a total of 2Kbytes,
 *  adding 12 rotate operations per round to compute the values contained in the other tables from
 *  the contents of the first
 * 
 *  The slowest version uses no static tables at all and computes the values
 *  in each round.
 *  <p>
 *  This file contains the slowest performance version with no static tables
 *  for round precomputation, but it has the smallest foot print.
 */
public class AESLightEngine implements org.bouncycastle.crypto.BlockCipher {

	/**
	 *  default constructor - 128 bit block size.
	 */
	public AESLightEngine() {
	}

	/**
	 *  initialise an AES cipher.
	 * 
	 *  @param forEncryption whether or not we are for encryption.
	 *  @param params the parameters required to set up the cipher.
	 *  @exception IllegalArgumentException if the params argument is
	 *  inappropriate.
	 */
	public void init(boolean forEncryption, org.bouncycastle.crypto.CipherParameters params) {
	}

	public String getAlgorithmName() {
	}

	public int getBlockSize() {
	}

	public int processBlock(byte[] in, int inOff, byte[] out, int outOff) {
	}

	public void reset() {
	}
}
