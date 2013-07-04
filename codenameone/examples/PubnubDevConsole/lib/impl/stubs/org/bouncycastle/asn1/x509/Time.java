/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


public class Time extends org.bouncycastle.asn1.ASN1Object implements org.bouncycastle.asn1.ASN1Choice {

	public Time(org.bouncycastle.asn1.ASN1Primitive time) {
	}

	/**
	 *  creates a time object from a given date - if the date is between 1950
	 *  and 2049 a UTCTime object is generated, otherwise a GeneralizedTime
	 *  is used.
	 */
	public Time(java.util.Date date) {
	}

	public static Time getInstance(org.bouncycastle.asn1.ASN1TaggedObject obj, boolean explicit) {
	}

	public static Time getInstance(Object obj) {
	}

	public String getTime() {
	}

	public java.util.Date getDate() {
	}

	/**
	 *  Produce an object suitable for an ASN1OutputStream.
	 *  <pre>
	 *  Time ::= CHOICE {
	 *              utcTime        UTCTime,
	 *              generalTime    GeneralizedTime }
	 *  </pre>
	 */
	public org.bouncycastle.asn1.ASN1Primitive toASN1Primitive() {
	}

	public String toString() {
	}
}
