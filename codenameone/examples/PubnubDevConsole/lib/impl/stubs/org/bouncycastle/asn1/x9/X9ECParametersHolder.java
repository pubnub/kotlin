/**
 * 
 * Support classes useful for encoding and supporting X9.62 elliptic curve.
 */
package org.bouncycastle.asn1.x9;


public abstract class X9ECParametersHolder {

	public X9ECParametersHolder() {
	}

	public X9ECParameters getParameters() {
	}

	protected abstract X9ECParameters createParameters() {
	}
}
