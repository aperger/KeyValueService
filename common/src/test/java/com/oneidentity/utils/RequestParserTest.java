package com.oneidentity.utils;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class RequestParserTest {
	
	private static String[] requests;
	private static boolean[] results;

	@BeforeClass
	public static void setUpBeforeClass() {
		requests = new String[]{
				"\\send ADRR1212d Ez az első tesztsor",
				"kud tidtr",
				"\\push jhj",
				"\\send ADRR52742ZZU1212d 2. tesztsor",
				"\\send CC3333VVV333 3.",
				"\\get CC3333VVV333",
				"\\get",
				"\\get MISSING01",
				"\\getall",
				"\\quit"
			};
		results = new boolean[] {true, false, false, true, true, true, true, true, false, true};
	}

	@Test
	public final void testCheck() {
		final String commandFailed = "Value of command is wrong";
		final String keyFailed = "Value of key is wrong";
		final String valueFailed = "Value is wrong";
		RequestParser parser = new RequestParser();
		for (int i = 0; i < results.length; i++) {
			boolean expected = results[i];
			String request = requests[i];
			parser.setRawRequest(request);
			
			if (expected) {
				assertTrue("Test failed at request: " + request, parser.isValid());
			} else {
				assertFalse("Test failed at request: " + request, parser.isValid());
			}
			
			switch (i) {
			case 0:
				assertEquals(commandFailed, RequestParser.COMMAND_SEND, parser.getCommand());
				assertEquals(keyFailed, "ADRR1212d", parser.getKey());
				assertEquals(valueFailed, "Ez az első tesztsor", parser.getValue());
				break;
			case 1:
			case 2:
			case 8:
				assertEquals(commandFailed, "", parser.getCommand());
				assertEquals(keyFailed, "", parser.getKey());
				assertEquals(valueFailed, "", parser.getValue());				
				break;
			case 3:
				assertEquals(commandFailed, RequestParser.COMMAND_SEND, parser.getCommand());
				assertEquals(keyFailed, "ADRR52742ZZU1212d", parser.getKey());
				assertEquals(valueFailed, "2. tesztsor", parser.getValue());				
				break;
			case 4:
				assertEquals(commandFailed, RequestParser.COMMAND_SEND, parser.getCommand());
				assertEquals(keyFailed, "CC3333VVV333", parser.getKey());
				assertEquals(valueFailed, "3.", parser.getValue());
				break;
			case 5:
				assertEquals(commandFailed, RequestParser.COMMAND_GET, parser.getCommand());
				assertEquals(keyFailed, "CC3333VVV333", parser.getKey());
				assertEquals(valueFailed, "", parser.getValue());
				break;
			case 6:
				assertEquals(commandFailed, RequestParser.COMMAND_GET, parser.getCommand());
				assertEquals(keyFailed, "", parser.getKey());
				assertEquals(valueFailed, "", parser.getValue());
				break;
			case 7:
				assertEquals(commandFailed, RequestParser.COMMAND_GET, parser.getCommand());
				assertEquals(keyFailed, "MISSING01", parser.getKey());
				assertEquals(valueFailed, "", parser.getValue());
				break;
			case 9:
				assertEquals(commandFailed, RequestParser.COMMAND_QUIT, parser.getCommand());
				assertEquals(keyFailed, "", parser.getKey());
				assertEquals(valueFailed, "", parser.getValue());
				break;				
			default:
				break;
			}
		}
	}

}
