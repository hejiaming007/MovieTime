package com.jimmystudio.movietime_service.entity;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class User extends AbstractEntity {

	private String name;

	private String password;

	private Date registionDate;
//
//	@ManyToMany
//	List<Group> groups;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getRegistionDate() {
		return registionDate;
	}

	public void setRegistionDate(Date registionDate) {
		this.registionDate = registionDate;
	}
//
//	public List<Group> getGroups() {
//		return groups;
//	}
//
//	public void setGroups(List<Group> groups) {
//		this.groups = groups;
//	}

}
