package com.shruti.capstone.mapreduce.action.base;
import java.io.IOException;

public interface Mapping<KEYOUT, VALUEOUT> {
	public void map(MapReduce mapred, String... params) throws IOException;

}