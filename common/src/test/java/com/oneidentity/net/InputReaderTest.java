package com.oneidentity.net;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class InputReaderTest {
	
	
	private BufferedReader createInput(String rawValue) {
		Reader inputString = new StringReader(rawValue);
		return new BufferedReader(inputString);
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testValid() throws IOException {
		String rawRequest = "\\send SOMEID Some value of calculation";
		BufferedReader input = createInput(rawRequest + InputReader.MESSAGE_SEPARATOR);
		
		StringBuilder request = new StringBuilder();
		InputReader.read(input, request);
		
		assertEquals("The reading of the request stream failded", rawRequest, request.toString());
	}

	
	@Test
	public final void testInvalid() throws IOException {
		String rawRequest = "\\send SOMEID Some value of calculation";
		BufferedReader input = createInput(rawRequest);
		
		StringBuilder request = new StringBuilder();
		InputReader.read(input, request);
		
		assertNotEquals("The parsing of the request stream must provide empty result", rawRequest, request.toString());
	}
}
