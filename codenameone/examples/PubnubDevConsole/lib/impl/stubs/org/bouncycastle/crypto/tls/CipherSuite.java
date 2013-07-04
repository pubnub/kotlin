/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


/**
 *  RFC 2246 A.5
 */
public class CipherSuite {

	public static final int TLS_NULL_WITH_NULL_NULL = 0;

	public static final int TLS_RSA_WITH_NULL_MD5 = 1;

	public static final int TLS_RSA_WITH_NULL_SHA = 2;

	public static final int TLS_RSA_EXPORT_WITH_RC4_40_MD5 = 3;

	public static final int TLS_RSA_WITH_RC4_128_MD5 = 4;

	public static final int TLS_RSA_WITH_RC4_128_SHA = 5;

	public static final int TLS_RSA_EXPORT_WITH_RC2_CBC_40_MD5 = 6;

	public static final int TLS_RSA_WITH_IDEA_CBC_SHA = 7;

	public static final int TLS_RSA_EXPORT_WITH_DES40_CBC_SHA = 8;

	public static final int TLS_RSA_WITH_DES_CBC_SHA = 9;

	public static final int TLS_RSA_WITH_3DES_EDE_CBC_SHA = 10;

	public static final int TLS_DH_DSS_EXPORT_WITH_DES40_CBC_SHA = 11;

	public static final int TLS_DH_DSS_WITH_DES_CBC_SHA = 12;

	public static final int TLS_DH_DSS_WITH_3DES_EDE_CBC_SHA = 13;

	public static final int TLS_DH_RSA_EXPORT_WITH_DES40_CBC_SHA = 14;

	public static final int TLS_DH_RSA_WITH_DES_CBC_SHA = 15;

	public static final int TLS_DH_RSA_WITH_3DES_EDE_CBC_SHA = 16;

	public static final int TLS_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA = 17;

	public static final int TLS_DHE_DSS_WITH_DES_CBC_SHA = 18;

	public static final int TLS_DHE_DSS_WITH_3DES_EDE_CBC_SHA = 19;

	public static final int TLS_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA = 20;

	public static final int TLS_DHE_RSA_WITH_DES_CBC_SHA = 21;

	public static final int TLS_DHE_RSA_WITH_3DES_EDE_CBC_SHA = 22;

	public static final int TLS_DH_anon_EXPORT_WITH_RC4_40_MD5 = 23;

	public static final int TLS_DH_anon_WITH_RC4_128_MD5 = 24;

	public static final int TLS_DH_anon_EXPORT_WITH_DES40_CBC_SHA = 25;

	public static final int TLS_DH_anon_WITH_DES_CBC_SHA = 26;

	public static final int TLS_DH_anon_WITH_3DES_EDE_CBC_SHA = 27;

	public static final int TLS_RSA_WITH_AES_128_CBC_SHA = 47;

	public static final int TLS_DH_DSS_WITH_AES_128_CBC_SHA = 48;

	public static final int TLS_DH_RSA_WITH_AES_128_CBC_SHA = 49;

	public static final int TLS_DHE_DSS_WITH_AES_128_CBC_SHA = 50;

	public static final int TLS_DHE_RSA_WITH_AES_128_CBC_SHA = 51;

	public static final int TLS_DH_anon_WITH_AES_128_CBC_SHA = 52;

	public static final int TLS_RSA_WITH_AES_256_CBC_SHA = 53;

	public static final int TLS_DH_DSS_WITH_AES_256_CBC_SHA = 54;

	public static final int TLS_DH_RSA_WITH_AES_256_CBC_SHA = 55;

	public static final int TLS_DHE_DSS_WITH_AES_256_CBC_SHA = 56;

	public static final int TLS_DHE_RSA_WITH_AES_256_CBC_SHA = 57;

	public static final int TLS_DH_anon_WITH_AES_256_CBC_SHA = 58;

	public static final int TLS_PSK_WITH_RC4_128_SHA = 138;

	public static final int TLS_PSK_WITH_3DES_EDE_CBC_SHA = 139;

	public static final int TLS_PSK_WITH_AES_128_CBC_SHA = 140;

	public static final int TLS_PSK_WITH_AES_256_CBC_SHA = 141;

	public static final int TLS_DHE_PSK_WITH_RC4_128_SHA = 142;

	public static final int TLS_DHE_PSK_WITH_3DES_EDE_CBC_SHA = 143;

	public static final int TLS_DHE_PSK_WITH_AES_128_CBC_SHA = 144;

	public static final int TLS_DHE_PSK_WITH_AES_256_CBC_SHA = 145;

	public static final int TLS_RSA_PSK_WITH_RC4_128_SHA = 146;

	public static final int TLS_RSA_PSK_WITH_3DES_EDE_CBC_SHA = 147;

	public static final int TLS_RSA_PSK_WITH_AES_128_CBC_SHA = 148;

	public static final int TLS_RSA_PSK_WITH_AES_256_CBC_SHA = 149;

	public static final int TLS_ECDH_ECDSA_WITH_NULL_SHA = 49153;

	public static final int TLS_ECDH_ECDSA_WITH_RC4_128_SHA = 49154;

	public static final int TLS_ECDH_ECDSA_WITH_3DES_EDE_CBC_SHA = 49155;

	public static final int TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA = 49156;

	public static final int TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA = 49157;

	public static final int TLS_ECDHE_ECDSA_WITH_NULL_SHA = 49158;

	public static final int TLS_ECDHE_ECDSA_WITH_RC4_128_SHA = 49159;

	public static final int TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA = 49160;

	public static final int TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA = 49161;

	public static final int TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA = 49162;

	public static final int TLS_ECDH_RSA_WITH_NULL_SHA = 49163;

	public static final int TLS_ECDH_RSA_WITH_RC4_128_SHA = 49164;

	public static final int TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA = 49165;

	public static final int TLS_ECDH_RSA_WITH_AES_128_CBC_SHA = 49166;

	public static final int TLS_ECDH_RSA_WITH_AES_256_CBC_SHA = 49167;

	public static final int TLS_ECDHE_RSA_WITH_NULL_SHA = 49168;

	public static final int TLS_ECDHE_RSA_WITH_RC4_128_SHA = 49169;

	public static final int TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA = 49170;

	public static final int TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA = 49171;

	public static final int TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA = 49172;

	public static final int TLS_ECDH_anon_WITH_NULL_SHA = 49173;

	public static final int TLS_ECDH_anon_WITH_RC4_128_SHA = 49174;

	public static final int TLS_ECDH_anon_WITH_3DES_EDE_CBC_SHA = 49175;

	public static final int TLS_ECDH_anon_WITH_AES_128_CBC_SHA = 49176;

	public static final int TLS_ECDH_anon_WITH_AES_256_CBC_SHA = 49177;

	public static final int TLS_SRP_SHA_WITH_3DES_EDE_CBC_SHA = 49178;

	public static final int TLS_SRP_SHA_RSA_WITH_3DES_EDE_CBC_SHA = 49179;

	public static final int TLS_SRP_SHA_DSS_WITH_3DES_EDE_CBC_SHA = 49180;

	public static final int TLS_SRP_SHA_WITH_AES_128_CBC_SHA = 49181;

	public static final int TLS_SRP_SHA_RSA_WITH_AES_128_CBC_SHA = 49182;

	public static final int TLS_SRP_SHA_DSS_WITH_AES_128_CBC_SHA = 49183;

	public static final int TLS_SRP_SHA_WITH_AES_256_CBC_SHA = 49184;

	public static final int TLS_SRP_SHA_RSA_WITH_AES_256_CBC_SHA = 49185;

	public static final int TLS_SRP_SHA_DSS_WITH_AES_256_CBC_SHA = 49186;

	public static final int TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256 = 49187;

	public static final int TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384 = 49188;

	public static final int TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256 = 49189;

	public static final int TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA384 = 49190;

	public static final int TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256 = 49191;

	public static final int TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384 = 49192;

	public static final int TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256 = 49193;

	public static final int TLS_ECDH_RSA_WITH_AES_256_CBC_SHA384 = 49194;

	public static final int TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256 = 49195;

	public static final int TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384 = 49196;

	public static final int TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256 = 49197;

	public static final int TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384 = 49198;

	public static final int TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256 = 49199;

	public static final int TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384 = 49200;

	public static final int TLS_ECDH_RSA_WITH_AES_128_GCM_SHA256 = 49201;

	public static final int TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384 = 49202;

	public static final int TLS_EMPTY_RENEGOTIATION_INFO_SCSV = 255;

	public CipherSuite() {
	}
}
