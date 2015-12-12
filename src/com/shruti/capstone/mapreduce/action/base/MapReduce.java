package com.shruti.capstone.mapreduce.action.base;
public interface MapReduce<K, V,File> {
	public void mapSet();

	public void generate(K key, V value, File outputFile,Boolean... bool);
}
