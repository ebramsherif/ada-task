package com.ada.genetics.model;

public class ProfileKey {
	final private String name;
	
	public ProfileKey(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return String.format("Profile[name='%s']");
	}
}
