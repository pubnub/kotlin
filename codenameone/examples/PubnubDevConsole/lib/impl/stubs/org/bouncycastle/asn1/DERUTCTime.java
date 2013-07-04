/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


/**
 *  UTC time object.
 */
public class DERUTCTime extends ASN1Primitive {

	/**
	 *  The correct format for this is YYMMDDHHMMSSZ (it used to be that seconds were
	 *  never encoded. When you're creating one of these objects from scratch, that's
	 *  what you want to use, otherwise we'll try to deal with whatever gets read from
	 *  the input stream... (this is why the input format is different from the getTime()
	 *  method output).
	 *  <p>
	 * 
	 *  @param time the time string.
	 */
	public DERUTCTime(String time) {
	}

	/**
	 *  base constructor from a java.util.date object
	 */
	public DERUTCTime(java.util.Date time) {
	}

	/**
	 *  return an UTC Time from the passed in object.
	 * 
	 *  @exception IllegalArgumentException if the object cannot be converted.
	 */
	public static ASN1UTCTime getInstance(Object obj) {
	}

	/**
	 *  return an UTC Time from a tagged object.
	 * 
	 *  @param obj the tagged object holding the object we want
	 *  @param explicit true if the object is meant to be explicitly
	 *               tagged false otherwise.
	 *  @exception IllegalArgumentException if the tagged object cannot
	 *                be converted.
	 */
	public static ASN1UTCTime getInstance(ASN1TaggedObject obj, boolean explicit) {
	}

	/**
	 *  return the time as a date based on whatever a 2 digit year will return. For
	 *  standardised processing use getAdjustedDate().
	 * 
	 *  @return the resulting date
	 */
	public java.util.Date getDate() {
	}

	/**
	 *  return the time as an adjusted date
	 *  in the range of 1950 - 2049.
	 * 
	 *  @return a date in the range of 1950 to 2049.
	 */
	public java.util.Date getAdjustedDate() {
	}

	/**
	 *  return the time - always in the form of 
	 *   YYMMDDhhmmssGMT(+hh:mm|-hh:mm).
	 *  <p>
	 *  Normally in a certificate we would expect "Z" rather than "GMT",
	 *  however adding the "GMT" means we can just use:
	 *  <pre>
	 *      dateF = new SimpleDateFormat("yyMMddHHmmssz");
	 *  </pre>
	 *  To read in the time and get a date which is compatible with our local
	 *  time zone.
	 *  <p>
	 *  <b>Note:</b> In some cases, due to the local date processing, this
	 *  may lead to unexpected results. If you want to stick the normal
	 *  convention of 1950 to 2049 use the getAdjustedTime() method.
	 */
	public String getTime() {
	}

	/**
	 *  return a time string as an adjusted date with a 4 digit year. This goes
	 *  in the range of 1950 - 2049.
	 */
	public String getAdjustedTime() {
	}

	/**
	 *  Return the time.
	 *  @return The time string as it appeared in the encoded object.
	 */
	public String getTimeString() {
	}

	public int hashCode() {
	}

	public String toString() {
	}
}
