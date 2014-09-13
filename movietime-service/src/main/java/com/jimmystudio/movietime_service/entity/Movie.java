package com.jimmystudio.movietime_service.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Movie extends AbstractEntity {

	private String name;

	private String description;

	private Date creationDate;

	/**
	 * Usually load a movie will get its creator also; and one Movie only has
	 * one user, so load creator by EAGER
	 */
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "CREATOR_ID")
	private User creator;

	/**
	 * Because one movie might has many comments, so we select LAZY load for
	 * comments here.
	 */
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Comment> comments = new ArrayList<Comment>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments.clear();
		if (comments != null) {
			this.comments.addAll(comments);
		}
	}

}
