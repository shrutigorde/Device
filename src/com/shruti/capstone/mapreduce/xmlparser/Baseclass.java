package com.shruti.capstone.mapreduce.xmlparser;
/*
 * Capstone project by Shruti Gorde
 * class to parse the xml file
 * 
 */
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public abstract class Baseclass implements Parsing {
	static final String FUNCTION = "function";
	static final String TASK_ASSIGN = "taskassign";
	static final String VALUES = "values";
	static final String VALUE = "value";
	static final String EXPLAIN = "explain";
	static final String TITLE= "title";
	
	static final String TYPEACTIVITY = "typeactivity";

	private final Socket soc;

	protected Baseclass(Socket soc) {
		this.soc = soc;
	}

	protected InputStream getInputStream() {
		try {
			return soc.getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}