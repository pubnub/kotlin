/**
 * 
 * Basic cipher classes.
 */
package org.bouncycastle.crypto.engines;


/**
 *  A class that provides Blowfish key encryption operations,
 *  such as encoding data and generating keys.
 *  All the algorithms herein are from Applied Cryptography
 *  and implement a simplified cryptography interface.
 */
public final class BlowfishEngine implements org.bouncycastle.crypto.BlockCipher {

	public BlowfishEngine() {
	}

	/**
	 *  initialise a Blowfish cipher.
	 * 
	 *  @param encrypting whether or not we are for encryption.
	 *  @param params the parameters required to set up the cipher.
	 *  @exception IllegalArgumentException if the params argument is
	 *  inappropriate.
	 */
	public void init(boolean encrypting, org.bouncycastle.crypto.CipherParameters params) {
	}

	public String getAlgorithmName() {
	}

	public final int processBlock(byte[] in, int inOff, byte[] out, int outOff) {
	}

	public void reset() {
	}

	public int getBlockSize() {
	}
}
