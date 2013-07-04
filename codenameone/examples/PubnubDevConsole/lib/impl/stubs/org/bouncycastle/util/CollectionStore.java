package org.bouncycastle.util;


/**
 *  A simple collection backed store.
 */
public class CollectionStore implements Store {

	/**
	 *  Basic constructor.
	 * 
	 *  @param collection - initial contents for the store, this is copied.
	 */
	public CollectionStore(java.util.Collection collection) {
	}

	/**
	 *  Return the matches in the collection for the passed in selector.
	 * 
	 *  @param selector the selector to match against.
	 *  @return a possibly empty collection of matching objects.
	 */
	public java.util.Collection getMatches(Selector selector) {
	}
}
