
package localdns


import java.security.Security

import javax.naming.Context
import javax.naming.directory.DirContext
import javax.naming.directory.InitialDirContext

import org.xbill.DNS.Address
import org.xbill.DNS.Lookup
import org.xbill.DNS.MXRecord
import org.xbill.DNS.Record
import org.xbill.DNS.Resolver
import org.xbill.DNS.SimpleResolver
import org.xbill.DNS.Type

class TestingController {
	
	
	
	
    def index() { 
		System.setProperty("sun.net.spi.nameservice.provider.1", "dns,"+LocalManagedDnsDescriptor.DNS_PROVIDER_NAME)
		System.setProperty("sun.net.spi.nameservice.provider.2", "dns,sun")
		Security.setProperty("networkaddress.cache.ttl", "0")
		def hostName = "google.com"
		def ipAddress = "192.168.1.4"
		NameStore.getInstance().put(hostName, ipAddress)
		performLookup(hostName)
		
		/*
		def http = new HTTPBuilder( 'http://google.com' )
		
	   http.request(GET,TEXT) { req ->
		 uri.path = '/' // overrides any path in the default URL
		 headers.'User-Agent' = 'Mozilla/5.0'
		
		 response.success = { resp, reader ->
		   assert resp.status == 200
		   println "My response handler got response: ${resp.statusLine}"
		   println "Response length: ${resp.headers.'Content-Length'}"
		   System.out << reader // print response reader
		 }
		
		 // called only for a 404 (not found) status code:
		 response.'404' = { resp ->
		   println 'Not found'
		 }
	   }
		*/
		
		render "done lookup"
		
		
		
	}
	
	def localResolver() {
		
		Resolver resolver = new SimpleResolver("8.8.8.8");
		Lookup lookup = new Lookup("dribbleeree.com", Type.A);
		lookup.setResolver(resolver);
		Record[] records = lookup.run();
		
		render "${records}"
	}
	
	def dnsLookup()  {
		System.setProperty("sun.net.spi.nameservice.provider.1","dns,dnsjava")
		Lookup lookup = new Lookup('grails.com', Type.ANY)
		def records = lookup.run()
		println "${records?.size()} record(s) found"
		records.each{ println it }
		render "View output on console"
	}
	def index2 () {
		
		System.out.println(System.getProperty("sun.net.spi.nameservice.nameservers"));
		System.out.println(System.getProperty("sun.net.spi.nameservice.provider.1"));
		// no effect?
		//System.setProperty("sun.net.spi.nameservice.nameservers", "8.8.8.8");
		//System.setProperty("sun.net.spi.nameservice.provider.1", "dns,sun");
		System.setProperty("sun.net.spi.nameservice.provider.1", "dnsjava");
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
		DirContext ictx = new InitialDirContext(env);
		String dnsServers = (String) ictx.getEnvironment().get("java.naming.provider.url");
		System.out.println("DNS Servers: " + dnsServers);
		List nameservers = sun.net.dns.ResolverConfiguration.open().nameservers();
		for (Object dns : nameservers) {
		System.out.println(dns + " ");
		}
		
		
		System.out.println(System.getProperty("sun.net.spi.nameservice.nameservers"));
		System.out.println(System.getProperty("sun.net.spi.nameservice.provider.1"));
		InetAddress addr = Address.getByName("www.dnsjava.org");
		Record[] records = new Lookup("gmail.com", Type.MX).run();
		for (int i = 0; i < records.length; i++) {
		MXRecord mx = (MXRecord) records[i];
		System.out.println("Host " + mx.getTarget() + " has preference " + mx.getPriority());
		}
		
		
		System.setProperty("sun.net.spi.nameservice.provider.1", LocalManagedDnsDescriptor.DNS_PROVIDER_NAME);
		env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
		ictx = new InitialDirContext(env);
		dnsServers = (String) ictx.getEnvironment().get("java.naming.provider.url");
		System.out.println("DNS Servers 2 : " + dnsServers);
		nameservers = sun.net.dns.ResolverConfiguration.open().nameservers();
		for (Object dns : nameservers) {
		System.out.println(dns + " ");
		}
		
		dnsServers = (String) ictx.getEnvironment().get("sun.net.spi.nameservice.provider.1");
		System.out.println("Providers2 : " + dnsServers);
		
		
		def hostName = "google.com";
		def ipAddress = "127.0.0.1";

		NameStore.getInstance().put(hostName, ipAddress);
		//
		records = new Lookup(hostName, Type.A).run();
		for (int i = 0; i < records.length; i++) {
			println "--- > ${records[i]}"
		}
		performLookup(hostName);
		render "WOOOOT"
		
	}
	
	public static void performLookup(String hostName) throws UnknownHostException {
		InetAddress[] addrs = InetAddress.getAllByName(hostName);
		for (int j = 0; j < addrs.length; j++) {
			println("------>"+addrs[j].toString());
		}
	}
}
