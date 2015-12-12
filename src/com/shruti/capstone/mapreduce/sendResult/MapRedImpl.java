package com.shruti.capstone.mapreduce.sendResult;
/*
 * Capstone project by Shruti Gorde
 * 
 * 
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.shruti.capstone.mapreduce.action.base.MapReduce;

import android.util.Log;
public class MapRedImpl<K, V,File> implements MapReduce<K, V,File> {

	FileType file = FileType.PROPERTY;

	public MapRedImpl() {
		mapSet();
	}

	public void mapSet() {
		
	}
//generate the output file in the device
	public void generate(K key, V value, File outputFile,Boolean... bool) {

		if (key != null && value != null) {
			try {
			
				System.out.println("output file is:: "+ outputFile);
				System.out.println("check if result.txt exists:: "+((java.io.File) outputFile).exists());
				if(((java.io.File) outputFile).exists()){	
				FileWriter outFile = new FileWriter((java.io.File) outputFile, true);
					PrintWriter printw = new PrintWriter(outFile);
			
				Log.i(MapRedImpl.class.getName(), "Write result to file "
						+ SendResult.Result_file + " file ...");
				if (bool != null && (bool.length > 0)) {
					if (bool[0])
						printw.println("#" + key + "=" + value);
				} else {
					printw.println(key + "=" + value);
				}
				
				printw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

}

enum FileType {
	TEXT,PROPERTY
}
