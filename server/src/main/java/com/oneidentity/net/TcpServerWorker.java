package com.oneidentity.net;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

import com.oneidentity.utils.RequestParser;

public class TcpServerWorker extends AbstractTcpServerWorker {

	public static final String ACK_DONE = "DONE";
	public static final String ACK_ERROR = "ERROR";
	
	private Map<String, String> items;

	private RequestParser requestParser;

	public TcpServerWorker(ClientSocketItem clientSocket, Map<String, String> items) {
		super(clientSocket);
		this.requestParser = new RequestParser();
		this.items = items;
	}

	private void actionSend(String key, String value, PrintWriter output) {
		if (key == null || value == null || key.isEmpty()) {
			output.write(ACK_ERROR + " : invalid key-value pair");
		} else {
			items.put(key, value);
			output.write(ACK_DONE + " : size is " + items.size());
		}
		output.write(System.lineSeparator());
	}

	private void actionGet(String key, PrintWriter output) {
		if (key != null && !key.isEmpty()) {
			// send back only one value
			String value = items.get(key);
			output.write(String.format("%s %s%n", key, value));
		} else {
			synchronized (items) { // Synchronizing on map, not on set!
				Set<String> keys = items.keySet();
				for (String actKey : keys) {
					output.write(String.format("%s %s%n", actKey, items.get(actKey)));
				}
			}

		}
		output.write(System.lineSeparator());
	}

	private void actionQuit(PrintWriter output) {
		// ECHO
		output.write(RequestParser.COMMAND_QUIT);
		output.write(System.lineSeparator());
	}

	/**
	 * @see AbstractTcpServerWorker#workOnRequest(PrintWriter, String)
	 */
	public void workOnRequest(PrintWriter output, String request) {
		try {
			requestParser.setRawRequest(request);

			if (RequestParser.COMMAND_SEND.equals(requestParser.getCommand())) {
				actionSend(requestParser.getKey(), requestParser.getValue(), output);
			} else if (RequestParser.COMMAND_GET.equals(requestParser.getCommand())) {
				actionGet(requestParser.getKey(), output);
			} else if (RequestParser.COMMAND_QUIT.equals(requestParser.getCommand())) {
				actionQuit(output);
			}
		} catch (Exception e1) {
			return;
		}
		output.flush();
	}

}
