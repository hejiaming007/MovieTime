package com.jimmystudio.movietime_service.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class UserGroup extends AbstractEntity {

	private String name;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "UserGroup_User_Mappings", joinColumns = @JoinColumn(name = "UserGroup_ID"), inverseJoinColumns = @JoinColumn(name = "User_ID"))
	private List<User> users = new ArrayList<User>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users.clear();
		if (users != null) {
			this.users.addAll(users);
		}
	}

}
