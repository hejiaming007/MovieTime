package com.jimmystudio.movietime_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jimmystudio.movietime_service.entity.User;
import com.jimmystudio.movietime_service.entity.UserGroup;

@RestController
@RepositoryRestResource(path = "users")
public interface UserRepository extends JpaRepository<User, Long> {

	@RequestMapping(value="/findByUserGroupsIn", method=RequestMethod.GET)
	List<User> findByUserGroupsIn(List<UserGroup> userGroups);
 

	
	
}
