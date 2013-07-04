/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


/**
 *  Stream that outputs encoding based on definite length.
 */
public class DLOutputStream extends ASN1OutputStream {

	public DLOutputStream(java.io.OutputStream os) {
	}

	public void writeObject(ASN1Encodable obj) {
	}
}
