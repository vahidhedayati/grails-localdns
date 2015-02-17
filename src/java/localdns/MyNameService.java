package localdns;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.lang.StringUtils;

import sun.net.spi.nameservice.NameService;
import sun.net.spi.nameservice.dns.DNSNameService;
public class MyNameService implements NameService {
	static {
		//System.setProperty("sun.net.spi.nameservice.nameservers", "localhost"); // ETL
	}
	NameService ns;
	public MyNameService() throws Exception {
		ns = new DNSNameService();
	}


	public String getHostByAddr(byte[] ip) throws UnknownHostException {
		System.out.println ("---LocalManagedDns getHostByAddr");
		return ns.getHostByAddr(ip);
	}

	public InetAddress[] lookupAllHostAddr(String name)
			throws UnknownHostException {
		System.out.println ("---LocalManagedDns lookupAllHostAddr: "+name);
		String ipAddress = NameStore.getInstance().get(name);
		System.out.println ("---LocalManagedDns lookupAllHostAddr IP: "+ipAddress);
		if (StringUtils.isNotEmpty(ipAddress)) {
			InetAddress address = Inet4Address.getByAddress(Util
					.textToNumericFormat(ipAddress));
			return new InetAddress[] { address };
		} else {
			return ns.lookupAllHostAddr(name);
		}
	}
}