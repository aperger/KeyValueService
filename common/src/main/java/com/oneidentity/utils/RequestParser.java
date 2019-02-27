package com.oneidentity.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestParser {

	public static final String COMMAND_SEND = "\\send";
	public static final String COMMAND_GET = "\\get";
	public static final String COMMAND_QUIT = "\\quit";

	public static final String PATTERN = "([\\" + COMMAND_SEND 
			+ "|\\"	+ COMMAND_GET 
			+ "|\\" + COMMAND_QUIT
			+ "]+)\\s{0,1}([a-z,A-Z,0-9]*)?\\s{0,1}(.*)";

	private String rawRequest;
	private boolean isValid;
	private String command;
	private String key;
	private String value;

	protected void check() {
		Pattern r = Pattern.compile(PATTERN);

		command = "";
		key= "";
		value= "";
		
		Matcher m = r.matcher(getRawRequest());
		setValid(m.find());
		if (isValid()) {
			if (m.groupCount() >= 3) {
				command = m.group(1);
				key = m.group(2);
				value = m.group(3);
			} else if (m.groupCount() >= 2) {
				if (COMMAND_SEND.equals(m.group(1))) {
					setValid(false);
				} else {
					command = m.group(1);
					key= m.group(2);
					value= "";
				}
			} else if (m.groupCount() >= 1) {
				if (COMMAND_SEND.equals(m.group(1))) {
					setValid(false);
				} else {
					command = m.group(1);
					key = "";
					value = "";
				}
			}
		} 
	}

	public String getRawRequest() {
		return rawRequest;
	}

	public void setRawRequest(String rawRequest) {
		this.rawRequest = rawRequest;
		check();
	}

	public boolean isValid() {
		return isValid;
	}

	protected void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public String getCommand() {
		return command;
	}

	public static void main(String[] args) {
		String[] requests = new String[]{
			"\\send ADRR1212d Ez az első tesztsor",
			"kud tidtr",
			"\\push jhj",
			"\\send ADRR52742ZZU1212d 2. tesztsor",
			"\\send CC3333VVV333 3.",
			"\\get CC3333VVV333",
			"\\get",
			"\\quit"
		};
		boolean[] results = new boolean[] {true, false, false, true, true, true, true, true};
		
		if (results.length != requests.length) {
			throw new RuntimeException("Invalid test input");
		}
		RequestParser parser = new RequestParser();
		int counter = 0;
		for (int i = 0; i < results.length; i++) {
			boolean expected = results[i];
			String request = requests[i];
			parser.setRawRequest(request);
			
			if (expected != parser.isValid()) {
				System.err.println("Test failed at request: " + request);
			} else {
				System.out.printf("Valid (\"%s\")-> comand: %s, key: %s, value: %s%n", request, parser.getCommand(), parser.getKey(), parser.getValue());
				counter++;
			}
		}
		
		if (counter == 6) {
			System.out.println("Test successed");
		} else {
			System.out.println("Test failed");
		}
		
	}
}
