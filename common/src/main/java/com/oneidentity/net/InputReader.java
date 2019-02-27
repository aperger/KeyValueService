package com.oneidentity.net;

import java.io.BufferedReader;
import java.io.IOException;

public class InputReader {

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
			char[] buffer = new char[BUFFERSIZE];
			readsize = input.read(buffer, 0, BUFFERSIZE);
			if (readsize > 0) {
				allReaded += readsize;
				strBuffer = (new String(buffer)).substring(0, readsize);

				// invalid request
				if ((strBuffer.length() == 0)) {
					return -1 * allReaded;
				}

				request.append(strBuffer);

				if (strBuffer.endsWith(System.lineSeparator())) {
					return allReaded;
				}
			}

		} while (readsize > 0);
		return allReaded;
	}

}
