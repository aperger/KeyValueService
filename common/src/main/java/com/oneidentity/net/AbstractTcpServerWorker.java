package com.oneidentity.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public abstract class AbstractTcpServerWorker implements Runnable {

	private ClientSocketItem clientSocket = null;

	public AbstractTcpServerWorker(ClientSocketItem clientSocket) {
		super();
		this.clientSocket = clientSocket;
	}

	public ClientSocketItem getClientSocket() {
		return clientSocket;
	}

	/**
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		try {
			StringBuilder request = new StringBuilder();
			try {
				BufferedReader input = new BufferedReader(
						new InputStreamReader(clientSocket.getSocket().getInputStream()));
				PrintWriter output = new PrintWriter(clientSocket.getSocket().getOutputStream());
				InputReader.read(input, request);
				workOnRequest(output, request.toString());
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		} finally {
			try {
				System.out.println("Close a client sokcet: " + clientSocket.getTimestamp());
				clientSocket.getSocket().close();
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
	}

	public abstract void workOnRequest(PrintWriter output, String request);

}
