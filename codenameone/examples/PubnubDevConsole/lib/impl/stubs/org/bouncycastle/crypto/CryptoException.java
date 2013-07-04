/**
 * 
 * Base classes for the lightweight API.
 */
package org.bouncycastle.crypto;


/**
 *  the foundation class for the hard exceptions thrown by the crypto packages.
 */
public class CryptoException extends Exception {

	/**
	 *  base constructor.
	 */
	public CryptoException() {
	}

	/**
	 *  create a CryptoException with the given message.
	 * 
	 *  @param message the message to be carried with the exception.
	 */
	public CryptoException(String message) {
	}

	/**
	 *  Create a CryptoException with the given message and underlying cause.
	 * 
	 *  @param message message describing exception.
	 *  @param cause the throwable that was the underlying cause.
	 */
	public CryptoException(String message, Throwable cause) {
	}

	public Throwable getCause() {
	}
}
