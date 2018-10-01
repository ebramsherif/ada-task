package com.ada.genetics.model;

import org.springframework.data.mongodb.core.index.Indexed;

public class GeneticMarker {
	@Indexed
	final private String profileKey;
	final private String chromosome;
	final private String rsid;
	final private int position;
	final private String genotype;
	
	public GeneticMarker(String profileKey, String rsid ,String chromosome, int position, String genotype) {
		this.profileKey = profileKey;
		this.chromosome = chromosome;
		this.rsid = rsid;
		this.position = position;
		this.genotype = genotype;
	}

	public String getProfileKey() {
		return profileKey;
	}

	public String getChromosome() {
		return chromosome;
	}

	public String getRsid() {
		return rsid;
	}

	public int getPosition() {
		return position;
	}

	public String getGenotype() {
		return genotype;
	}
	
}
