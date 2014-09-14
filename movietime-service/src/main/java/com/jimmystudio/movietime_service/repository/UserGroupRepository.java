package com.jimmystudio.movietime_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jimmystudio.movietime_service.entity.User;
import com.jimmystudio.movietime_service.entity.UserGroup;


public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
	
	List<UserGroup> findByUsersIn(List<User> users);
	
}
