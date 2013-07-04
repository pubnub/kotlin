package org.bouncycastle.asn1.x500;


public class DirectoryString extends org.bouncycastle.asn1.ASN1Object implements org.bouncycastle.asn1.ASN1Choice, org.bouncycastle.asn1.ASN1String {

	public DirectoryString(String string) {
	}

	public static DirectoryString getInstance(Object o) {
	}

	public static DirectoryString getInstance(org.bouncycastle.asn1.ASN1TaggedObject o, boolean explicit) {
	}

	public String getString() {
	}

	public String toString() {
	}

	/**
	 *  <pre>
	 *   DirectoryString ::= CHOICE {
	 *     teletexString               TeletexString (SIZE (1..MAX)),
	 *     printableString             PrintableString (SIZE (1..MAX)),
	 *     universalString             UniversalString (SIZE (1..MAX)),
	 *     utf8String                  UTF8String (SIZE (1..MAX)),
	 *     bmpString                   BMPString (SIZE (1..MAX))  }
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
