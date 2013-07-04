/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


/**
 *  Marker interface for CHOICE objects - if you implement this in a role your
 *  own object any attempt to tag the object implicitly will convert the tag to
 *  an explicit one as the encoding rules require.
 *  <p>
 *  If you use this interface your class should also implement the getInstance
 *  pattern which takes a tag object and the tagging mode used. 
 */
public interface ASN1Choice {
}
