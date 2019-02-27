package com.oneidentity.client;

import com.oneidentity.net.AbstractTcpClient;


public class TcpClient extends AbstractTcpClient {
    
    public TcpClient(String server, int serverPort) {
		super(server, serverPort);
	}

	/* (non-Javadoc)
	 * @see com.oneidentity.net.AbstractTcpClient#parseResponse()
	 */
	@Override
	public boolean parseResponse() {
		return this.getResponse().length() > 0;
	}
    
	
	
}