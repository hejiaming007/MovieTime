package com.jimmystudio.movietime_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RestController;

import com.jimmystudio.movietime_service.entity.Movie;

@RestController
@RepositoryRestResource(path = "movie")
public interface MovieRepository extends JpaRepository<Movie, Long> {

	// List<Movie> findByCommentsIn(List<Comment> commentList);

}
