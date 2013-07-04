/**
 * 
 * Classes for support of the SEC standard for Elliptic Curve.
 */
package org.bouncycastle.asn1.sec;


public class SECNamedCurves {

	public SECNamedCurves() {
	}

	public static org.bouncycastle.asn1.x9.X9ECParameters getByName(String name) {
	}

	/**
	 *  return the X9ECParameters object for the named curve represented by
	 *  the passed in object identifier. Null if the curve isn't present.
	 * 
	 *  @param oid an object identifier representing a named curve, if present.
	 */
	public static org.bouncycastle.asn1.x9.X9ECParameters getByOID(org.bouncycastle.asn1.ASN1ObjectIdentifier oid) {
	}

	/**
	 *  return the object identifier signified by the passed in name. Null
	 *  if there is no object identifier associated with name.
	 * 
	 *  @return the object identifier associated with name, if present.
	 */
	public static org.bouncycastle.asn1.ASN1ObjectIdentifier getOID(String name) {
	}

	/**
	 *  return the named curve name represented by the given object identifier.
	 */
	public static String getName(org.bouncycastle.asn1.ASN1ObjectIdentifier oid) {
	}

	/**
	 *  returns an enumeration containing the name strings for curves
	 *  contained in this structure.
	 */
	public static java.util.Enumeration getNames() {
	}
}
