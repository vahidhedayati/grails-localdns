package localdns;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.lang.StringUtils;

import sun.net.spi.nameservice.NameService;
import sun.net.spi.nameservice.dns.DNSNameService;

/**
 * 
 * @version $Id$
 * @author Roman Kuzmik (rkuzmik@gmail.com)
 */
@SuppressWarnings("restriction")
public class LocalManagedDns implements NameService {

	NameService defaultDnsImpl = new DNSJavaNameService();
	
	/*NameService defaultDnsImpl;
	public LocalManagedDns() throws Exception {
		defaultDnsImpl = new DNSNameService();
	}*/
	/**
	 * @see sun.net.spi.nameservice.NameService#getHostByAddr(byte[])
	 */
	@Override
	public String getHostByAddr(byte[] ip) throws UnknownHostException {
		System.out.println ("---LocalManagedDns getHostByAddr");
		return defaultDnsImpl.getHostByAddr(ip);
	}

	/**
	 * @see sun.net.spi.nameservice.NameService#lookupAllHostAddr(java.lang.String)
	 */
	@Override
	public InetAddress[] lookupAllHostAddr(String name)
			throws UnknownHostException {
		System.out.println ("---LocalManagedDns lookupAllHostAddr: "+name);
		String ipAddress = NameStore.getInstance().get(name);
		if (StringUtils.isNotEmpty(ipAddress)) {
			InetAddress address = Inet4Address.getByAddress(Util
					.textToNumericFormat(ipAddress));
			return new InetAddress[] { address };
		} else {
			return defaultDnsImpl.lookupAllHostAddr(name);
		}
	}

}
