package com.shruti.capstone.mapreduce.sample;
/*
 * Capstone project by Shruti Gorde
 * class to count the occurences of the word in input file
 * 
 */
import java.io.File;

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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import android.os.Environment;
import android.util.Log;

import com.shruti.capstone.mapreduce.action.base.MapReduce;
import com.shruti.capstone.mapreduce.action.base.Mapping;
import com.shruti.capstone.mapreduce.resultdownload.InputFiles;
import com.shruti.capstone.mapreduce.sendResult.SendResult;

public class OccurencesWordsMap implements Mapping<String, Integer> {
//defining the map job
	public void map(MapReduce context, String... params) throws IOException {
		Log.i(this.getClass().getSimpleName(), "doing the task....");
		List<String> list  = new ArrayList<String>();
		for (String param:params){
			list.add(param);
		}
		String fdir = SendResult.Result_filedir;
		File outputFileDir = new File(fdir);
		if (!outputFileDir.exists())
			outputFileDir.mkdirs();
		
		File outputFile = new File(outputFileDir,
				"result2.txt");
		outputFile.delete();

		outputFile.createNewFile();
		Map<String, Integer> res = occurences(list);
		for (Map.Entry<String, Integer> r : res.entrySet()) {
			context.generate(r.getKey(), r.getValue(),outputFile);
		}
	}

	public static Map<String, Integer> occurences(List<String> parameters) {
		TreeMap<String, Integer> data = new TreeMap<String, Integer>();

		Log.i(OccurencesWordsMap.class.getName(), "Read ...");
		gothroughtextfile(data,parameters);
		Log.i(OccurencesWordsMap.class.getName(), "Print ...");
		display(data);
		System.out.println("state of sd card:::"+Environment.getExternalStorageState());
		return (Map<String, Integer>) data;
	}

	public static int considerNumber(String word,
			TreeMap<String, Integer> data) {
		if (data.containsKey(word)) { 
			return data.get(word); 
		} else { 
			return 0;
		}
	}
//print the words with its count
	public static void display(TreeMap<String, Integer> data) {

		for (String d : data.keySet()) {

			System.out.println("word = " + d + " , count = "
					+ data.get(d));
		}
	}

	@SuppressWarnings("resource")
	//traverse  the text file
	public static void gothroughtextfile(TreeMap<String, Integer> data,List<String> params) {
		Scanner textFile;
		String word; 
		Integer count; 

		try {
			textFile = new Scanner(new FileReader(
					InputFiles.Path_file
							+ InputFiles.File_text));
			System.out.printf("wordfile is::"+ textFile);

		} catch (FileNotFoundException e) {
			System.err.println(e);
			return;
		}

		while (textFile.hasNext()) {
			
			word = textFile.next();
			
			Log.i(OccurencesWordsMap.class.getName(), "word encountered = "
					+ word);
			 count = considerNumber(word, data) + 1;

			if(word!=null && params.contains(word))
				data.put(word, count);
		}
	}
}
