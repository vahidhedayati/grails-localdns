package localdns;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.xbill.DNS.ARecord;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.PTRRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

import sun.net.spi.nameservice.NameService;

/**
 * 
 * @version $Id$
 * @author Roman Kuzmik (rkuzmik@gmail.com)
 */
@SuppressWarnings("restriction")
public class DNSJavaNameService implements NameService {

	/**
	 * Finds A records (ip addresses) for the host name.
	 * 
	 * @param The
	 *            host name to resolve.
	 * @return All the ip addresses found for the host name.
	 */
	public InetAddress[] lookupAllHostAddr(String name)
			throws UnknownHostException {

		try {
			System.out.println("----------- DNSJavaNameService InetAddress "+name);
			Record[] records = new Lookup(name, Type.A).run();
			if (records == null)
				throw new UnknownHostException(name);

			InetAddress[] array = new InetAddress[records.length];
			for (int i = 0; i < records.length; i++) {
				ARecord a = (ARecord) records[i];
				array[i] = a.getAddress();
			}

			return array;
		} catch (TextParseException e) {
			throw new UnknownHostException(e.getMessage());
		}
	}

	/**
	 * Finds PTR records (reverse dns lookups) for the ip address
	 * 
	 * @param The
	 *            ip address to lookup.
	 * @return The host name found for the ip address.
	 * @throws TextParseException
	 */
	public String getHostByAddr(byte[] ip) throws UnknownHostException {
		System.out.println("----------- DNSJavaNameService getHostByAddr ");
		try {
			String addr = Util.numericToTextFormat(ip);
			Record[] records = new Lookup(addr, Type.PTR).run();
			if (records == null)
				throw new UnknownHostException(addr);
			PTRRecord ptr = (PTRRecord) records[0];
			return ptr.getTarget().toString();
		} catch (TextParseException e) {
			throw new UnknownHostException(e.getMessage());
		}
	}
}
