# grails-localdns

This is an attempt to make this project work under grails:

http://rkuzmik.blogspot.co.uk/2006/08/local-managed-dns-java_11.html

The above links zip file did not work and was tracked down to another project..

http://jira.cubrid.org/secure/attachment/12394/grinder-core-ex-for-dns.zip

In both examples it talks about a file:

grinder-core-ex/target/classes/META-INF/services/sun.net.spi.nameservice.NameServiceDescriptor

````
# dns service provider descriptor
# This will provide name resolution using local API
net.grinder.dns.LocalManagedDnsDescriptor
```

I have tried this but this did not work for me, I then attempted registering in resources.groovy:

```
import localdns.LocalManagedDnsDescriptor

// Place your Spring DSL code here
beans = {
	NameServiceDescriptor(LocalManagedDnsDescriptor)
}
```


So far tried grails run-app not working

Then attempted to pull it into tomcat:
```
root      6520  135  3.5 1297200 142436 pts/12 Sl   11:00   0:05 /usr/bin/java -Djava.util.logging.config.file=/opt/vh/apache-tomcat-7.0.57/conf/logging.properties -Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager -Dsun.net.spi.nameservice.provider.1=dns,LocalManagedDns -Djava.endorsed.dirs=/opt/vh/apache-tomcat-7.0.57/endorsed -classpath /opt/vh/apache-tomcat-7.0.57/bin/bootstrap.jar:/opt/vh/apache-tomcat-7.0.57/bin/tomcat-juli.jar -Dcatalina.base=/opt/vh/apache-tomcat-7.0.57 -Dcatalina.home=/opt/vh/apache-tomcat-7.0.57 -Djava.io.tmpdir=/opt/vh/apache-tomcat-7.0.57/temp org.apache.catalina.startup.Bootstrap start
```

You can see:
```
-Dsun.net.spi.nameservice.provider.1=dns,LocalManagedDns
```

When I run:
http://localhost:8080/grails-localdns/testing/index

````
 def index() { 
		System.setProperty("sun.net.spi.nameservice.provider.1", "dns,"+LocalManagedDnsDescriptor.DNS_PROVIDER_NAME)
		System.setProperty("sun.net.spi.nameservice.provider.2", "dns,sun")
		Security.setProperty("networkaddress.cache.ttl", "0")
		def hostName = "google.com"
		def ipAddress = "192.168.1.4"
		NameStore.getInstance().put(hostName, ipAddress)
		performLookup(hostName)
		render "done lookup"
	}
```
log shows:

```
eb 07, 2015 11:00:35 AM org.apache.coyote.AbstractProtocol start
INFO: Starting ProtocolHandler ["ajp-bio-8009"]
Feb 07, 2015 11:00:35 AM org.apache.catalina.startup.Catalina start
INFO: Server startup in 22695 ms
NameStore Adding 192.168.1.4
------>google.com/216.58.210.46
------>google.com/2a00:1450:4009:800:0:0:0:200e

````


When I do grails run-app

```
	System.out.println("LocalManagedDnsDescriptor------- static");
```

Works but none of the other methods are being called, so it seems the 
```
System.setProperty("sun.net.spi.nameservice.provider.1", "dns,"+LocalManagedDnsDescriptor.DNS_PROVIDER_NAME)
```
is being ignored 


Updated with new simpler override method - still ignored - and in both cases when given an invalid DNS entry - the output shows it attempted to access java.net.InetAddress$1.lookupAllHostAddr rather than local overriden method.


```
NameStore Adding 192.168.1.4
github.com/192.30.252.129
NameStore Adding 192.168.1.4
2015-02-17 09:54:34,091 [http-nio-8080-exec-5] ERROR errors.GrailsExceptionResolver  - UnknownHostException occurred when processing request: [GET] /localdns/testing/index5
githubbaaa.com: Name or service not known. Stacktrace follows:
java.net.UnknownHostException: githubbaaa.com: Name or service not known
	at java.net.InetAddress$1.lookupAllHostAddr(InetAddress.java:901)
	at java.net.InetAddress.getAddressesFromNameService(InetAddress.java:1293)
	at java.net.InetAddress.getAllByName0(InetAddress.java:1246)
	at java.net.InetAddress.getAllByName(InetAddress.java:1162)
	at java.net.InetAddress.getAllByName(InetAddress.java:1098)
	at java.net.InetAddress.getByName(InetAddress.java:1048)
	at localdns.TestingController.index5(TestingController.groovy:106)
	at grails.plugin.cache.web.filter.PageFragmentCachingFilter.doFilter(PageFragmentCachingFilter.java:198)
	at grails.plugin.cache.web.filter.AbstractFilter.doFilter(AbstractFilter.java:63)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
	at java.lang.Thread.run(Thread.java:745)
```


cat setenv.sh
``` 
# export CATALINA_OPTS="$CATALINA_OPTS -Dsun.net.spi.nameservice.provider.1=dns,LocalManagedDns"
export CATALINA_OPTS="$CATALINA_OPTS -Dsun.net.inetaddr.ttl=0"
export CATALINA_OPTS="$CATALINA_OPTS -Dsun.net.spi.nameservice.provider.1=dns,mine"
```


