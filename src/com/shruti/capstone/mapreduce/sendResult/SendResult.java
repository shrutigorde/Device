package com.shruti.capstone.mapreduce.sendResult;
/*
 * Capstone project by Shruti Gorde
 * class to send the result file to the server
 * 
 */
import android.os.Environment;

public class SendResult {
	public static final String Result_filedir = Environment
			.getExternalStorageDirectory().getAbsolutePath()+ "/demo/result7";
	public static final String Result_file = "result.txt";
	
}
