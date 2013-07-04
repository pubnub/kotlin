/**
 * 
 * Support classes useful for encoding and processing X.509 certificates.
 */
package org.bouncycastle.asn1.x509;


public interface X509ObjectIdentifiers {

	public static final String id = "2.5.4";

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier commonName;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier countryName;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier localityName;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier stateOrProvinceName;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier organization;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier organizationalUnitName;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_at_telephoneNumber;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_at_name;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_SHA1;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier ripemd160;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier ripemd160WithRSAEncryption;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_ea_rsa;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_pkix;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_pe;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_ce;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_ad;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_ad_caIssuers;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier id_ad_ocsp;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier ocspAccessMethod;

	public static final org.bouncycastle.asn1.ASN1ObjectIdentifier crlAccessMethod;
}
