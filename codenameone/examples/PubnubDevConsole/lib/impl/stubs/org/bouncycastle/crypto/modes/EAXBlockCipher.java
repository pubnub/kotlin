/**
 * 
 * Modes for symmetric ciphers.
 */
package org.bouncycastle.crypto.modes;


/**
 *  A Two-Pass Authenticated-Encryption Scheme Optimized for Simplicity and 
 *  Efficiency - by M. Bellare, P. Rogaway, D. Wagner.
 *  
 *  http://www.cs.ucdavis.edu/~rogaway/papers/eax.pdf
 *  
 *  EAX is an AEAD scheme based on CTR and OMAC1/CMAC, that uses a single block 
 *  cipher to encrypt and authenticate data. It's on-line (the length of a 
 *  message isn't needed to begin processing it), has good performances, it's
 *  simple and provably secure (provided the underlying block cipher is secure).
 *  
 *  Of course, this implementations is NOT thread-safe.
 */
public class EAXBlockCipher implements AEADBlockCipher {

	/**
	 *  Constructor that accepts an instance of a block cipher engine.
	 * 
	 *  @param cipher the engine to use
	 */
	public EAXBlockCipher(org.bouncycastle.crypto.BlockCipher cipher) {
	}

	public String getAlgorithmName() {
	}

	public org.bouncycastle.crypto.BlockCipher getUnderlyingCipher() {
	}

	public int getBlockSize() {
	}

	public void init(boolean forEncryption, org.bouncycastle.crypto.CipherParameters params) {
	}

	public void reset() {
	}

	public int processByte(byte in, byte[] out, int outOff) {
	}

	public int processBytes(byte[] in, int inOff, int len, byte[] out, int outOff) {
	}

	public int doFinal(byte[] out, int outOff) {
	}

	public byte[] getMac() {
	}

	public int getUpdateOutputSize(int len) {
	}

	public int getOutputSize(int len) {
	}
}
