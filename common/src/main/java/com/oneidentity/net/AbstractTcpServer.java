package com.oneidentity.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractTcpServer extends Thread {        
    
    public static final int MAX_COUNT_OF_CLIENT_TREADS = 10;    
    private ServerSocket ss = null;
    private int serverPort = -1;
    private int timeout = 0;
    private NetworkInterface netInterface;
    private int countOfClientThread = 0;
    private Object lockCounter = new Object();
    private ClientSocketMap clients;
    private boolean closeThread = false;
    
    protected AtomicLong clientIdGenerator;
    

    public AbstractTcpServer(String aInterfaceName, int aServerPort, int aTimeOut) {
        super();
        this.clientIdGenerator = new AtomicLong(0);
        this.serverPort = aServerPort;
        this.timeout = aTimeOut;
        this.countOfClientThread = 0;
        this.clients = new ClientSocketMap();
        try {
            setUpNetInterface(aInterfaceName);
            this.ss = new ServerSocket();            
        } catch (Exception e) {
            this.ss = null;
            this.serverPort = -1;
            this.netInterface = null;  
        }        
    }
    
    /**
     * The abstract method, which will be called in {@link AbstractTcpServer#waitForClients()}
     * @param aClientSocket
     * @return {@link AbstractTcpServerWorker}
     */
    public abstract AbstractTcpServerWorker getWorkerThread(ClientSocketItem aClientSocket);

    public AbstractTcpServer(int aServerPort) {
        this(null,aServerPort, 0);            
    }   

    private void  setUpNetInterface(String aInterfaceName) {
        if ( (aInterfaceName == null) || (aInterfaceName.length()==0) ) {
            netInterface = null;
            return;
        }
        try {
            netInterface = NetworkInterface.getByName(aInterfaceName);            
        } catch (SocketException ex1) {
            netInterface = null;
        }
    }
    
    public boolean bind() {
        if (ss == null) {return false;}
        
        if (serverPort < 0 || serverPort > 0xFFFF) {
            System.err.println("Port value out of range: " + serverPort);
            return false;
        }        
        
        // collect ips -> define addresses
        try {
            ss.setSoTimeout(this.timeout);
            if (netInterface == null) {                  
                bindOnAvailableInterfaces();
            } else {
            	bindOnSelectedInterface();
            }
        } catch (IOException ex) {
        	System.err.println(ex.getMessage());
            return false;            
        }
        return true;
    }

	private void bindOnSelectedInterface() throws IOException {
		InetAddress addr;
		System.out.println("NetInterface: " + netInterface.getName() + " - " + netInterface.getDisplayName()); 
		Enumeration<InetAddress> inetAddList = netInterface.getInetAddresses();
		int count = 0;
		while (inetAddList.hasMoreElements()) {                        
		    addr = inetAddList.nextElement();
		    System.out.println("IP" + (++count)+ ". = " + addr.getHostAddress());
		    if (addr.isLinkLocalAddress()) {
		    	System.out.println(addr.getHostAddress() + " is a linked address, not bound into!");
		        continue;
		    }
		    ss.bind(new InetSocketAddress(addr, serverPort));
		    System.out.println(this.getClass().getName() + " is bound into " + addr.getHostAddress() + " on " + serverPort);
		}
	}

	private void bindOnAvailableInterfaces() throws SocketException, IOException {
		InetAddress addr;
		int count = 0;
		Enumeration<java.net.NetworkInterface> interfaces = java.net.NetworkInterface.getNetworkInterfaces();
		Enumeration<InetAddress> inetaddresses = null;
		NetworkInterface nwi;
		while (interfaces.hasMoreElements()){
		    nwi = interfaces.nextElement();
		    inetaddresses = nwi.getInetAddresses();
		    String link;
		    while (inetaddresses.hasMoreElements()) {
		        addr = inetaddresses.nextElement();
		        if (addr.isLinkLocalAddress()) {
		            link = " is a linked address";
		        } else {
		            link = "";
		        }
		        System.out.println("IP" + (++count)+ ". = " + addr.getHostAddress() + link);                         
		    }
		}
		
		ss.bind(new InetSocketAddress(this.serverPort));
		addr = ss.getInetAddress();
		if (addr != null) {
			System.out.println(this.getClass().getName() + " is bound on " + serverPort);             
		}
	}

	/**
	 * Start two threads:<br />
	 * - the listener one -> accept client requests<br />
	 * - a cleanup thread (if it was not started) -> reduce handled clients (sockets).<br />
	 */
    public void waitForClients() {
        Socket s = null;
        if (!this.isAlive()) {
        	start();
        }
        try {
            while (!closeThread) {        
                try {
                    s = ss.accept();
                    
                    ClientSocketItem cs = increaseCountOfClientThread(s);                                       
                    if (cs == null) continue;                    
                    AbstractTcpServerWorker worker = getWorkerThread(cs);
                    new Thread(worker).start();
                } catch (SocketTimeoutException e) {
                    // do nothing
                } catch (IOException ex) {
                	System.err.println(ex.getMessage());
                }
            }
        } finally {
            stopp();
            closeServerSocket();
        }
    }
        
    public void closeServerSocket() {
        if (ss==null) return;
        if (!ss.isBound()) return;
        if (ss.isClosed()) return;
        try {
            ss.close();
            ss = null;
        } catch (IOException ex) {
        	System.err.println(ex.getMessage());
        }
    }
    
    
    public ClientSocketItem increaseCountOfClientThread(Socket s) {
        synchronized (lockCounter) {
            
            if (MAX_COUNT_OF_CLIENT_TREADS <= this.countOfClientThread) {
                // TODO: Send response -> "THE SERVER IS TOO BUSY"
                try {
                    s.close();
                } catch (IOException e) {
                	// do nothing
                }
                return null;
            }                        

            if (clientIdGenerator.get() == Long.MAX_VALUE) {
            	clientIdGenerator.set(0); 
            }
            long longID = clientIdGenerator.addAndGet(1);
            ClientSocketItem cs = new ClientSocketItem(s, longID);
            this.clients.put(longID, cs);
            countOfClientThread++;
            return cs;
        }
    }
        
    public void decreaseCountOfClientThread(ClientSocketItem cs) {
        synchronized (lockCounter) {
            this.clients.remove(cs.getTimestamp());
            countOfClientThread--;
        }
    }
    
    /**
     * The cleanup part of the server , this started by the Thread.start() command.<br/>
     * DO NOT CALL directly the "start()" : {@link AbstractTcpServer#waitForClients()}
     */
    @Override
    public void run() {
        while (!closeThread) {
            ClientSocketItem cs;
            Iterator<ClientSocketItem> it = this.clients.values().iterator();
            while (it.hasNext()) {
                cs = it.next();
                if (cs.getSocket().isClosed()) {
                    it.remove();
                    decreaseCountOfClientThread(cs);
                }
            }            
            try {
            	Thread.sleep(100);
            } catch (InterruptedException e) {
            	System.err.println(e.getMessage());
            	Thread.currentThread().interrupt();
            }
        }        
    }
    
    
    public void stopp() {
        closeThread = true;
    }    
        
}


