package com.oneidentity.app;

import com.oneidentity.net.TcpServer;

public class Server {

	private TcpServer tcpServer = null;
	private int portNumber;

	public Server(int portNumber) {
		this.portNumber = portNumber;
		new Thread(this::startTcpServer).start();
	}

	public void startTcpServer() {
		tcpServer = new TcpServer(this.portNumber, 400);
		if (!tcpServer.bind())
			return;
		tcpServer.waitForClients();
	}

	public void stopTCPServer() {
		if (tcpServer != null) {
			tcpServer.stopp();
		}
	}

	public TcpServer getTcpServer() {
		return tcpServer;
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: java -cp [...] " + Server.class.getSimpleName() + " <port number>");
			System.err.println("-- OR --");
			System.err.println("Usage: <path to keyvalue-server>/bin/keyvalue-server <port number>");
			System.exit(1);
		}
		new Server(Integer.parseInt(args[0]));
	}

}
