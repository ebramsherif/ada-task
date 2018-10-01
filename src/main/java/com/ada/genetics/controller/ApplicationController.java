package com.ada.genetics.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ada.genetics.model.GeneticProfile;
import com.ada.genetics.service.GeneticsService;
import com.google.common.collect.ImmutableList;

@RestController
@RequestMapping("/genetics")
public class ApplicationController {
	private GeneticsService service;
	
	public ApplicationController(GeneticsService service) {
		this.service = service;
	}
	
	@RequestMapping(value="/add", method = RequestMethod.POST)
	public void addGeneticProfile(@RequestParam(value="geneticProfileKey") String geneticProfileKey, 
			@RequestParam(value="rawData") String rawData) {
		service.addGeneticProfile(geneticProfileKey,rawData);
	}
	
	@RequestMapping(value="/count/profile", method = RequestMethod.GET)
	public long getGeneticProfileCount() {
		return service.getGeneticProfileCount();
	}
	
	@RequestMapping(value="/geneticProfiles", method = RequestMethod.GET)
	public ImmutableList<String> getGeneticProfiles() {
		return (ImmutableList<String>) service.getGeneticProfileKeys();
	}
	
	@RequestMapping(value="/geneticProfile", method = RequestMethod.GET)
	public GeneticProfile getGeneticProfile(@RequestParam(value="geneticProfileKey") String geneticProfileKey) {
		return service.getGeneticProfile(geneticProfileKey);
	}
	
	@RequestMapping(value="/genotypeHistogram", method = RequestMethod.GET)
	public Map<String,Long> getGenoTypeHistogram(@RequestParam(value="geneticProfileKey") String geneticProfileKey) {
		return service.getGenotypeHistogram(geneticProfileKey);
	}
	
	@RequestMapping(value="/genotypeGlobalHistogram", method = RequestMethod.GET)
	public Map<String,Long> getGenoTypeGlobalHistogram() {
		return service.getGlobalGenotypeHistogram();
	}
	
	@RequestMapping(value="/genotype", method = RequestMethod.GET)
	public String getGenotype(@RequestParam(value="geneticProfileKey") String geneticProfileKey,@RequestParam(value="rsId") String rsId) {
		return service.getGenotype(geneticProfileKey, rsId);
	}
}
