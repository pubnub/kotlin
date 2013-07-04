/**
 * 
 * A lightweight TLS API.
 */
package org.bouncycastle.crypto.tls;


public class ProtocolVersion {

	public static final ProtocolVersion SSLv3;

	public static final ProtocolVersion TLSv10;

	public static final ProtocolVersion TLSv11;

	public static final ProtocolVersion TLSv12;

	public int getFullVersion() {
	}

	public int getMajorVersion() {
	}

	public int getMinorVersion() {
	}

	public boolean equals(Object obj) {
	}

	public int hashCode() {
	}

	public static ProtocolVersion get(int major, int minor) {
	}
}
