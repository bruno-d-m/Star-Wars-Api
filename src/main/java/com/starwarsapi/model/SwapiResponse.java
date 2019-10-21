package com.starwarsapi.model;

import java.util.List;

public class SwapiResponse {

	private List<SwapiPlanet> results;

	public List<SwapiPlanet> getResults() {
		return results;
	}

	public void setResults(List<SwapiPlanet> results) {
		this.results = results;
	}
}
