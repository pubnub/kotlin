/**
 * 
 * Support classes useful for encoding and processing messages based around RFC3739
 */
package org.bouncycastle.asn1.x509.qualified;


/**
 *  The Iso4217CurrencyCode object.
 *  <pre>
 *  Iso4217CurrencyCode  ::=  CHOICE {
 *        alphabetic              PrintableString (SIZE 3), --Recommended
 *        numeric              INTEGER (1..999) }
 *  -- Alphabetic or numeric currency code as defined in ISO 4217
 *  -- It is recommended that the Alphabetic form is used
 *  </pre>
 */
public class Iso4217CurrencyCode extends org.bouncycastle.asn1.ASN1Object implements org.bouncycastle.asn1.ASN1Choice {

	public Iso4217CurrencyCode(int numeric) {
	}

	public Iso4217CurrencyCode(String alphabetic) {
	}

	public static Iso4217CurrencyCode getInstance(Object obj) {
	}

	public boolean isAlphabetic() {
	}

	public String getAlphabetic() {
	}

	public int getNumeric() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
