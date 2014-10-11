package com.jimmystudio.movietime_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RestController;

import com.jimmystudio.movietime_service.entity.Comment;
import com.jimmystudio.movietime_service.entity.Movie;

@RestController
@RepositoryRestResource(path = "comment")
public interface CommentRepository extends JpaRepository<Comment, Long> {

	public List<Comment> findByMovieOrderByCreationDateDesc(Movie movie);

}
