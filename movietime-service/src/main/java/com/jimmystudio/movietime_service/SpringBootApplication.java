package com.jimmystudio.movietime_service;

import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jimmystudio.movietime_service.entity.Movie;
import com.jimmystudio.movietime_service.entity.User;

/**
 * @author Administrator
 * 
 * Run the executable jar:  java -jar movietime-service-0.0.1-SNAPSHOT-exec.jar
 *
 */
@RestController
@EnableAutoConfiguration
@Configuration
@ComponentScan
public class SpringBootApplication {

	
	@Autowired
	JpaRepository<User, Long> userRepository;
	
	
	@RequestMapping("/")
    String home() {
		
		User user = new User();
		user.setName("Jimmy");
		user.setPassword("password");
		userRepository.save(user);
		
        return "Hello World! Movie count:" + userRepository.count();
    }
	

    public static void main(String[] args) throws Exception {
       
        new SpringApplicationBuilder()
        .showBanner(false)
        .sources(SpringBootApplication.class)
        .run(args);
        
        Server.openBrowser("http://localhost:8080");

    }

}
