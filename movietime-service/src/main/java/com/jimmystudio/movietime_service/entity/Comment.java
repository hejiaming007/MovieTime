package com.jimmystudio.movietime_service.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "COMMENT", indexes = { @Index(name = "COMMENT_MOVIEID_IDX", columnList = "MOVIE_ID, CREATOR_ID") })
public class Comment extends AbstractEntity {

	@Column(length = 5000)
	private String content;

	private Date creationDate;

	@ManyToOne
	@JoinColumn(name = "CREATOR_ID")
	private User creator;

	@ManyToOne()
	@JoinColumn(name = "MOVIE_ID")
	private Movie movie;

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
	}

}
