package org.bouncycastle.crypto.modes.gcm;


public interface GCMExponentiator {

	public void init(byte[] x);

	public void exponentiateX(long pow, byte[] output);
}
