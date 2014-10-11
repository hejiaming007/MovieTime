package com.jimmystudio.movietime_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RestController;

import com.jimmystudio.movietime_service.entity.User;
import com.jimmystudio.movietime_service.entity.UserGroup;

@RestController
@RepositoryRestResource(path = "users")
//@RequestMapping(value="/users", headers = "Accept=application/json, application/xml")
public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findByUserGroupsIn(List<UserGroup> userGroups);
 
}
