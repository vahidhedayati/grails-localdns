package localdns;

/**
 * Static utilities used to convert IP Addresses between the various formats
 * used internally by the java.net.InetAddress class and associated classes.
 * 
 * @author Paul Cowan (pwc21@yahoo.com)
 * @version 0.9
 * 
 */
public class Util {
	public static final int INADDRSZ = 4;

	private Util() {
		// na
	}

	/**
	 * Converts the internal integer representation of an IPv4 into a binary
	 * address
	 * 
	 * @param an
	 *            integer representation of the IPv4 address
	 * @return a byte array representing an IPv4 numeric address
	 */
	public static byte[] intToNumericFormat(int src) {
		byte[] addr = new byte[INADDRSZ];

		addr[0] = (byte) ((src >>> 24) & 0xFF);
		addr[1] = (byte) ((src >>> 16) & 0xFF);
		addr[2] = (byte) ((src >>> 8) & 0xFF);
		addr[3] = (byte) (src & 0xFF);

		return addr;
	}

	/**
	 * Converts IPv4 binary address a single integer representation as used
	 * internally by Inet4Address
	 * 
	 * @param src
	 *            a byte array representing an IPv4 numeric address
	 * @return an integer representation of the IPv4 address
	 */
	public static int numericToIntFormat(byte[] addr) {
		int address = -1;

		if (addr.length == INADDRSZ) {
			address = addr[3] & 0xFF;
			address |= ((addr[2] << 8) & 0xFF00);
			address |= ((addr[1] << 16) & 0xFF0000);
			address |= ((addr[0] << 24) & 0xFF000000);
		}

		return address;
	}

	/**
	 * Converts IPv4 binary address into a string suitable for presentation.
	 * 
	 * @param src
	 *            a byte array representing an IPv4 numeric address
	 * @return a String representing the IPv4 address in textual representation
	 *         format
	 */
	public static String numericToTextFormat(byte[] src) {
		return (src[0] & 0xff) + "." + (src[1] & 0xff) + "." + (src[2] & 0xff)
				+ "." + (src[3] & 0xff);
	}

	/**
	 * Converts IPv4 address in its textual presentation form into its numeric
	 * binary form.
	 * 
	 * @param src
	 *            a String representing an IPv4 address in standard format
	 * @return a byte array representing the IPv4 numeric address
	 */
	public static byte[] textToNumericFormat(String src) {
		if (src == null || src.length() == 0)
			return null;

		int octets;
		char ch;
		byte[] dst = new byte[INADDRSZ];
		char[] srcb = src.toCharArray();
		boolean saw_digit = false;

		octets = 0;
		int i = 0;
		int cur = 0;
		while (i < srcb.length) {
			ch = srcb[i++];
			if (Character.isDigit(ch)) {
				int sum = dst[cur] * 10 + (Character.digit(ch, 10) & 0xff);

				if (sum > 255)
					return null;

				dst[cur] = (byte) (sum & 0xff);
				if (!saw_digit) {
					if (++octets > INADDRSZ)
						return null;
					saw_digit = true;
				}
			} else if (ch == '.' && saw_digit) {
				if (octets == INADDRSZ)
					return null;
				cur++;
				dst[cur] = 0;
				saw_digit = false;
			} else
				return null;
		}

		if (octets < INADDRSZ)
			return null;
		return dst;
	}

}
