package org.bouncycastle.asn1.x500;


public class X500NameBuilder {

	public X500NameBuilder(X500NameStyle template) {
	}

	public X500NameBuilder addRDN(org.bouncycastle.asn1.ASN1ObjectIdentifier oid, String value) {
	}

	public X500NameBuilder addRDN(org.bouncycastle.asn1.ASN1ObjectIdentifier oid, org.bouncycastle.asn1.ASN1Encodable value) {
	}

	public X500NameBuilder addRDN(AttributeTypeAndValue attrTAndV) {
	}

	public X500NameBuilder addMultiValuedRDN(org.bouncycastle.asn1.ASN1ObjectIdentifier[] oids, String[] values) {
	}

	public X500NameBuilder addMultiValuedRDN(org.bouncycastle.asn1.ASN1ObjectIdentifier[] oids, org.bouncycastle.asn1.ASN1Encodable[] values) {
	}

	public X500NameBuilder addMultiValuedRDN(AttributeTypeAndValue[] attrTAndVs) {
	}

	public X500Name build() {
	}
}
