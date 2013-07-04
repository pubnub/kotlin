/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


/**
 *  Stream that produces output based on the default encoding for the passed in objects.
 */
public class ASN1OutputStream {

	public ASN1OutputStream(java.io.OutputStream os) {
	}

	protected void writeNull() {
	}

	public void writeObject(ASN1Encodable obj) {
	}

	public void close() {
	}

	public void flush() {
	}
}
