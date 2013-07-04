/**
 * 
 * Classes for producing and reading Base64 and Hex strings.
 */
package org.bouncycastle.util.encoders;


/**
 *  a buffering class to allow translation from one format to another to
 *  be done in discrete chunks.
 */
public class BufferedEncoder {

	protected byte[] buf;

	protected int bufOff;

	protected Translator translator;

	/**
	 *  @param translator the translator to use.
	 *  @param bufSize amount of input to buffer for each chunk.
	 */
	public BufferedEncoder(Translator translator, int bufSize) {
	}

	public int processByte(byte in, byte[] out, int outOff) {
	}

	public int processBytes(byte[] in, int inOff, int len, byte[] out, int outOff) {
	}
}
