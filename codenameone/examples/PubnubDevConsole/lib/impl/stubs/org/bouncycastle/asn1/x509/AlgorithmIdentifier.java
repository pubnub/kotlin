/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


public class AlgorithmIdentifier extends org.bouncycastle.asn1.ASN1Object {

	public AlgorithmIdentifier(org.bouncycastle.asn1.ASN1ObjectIdentifier objectId) {
	}

	/**
	 *  @deprecated use ASN1ObjectIdentifier
	 *  @param objectId
	 */
	public AlgorithmIdentifier(String objectId) {
	}

	/**
	 *  @deprecated use ASN1ObjectIdentifier
	 *  @param objectId
	 */
	public AlgorithmIdentifier(org.bouncycastle.asn1.DERObjectIdentifier objectId) {
	}

	/**
	 *  @deprecated use ASN1ObjectIdentifier
	 *  @param objectId
	 *  @param parameters
	 */
	public AlgorithmIdentifier(org.bouncycastle.asn1.DERObjectIdentifier objectId, org.bouncycastle.asn1.ASN1Encodable parameters) {
	}

	public AlgorithmIdentifier(org.bouncycastle.asn1.ASN1ObjectIdentifier objectId, org.bouncycastle.asn1.ASN1Encodable parameters) {
	}

	public AlgorithmIdentifier(org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	public static AlgorithmIdentifier getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public static AlgorithmIdentifier getInstance(Object obj) {
	}

	public org.bouncycastle.asn1.ASN1ObjectIdentifier getAlgorithm() {
	}

	/**
	 *  @deprecated use getAlgorithm
	 *  @return
	 */
	public org.bouncycastle.asn1.ASN1ObjectIdentifier getObjectId() {
	}

	public org.bouncycastle.asn1.ASN1Encodable getParameters() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  <pre>
	 *       AlgorithmIdentifier ::= SEQUENCE {
	 *                             algorithm OBJECT IDENTIFIER,
	 *                             parameters ANY DEFINED BY algorithm OPTIONAL }
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
