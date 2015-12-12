package com.shruti.capstone.mapreduce.xmlparser;
/*
 * Capstone project by Shruti Gorde
 * class to traverse the word file element by element.
 * 
 */
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class Handler extends DefaultHandler {
	private StringBuilder str;
	private List<Response> res;
	private Response rp;
	

	public List<Response> getRes() {
		return this.res;
	}

	@Override
	public void characters(char[] character, int begin, int size)
			throws SAXException {
		super.characters(character, begin, size);
		str.append(character, begin, size);
	}

	@Override
	public void endElement(String string, String device, String title)
			throws SAXException {
		super.endElement(string, device, title);
		if (this.rp != null) {
			if (device.equalsIgnoreCase(Baseclass.EXPLAIN)) {
				rp.setExplain(str.toString().trim());
			} else if (device.equalsIgnoreCase(Baseclass.TITLE)) {
				rp.setTitle(str.toString().trim());
			} else if (device.equalsIgnoreCase(Baseclass.VALUE)) {
				rp.getValues().add(str.toString().trim());
			} else if (device.equalsIgnoreCase(Baseclass.TASK_ASSIGN)) {
				res.add(rp);
			} else if (device.equalsIgnoreCase(Baseclass.TYPEACTIVITY)) {
				rp.setActivity(str.toString().trim().toUpperCase());
			} 
			str.setLength(0);
		}
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		res = new ArrayList<Response>();
		str = new StringBuilder();
	}

	@Override
	public void startElement(String string, String device, String title,
			Attributes attr) throws SAXException {
		super.startElement(string, device, title, attr);
		if (device.equalsIgnoreCase(Baseclass.TASK_ASSIGN)) {
			this.rp = new Response();
		}
	}
}