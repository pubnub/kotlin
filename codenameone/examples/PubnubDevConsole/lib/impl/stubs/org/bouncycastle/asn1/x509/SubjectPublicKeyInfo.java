/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  The object that contains the public key stored in a certficate.
 *  <p>
 *  The getEncoded() method in the public keys in the JCE produces a DER
 *  encoded one of these.
 */
public class SubjectPublicKeyInfo extends org.bouncycastle.asn1.ASN1Object {

	public SubjectPublicKeyInfo(AlgorithmIdentifier algId, org.bouncycastle.asn1.ASN1Encodable publicKey) {
	}

	public SubjectPublicKeyInfo(AlgorithmIdentifier algId, byte[] publicKey) {
	}

	public SubjectPublicKeyInfo(org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	public static SubjectPublicKeyInfo getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public static SubjectPublicKeyInfo getInstance(Object obj) {
	}

	public AlgorithmIdentifier getAlgorithm() {
	}

	/**
	 *  @deprecated use getAlgorithm()
	 *  @return    alg ID.
	 */
	public AlgorithmIdentifier getAlgorithmId() {
	}

	/**
	 *  for when the public key is an encoded object - if the bitstring
	 *  can't be decoded this routine throws an IOException.
	 * 
	 *  @exception IOException - if the bit string doesn't represent a DER
	 *  encoded object.
	 *  @return the public key as an ASN.1 primitive.
	 */
	public org.bouncycastle.asn1.ASN1Primitive parsePublicKey() {
	}

	/**
	 *  for when the public key is an encoded object - if the bitstring
	 *  can't be decoded this routine throws an IOException.
	 * 
	 *  @exception IOException - if the bit string doesn't represent a DER
	 *  encoded object.
	 *  @deprecated use parsePublicKey
	 *  @return the public key as an ASN.1 primitive.
	 */
	public org.bouncycastle.asn1.ASN1Primitive getPublicKey() {
	}

	/**
	 *  for when the public key is raw bits.
	 * 
	 *  @return the public key as the raw bit string...
	 */
	public org.bouncycastle.asn1.DERBitString getPublicKeyData() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  <pre>
	 *  SubjectPublicKeyInfo ::= SEQUENCE {
	 *                           algorithm AlgorithmIdentifier,
	 *                           publicKey BIT STRING }
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
