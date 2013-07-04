/**
 * 
 * Basic cipher classes.
 */
package org.bouncycastle.crypto.engines;


/**
 *  The no-op engine that just copies bytes through, irrespective of whether encrypting and decrypting.
 *  Provided for the sake of completeness.
 */
public class NullEngine implements org.bouncycastle.crypto.BlockCipher {

	protected static final int BLOCK_SIZE = 1;

	/**
	 *  Standard constructor.
	 */
	public NullEngine() {
	}

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
