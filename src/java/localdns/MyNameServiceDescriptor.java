package localdns;
import sun.net.spi.nameservice.NameService;
import sun.net.spi.nameservice.NameServiceDescriptor;

public final class MyNameServiceDescriptor implements NameServiceDescriptor {
	public NameService createNameService() throws Exception {
		return new MyNameService();
	}
	public String getProviderName() {
		return "mine";
	}
	public String getType() {
		return "dns";
	}
}