package com.jimmystudio.movietime_service.repository;

import java.util.Date;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jimmystudio.movietime_service.entity.Movie;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class MovieRepositoryTest {
 
	@Autowired
	MovieRepository movieRepository;
	
	@Test
	public void testSingleMovieRepository(){
		
		Assert.assertNotNull(movieRepository);
		
		Movie movie = new Movie();
		movie.setName("Titanic");
		movie.setCreationDate(new Date());
		movie.setDescription("Good movie!");
		movie = movieRepository.save(movie);
		
		Assert.assertNotNull(movie.getId());
		
		Movie movie2 = movieRepository.findOne(movie.getId());
		Integer version1 = movie2.getVersion();
		Assert.assertNotNull(movie2);
		Assert.assertEquals(movie2.getDescription(), movie.getDescription());
		
		
		
		Iterator<Movie> itr = movieRepository.findAll().iterator();
		int count = 0;
		while(itr.hasNext()){
			itr.next();
			count++;
		}
		
		Assert.assertEquals(1, count);
//		Assert.assertEquals(1, movieRepository.findAll().spliterator().estimateSize());
		
		String newDesc = "Very goold movie";
		movie.setDescription(newDesc);
		movie2 = movieRepository.save(movie);
		Integer version2 = movie2.getVersion();
		Assert.assertEquals(newDesc, movie2.getDescription()); 
		Assert.assertTrue(version2 > version1); 
		
		movieRepository.deleteAll();
		itr = movieRepository.findAll().iterator();
		count = 0;
		while(itr.hasNext()){
			itr.next();
			count++;
		}
		Assert.assertEquals(0, count);
		
		
	}
	
	
	
	
	
	
}
