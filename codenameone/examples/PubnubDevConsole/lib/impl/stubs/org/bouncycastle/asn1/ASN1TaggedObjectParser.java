/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


public interface ASN1TaggedObjectParser extends ASN1Encodable, InMemoryRepresentable {

	public int getTagNo();

	public ASN1Encodable getObjectParser(int tag, boolean isExplicit);
}
