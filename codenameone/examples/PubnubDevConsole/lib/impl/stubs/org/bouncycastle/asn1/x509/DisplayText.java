/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  <code>DisplayText</code> class, used in
 *  <code>CertificatePolicies</code> X509 V3 extensions (in policy qualifiers).
 * 
 *  <p>It stores a string in a chosen encoding. 
 *  <pre>
 *  DisplayText ::= CHOICE {
 *       ia5String        IA5String      (SIZE (1..200)),
 *       visibleString    VisibleString  (SIZE (1..200)),
 *       bmpString        BMPString      (SIZE (1..200)),
 *       utf8String       UTF8String     (SIZE (1..200)) }
 *  </pre>
 *  @see PolicyQualifierInfo
 *  @see PolicyInformation
 */
public class DisplayText extends org.bouncycastle.asn1.ASN1Object implements org.bouncycastle.asn1.ASN1Choice {

	/**
	 *  Constant corresponding to ia5String encoding. 
	 */
	public static final int CONTENT_TYPE_IA5STRING = 0;

	/**
	 *  Constant corresponding to bmpString encoding. 
	 */
	public static final int CONTENT_TYPE_BMPSTRING = 1;

	/**
	 *  Constant corresponding to utf8String encoding. 
	 */
	public static final int CONTENT_TYPE_UTF8STRING = 2;

	/**
	 *  Constant corresponding to visibleString encoding. 
	 */
	public static final int CONTENT_TYPE_VISIBLESTRING = 3;

	/**
	 *  Describe constant <code>DISPLAY_TEXT_MAXIMUM_SIZE</code> here.
	 */
	public static final int DISPLAY_TEXT_MAXIMUM_SIZE = 200;

	/**
	 *  Creates a new <code>DisplayText</code> instance.
	 * 
	 *  @param type the desired encoding type for the text. 
	 *  @param text the text to store. Strings longer than 200
	 *  characters are truncated. 
	 */
	public DisplayText(int type, String text) {
	}

	/**
	 *  Creates a new <code>DisplayText</code> instance.
	 * 
	 *  @param text the text to encapsulate. Strings longer than 200
	 *  characters are truncated. 
	 */
	public DisplayText(String text) {
	}

	public static DisplayText getInstance(Object obj) {
	}

	public static DisplayText getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}

	/**
	 *  Returns the stored <code>String</code> object. 
	 * 
	 *  @return the stored text as a <code>String</code>. 
	 */
	public String getString() {
	}
}
