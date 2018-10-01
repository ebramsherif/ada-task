package com.ada.genetics.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ada.genetics.model.ProfileKey;

@Repository
public interface GeneticsProfileRepository extends MongoRepository<ProfileKey,String>{
	public ProfileKey findByName(String Name);
}
