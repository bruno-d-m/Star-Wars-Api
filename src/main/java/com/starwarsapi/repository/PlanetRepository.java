package com.starwarsapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.starwarsapi.model.Planet;

public interface PlanetRepository extends MongoRepository<Planet, String> {
	
	public Page<Planet> findByName(String name, Pageable pageable);

}
