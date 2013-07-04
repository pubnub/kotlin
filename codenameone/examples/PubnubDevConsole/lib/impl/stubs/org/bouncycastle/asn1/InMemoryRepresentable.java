/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


public interface InMemoryRepresentable {

	public ASN1Primitive getLoadedObject();
}
