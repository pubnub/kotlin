/**
 * 
 * Basic cipher classes.
 */
package org.bouncycastle.crypto.engines;


/**
 *  HC-256 is a software-efficient stream cipher created by Hongjun Wu. It 
 *  generates keystream from a 256-bit secret key and a 256-bit initialization 
 *  vector.
 *  <p>
 *  http://www.ecrypt.eu.org/stream/p3ciphers/hc/hc256_p3.pdf
 *  </p><p>
 *  Its brother, HC-128, is a third phase candidate in the eStream contest.
 *  The algorithm is patent-free. No attacks are known as of today (April 2007). 
 *  See
 *  
 *  http://www.ecrypt.eu.org/stream/hcp3.html
 *  </p>
 */
public class HC256Engine implements org.bouncycastle.crypto.StreamCipher {

	public HC256Engine() {
	}

	public String getAlgorithmName() {
	}

	/**
	 *  Initialise a HC-256 cipher.
	 * 
	 *  @param forEncryption whether or not we are for encryption. Irrelevant, as
	 *                       encryption and decryption are the same.
	 *  @param params        the parameters required to set up the cipher.
	 *  @throws IllegalArgumentException if the params argument is
	 *                                   inappropriate (ie. the key is not 256 bit long).
	 */
	public void init(boolean forEncryption, org.bouncycastle.crypto.CipherParameters params) {
	}

	public void processBytes(byte[] in, int inOff, int len, byte[] out, int outOff) {
	}

	public void reset() {
	}

	public byte returnByte(byte in) {
	}
}
