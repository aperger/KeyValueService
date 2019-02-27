package com.oneidentity.app;

import java.util.Scanner;

import com.oneidentity.client.TcpClient;
import com.oneidentity.utils.RequestParser;

public class Client {

	private static Scanner scanner = new Scanner(System.in);
    
	public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println(
                "Usage: java -cp=[...]" + Client.class.getSimpleName() + " <host name> <port number>");
            System.exit(1);
        }
        
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        
        RequestParser parser = new RequestParser();
        
        TcpClient tcpClient = new TcpClient(hostName, portNumber);
		int counter = 0;
		boolean exit = false;
		while (!exit && scanner.hasNext() ) {
			String line = scanner.nextLine();
			exit = line.isEmpty() || "\\quit".equals(line);
			if(!exit) {
				
				
				parser.setRawRequest(line);
				
				if (!parser.isValid()) {
					System.err.println("Invalid request: " + line);
				} else {
					counter++;
					tcpClient.getRequest().setLength(0);
					tcpClient.getRequest().append(line);
					tcpClient.sendAndReceive();
					System.out.printf("Response #%d:\t%n%s", counter, tcpClient.getResponse().toString());
				}
				
				
			} 
		}

		System.out.println("********** E N D *********");
		tcpClient.close();
	}

}
