package com.jimmystudio.movietime_service.repository;

import org.springframework.data.repository.CrudRepository;

import com.jimmystudio.movietime_service.entity.User;



public interface UserRepository extends CrudRepository<User, Long> {

	
	
}
