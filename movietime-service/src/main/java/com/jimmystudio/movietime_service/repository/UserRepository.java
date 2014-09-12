package com.jimmystudio.movietime_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jimmystudio.movietime_service.entity.User;



public interface UserRepository extends JpaRepository<User, Long> {

	
	
}
