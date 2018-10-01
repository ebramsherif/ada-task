package com.ada.genetics.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ada.genetics.model.GeneticMarker;

public interface GeneticsMarkerRepository extends MongoRepository<GeneticMarker,String>{
	public List<GeneticMarker> findByProfileKey(String ProfileKey);
	public GeneticMarker findByProfileKeyAndRsid(String ProfileKey, String Rsid);
}

