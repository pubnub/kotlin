/**
 * 
 * Support classes useful for encoding and supporting the various RSA PKCS documents.
 */
package org.bouncycastle.asn1.pkcs;


public class RSASSAPSSparams extends org.bouncycastle.asn1.ASN1Object {

	public static final org.bouncycastle.asn1.x509.AlgorithmIdentifier DEFAULT_HASH_ALGORITHM;

	public static final org.bouncycastle.asn1.x509.AlgorithmIdentifier DEFAULT_MASK_GEN_FUNCTION;

	public static final org.bouncycastle.asn1.ASN1Integer DEFAULT_SALT_LENGTH;

	public static final org.bouncycastle.asn1.ASN1Integer DEFAULT_TRAILER_FIELD;

	/**
	 *  The default version
	 */
	public RSASSAPSSparams() {
	}

	public RSASSAPSSparams(org.bouncycastle.asn1.x509.AlgorithmIdentifier hashAlgorithm, org.bouncycastle.asn1.x509.AlgorithmIdentifier maskGenAlgorithm, org.bouncycastle.asn1.ASN1Integer saltLength, org.bouncycastle.asn1.ASN1Integer trailerField) {
	}

	public static RSASSAPSSparams getInstance(Object obj) {
	}

	public org.bouncycastle.asn1.x509.AlgorithmIdentifier getHashAlgorithm() {
	}

	public org.bouncycastle.asn1.x509.AlgorithmIdentifier getMaskGenAlgorithm() {
	}

	public javabc.BigInteger getSaltLength() {
	}

	public javabc.BigInteger getTrailerField() {
	}

	/**
	 *  <pre>
	 *  RSASSA-PSS-params ::= SEQUENCE {
	 *    hashAlgorithm      [0] OAEP-PSSDigestAlgorithms  DEFAULT sha1,
	 *     maskGenAlgorithm   [1] PKCS1MGFAlgorithms  DEFAULT mgf1SHA1,
	 *     saltLength         [2] INTEGER  DEFAULT 20,
	 *     trailerField       [3] TrailerField  DEFAULT trailerFieldBC
	 *   }
	 * 
	 *  OAEP-PSSDigestAlgorithms    ALGORITHM-IDENTIFIER ::= {
	 *     { OID id-sha1 PARAMETERS NULL   }|
	 *     { OID id-sha256 PARAMETERS NULL }|
	 *     { OID id-sha384 PARAMETERS NULL }|
	 *     { OID id-sha512 PARAMETERS NULL },
	 *     ...  -- Allows for future expansion --
	 *  }
	 * 
	 *  PKCS1MGFAlgorithms    ALGORITHM-IDENTIFIER ::= {
	 *    { OID id-mgf1 PARAMETERS OAEP-PSSDigestAlgorithms },
	 *     ...  -- Allows for future expansion --
	 *  }
	 *  
	 *  TrailerField ::= INTEGER { trailerFieldBC(1) }
	 *  </pre>
	 *  @return the asn1 primitive representing the parameters.
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
