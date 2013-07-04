/**
 * 
 * Basic cipher classes.
 */
package org.bouncycastle.crypto.engines;


/**
 *  a class that provides a basic DESede (or Triple DES) engine.
 */
public class DESedeEngine extends DESEngine {

	protected static final int BLOCK_SIZE = 8;

	/**
	 *  standard constructor.
	 */
	public DESedeEngine() {
	}

	/**
	 *  initialise a DESede cipher.
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

	public int getBlockSize() {
	}

	public int processBlock(byte[] in, int inOff, byte[] out, int outOff) {
	}

	public void reset() {
	}
}
