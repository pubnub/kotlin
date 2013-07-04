/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


public class ASN1ObjectIdentifier extends DERObjectIdentifier {

	public ASN1ObjectIdentifier(String identifier) {
	}

	/**
	 *  Return an OID that creates a branch under the current one.
	 * 
	 *  @param branchID node numbers for the new branch.
	 *  @return the OID for the new created branch.
	 */
	public ASN1ObjectIdentifier branch(String branchID) {
	}

	/**
	 *  Return  true if this oid is an extension of the passed in branch, stem.
	 *  @param stem the arc or branch that is a possible parent.
	 *  @return  true if the branch is on the passed in stem, false otherwise.
	 */
	public boolean on(ASN1ObjectIdentifier stem) {
	}
}
