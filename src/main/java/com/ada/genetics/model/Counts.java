package com.ada.genetics.model;

public class Counts {
	final private String geneticProfileKey;
	final private String key;
	final private long value;
	
	public Counts(String geneticProfileKey, String key, long value) {
		this.geneticProfileKey = geneticProfileKey;
		this.key = key;
		this.value = value;
	}

	public String getGeneticProfileKey() {
		return geneticProfileKey;
	}

	public String getKey() {
		return key;
	}

	public long getValue() {
		return value;
	}
}
