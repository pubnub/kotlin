/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


public abstract class DERGenerator extends ASN1Generator {

	protected DERGenerator(java.io.OutputStream out) {
	}

	public DERGenerator(java.io.OutputStream out, int tagNo, boolean isExplicit) {
	}
}
