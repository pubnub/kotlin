/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


/**
 *  Class representing the DER-type External
 */
public class DERExternal extends ASN1Primitive {

	public DERExternal(ASN1EncodableVector vector) {
	}

	/**
	 *  Creates a new instance of DERExternal
	 *  See X.690 for more informations about the meaning of these parameters
	 *  @param directReference The direct reference or <code>null</code> if not set.
	 *  @param indirectReference The indirect reference or <code>null</code> if not set.
	 *  @param dataValueDescriptor The data value descriptor or <code>null</code> if not set.
	 *  @param externalData The external data in its encoded form.
	 */
	public DERExternal(ASN1ObjectIdentifier directReference, ASN1Integer indirectReference, ASN1Primitive dataValueDescriptor, DERTaggedObject externalData) {
	}

	/**
	 *  Creates a new instance of DERExternal.
	 *  See X.690 for more informations about the meaning of these parameters
	 *  @param directReference The direct reference or <code>null</code> if not set.
	 *  @param indirectReference The indirect reference or <code>null</code> if not set.
	 *  @param dataValueDescriptor The data value descriptor or <code>null</code> if not set.
	 *  @param encoding The encoding to be used for the external data
	 *  @param externalData The external data
	 */
	public DERExternal(ASN1ObjectIdentifier directReference, ASN1Integer indirectReference, ASN1Primitive dataValueDescriptor, int encoding, ASN1Primitive externalData) {
	}

	public int hashCode() {
	}

	/**
	 *  Returns the data value descriptor
	 *  @return The descriptor
	 */
	public ASN1Primitive getDataValueDescriptor() {
	}

	/**
	 *  Returns the direct reference of the external element
	 *  @return The reference
	 */
	public ASN1ObjectIdentifier getDirectReference() {
	}

	/**
	 *  Returns the encoding of the content. Valid values are
	 *  <ul>
	 *  <li><code>0</code> single-ASN1-type</li>
	 *  <li><code>1</code> OCTET STRING</li>
	 *  <li><code>2</code> BIT STRING</li>
	 *  </ul>
	 *  @return The encoding
	 */
	public int getEncoding() {
	}

	/**
	 *  Returns the content of this element
	 *  @return The content
	 */
	public ASN1Primitive getExternalContent() {
	}

	/**
	 *  Returns the indirect reference of this element
	 *  @return The reference
	 */
	public ASN1Integer getIndirectReference() {
	}
}
