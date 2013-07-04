/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


/**
 *  A generic TLS MAC implementation, which can be used with any kind of Digest to act as
 *  an HMAC.
 */
public class TlsMac {

	protected TlsClientContext context;

	protected long seqNo;

	protected byte[] secret;

	protected org.bouncycastle.crypto.Mac mac;

	/**
	 *  Generate a new instance of an TlsMac.
	 *  
	 *  @param context the TLS client context
	 *  @param digest The digest to use.
	 *  @param key_block A byte-array where the key for this mac is located.
	 *  @param offset The number of bytes to skip, before the key starts in the buffer.
	 *  @param len The length of the key.
	 */
	public TlsMac(TlsClientContext context, org.bouncycastle.crypto.Digest digest, byte[] key_block, int offset, int len) {
	}

	/**
	 *  @return the MAC write secret
	 */
	public byte[] getMACSecret() {
	}

	/**
	 *  @return the current write sequence number
	 */
	public long getSequenceNumber() {
	}

	/**
	 *  Increment the current write sequence number
	 */
	public void incSequenceNumber() {
	}

	/**
	 *  @return The Keysize of the mac.
	 */
	public int getSize() {
	}

	/**
	 *  Calculate the mac for some given data.
	 *  <p/>
	 *  TlsMac will keep track of the sequence number internally.
	 *  
	 *  @param type The message type of the message.
	 *  @param message A byte-buffer containing the message.
	 *  @param offset The number of bytes to skip, before the message starts.
	 *  @param len The length of the message.
	 *  @return A new byte-buffer containing the mac value.
	 */
	public byte[] calculateMac(short type, byte[] message, int offset, int len) {
	}
}
