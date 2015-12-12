package com.shruti.capstone.mapreduce.action.base;
/*
 * Capstone project by Shruti Gorde
 * interface for mapper
 * 
 */
import java.io.IOException;

public interface Mapping<KEYOUT, VALUEOUT> {
	public void map(MapReduce mapred, String... params) throws IOException;

}