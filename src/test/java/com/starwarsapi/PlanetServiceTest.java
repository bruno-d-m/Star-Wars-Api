package com.starwarsapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.starwarsapi.model.Planet;
import com.starwarsapi.service.PlanetService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlanetServiceTest {
	
	
	@Autowired
	private PlanetService planetService;

	private Planet planet;
	private List<Planet> planets;
	private ObjectMapper mapper;
	
	private Planet successfulPlanet;
	private Planet badRequestPlanet;
	private String idNotFound;

	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException {
		mapper = new ObjectMapper();
		planets = new ArrayList<Planet>();
		planet = mapper.readValue(new File("src/test/resources/tatooine.json"), Planet.class);
		planets.add(planet);
		planet = mapper.readValue(new File("src/test/resources/alderaan.json"), Planet.class);
		planets.add(planet);
		planet = mapper.readValue(new File("src/test/resources/yavin iv.json"), Planet.class);
		planets.add(planet);
		planet = mapper.readValue(new File("src/test/resources/hoth.json"), Planet.class);
		planets.add(planet);
		planet = mapper.readValue(new File("src/test/resources/bespin.json"), Planet.class);
		planets.add(planet);
		
		planets.forEach(item -> planetService.create(item));
		
		successfulPlanet = mapper.readValue(new File("src/test/resources/dagobah.json"), Planet.class);
		badRequestPlanet = mapper.readValue(new File("src/test/resources/bad request.json"), Planet.class);
		idNotFound = "123456789abcd";
		
	}
	
	@After
	public void tearDown() {
		planets.forEach(item -> planetService.remove(item.getId()));
	}

	//Testa a função de criar um planeta existente em Star Wars
	@Test
	public void successfulCreate() {
		planetService.create(successfulPlanet);
		assertNotNull(successfulPlanet.getId());
	}
	
	//Testa a função de criar um planeta não existente em Star Wars
	@Test
	public void badRequestCreate() {
		planetService.create(badRequestPlanet);
		assertNull(badRequestPlanet.getId());
	}

	//Testa a função de retornar todos os planetas
	@Test
	public void findAll() {
		assertNotNull(planetService.findPlanet(null, 0).getContent());
		assertNotEquals(planetService.findPlanet(null, 0).getContent(), Lists.emptyList());
		planetService.findPlanet(null, 0).getContent().forEach(item -> assertEachItemNotNull(item));
	}
	
	private void assertEachItemNotNull(Planet planet) {
		assertNotNull(planet.getId());
		assertNotNull(planet.getName());
		assertNotNull(planet.getClimate());
		assertNotNull(planet.getTerrain());
		assertNotNull(planet.getTotalMovies());
	}
	
	//Testa a função de retornar planeta por nome com planetas que estão na base
	@Test
	public void successfulFindByName() {
		planets.forEach(item -> assertNotNull(planetService.findPlanet(item.getName(), 0).getContent()));
		planets.forEach(item -> assertNotEquals(planetService.findPlanet(item.getName(), 0).getContent(), Lists.emptyList() ));
	}
	
	//Testa a função de retornar planeta por nome com um planeta que não está na base
	@Test
	public void notFoundFindByName() {
		assertNotNull(planetService.findPlanet(badRequestPlanet.getName(), 0).getContent());
		assertEquals(planetService.findPlanet(badRequestPlanet.getName(), 0).getContent(), Lists.emptyList());
	}
	
	//Testa a função de retornar planeta por id com planetas que estão na base
	@Test
	public void successfulFindById() {
		planets.forEach(item -> assertNotNull(planetService.findById(item.getId())));
	}
	
	//Testa a função de retornar planeta por id com um planeta que não está na base
	@Test
	public void notFoundFindById() {
		assertFalse(planetService.findById((idNotFound)).isPresent());
	}
	
	//Testa a função de remover um planeta que está na base
	@Test
	public void successfulRemove() {
		planets.forEach(item -> planetService.create(item));
		planets.forEach(item -> assertNotNull(planetService.remove(item.getId())));
		
		planetService.create(planet);
		assertNotNull(planetService.remove(planet.getId()));
	}
	
	//Testa a função de remover um planeta que não está na base
	@Test
	public void notFoundRemove() {
		assertFalse(planetService.remove(idNotFound).isPresent());
	}
}
