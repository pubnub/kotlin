/**
 * 
 * Modes for symmetric ciphers.
 */
package org.bouncycastle.crypto.modes;


/**
 *  A Cipher Text Stealing (CTS) mode cipher. CTS allows block ciphers to
 *  be used to produce cipher text which is the same length as the plain text.
 */
public class CTSBlockCipher extends org.bouncycastle.crypto.BufferedBlockCipher {

	/**
	 *  Create a buffered block cipher that uses Cipher Text Stealing
	 * 
	 *  @param cipher the underlying block cipher this buffering object wraps.
	 */
	public CTSBlockCipher(org.bouncycastle.crypto.BlockCipher cipher) {
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
	 *  process a single byte, producing an output block if neccessary.
	 * 
	 *  @param in the input byte.
	 *  @param out the space for any output that might be produced.
	 *  @param outOff the offset from which the output will be copied.
	 *  @return the number of output bytes copied to out.
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
	 *  @return the number of output bytes copied to out.
	 *  @exception DataLengthException if there isn't enough space in out.
	 *  @exception IllegalStateException if the cipher isn't initialised.
	 */
	public int processBytes(byte[] in, int inOff, int len, byte[] out, int outOff) {
	}

	/**
	 *  Process the last block in the buffer.
	 * 
	 *  @param out the array the block currently being held is copied into.
	 *  @param outOff the offset at which the copying starts.
	 *  @return the number of output bytes copied to out.
	 *  @exception DataLengthException if there is insufficient space in out for
	 *  the output.
	 *  @exception IllegalStateException if the underlying cipher is not
	 *  initialised.
	 *  @exception InvalidCipherTextException if cipher text decrypts wrongly (in
	 *  case the exception will never get thrown).
	 */
	public int doFinal(byte[] out, int outOff) {
	}
}
