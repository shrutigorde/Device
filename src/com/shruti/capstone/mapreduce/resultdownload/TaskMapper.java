package com.shruti.capstone.mapreduce.resultdownload;
/*
 * Capstone project by Shruti Gorde
 * class for performing mapping
 * 
 */
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.shruti.capstone.Memo;
import com.shruti.capstone.Memokind;

import android.util.Log;

public class TaskMapper {
//download the xml file
	public void taskdownload(Socket socket) {
		FileOutputStream fos = null;
		try {
			Log.i(this.getClass().getSimpleName(), "waiting.....");
			ObjectInputStream ois = new ObjectInputStream(
					socket.getInputStream());
			Memo memo = (Memo) ois.readObject();
			Log.i(this.getClass().getSimpleName(),
					"Message recieved....");

			if (Memokind.MAP.equals(memo.getMemoKind())) {

				File file = new File(InputFiles.Path_Download);
				file.mkdirs();
				File outputFile = new File(file, InputFiles.File_xml);				
				fos = new FileOutputStream(outputFile);
				fos.write(memo.getcontent());
				//fos.close();
				Log.i(this.getClass().getSimpleName(),
						"Files Successfully downloaded....");
			}
		} catch (IOException e) {
			Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			/*try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
			}*/
		}
	}
//download the text file
	public void textFileDownload(DataInputStream dataInputStream,
			String pathdownload, String textfile) {
		FileOutputStream fos = null;
		try {
			File file = new File(pathdownload);
			file.mkdirs();
			File outputFile = new File(file, textfile);
			fos = new FileOutputStream(outputFile);
           
			byte[] buffer = new byte[1024];
			int len = 0;
			int len1 = dataInputStream.read(buffer);
			System.out.print("length is "+len1);
			fos.write(buffer, 0, 0);
			BufferedReader br=new BufferedReader(new FileReader(outputFile));
			String line = null;
		
			fos.write(buffer,0,len1);
			buffer = null;
			Log.i(this.getClass().getSimpleName(),
					"Text file downloaded....");
		} catch (IOException e) {
			Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
		} finally {
			if (fos != null)
			System.out.println("close connection ");
				//fos.close();
		}
	}
	
	
	
}
