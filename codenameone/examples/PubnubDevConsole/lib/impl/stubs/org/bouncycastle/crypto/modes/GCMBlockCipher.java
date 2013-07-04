/**
 * 
 * Modes for symmetric ciphers.
 */
package org.bouncycastle.crypto.modes;


/**
 *  Implements the Galois/Counter mode (GCM) detailed in
 *  NIST Special Publication 800-38D.
 */
public class GCMBlockCipher implements AEADBlockCipher {

	public GCMBlockCipher(org.bouncycastle.crypto.BlockCipher c) {
	}

	public GCMBlockCipher(org.bouncycastle.crypto.BlockCipher c, gcm.GCMMultiplier m) {
	}

	public org.bouncycastle.crypto.BlockCipher getUnderlyingCipher() {
	}

	public String getAlgorithmName() {
	}

	public void init(boolean forEncryption, org.bouncycastle.crypto.CipherParameters params) {
	}

	public byte[] getMac() {
	}

	public int getOutputSize(int len) {
	}

	public int getUpdateOutputSize(int len) {
	}

	public int processByte(byte in, byte[] out, int outOff) {
	}

	public int processBytes(byte[] in, int inOff, int len, byte[] out, int outOff) {
	}

	public int doFinal(byte[] out, int outOff) {
	}

	public void reset() {
	}
}
