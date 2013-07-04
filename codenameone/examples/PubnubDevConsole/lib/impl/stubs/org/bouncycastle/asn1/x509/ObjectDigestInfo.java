/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  ObjectDigestInfo ASN.1 structure used in v2 attribute certificates.
 *  
 *  <pre>
 *   
 *     ObjectDigestInfo ::= SEQUENCE {
 *          digestedObjectType  ENUMERATED {
 *                  publicKey            (0),
 *                  publicKeyCert        (1),
 *                  otherObjectTypes     (2) },
 *                          -- otherObjectTypes MUST NOT
 *                          -- be used in this profile
 *          otherObjectTypeID   OBJECT IDENTIFIER OPTIONAL,
 *          digestAlgorithm     AlgorithmIdentifier,
 *          objectDigest        BIT STRING
 *     }
 *    
 *  </pre>
 *  
 */
public class ObjectDigestInfo extends org.bouncycastle.asn1.ASN1Object {

	/**
	 *  The public key is hashed.
	 */
	public static final int publicKey = 0;

	/**
	 *  The public key certificate is hashed.
	 */
	public static final int publicKeyCert = 1;

	/**
	 *  An other object is hashed.
	 */
	public static final int otherObjectDigest = 2;

	/**
	 *  Constructor from given details.
	 *  <p>
	 *  If <code>digestedObjectType</code> is not {@link #publicKeyCert} or
	 *  {@link #publicKey} <code>otherObjectTypeID</code> must be given,
	 *  otherwise it is ignored.
	 *  
	 *  @param digestedObjectType The digest object type.
	 *  @param otherObjectTypeID The object type ID for
	 *             <code>otherObjectDigest</code>.
	 *  @param digestAlgorithm The algorithm identifier for the hash.
	 *  @param objectDigest The hash value.
	 */
	public ObjectDigestInfo(int digestedObjectType, org.bouncycastle.asn1.ASN1ObjectIdentifier otherObjectTypeID, AlgorithmIdentifier digestAlgorithm, byte[] objectDigest) {
	}

	public static ObjectDigestInfo getInstance(Object obj) {
	}

	public static ObjectDigestInfo getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public org.bouncycastle.asn1.DEREnumerated getDigestedObjectType() {
	}

	public org.bouncycastle.asn1.ASN1ObjectIdentifier getOtherObjectTypeID() {
	}

	public AlgorithmIdentifier getDigestAlgorithm() {
	}

	public org.bouncycastle.asn1.DERBitString getObjectDigest() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  
	 *  <pre>
	 *   
	 *     ObjectDigestInfo ::= SEQUENCE {
	 *          digestedObjectType  ENUMERATED {
	 *                  publicKey            (0),
	 *                  publicKeyCert        (1),
	 *                  otherObjectTypes     (2) },
	 *                          -- otherObjectTypes MUST NOT
	 *                          -- be used in this profile
	 *          otherObjectTypeID   OBJECT IDENTIFIER OPTIONAL,
	 *          digestAlgorithm     AlgorithmIdentifier,
	 *          objectDigest        BIT STRING
	 *     }
	 *    
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
