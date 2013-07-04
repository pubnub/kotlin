/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


public class BERGenerator extends ASN1Generator {

	protected BERGenerator(java.io.OutputStream out) {
	}

	public BERGenerator(java.io.OutputStream out, int tagNo, boolean isExplicit) {
	}

	public java.io.OutputStream getRawOutputStream() {
	}

	protected void writeBERHeader(int tag) {
	}

	protected void writeBERBody(java.io.InputStream contentStream) {
	}

	protected void writeBEREnd() {
	}
}
