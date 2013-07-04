/**
 * 
 * Support classes useful for encoding and supporting the various RSA PKCS documents.
 */
package org.bouncycastle.asn1.pkcs;


public class MacData extends org.bouncycastle.asn1.ASN1Object {

	public MacData(org.bouncycastle.asn1.x509.DigestInfo digInfo, byte[] salt, int iterationCount) {
	}

	public static MacData getInstance(Object obj) {
	}

	public org.bouncycastle.asn1.x509.DigestInfo getMac() {
	}

	public byte[] getSalt() {
	}

	public javabc.BigInteger getIterationCount() {
	}

	/**
	 *  <pre>
	 *  MacData ::= SEQUENCE {
	 *      mac      DigestInfo,
	 *      macSalt  OCTET STRING,
	 *      iterations INTEGER DEFAULT 1
	 *      -- Note: The default is for historic reasons and its use is deprecated. A
	 *      -- higher value, like 1024 is recommended.
	 *  </pre>
	 *  @return the basic ASN1Primitive construction.
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
