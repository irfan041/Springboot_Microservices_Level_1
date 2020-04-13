package com.demo.moviecatalogservice.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.demo.moviecatalogservice.models.CatalogItem;
import com.demo.moviecatalogservice.models.Movie;
import com.demo.moviecatalogservice.models.Rating;
import com.demo.moviecatalogservice.models.UserRating;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class CatalogResource {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	WebClient.Builder webClientBuilder;

	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
		//List<Rating> rating1 = Arrays.asList(new Rating("1234", 4), new Rating("4567", 3));

		
		  UserRating userRating =
		  restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/" +
		  userId, UserRating.class);
		  
		  return userRating.getRatings().stream() .map(rating -> { Movie movie =
		  restTemplate.getForObject("http://movie-info-service/movies/" +
		  rating.getMovieId(), Movie.class); return new CatalogItem(movie.getName(),
		  movie.getDescription(), rating.getRating()); })
		 .collect(Collectors.toList());
		 
		/*
		 * return rating1.stream().map(rating12 -> new CatalogItem("Transformer",
		 * "Test", 4)) .collect(Collectors.toList());
		 */
	}
}

/*
 * Alternative WebClient way Movie movie =
 * webClientBuilder.build().get().uri("http://localhost:8082/movies/"+
 * rating.getMovieId()) .retrieve().bodyToMono(Movie.class).block();
 */