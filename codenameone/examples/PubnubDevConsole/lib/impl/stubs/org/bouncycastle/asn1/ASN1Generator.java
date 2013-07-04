/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


public abstract class ASN1Generator {

	protected java.io.OutputStream _out;

	public ASN1Generator(java.io.OutputStream out) {
	}

	public abstract java.io.OutputStream getRawOutputStream() {
	}
}
