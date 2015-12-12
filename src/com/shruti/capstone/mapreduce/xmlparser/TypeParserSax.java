package com.shruti.capstone.mapreduce.xmlparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.shruti.capstone.mapreduce.resultdownload.InputFiles;

import android.util.Log;

public class TypeParserSax extends Baseclass {

	protected TypeParserSax(Socket s) {
		super(s);
	}

	public List<Response> xmlparsing() {
		SAXParser xmlp;
		List<Response> msg = null;
		Handler h = new Handler();
		SAXParserFactory f = SAXParserFactory.newInstance();
		try {
			xmlp = f.newSAXParser();
			InputStream is = new FileInputStream(
					new File(InputFiles.Path_Download +
							File.separator + InputFiles.File_xml)); 
			xmlp.parse(is, h);			
			msg = h.getRes();
		} catch (ParserConfigurationException e) {
			Log.e(this.getClass().getSimpleName(), e.getMessage());
		} catch (SAXException e) {
			Log.e(this.getClass().getSimpleName(), e.getMessage());
		} catch (IOException e) {
			msg = h.getRes();
		}
	
		return msg;
	}
}