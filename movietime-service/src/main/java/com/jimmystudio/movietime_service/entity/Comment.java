package com.jimmystudio.movietime_service.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Comment extends AbstractEntity {

	private String content;

	private Date creationDate;

	@ManyToOne
	private Movie movie;

	@ManyToOne
	private User creator;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {

		this.movie = movie;
		//
		// //prevent endless loop
		// if (sameAsFormer(movie))
		// return ;
		// //set new owner
		// Movie oldMovie = this.movie;
		// this.movie = movie;
		// //remove from the old owner
		// if (oldMovie!=null)
		// oldMovie.removeComment(this);
		// //set myself into new owner
		// if (movie!=null)
		// this.movie.addComment(this);
	}

}
