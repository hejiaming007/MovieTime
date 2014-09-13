package com.jimmystudio.movietime_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jimmystudio.movietime_service.entity.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {

}
