/**
 * 
 * Basic cipher classes.
 */
package org.bouncycastle.crypto.engines;


/**
 *  Wrap keys according to
 *  <A HREF="http://www.ietf.org/internet-drafts/draft-ietf-smime-key-wrap-01.txt">
 *  draft-ietf-smime-key-wrap-01.txt</A>.
 *  <p>
 *  Note: 
 *  <ul>
 *  <li>this is based on a draft, and as such is subject to change - don't use this class for anything requiring long term storage.
 *  <li>if you are using this to wrap triple-des keys you need to set the
 *  parity bits on the key and, if it's a two-key triple-des key, pad it
 *  yourself.
 *  </ul>
 */
public class DESedeWrapEngine implements org.bouncycastle.crypto.Wrapper {

	public DESedeWrapEngine() {
	}

	/**
	 *  Method init
	 * 
	 *  @param forWrapping
	 *  @param param
	 */
	public void init(boolean forWrapping, org.bouncycastle.crypto.CipherParameters param) {
	}

	/**
	 *  Method getAlgorithmName
	 * 
	 *  @return the algorithm name "DESede".
	 */
	public String getAlgorithmName() {
	}

	/**
	 *  Method wrap
	 * 
	 *  @param in
	 *  @param inOff
	 *  @param inLen
	 *  @return the wrapped bytes.
	 */
	public byte[] wrap(byte[] in, int inOff, int inLen) {
	}

	/**
	 *  Method unwrap
	 * 
	 *  @param in
	 *  @param inOff
	 *  @param inLen
	 *  @return the unwrapped bytes.
	 *  @throws InvalidCipherTextException
	 */
	public byte[] unwrap(byte[] in, int inOff, int inLen) {
	}
}
