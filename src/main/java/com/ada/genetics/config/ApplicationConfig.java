package com.ada.genetics.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

import com.ada.genetics.controller.ApplicationController;
import com.ada.genetics.model.GeneticMarker;
import com.ada.genetics.repository.CountsRepository;
import com.ada.genetics.repository.GeneticsMarkerRepository;
import com.ada.genetics.repository.GeneticsProfileRepository;
import com.ada.genetics.service.GeneticsService;
import com.mongodb.MongoClient;

@Configuration
@EnableMongoRepositories("com.ada.genetics.repository")
public class ApplicationConfig {
	@Value("${spring.data.mongodb.host}")
	private String mongoHost;

	@Value("${spring.data.mongodb.port}")
	private int mongoPort;

	@Value("${spring.data.mongodb.database}")
	private String mongoDB;
	
	@Bean 
	public ApplicationController applicationController() {
		return new ApplicationController(geneticsService());
	}
	
	@Bean
	public GeneticsService geneticsService() {
		MongoOperations operations = new MongoTemplate(new MongoClient(mongoHost,mongoPort), mongoDB);
		operations.indexOps(GeneticMarker.class).ensureIndex(new Index().on("profileKey", Direction.ASC));
		MongoRepositoryFactory factory = new MongoRepositoryFactory(operations);
		GeneticsProfileRepository geneticsProfileRepository = factory.getRepository(GeneticsProfileRepository.class);
		GeneticsMarkerRepository geneticsMarkerRepository= factory.getRepository(GeneticsMarkerRepository.class);
		CountsRepository countsRepository = factory.getRepository(CountsRepository.class);
		return new GeneticsService(geneticsProfileRepository,geneticsMarkerRepository, countsRepository,logger());
	}
	
	@Bean
	public Logger logger() {
		return LoggerFactory.getLogger(getClass());
	}
	
}