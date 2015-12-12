package com.shruti.capstone.mapreduce.sample;
/*
 * Capstone project by Shruti Gorde
 * class to perform reduction
 * 
 */
import java.util.Map;
import java.util.TreeMap;

import com.shruti.capstone.mapreduce.action.base.Reducing;

import android.util.Log;

public class OccurencesWordsReduce implements Reducing<String, Integer> {

	public Map<String, Integer> execute(String... parameters) {
		Log.i(this.getClass().getSimpleName(), "in reduce....");

		for (int i = 0; i < parameters.length; i++) {
			String[] kv = parameters[i].split("=");
			System.out.println("key = " + kv[0] + ", value = "
					+ kv[1]);
		}

		return null;
	}

	public static int reducefunc(String word,
			TreeMap<String, Integer> data) {
		if (data.containsKey(word)) { 
			return data.get(word); 
		} else { 
			return 0;
		}
	}

}
