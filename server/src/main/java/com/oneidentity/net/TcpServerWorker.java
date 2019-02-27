package com.oneidentity.net;

import java.io.PrintWriter;

public class TcpServerWorker extends AbstractTcpServerWorker {

	public TcpServerWorker(ClientSocketItem aClientSocket) {
		super(aClientSocket);
	}

	/**
     * @see AbstractTcpServerWorker#workOnRequest(PrintWriter, String)
     */ 
    public void workOnRequest(PrintWriter output, String request) {
        try {
        	System.out.println("Request: " + request);
        	output.write("Received");
        } catch (Exception e1) {
            return;
        }        
        output.flush();
    }
    
}
