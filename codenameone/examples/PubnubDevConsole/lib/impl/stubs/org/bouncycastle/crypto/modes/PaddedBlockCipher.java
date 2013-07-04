/**
 * 
 * Modes for symmetric ciphers.
 */
package org.bouncycastle.crypto.modes;


/**
 *  A wrapper class that allows block ciphers to be used to process data in
 *  a piecemeal fashion with PKCS5/PKCS7 padding. The PaddedBlockCipher
 *  outputs a block only when the buffer is full and more data is being added,
 *  or on a doFinal (unless the current block in the buffer is a pad block).
 *  The padding mechanism used is the one outlined in PKCS5/PKCS7.
 * 
 *  @deprecated use org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher instead.
 */
public class PaddedBlockCipher extends org.bouncycastle.crypto.BufferedBlockCipher {

	/**
	 *  Create a buffered block cipher with, or without, padding.
	 * 
	 *  @param cipher the underlying block cipher this buffering object wraps.
	 */
	public PaddedBlockCipher(org.bouncycastle.crypto.BlockCipher cipher) {
	}

	/**
	 *  return the size of the output buffer required for an update plus a
	 *  doFinal with an input of len bytes.
	 * 
	 *  @param len the length of the input.
	 *  @return the space required to accommodate a call to update and doFinal
	 *  with len bytes of input.
	 */
	public int getOutputSize(int len) {
	}

	/**
	 *  return the size of the output buffer required for an update 
	 *  an input of len bytes.
	 * 
	 *  @param len the length of the input.
	 *  @return the space required to accommodate a call to update
	 *  with len bytes of input.
	 */
	public int getUpdateOutputSize(int len) {
	}

	/**
	 *  process a single byte, producing an output block if neccessary.
	 * 
	 *  @param in the input byte.
	 *  @param out the space for any output that might be produced.
	 *  @param outOff the offset from which the output will be copied.
	 *  @exception DataLengthException if there isn't enough space in out.
	 *  @exception IllegalStateException if the cipher isn't initialised.
	 */
	public int processByte(byte in, byte[] out, int outOff) {
	}

	/**
	 *  process an array of bytes, producing output if necessary.
	 * 
	 *  @param in the input byte array.
	 *  @param inOff the offset at which the input data starts.
	 *  @param len the number of bytes to be copied out of the input array.
	 *  @param out the space for any output that might be produced.
	 *  @param outOff the offset from which the output will be copied.
	 *  @exception DataLengthException if there isn't enough space in out.
	 *  @exception IllegalStateException if the cipher isn't initialised.
	 */
	public int processBytes(byte[] in, int inOff, int len, byte[] out, int outOff) {
	}

	/**
	 *  Process the last block in the buffer. If the buffer is currently
	 *  full and padding needs to be added a call to doFinal will produce
	 *  2 * getBlockSize() bytes.
	 * 
	 *  @param out the array the block currently being held is copied into.
	 *  @param outOff the offset at which the copying starts.
	 *  @exception DataLengthException if there is insufficient space in out for
	 *  the output or we are decrypting and the input is not block size aligned.
	 *  @exception IllegalStateException if the underlying cipher is not
	 *  initialised.
	 *  @exception InvalidCipherTextException if padding is expected and not found.
	 */
	public int doFinal(byte[] out, int outOff) {
	}
}
