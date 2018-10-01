package com.ada.genetics.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ada.genetics.model.Counts;

public interface CountsRepository extends MongoRepository<Counts,String>{
	public List<Counts> findByGeneticProfileKey(String GeneticProfileKey);

}
