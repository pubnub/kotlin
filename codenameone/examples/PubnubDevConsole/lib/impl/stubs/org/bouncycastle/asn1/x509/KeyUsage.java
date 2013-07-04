/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  The KeyUsage object.
 *  <pre>
 *     id-ce-keyUsage OBJECT IDENTIFIER ::=  { id-ce 15 }
 * 
 *     KeyUsage ::= BIT STRING {
 *          digitalSignature        (0),
 *          nonRepudiation          (1),
 *          keyEncipherment         (2),
 *          dataEncipherment        (3),
 *          keyAgreement            (4),
 *          keyCertSign             (5),
 *          cRLSign                 (6),
 *          encipherOnly            (7),
 *          decipherOnly            (8) }
 *  </pre>
 */
public class KeyUsage extends org.bouncycastle.asn1.DERBitString {

	public static final int digitalSignature = 128;

	public static final int nonRepudiation = 64;

	public static final int keyEncipherment = 32;

	public static final int dataEncipherment = 16;

	public static final int keyAgreement = 8;

	public static final int keyCertSign = 4;

	public static final int cRLSign = 2;

	public static final int encipherOnly = 1;

	public static final int decipherOnly = 32768;

	/**
	 *  Basic constructor.
	 *  
	 *  @param usage - the bitwise OR of the Key Usage flags giving the
	 *  allowed uses for the key.
	 *  e.g. (KeyUsage.keyEncipherment | KeyUsage.dataEncipherment)
	 */
	public KeyUsage(int usage) {
	}

	public KeyUsage(org.bouncycastle.asn1.DERBitString usage) {
	}

	public static org.bouncycastle.asn1.DERBitString getInstance(Object obj) {
	}

	public String toString() {
	}
}
