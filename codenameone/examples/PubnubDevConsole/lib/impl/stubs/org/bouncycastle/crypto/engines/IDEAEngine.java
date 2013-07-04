/**
 * 
 * Basic cipher classes.
 */
package org.bouncycastle.crypto.engines;


/**
 *  A class that provides a basic International Data Encryption Algorithm (IDEA) engine.
 *  <p>
 *  This implementation is based on the "HOWTO: INTERNATIONAL DATA ENCRYPTION ALGORITHM"
 *  implementation summary by Fauzan Mirza (F.U.Mirza@sheffield.ac.uk). (baring 1 typo at the
 *  end of the mulinv function!).
 *  <p>
 *  It can be found at ftp://ftp.funet.fi/pub/crypt/cryptography/symmetric/idea/
 *  <p>
 *  Note 1: This algorithm is patented in the USA, Japan, and Europe including
 *  at least Austria, France, Germany, Italy, Netherlands, Spain, Sweden, Switzerland
 *  and the United Kingdom. Non-commercial use is free, however any commercial
 *  products are liable for royalties. Please see
 *  <a href="http://www.mediacrypt.com">www.mediacrypt.com</a> for
 *  further details. This announcement has been included at the request of
 *  the patent holders.
 *  <p>
 *  Note 2: Due to the requests concerning the above, this algorithm is now only
 *  included in the extended Bouncy Castle provider and JCE signed jars. It is
 *  not included in the default distributions.
 */
public class IDEAEngine implements org.bouncycastle.crypto.BlockCipher {

	protected static final int BLOCK_SIZE = 8;

	/**
	 *  standard constructor.
	 */
	public IDEAEngine() {
	}

	/**
	 *  initialise an IDEA cipher.
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
