/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  class for breaking up an X500 Name into it's component tokens, ala
 *  java.util.StringTokenizer. We need this class as some of the
 *  lightweight Java environment don't support classes like
 *  StringTokenizer.
 */
public class X509NameTokenizer {

	public X509NameTokenizer(String oid) {
	}

	public X509NameTokenizer(String oid, char seperator) {
	}

	public boolean hasMoreTokens() {
	}

	public String nextToken() {
	}
}
