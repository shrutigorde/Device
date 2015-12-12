package com.shruti.capstone;
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

		executeBeforeDownload(soc);
		downloadFile(soc);
		List<Response> messages = parseMapTask(TypeXML.SIMPLE, soc);
		performAction(messages, soc);

		return null;
	}

	private void executeBeforeDownload(Socket soc) {		
		Log.i(this.getClass().getSimpleName(), "Removing the old files, if download previously.....");		
			File file = new File(InputFiles.Path_Download);
			if (file.isDirectory()) {
		        String[] children = file.list();
		        for (int i = 0; i < children.length; i++) {
		            new File(file, children[i]).delete();
		        }
		    }			
			Log.i(this.getClass().getSimpleName(),
				"Previous job file removed successfully....");					
	}

	private void performReduceAction(Socket soc) {
		FileOutputStream fos = null;
		try {
			Log.i(this.getClass().getSimpleName(),
					"Waiting for Reduce Job.....");
			ObjectInputStream fromServer = new ObjectInputStream(
					soc.getInputStream());
			com.shruti.capstone.Memo message = (com.shruti.capstone.Memo) fromServer
					.readObject();
			Log.i(this.getClass().getSimpleName(),
					"Recieve Message object for Reduce Job at client side....");

			if (Memokind.REDUCE.equals(message.getMemoKind())) {

				File file = new File(InputFiles.Path_Download);
				file.mkdirs();
				File outputFile = new File(file,
						InputFiles.File_red);
				fos = new FileOutputStream(outputFile);
				fos.write(message.getcontent());
				fos.close();
				Log.i(this.getClass().getSimpleName(),
						"Client docs downloaded successfully....");
			}
		} catch (IOException e) {
			Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
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

	private static void downloadFile(Socket soc) {
		TaskMapper mapTask = new TaskMapper();
		mapTask.taskdownload(soc);
		try {
			mapTask.textFileDownload(new DataInputStream(soc.getInputStream()), InputFiles.Path_Download,  InputFiles.File_text);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}

	private static List<Response> parseMapTask(TypeXML type, Socket soc) {
		Parsing parser = XMLParser.getXML(type, soc);
		return parser.xmlparsing();
	}

	private static void performAction(List<Response> messages, Socket soc) {

		Log.i(taskTracker.class.getSimpleName(), "" + messages.size());
		for (int i = 0; i < messages.size(); i++) {
			Response message = messages.get(i);
			String classPackage = message.getTilte();
			Log.i(taskTracker.class.getSimpleName(), classPackage);
			try {
				Class klaz = Class.forName(classPackage);
				// TODO enum valueOf
				Mapping mapper = null;
				// if ("MAP".equals(message.getActionType())) {
				mapper = (Mapping) klaz.newInstance();
				// }
				List<String> params = message.getValues();
				String[] paramArr = null;

				if (params != null) {
					Log.i(taskTracker.class.getName(),
							"params is " + params.toString());
					paramArr = new String[params.size()];
					paramArr = params.toArray(paramArr);
					Log.i(taskTracker.class.getName(),
							"No. of parameters received by android = "
									+ params.size());
				}
				Log.i(taskTracker.class.getName(), "Running word count task...");

				// Map<String, Integer> resultMap = handler.execute(stockArr);
				MapReduce context = new MapRedImpl();				
				mapper.map(context, paramArr);
				sendResponseToServer(soc);
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

	public static String[] getKeyValueArray(Map<String, Integer> resultMap) {
		String[] keyValueArray = new String[resultMap.size()];
		int i = 0;
		for (String word : resultMap.keySet()) {
			keyValueArray[i] = word + "=" + resultMap.get(word);
			i++;
		}

		return keyValueArray;

	}

	public static void sendResponseToServer(Socket soc) {

		File file = new File(SendResult.Result_filedir);
		if (!file.exists()) {
			Log.e(taskTracker.class.getName(), "Response file not created!");
			return;
		}
		File responseFile = new File(file,
				"result2.txt");

		byte[] responseFileContent = new byte[(int) responseFile.length()];
	System.out.println(responseFileContent); 
		FileInputStream fis;
		try {
			fis = new FileInputStream(responseFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			bis.read(responseFileContent, 0, responseFileContent.length);
			com.shruti.capstone.Memo message = new com.shruti.capstone.Memo();
			message.setMemoKind(Memokind.ANSWER);
			message.setcontent(responseFileContent);
			ObjectOutputStream toServer = new ObjectOutputStream(
					soc.getOutputStream());
			 OutputStreamWriter osw = new OutputStreamWriter(toServer);
	            BufferedWriter bw = new BufferedWriter(osw);
			toServer.reset();
			toServer.writeObject(message);
			toServer.writeObject(responseFile.getName());
			toServer.close();
			toServer.flush();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Sending response file ...");
	}
}
