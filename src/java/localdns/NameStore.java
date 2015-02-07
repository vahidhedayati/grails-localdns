package localdns;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
 
/**
 *
 * @version $Id$
 * @author Roman Kuzmik
 */

public class NameStore {
	   protected static NameStore singleton;
	   
	    protected Map globalNames;
	    protected ThreadLocal localThread;
	 
	    protected NameStore(){
	        globalNames = Collections.synchronizedMap(new HashMap());
	        localThread = new ThreadLocal();
	    }
	 
	    public static NameStore getInstance(){
	        if (singleton == null) {
	            synchronized (NameStore.class) {
	                if (singleton == null) {
	                    singleton = new NameStore();
	                }
	            }
	        }
	        return singleton;
	    }
	    public InetAddress[] lookupVahid(String name) 	throws UnknownHostException {
	    	InetAddress address = Inet4Address.getByAddress(Util
					.textToNumericFormat("127.0.0.9"));
			return new InetAddress[] { address };
		}
	    
	    public void put(String hostName, String ipAddress){
	    	System.out.println("NameStore Adding "+ipAddress);
	        globalNames.put(hostName, ipAddress);
	    }
	    public void remove(String hostName){
	        globalNames.remove(hostName);
	    }
	 
	    public synchronized void putLocal(String hostName, String ipAddress){
	        Map localThreadNames = (Map) localThread.get();
	        if (localThreadNames == null){
	            localThreadNames = Collections.synchronizedMap(new HashMap());
	            localThread.set(localThreadNames);
	        }
	        localThreadNames.put(hostName, ipAddress);
	    }
	    public void removeLocal(String hostName){
	        Map localThreadNames = (Map) localThread.get();
	        if (localThreadNames != null){
	            localThreadNames.remove(hostName);
	        }
	    }
	 
	    public String get(String hostName){
	        String ipAddress = null;
	        Map localThreadNames = (Map) localThread.get();
	        if (localThreadNames != null){
	            ipAddress = (String)localThreadNames.get(hostName);
	        }
	        if (StringUtils.isEmpty(ipAddress)) {
	            return (String)globalNames.get(hostName);
	        }
	        return ipAddress;
	    }
}