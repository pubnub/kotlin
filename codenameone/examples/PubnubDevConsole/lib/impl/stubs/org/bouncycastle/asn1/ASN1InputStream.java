/**
 * 
 * A library for parsing and writing ASN.1 objects. Support is provided for DER and BER encoding.
 */
package org.bouncycastle.asn1;


/**
 *  a general purpose ASN.1 decoder - note: this class differs from the
 *  others in that it returns null after it has read the last object in
 *  the stream. If an ASN.1 NULL is encountered a DER/BER Null object is
 *  returned.
 */
public class ASN1InputStream extends javabc.FilterInputStream implements BERTags {

	public ASN1InputStream(java.io.InputStream is) {
	}

	/**
	 *  Create an ASN1InputStream based on the input byte array. The length of DER objects in
	 *  the stream is automatically limited to the length of the input array.
	 *  
	 *  @param input array containing ASN.1 encoded data.
	 */
	public ASN1InputStream(byte[] input) {
	}

	/**
	 *  Create an ASN1InputStream based on the input byte array. The length of DER objects in
	 *  the stream is automatically limited to the length of the input array.
	 * 
	 *  @param input array containing ASN.1 encoded data.
	 *  @param lazyEvaluate true if parsing inside constructed objects can be delayed.
	 */
	public ASN1InputStream(byte[] input, boolean lazyEvaluate) {
	}

	/**
	 *  Create an ASN1InputStream where no DER object will be longer than limit.
	 *  
	 *  @param input stream containing ASN.1 encoded data.
	 *  @param limit maximum size of a DER encoded object.
	 */
	public ASN1InputStream(java.io.InputStream input, int limit) {
	}

	/**
	 *  Create an ASN1InputStream where no DER object will be longer than limit, and constructed
	 *  objects such as sequences will be parsed lazily.
	 * 
	 *  @param input stream containing ASN.1 encoded data.
	 *  @param lazyEvaluate true if parsing inside constructed objects can be delayed.
	 */
	public ASN1InputStream(java.io.InputStream input, boolean lazyEvaluate) {
	}

	/**
	 *  Create an ASN1InputStream where no DER object will be longer than limit, and constructed
	 *  objects such as sequences will be parsed lazily.
	 * 
	 *  @param input stream containing ASN.1 encoded data.
	 *  @param limit maximum size of a DER encoded object.
	 *  @param lazyEvaluate true if parsing inside constructed objects can be delayed.
	 */
	public ASN1InputStream(java.io.InputStream input, int limit, boolean lazyEvaluate) {
	}

	protected int readLength() {
	}

	protected void readFully(byte[] bytes) {
	}

	/**
	 *  build an object given its tag and the number of bytes to construct it from.
	 */
	protected ASN1Primitive buildObject(int tag, int tagNo, int length) {
	}

	public ASN1Primitive readObject() {
	}
}
