package com.shruti.capstone;
/*
 * Capstone project by Shruti Gorde
 * task tracker class to keep track of all the tasks
 * 
 */
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.List;
import java.util.Map;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.shruti.capstone.mapreduce.action.base.MapReduce;
import com.shruti.capstone.mapreduce.action.base.Mapping;
import com.shruti.capstone.mapreduce.networkconfig.Ipconfig;
import com.shruti.capstone.mapreduce.resultdownload.InputFiles;
import com.shruti.capstone.mapreduce.resultdownload.TaskMapper;
import com.shruti.capstone.mapreduce.sendResult.MapRedImpl;
import com.shruti.capstone.mapreduce.sendResult.SendResult;
import com.shruti.capstone.mapreduce.xmlparser.Response;
import com.shruti.capstone.mapreduce.xmlparser.TypeXML;
import com.shruti.capstone.mapreduce.xmlparser.Parsing;
import com.shruti.capstone.mapreduce.xmlparser.XMLParser;
@SuppressLint("NewApi")
public class taskTracker extends AsyncTask<URL, Integer, Long> {

	@Override
	//connecting to the server
	protected Long doInBackground(URL... arg0) {
		Socket soc = new Socket();
		SocketAddress IPaddress = new InetSocketAddress(Ipconfig.LaptopIP,
				Ipconfig.LaptopPort);
		try {
			System.out.printf("address is:: %s",IPaddress);
			soc.connect(IPaddress);
			Ipconfig.doTaskRequest(soc);
		} catch (IOException e) {
			Log.e(taskTracker.class.getSimpleName(), e.getMessage(), e);
			System.out.println("inside catch");
		}

		deletePreviousFiles(soc);
		inputFiles(soc);
		List<Response> msgs = parseMapTask(TypeXML.SIMPLE, soc);
		processing(msgs, soc);

		return null;
	}
	// delete all the existing previous files that were
	//downloaded from the server
	private void deletePreviousFiles(Socket soc) {		
		Log.i(this.getClass().getSimpleName(), "previous files deleting...");		
			File f = new File(InputFiles.Path_Download);
			if (f.isDirectory()) {
		        String[] files = f.list();
		        for (int i = 0; i < files.length; i++) {
		            new File(f, files[i]).delete();
		        }
		    }			
			Log.i(this.getClass().getSimpleName(),
				"deleted...");					
	}
	//perform reduction
	private void reducing(Socket soc) {
		FileOutputStream fos = null;
		try {
			
			ObjectInputStream serverside = new ObjectInputStream(
					soc.getInputStream());
			com.shruti.capstone.Memo reply = (com.shruti.capstone.Memo) serverside
					.readObject();
			

			if (Memokind.REDUCE.equals(reply.getMemoKind())) {

				File file = new File(InputFiles.Path_Download);
				file.mkdirs();
				File outputFile = new File(file,
						InputFiles.File_red);
				fos = new FileOutputStream(outputFile);
				fos.write(reply.getcontent());
				fos.close();
				Log.i(this.getClass().getSimpleName(),
						"sucessfully downloaded input files..");
			}
		} catch (IOException e) {
			Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
			}
		}

	}
//download the xml and text files from the server
	private static void inputFiles(Socket soc) {
		TaskMapper mapTask = new TaskMapper();
		mapTask.taskdownload(soc);
		try {
			mapTask.textFileDownload(new DataInputStream(soc.getInputStream()), InputFiles.Path_Download,  InputFiles.File_text);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
//parsing the xml file
	private static List<Response> parseMapTask(TypeXML type, Socket soc) {
		Parsing parser = XMLParser.getXML(type, soc);
		return parser.xmlparsing();
	}
//doing the map processing
	private static void processing(List<Response> responses, Socket soc) {

		Log.i(taskTracker.class.getSimpleName(), "" + responses.size());
		for (int i = 0; i < responses.size(); i++) {
			Response res = responses.get(i);
			String pckg = res.getTilte();
			Log.i(taskTracker.class.getSimpleName(), pckg);
			try {
				Class newclass = Class.forName(pckg);
				Mapping maptask = null;
				maptask = (Mapping) newclass.newInstance();
				List<String> keyvalue = res.getValues();
				String[] paramArr = null;

				if (keyvalue != null) {
					Log.i(taskTracker.class.getName(),
							"parameters are " + keyvalue.toString());
					paramArr = new String[keyvalue.size()];
					paramArr = keyvalue.toArray(paramArr);
					Log.i(taskTracker.class.getName(),
							"total numner of input words to count::"
									+ keyvalue.size());
				}
				
				MapReduce mr = new MapRedImpl();				
				maptask.map(mr, paramArr);
				resultfilesend(soc);
			} catch (ClassNotFoundException e) {
				Log.e(taskTracker.class.getSimpleName(), e.getMessage(), e);
			} catch (InstantiationException e) {
				Log.e(taskTracker.class.getSimpleName(), e.getMessage(), e);
			} catch (IllegalAccessException e) {
				Log.e(taskTracker.class.getSimpleName(), e.getMessage(), e);
			} catch (IOException e) {
				Log.e(taskTracker.class.getSimpleName(), e.getMessage(), e);
			}

		}
	}

	public static String[] getpairs(Map<String, Integer> resmr) {
		String[] arrpairs = new String[resmr.size()];
		int i = 0;
		for (String word : resmr.keySet()) {
			arrpairs[i] = word + "=" + resmr.get(word);
			i++;
		}

		return arrpairs;

	}
//sending the result file to the server
	public static void resultfilesend(Socket soc) {

		File file = new File(SendResult.Result_filedir);
		if (!file.exists()) {
			Log.e(taskTracker.class.getName(), "result not generated...!");
			return;
		}
		File resFile = new File(file,
				"result2.txt");

		byte[] bytesbuffer = new byte[(int) resFile.length()];
	System.out.println(bytesbuffer); 
		FileInputStream fis;
		try {
			fis = new FileInputStream(resFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			bis.read(bytesbuffer, 0, bytesbuffer.length);
			com.shruti.capstone.Memo message = new com.shruti.capstone.Memo();
			message.setMemoKind(Memokind.RESPONSE);
			message.setcontent(bytesbuffer);
			ObjectOutputStream send = new ObjectOutputStream(
					soc.getOutputStream());
			 OutputStreamWriter osw = new OutputStreamWriter(send);
	            BufferedWriter bw = new BufferedWriter(osw);
			send.reset();
			send.writeObject(message);
			send.writeObject(resFile.getName());
			send.close();
			send.flush();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Sending output file ...");
	}
}
