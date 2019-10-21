package com.starwarsapi.controller;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.starwarsapi.model.Planet;
import com.starwarsapi.service.PlanetService;

@RestController
@RequestMapping("/planet")
public class PlanetController {
	
	@Autowired
	private PlanetService planetService;
	
	//Método POST para adicionar um planeta
	@PostMapping
	public ResponseEntity<Planet> create(@RequestBody @Valid Planet planet) {

		planet = planetService.create(planet);
		
		if (planet == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();	//400 Bad Request - Caso de o planeta não existir no Swapi e retornar null 
        }

		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.buildAndExpand(planet.getId())
				.toUri();

		return ResponseEntity.created(uri).body(planet);	//201 Created	
	}
	
	//Método GET para buscar todos os planetas ou buscar um planeta por nome
	@GetMapping
	public ResponseEntity<Page<Planet>> findPlanet(@RequestParam(name = "name", required=false) @Valid String name, @RequestParam(name = "page", required=false) @Valid Integer page) {
		return Optional.ofNullable(planetService.findPlanet(name, page))
				.map(planet -> ResponseEntity.ok().body(planet))       	//200 OK
				.orElseGet(() -> ResponseEntity.notFound().build()); 	//404 Not found
	}


	//Método GET para buscar um planeta por Id
	@GetMapping("{planetId}")
	public ResponseEntity<Planet> findById(@PathVariable("planetId") @Valid String id) {
		return planetService.findById(id)
				.map(planet -> ResponseEntity.ok().body(planet))       	//200 OK
				.orElseGet(() -> ResponseEntity.notFound().build()); 	//404 Not found
	}
	
	//Método DELETE para remover um planeta
	@DeleteMapping("{planetId}")
	public ResponseEntity<Object> remove(@PathVariable("planetId") @Valid String id) {
		return planetService.remove(id)
				.map(planet -> ResponseEntity.noContent().build())		//204 No content
				.orElseGet(() -> ResponseEntity.notFound().build());	//404 Not found
	}
}
