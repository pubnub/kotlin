/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  The GeneralName object.
 *  <pre>
 *  GeneralName ::= CHOICE {
 *       otherName                       [0]     OtherName,
 *       rfc822Name                      [1]     IA5String,
 *       dNSName                         [2]     IA5String,
 *       x400Address                     [3]     ORAddress,
 *       directoryName                   [4]     Name,
 *       ediPartyName                    [5]     EDIPartyName,
 *       uniformResourceIdentifier       [6]     IA5String,
 *       iPAddress                       [7]     OCTET STRING,
 *       registeredID                    [8]     OBJECT IDENTIFIER}
 * 
 *  OtherName ::= SEQUENCE {
 *       type-id    OBJECT IDENTIFIER,
 *       value      [0] EXPLICIT ANY DEFINED BY type-id }
 * 
 *  EDIPartyName ::= SEQUENCE {
 *       nameAssigner            [0]     DirectoryString OPTIONAL,
 *       partyName               [1]     DirectoryString }
 *  
 *  Name ::= CHOICE { RDNSequence }
 *  </pre>
 */
public class GeneralName extends org.bouncycastle.asn1.ASN1Object implements org.bouncycastle.asn1.ASN1Choice {

	public static final int otherName = 0;

	public static final int rfc822Name = 1;

	public static final int dNSName = 2;

	public static final int x400Address = 3;

	public static final int directoryName = 4;

	public static final int ediPartyName = 5;

	public static final int uniformResourceIdentifier = 6;

	public static final int iPAddress = 7;

	public static final int registeredID = 8;

	/**
	 *  @deprecated use X500Name constructor.
	 *  @param dirName
	 */
	public GeneralName(X509Name dirName) {
	}

	public GeneralName(org.bouncycastle.asn1.x500.X500Name dirName) {
	}

	/**
	 *  When the subjectAltName extension contains an Internet mail address,
	 *  the address MUST be included as an rfc822Name. The format of an
	 *  rfc822Name is an "addr-spec" as defined in RFC 822 [RFC 822].
	 * 
	 *  When the subjectAltName extension contains a domain name service
	 *  label, the domain name MUST be stored in the dNSName (an IA5String).
	 *  The name MUST be in the "preferred name syntax," as specified by RFC
	 *  1034 [RFC 1034].
	 * 
	 *  When the subjectAltName extension contains a URI, the name MUST be
	 *  stored in the uniformResourceIdentifier (an IA5String). The name MUST
	 *  be a non-relative URL, and MUST follow the URL syntax and encoding
	 *  rules specified in [RFC 1738].  The name must include both a scheme
	 *  (e.g., "http" or "ftp") and a scheme-specific-part.  The scheme-
	 *  specific-part must include a fully qualified domain name or IP
	 *  address as the host.
	 * 
	 *  When the subjectAltName extension contains a iPAddress, the address
	 *  MUST be stored in the octet string in "network byte order," as
	 *  specified in RFC 791 [RFC 791]. The least significant bit (LSB) of
	 *  each octet is the LSB of the corresponding byte in the network
	 *  address. For IP Version 4, as specified in RFC 791, the octet string
	 *  MUST contain exactly four octets.  For IP Version 6, as specified in
	 *  RFC 1883, the octet string MUST contain exactly sixteen octets [RFC
	 *  1883].
	 */
	public GeneralName(int tag, org.bouncycastle.asn1.ASN1Encodable name) {
	}

	/**
	 *  Create a GeneralName for the given tag from the passed in String.
	 *  <p>
	 *  This constructor can handle:
	 *  <ul>
	 *  <li>rfc822Name
	 *  <li>iPAddress
	 *  <li>directoryName
	 *  <li>dNSName
	 *  <li>uniformResourceIdentifier
	 *  <li>registeredID
	 *  </ul>
	 *  For x400Address, otherName and ediPartyName there is no common string
	 *  format defined.
	 *  <p>
	 *  Note: A directory name can be encoded in different ways into a byte
	 *  representation. Be aware of this if the byte representation is used for
	 *  comparing results.
	 * 
	 *  @param tag tag number
	 *  @param name string representation of name
	 *  @throws IllegalArgumentException if the string encoding is not correct or     *             not supported.
	 */
	public GeneralName(int tag, String name) {
	}

	public static GeneralName getInstance(Object obj) {
	}

	public static GeneralName getInstance(org.bouncycastle.asn1.ASN1TaggedObject tagObj, boolean explicit) {
	}

	public int getTagNo() {
	}

	public org.bouncycastle.asn1.ASN1Encodable getName() {
	}

	public String toString() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
