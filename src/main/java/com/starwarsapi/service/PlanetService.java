package com.starwarsapi.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Strings;
import com.starwarsapi.model.Planet;
import com.starwarsapi.model.SwapiResponse;
import com.starwarsapi.repository.PlanetRepository;

@Service
public class PlanetService {
	
	@Value( "${page.limit}")
	private Integer pageLimit;
	
	@Value( "${url.swapi}" )
	private String urlSwapi;
	
	private RestTemplate restTemplate;
	
	private HttpEntity<String> entity;
	
	@Autowired
	private PlanetRepository planetRepository;

	public Planet create(Planet planet) {
		getTotalMovies(Optional.ofNullable(planet));
		if(planet.getTotalMovies() == null) {
			return null;  //Se o total de filmes for null, quer dizer que não conseguiu encontrar o planeta no swapi e, portanto, ele não existe
		}
		planetRepository.save(planet);
		
		return planet;
	}

	public Page<Planet> findPlanet(String name, Integer page) {
		if(page == null) {
			page = 0;
		}
		Pageable pageable = PageRequest.of(page, pageLimit);
		if(Strings.isNullOrEmpty(name)) {
			Page<Planet> planets = planetRepository.findAll(pageable);
			planets.getContent().forEach(planet -> getTotalMovies(Optional.ofNullable(planet)));
			return  planets;
		} else {
			Page<Planet> planets = planetRepository.findByName(name, pageable);
			planets.getContent().forEach(planet -> getTotalMovies(Optional.ofNullable(planet)));
			return planets;
		}
	}

	public Optional<Planet> findById(String id) {
		Optional<Planet> planet = planetRepository.findById(id);	
		
		getTotalMovies(planet);
		
		return planet;
	}


	public Optional<Planet> remove(String id) {
		Optional<Planet> planet = planetRepository.findById(id);
		getTotalMovies(planet);
		planet.ifPresent(item -> planetRepository.delete(item));
		
		return planet;
	}
	
	
	public void getTotalMovies(Optional<Planet> planet) {
		restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		entity = new HttpEntity<String>("parameters", headers);
		
		planet.ifPresent(item -> {
			ResponseEntity<SwapiResponse> response = restTemplate.exchange(urlSwapi.concat("?search=").concat(item.getName()), HttpMethod.GET, entity, SwapiResponse.class);
			
			if(HttpStatus.OK.equals(response.getStatusCode())) {
				if(response.getBody().getResults() != null && !response.getBody().getResults().isEmpty()) {
					item.setTotalMovies(response.getBody().getResults().get(0).getFilms().size());
				}
			}else {
				if(item.getTotalMovies() == null) {
					item.setTotalMovies(-1); //Se não tem um valor no banco e não conseguiu acessar o Swapi, retorna -1 para o total de filmes
				}
			}
		});
		
	}
}
