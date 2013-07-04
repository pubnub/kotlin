/**
 * 
 * Basic cipher classes.
 */
package org.bouncycastle.crypto.engines;


/**
 *  a class that provides a basic DES engine.
 */
public class DESEngine implements org.bouncycastle.crypto.BlockCipher {

	protected static final int BLOCK_SIZE = 8;

	/**
	 *  standard constructor.
	 */
	public DESEngine() {
	}

	/**
	 *  initialise a DES cipher.
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

	/**
	 *  generate an integer based working key based on our secret key
	 *  and what we processing we are planning to do.
	 * 
	 *  Acknowledgements for this routine go to James Gillogly & Phil Karn.
	 *          (whoever, and wherever they are!).
	 */
	protected int[] generateWorkingKey(boolean encrypting, byte[] key) {
	}

	/**
	 *  the DES engine.
	 */
	protected void desFunc(int[] wKey, byte[] in, int inOff, byte[] out, int outOff) {
	}
}
