package com.jimmystudio.movietime_service;

import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;



/**
 * @author Administrator
 * 
 * Run the executable jar:  java -jar movietime-service-0.0.1-SNAPSHOT-exec.jar
 *
 */
@Configuration
@EnableJpaRepositories
@Import(RepositoryRestMvcConfiguration.class)
@EnableAutoConfiguration
public class SpringBootApplication {

    public static void main(String[] args) throws Exception {
       
    	SpringApplication.run(SpringBootApplication.class, args);
        
        Server.openBrowser("http://localhost:8080/user.html");

    }
    


}
