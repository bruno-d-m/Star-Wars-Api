package com.starwarsapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "planets")
public class Planet {

	@Id
	private String id;
	
	private String name;
	
	private String climate;
	
	private String terrain;
	
	private Integer totalMovies;
	
	public Planet() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClimate() {
		return climate;
	}

	public void setClimate(String clima) {
		this.climate = clima;
	}

	public String getTerrain() {
		return terrain;
	}

	public void setTerrain(String terreno) {
		this.terrain = terreno;
	}
	
	public Integer getTotalMovies() {
		return totalMovies;
	}

	public void setTotalMovies(Integer totalMovies) {
		this.totalMovies = totalMovies;
	}
}
