# Star Wars API

This is a REST API built to maintain a Star Wars' planets database. It let's the user create, remove and search through the added planets, all while accessing another API called SWAPI, to get information about how many Star Wars movies that planet appeared in.


## Requirements
* Java 8
* Maven 3.5
* Docker 19.03.4
* Docker Compose 1.24.1


## Build and run 
Once cloned from git the application can be built and run using docker compose:

    docker-compose up -d 
    
The api will start on `localhost` and will be listening to port `8080`.
You can use it either by using a program like Postman or making cURL calls to `localhost:8080/planet` or by using `localhost:8080/swagger-ui.html` which will provide a UI to make the calls.
The API can also be used at Heroku with either method, without having to build and run it locally, by accessing either `https://far-away-galaxy.herokuapp.com/planet` or `https://far-away-galaxy.herokuapp.com/swagger-ui.html`.


## API Documentation
The following REST endpoints are exposed by the API

| Http method | Endpoint                                        | Request                                                                                    | Description                                                    |
|-------------|-------------------------------------------------|--------------------------------------------------------------------------------------------|----------------------------------------------------------------|
| GET         | /planet?page={page}                       	    |                                                                                            | Gets a paginated list of all planets in the database           |
| GET         | /planet?planet?name={planetName}                |                                                                                            | Gets a planet with name = {planetName} in the database         |
| GET         | /planet/{planetId}                              |                                                                                            | Gets a planet with id = {planetId} in the database             |
| POST        | /planet                                         | { "name": "planet's name", "climate": "planet's climate", "terrain": "planet's terrain" }  | Create a new planet                                            |
| DELETE      | /planet/{planetId}                              |                                                                                            | Delete planet with id = {id}                                   |
