package com.shruti.capstone.mapreduce.resultdownload;
import android.os.Environment;

public class InputFiles {
	public static final String Path_Download = Environment
			.getExternalStorageDirectory() + "/demo/download/";
	public static final String Path_file = Environment
			.getExternalStorageDirectory() + "/demo/download/";
	public static final String File_text= "test.txt";
	public static final String File_xml = "task.xml";
	public static final String File_netset = "network.mfs";
	public static final String File_red = "reduce.mmr";
}
