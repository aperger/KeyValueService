package com.oneidentity.net;

import java.io.BufferedReader;
import java.io.IOException;

public class InputReader {
	
	public static final String MESSAGE_SEPARATOR = "\n\t";

	private InputReader() {
	}

	public static final void clearBuffer(StringBuilder buff) {
		buff.setLength(0);
	}

	public static int read(BufferedReader input, StringBuilder request) throws IOException {
		int readsize = 0;
		int allReaded = 0;
		String strBuffer = "";
		final int BUFFERSIZE = 8192;

		InputReader.clearBuffer(request);

		do {
			char[] buffer = new char[BUFFERSIZE + MESSAGE_SEPARATOR.length()];
			readsize = input.read(buffer, 0, BUFFERSIZE + MESSAGE_SEPARATOR.length());
			if (readsize > 0) {
				allReaded += readsize;
				strBuffer = (new String(buffer)).substring(0, readsize);
				request.append(strBuffer);
				
				// Check the end of hole request to find the separator
				int fromIndex = request.length() > MESSAGE_SEPARATOR.length() ? request.length() - MESSAGE_SEPARATOR.length() : 0;
				int pos = request.indexOf(MESSAGE_SEPARATOR, fromIndex);
				if (pos >= 0) {
					request.setLength(request.length() - MESSAGE_SEPARATOR.length());
					return allReaded;
				}
			} else {
				int fromIndex = request.length() > MESSAGE_SEPARATOR.length() ? request.length() - MESSAGE_SEPARATOR.length() : 0;
				int pos = request.indexOf(MESSAGE_SEPARATOR, fromIndex);
				if (pos < 0) {
					request.setLength(0);
				}
			}

		} while (readsize > 0);
		return allReaded;
	}

}
