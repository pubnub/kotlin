/**
 * 
 * Basic cipher classes.
 */
package org.bouncycastle.crypto.engines;


/**
 *  Implementation of Daniel J. Bernstein's Salsa20 stream cipher, Snuffle 2005
 */
public class Salsa20Engine implements org.bouncycastle.crypto.StreamCipher {

	public Salsa20Engine() {
	}

	/**
	 *  initialise a Salsa20 cipher.
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

	public byte returnByte(byte in) {
	}

	public void processBytes(byte[] in, int inOff, int len, byte[] out, int outOff) {
	}

	public void reset() {
	}

	/**
	 *  Salsa20 function
	 * 
	 *  @param   input   input data
	 * 
	 *  @return  keystream
	 */
	public static void salsaCore(int rounds, int[] input, int[] x) {
	}
}
