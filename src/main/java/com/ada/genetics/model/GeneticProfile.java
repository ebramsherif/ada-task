package com.ada.genetics.model;

import com.google.common.collect.ImmutableList;

public class GeneticProfile {
	final private String name;
	final private ImmutableList<GeneticMarker> geneticMarkers;
	
	public GeneticProfile(String name, ImmutableList<GeneticMarker> geneticMarkers) {
		this.name = name;
		this.geneticMarkers = geneticMarkers;
	}

	public String getName() {
		return name;
	}

	public ImmutableList<GeneticMarker> getGeneticMarkers() {
		return geneticMarkers;
	}
}
