package com.shruti.capstone.mapreduce.xmlparser;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ParseDevice extends Baseclass {

	public ParseDevice(Socket s) {
		super(s);
	}

	public List<Response> xmlparsing() {
		final List<Response> res = new ArrayList<Response>();
		return res;
	}
}
