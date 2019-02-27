package com.oneidentity.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Abstract TCP client for communication
 */
public abstract class AbstractTcpClient {

	private Socket s;
	private String server;
	private int serverPort = -1;
	private BufferedReader input;
	private PrintWriter output;
	private StringBuilder request;
	private StringBuilder response;

	public AbstractTcpClient(String server, int serverPort) {
		super();
		this.request = new StringBuilder();
		this.response = new StringBuilder();
		this.serverPort = serverPort;
		this.server = server;
		this.s = null;
	}

	/**
	 * Parse the response, from the input stream as a string
	 * 
	 * @return boolean, true parse is ready without error.
	 */
	protected abstract boolean parseResponse();

	public boolean connect() {
		if (s == null) {
			s = new Socket();
		}
		if (this.s.isConnected() && this.s.isBound() && (!this.s.isClosed()))
			return true;
		try {
			InetAddress addr = InetAddress.getByName(this.server);
			s.setKeepAlive(true);
			this.s.connect(new InetSocketAddress(addr, this.serverPort));
			input = new BufferedReader(new InputStreamReader(s.getInputStream()));
			output = new PrintWriter(s.getOutputStream());
			return true;
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
		s = null;
		return false;
	}

	/**
	 * Close the socket
	 */
	public void close() {
		if (s == null)
			return;
		if (!s.isConnected())
			return;
		if (s.isClosed())
			return;
		try {
			s.close();
			s = null;
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}

	public boolean sendAndReceive() {
		if (!isConnected()) {
			this.connect();
		}
		boolean result = workOnSendRequest(output);
		if (result) {
			result = workOnReceiveResponse(input);
		}
		this.close();
		return result;
	}

	protected boolean workOnSendRequest(PrintWriter output) {
		if ((this.request == null))
			return false;
		if (this.request.length() == 0)
			return false;
		output.println(request.toString());
		output.println();
		output.flush();
		return true;
	}

	protected boolean workOnReceiveResponse(BufferedReader input) {
		if ((this.response == null))
			return false;
		this.response.setLength(0);
		try {
			int count = InputReader.read(input, response);
			if (count < 0) {
				return false;
			}
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
		return parseResponse();
	}

	public boolean isConnected() {
		if (this.s == null)
			return false;
		return (this.s.isConnected() && (!this.s.isClosed()) && this.s.isBound());
	}

	public String getServer() {
		return server;
	}

	public int getServerport() {
		return serverPort;
	}

	public StringBuilder getRequest() {
		return request;
	}

	public StringBuilder getResponse() {
		return response;
	}

}
