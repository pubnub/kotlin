/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


/**
 *  class for breaking up an OID into it's component tokens, ala
 *  java.util.StringTokenizer. We need this class as some of the
 *  lightweight Java environment don't support classes like
 *  StringTokenizer.
 */
public class OIDTokenizer {

	public OIDTokenizer(String oid) {
	}

	public boolean hasMoreTokens() {
	}

	public String nextToken() {
	}
}
