/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


/**
 *  Generalized time object.
 */
public class DERGeneralizedTime extends ASN1Primitive {

	/**
	 *  The correct format for this is YYYYMMDDHHMMSS[.f]Z, or without the Z
	 *  for local time, or Z|[+|-]HHMM on the end, for difference between local
	 *  time and UTC time. The fractional second amount f must consist of at
	 *  least one number with trailing zeroes removed.
	 * 
	 *  @param time the time string.
	 *  @exception IllegalArgumentException if String is an illegal format.
	 */
	public DERGeneralizedTime(String time) {
	}

	/**
	 *  base constructer from a java.util.date object
	 */
	public DERGeneralizedTime(java.util.Date time) {
	}

	protected DERGeneralizedTime(java.util.Date date, boolean includeMillis) {
	}

	/**
	 *  return a generalized time from the passed in object
	 * 
	 *  @exception IllegalArgumentException if the object cannot be converted.
	 */
	public static ASN1GeneralizedTime getInstance(Object obj) {
	}

	/**
	 *  return a Generalized Time object from a tagged object.
	 * 
	 *  @param obj the tagged object holding the object we want
	 *  @param explicit true if the object is meant to be explicitly
	 *               tagged false otherwise.
	 *  @exception IllegalArgumentException if the tagged object cannot
	 *                be converted.
	 */
	public static ASN1GeneralizedTime getInstance(ASN1TaggedObject obj, boolean explicit) {
	}

	/**
	 *  Return the time.
	 *  @return The time string as it appeared in the encoded object.
	 */
	public String getTimeString() {
	}

	/**
	 *  return the time - always in the form of 
	 *   YYYYMMDDhhmmssGMT(+hh:mm|-hh:mm).
	 *  <p>
	 *  Normally in a certificate we would expect "Z" rather than "GMT",
	 *  however adding the "GMT" means we can just use:
	 *  <pre>
	 *      dateF = new SimpleDateFormat("yyyyMMddHHmmssz");
	 *  </pre>
	 *  To read in the time and get a date which is compatible with our local
	 *  time zone.
	 */
	public String getTime() {
	}

	public java.util.Date getDate() {
	}

	public int hashCode() {
	}
}
