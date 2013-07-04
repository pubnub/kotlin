package org.bouncycastle.util;


public interface Selector {

	public boolean match(Object obj);

	public Object clone();
}
