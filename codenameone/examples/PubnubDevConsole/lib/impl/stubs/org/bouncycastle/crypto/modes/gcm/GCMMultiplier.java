package org.bouncycastle.crypto.modes.gcm;


public interface GCMMultiplier {

	public void init(byte[] H);

	public void multiplyH(byte[] x);
}
