package com.oneidentity.test;

import java.util.Scanner;

import com.oneidentity.client.TcpClient;

public class Client {

	private static Scanner scanner = new Scanner(System.in);
    private static String hostName;
    private static int portNumber;
    
	public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println(
                "Usage: java -cp=[...]" + Client.class.getSimpleName() + " <host name> <port number>");
            System.exit(1);
        }
        
        hostName = args[0];
        portNumber = Integer.parseInt(args[1]);
        
        TcpClient tcpClient = new TcpClient(hostName, portNumber);
		int counter = 0;
		boolean exit = false;
		while (!exit && scanner.hasNext() ) {
			String line = scanner.nextLine();
			exit = line.isEmpty() || "\\quit".equals(line);
			if(!exit) {
				System.out.printf("%d. %s%s", ++counter, line, System.lineSeparator());
				tcpClient.getRequest().setLength(0);
				tcpClient.getRequest().append(line);
				tcpClient.sendAndReceive();
				System.out.println("Response: " + tcpClient.getResponse().toString());
			} 
		}

		System.out.println("********** E N D *********");
		tcpClient.close();
	}

}
