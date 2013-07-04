/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


/**
 *  It turns out that the number of standard ways the fields in a DN should be 
 *  encoded into their ASN.1 counterparts is rapidly approaching the
 *  number of machines on the internet. By default the X509Name class 
 *  will produce UTF8Strings in line with the current recommendations (RFC 3280).
 *  <p>
 *  An example of an encoder look like below:
 *  <pre>
 *  public class X509DirEntryConverter
 *      extends X509NameEntryConverter
 *  {
 *      public ASN1Primitive getConvertedValue(
 *          ASN1ObjectIdentifier  oid,
 *          String               value)
 *      {
 *          if (str.length() != 0 && str.charAt(0) == '#')
 *          {
 *              return convertHexEncoded(str, 1);
 *          }
 *          if (oid.equals(EmailAddress))
 *          {
 *              return new DERIA5String(str);
 *          }
 *          else if (canBePrintable(str))
 *          {
 *              return new DERPrintableString(str);
 *          }
 *          else if (canBeUTF8(str))
 *          {
 *              return new DERUTF8String(str);
 *          }
 *          else
 *          {
 *              return new DERBMPString(str);
 *          }
 *      }
 *  }
 */
public abstract class X509NameEntryConverter {

	public X509NameEntryConverter() {
	}

	/**
	 *  Convert an inline encoded hex string rendition of an ASN.1
	 *  object back into its corresponding ASN.1 object.
	 *  
	 *  @param str the hex encoded object
	 *  @param off the index at which the encoding starts
	 *  @return the decoded object
	 */
	protected org.bouncycastle.asn1.ASN1Primitive convertHexEncoded(String str, int off) {
	}

	/**
	 *  return true if the passed in String can be represented without
	 *  loss as a PrintableString, false otherwise.
	 */
	protected boolean canBePrintable(String str) {
	}

	/**
	 *  Convert the passed in String value into the appropriate ASN.1
	 *  encoded object.
	 *  
	 *  @param oid the oid associated with the value in the DN.
	 *  @param value the value of the particular DN component.
	 *  @return the ASN.1 equivalent for the value.
	 */
	public abstract org.bouncycastle.asn1.ASN1Primitive getConvertedValue(org.bouncycastle.asn1.ASN1ObjectIdentifier oid, String value) {
	}
}
