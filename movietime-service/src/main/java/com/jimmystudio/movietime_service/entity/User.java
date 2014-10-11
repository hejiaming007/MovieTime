package com.jimmystudio.movietime_service.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "USER")
public class User extends AbstractEntity {

	private String name;

	private String password;

	private Date registionDate;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "UserGroup_User_Mappings", joinColumns = @JoinColumn(name = "User_ID"), inverseJoinColumns = @JoinColumn(name = "UserGroup_ID"), indexes = { @Index(name = "UserGroup_User_Mappings_IDX", columnList = "User_ID, UserGroup_ID") })
	private List<UserGroup> userGroups = new ArrayList<UserGroup>();

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

	public List<UserGroup> getUserGroup() {
		return userGroups;
	}

	public void setUserGroup(List<UserGroup> userGroup) {
		this.userGroups.clear();
		if (userGroup != null) {
			this.userGroups.addAll(userGroup);
		}
	}

}
