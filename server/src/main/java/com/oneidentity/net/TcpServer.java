package com.oneidentity.net;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class TcpServer extends AbstractTcpServer {
	
	protected final Map<String, String> items = Collections.synchronizedMap(new TreeMap<>());
           
    public TcpServer(
            String aInterfaceName, 
            int aServerPort, 
            int aTimeOut) {
        super(aInterfaceName, aServerPort, aTimeOut); 
        this.setPriority( (Thread.NORM_PRIORITY + Thread.MAX_PRIORITY) / 2 );
    }
    
    public TcpServer(
            int aServerPort, 
            int aTimeOut) {
       this(null, aServerPort, aTimeOut);
    }

	/* (non-Javadoc)
	 * @see com.oneidentity.net.TCPAbstractServer#getWorkerThread(com.oneidentity.net.ClientSocketItem)
	 */
	@Override
	public AbstractTcpServerWorker getWorkerThread(ClientSocketItem clientSocketItem) {
		return new TcpServerWorker(clientSocketItem, items);
	}
    
}
