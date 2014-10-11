package com.jimmystudio.movietime_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jimmystudio.movietime_service.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {

	// List<Movie> findByCommentsIn(List<Comment> commentList);

}
