/**
 * 
 * Support classes useful for encoding and processing messages based around RFC3739
 */
package org.bouncycastle.asn1.x509.qualified;


/**
 *  The MonetaryValue object.
 *  <pre>
 *  MonetaryValue  ::=  SEQUENCE {
 *        currency              Iso4217CurrencyCode,
 *        amount               INTEGER, 
 *        exponent             INTEGER }
 *  -- value = amount * 10^exponent
 *  </pre>
 */
public class MonetaryValue extends org.bouncycastle.asn1.ASN1Object {

	public MonetaryValue(Iso4217CurrencyCode currency, int amount, int exponent) {
	}

	public static MonetaryValue getInstance(Object obj) {
	}

	public Iso4217CurrencyCode getCurrency() {
	}

	public javabc.BigInteger getAmount() {
	}

	public javabc.BigInteger getExponent() {
	}

	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}
}
