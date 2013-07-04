/**
 * 
 * Support classes useful for encoding and supporting the various RSA PKCS documents.
 */
package org.bouncycastle.asn1.pkcs;


public class RSAESOAEPparams extends org.bouncycastle.asn1.ASN1Object {

	public static final org.bouncycastle.asn1.x509.AlgorithmIdentifier DEFAULT_HASH_ALGORITHM;

	public static final org.bouncycastle.asn1.x509.AlgorithmIdentifier DEFAULT_MASK_GEN_FUNCTION;

	public static final org.bouncycastle.asn1.x509.AlgorithmIdentifier DEFAULT_P_SOURCE_ALGORITHM;

	/**
	 *  The default version
	 */
	public RSAESOAEPparams() {
	}

	public RSAESOAEPparams(org.bouncycastle.asn1.x509.AlgorithmIdentifier hashAlgorithm, org.bouncycastle.asn1.x509.AlgorithmIdentifier maskGenAlgorithm, org.bouncycastle.asn1.x509.AlgorithmIdentifier pSourceAlgorithm) {
	}

	public RSAESOAEPparams(org.bouncycastle.asn1.ASN1Sequence seq) {
	}

	public static RSAESOAEPparams getInstance(Object obj) {
	}

	public org.bouncycastle.asn1.x509.AlgorithmIdentifier getHashAlgorithm() {
	}

	public org.bouncycastle.asn1.x509.AlgorithmIdentifier getMaskGenAlgorithm() {
	}

	public org.bouncycastle.asn1.x509.AlgorithmIdentifier getPSourceAlgorithm() {
	}

	/**
	 *  <pre>
	 *   RSAES-OAEP-params ::= SEQUENCE {
	 *      hashAlgorithm      [0] OAEP-PSSDigestAlgorithms     DEFAULT sha1,
	 *      maskGenAlgorithm   [1] PKCS1MGFAlgorithms  DEFAULT mgf1SHA1,
	 *      pSourceAlgorithm   [2] PKCS1PSourceAlgorithms  DEFAULT pSpecifiedEmpty
	 *    }
	 *   
	 *    OAEP-PSSDigestAlgorithms    ALGORITHM-IDENTIFIER ::= {
	 *      { OID id-sha1 PARAMETERS NULL   }|
	 *      { OID id-sha256 PARAMETERS NULL }|
	 *      { OID id-sha384 PARAMETERS NULL }|
	 *      { OID id-sha512 PARAMETERS NULL },
	 *      ...  -- Allows for future expansion --
	 *    }
	 *    PKCS1MGFAlgorithms    ALGORITHM-IDENTIFIER ::= {
	 *      { OID id-mgf1 PARAMETERS OAEP-PSSDigestAlgorithms },
	 *     ...  -- Allows for future expansion --
	 *    }
	 *    PKCS1PSourceAlgorithms    ALGORITHM-IDENTIFIER ::= {
	 *      { OID id-pSpecified PARAMETERS OCTET STRING },
	 *      ...  -- Allows for future expansion --
	 *   }
	 *  </pre>
	 *  @return the asn1 primitive representing the parameters.
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
