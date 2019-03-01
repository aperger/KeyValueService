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
		final int BUFFERSIZE = 5; //8192;

		InputReader.clearBuffer(request);

		do {
			char[] buffer = new char[BUFFERSIZE + MESSAGE_SEPARATOR.length()];
			readsize = input.read(buffer, 0, BUFFERSIZE + MESSAGE_SEPARATOR.length());
			if (readsize > 0) {
				allReaded += readsize;
				strBuffer = (new String(buffer)).substring(0, readsize);
				int pos = strBuffer.lastIndexOf(MESSAGE_SEPARATOR);
				if (pos < 0) {
					request.append(strBuffer);
				} else {
					strBuffer = strBuffer.substring(0, pos);					
					request.append(strBuffer);
					return allReaded;
				}
			}

		} while (readsize > 0);
		return allReaded;
	}

}
