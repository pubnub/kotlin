/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  The DigestInfo object.
 *  <pre>
 *  DigestInfo::=SEQUENCE{
 *           digestAlgorithm  AlgorithmIdentifier,
 *           digest OCTET STRING }
 *  </pre>
 */
public class DigestInfo extends org.bouncycastle.asn1.ASN1Object {

	public DigestInfo(AlgorithmIdentifier algId, byte[] digest) {
	}

	public DigestInfo(org.bouncycastle.asn1.ASN1Sequence obj) {
	}

	public static DigestInfo getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public static DigestInfo getInstance(Object obj) {
	}

	public AlgorithmIdentifier getAlgorithmId() {
	}

	public byte[] getDigest() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
