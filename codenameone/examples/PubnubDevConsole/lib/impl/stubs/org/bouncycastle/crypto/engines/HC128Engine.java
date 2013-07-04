/**
 * 
 * Basic cipher classes.
 */
package org.bouncycastle.crypto.engines;


/**
 *  HC-128 is a software-efficient stream cipher created by Hongjun Wu. It
 *  generates keystream from a 128-bit secret key and a 128-bit initialization
 *  vector.
 *  <p>
 *  http://www.ecrypt.eu.org/stream/p3ciphers/hc/hc128_p3.pdf
 *  </p><p>
 *  It is a third phase candidate in the eStream contest, and is patent-free.
 *  No attacks are known as of today (April 2007). See
 * 
 *  http://www.ecrypt.eu.org/stream/hcp3.html
 *  </p>
 */
public class HC128Engine implements org.bouncycastle.crypto.StreamCipher {

	public HC128Engine() {
	}

	public String getAlgorithmName() {
	}

	/**
	 *  Initialise a HC-128 cipher.
	 * 
	 *  @param forEncryption whether or not we are for encryption. Irrelevant, as
	 *                       encryption and decryption are the same.
	 *  @param params        the parameters required to set up the cipher.
	 *  @throws IllegalArgumentException if the params argument is
	 *                                   inappropriate (ie. the key is not 128 bit long).
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
