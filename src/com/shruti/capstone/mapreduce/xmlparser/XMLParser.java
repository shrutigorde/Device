package com.shruti.capstone.mapreduce.xmlparser;
/*
 * Capstone project by Shruti Gorde
 * 
 * 
 */
import java.net.Socket;

public abstract class XMLParser {

	public static Parsing getXML(TypeXML xml, Socket s) {
		switch (xml) {
		case SIMPLE:
			return new TypeParserSax(s);
		case DEVICE:
			return new ParseDevice(s);
		default:
			return null;
		}
	}
}
