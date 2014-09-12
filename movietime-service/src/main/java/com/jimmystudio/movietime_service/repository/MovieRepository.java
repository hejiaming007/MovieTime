package com.jimmystudio.movietime_service.repository;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;

import com.jimmystudio.movietime_service.entity.Movie;



public interface MovieRepository extends CrudRepository<Movie, Long> {

	
	
}
