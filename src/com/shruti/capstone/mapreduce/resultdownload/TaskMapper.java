package com.shruti.capstone.mapreduce.resultdownload;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;

/**
 * Copyright (c) 2011, FAST NU and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of FAST-NU or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * 
 * @author  Muhammad Ali
 * @author  Jawwad Shamsi
 * @version 0.1
 * @since   0.1
 */

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
