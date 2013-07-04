package org.bouncycastle.util;


public class IPAddress {

	public IPAddress() {
	}

	/**
	 *  Validate the given IPv4 or IPv6 address.
	 * 
	 *  @param address the IP address as a String.
	 * 
	 *  @return true if a valid address, false otherwise
	 */
	public static boolean isValid(String address) {
	}

	/**
	 *  Validate the given IPv4 or IPv6 address and netmask.
	 * 
	 *  @param address the IP address as a String.
	 * 
	 *  @return true if a valid address with netmask, false otherwise
	 */
	public static boolean isValidWithNetMask(String address) {
	}

	/**
	 *  Validate the given IPv4 address.
	 *  
	 *  @param address the IP address as a String.
	 * 
	 *  @return true if a valid IPv4 address, false otherwise
	 */
	public static boolean isValidIPv4(String address) {
	}

	public static boolean isValidIPv4WithNetmask(String address) {
	}

	public static boolean isValidIPv6WithNetmask(String address) {
	}

	/**
	 *  Validate the given IPv6 address.
	 * 
	 *  @param address the IP address as a String.
	 * 
	 *  @return true if a valid IPv4 address, false otherwise
	 */
	public static boolean isValidIPv6(String address) {
	}
}
