package com.shruti.capstone.mapreduce.action.base;
import java.util.Map;

public interface Reducing<K, V> {

	public Map<K, V> execute(String... params);

}
