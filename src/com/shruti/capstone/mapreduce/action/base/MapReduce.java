package com.shruti.capstone.mapreduce.action.base;
/*
 * Capstone project by Shruti Gorde
 * interface to emit map result
 * 
 */
public interface MapReduce<K, V,File> {
	public void mapSet();

	public void generate(K key, V value, File outputFile,Boolean... bool);
}
