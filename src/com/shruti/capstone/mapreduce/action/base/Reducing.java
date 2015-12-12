package com.shruti.capstone.mapreduce.action.base;
/*
 * Capstone project by Shruti Gorde
 * interface for reduce
 * 
 */
import java.util.Map;

public interface Reducing<K, V> {

	public Map<K, V> execute(String... params);

}
