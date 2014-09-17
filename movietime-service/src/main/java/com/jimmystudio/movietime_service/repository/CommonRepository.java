package com.jimmystudio.movietime_service.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CommonRepository<T, D extends Serializable> extends
		JpaRepository<T, D> {
	// TODO is it possible to implement a common repository for all entity here?
}
