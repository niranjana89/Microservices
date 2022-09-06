package com.example.moviecatalog.resources;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.moviecatalog.model.CatalogItem;
import com.example.moviecatalog.model.Movie;
import com.example.moviecatalog.model.Rating;
import com.netflix.discovery.DiscoveryClient;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
	
	@Autowired
	private RestTemplate template;
	
	@Autowired
	private WebClient.Builder webclientBuilder;
	
	@Autowired
	private DiscoveryClient discoveryClient;
	
	@GetMapping(value = "/{userId}")
	public  List<CatalogItem> getCatalog(@PathVariable("userId") String userId){
		Rating rating = template.getForObject("http://rating-info-service/rating/"+"sx123", Rating.class);
		
		Movie movie = webclientBuilder.build()
						.get()
						.uri("http://movie-info-service/movies/"+"sx123")
						.retrieve()
						.bodyToMono(Movie.class)
						.block();
		return Collections.singletonList(new CatalogItem(movie.getName(),"Test",rating.getRating()));
	}
	
	@GetMapping(value = "/")
	public  String getHello(){
		return "Hello";
	}
	
	
	

}
