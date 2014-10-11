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

//	@Autowired
//	static RestTemplate restTemplate;
//	
//	    @Bean
//	public RestTemplate getRestTemplate(){
//		return new RestTemplate();
//	}
//	
	
//	          .addResourceMappingFor("lastName")
//	          .setPath("surname"); // Change 'lastName' to 'surname' in the JSON
	    
//	          .addResourceMappingFor("siblings")
//	          .setRel("siblings")
//	          .setPath("siblings"); // Pointless in this example,
//	                                // but shows how to change 'rel' and 'path' values.
	
	/*
	@Bean
    public HttpMessageConverters customConverters() {
        HttpMessageConverter<?> additional = new GsonHttpMessageConverter();
        HttpMessageConverter<?> another = new GsonHttpMessageConverter();
        return new HttpMessageConverters(additional, another);
    }*/
	
	/*
	
	
	
	@RequestMapping("/")
    String home() {
		
		User user = new User();
		user.setName("Jimmy");
		user.setPassword("password");
		userRepository.save(user);
		
        return "Hello World! Movie count:" + userRepository.count();
    }
	*/

    public static void main(String[] args) throws Exception {
       
    	SpringApplication.run(SpringBootApplication.class, args);
    	
//    	System.out.println("restTemplate:"+restTemplate);
//    	List messageConverters=new ArrayList();
//    	  messageConverters.add(new SourceHttpMessageConverter());
//    	  messageConverters.add(new FormHttpMessageConverter());
//    	  messageConverters.add(new MappingJackson2HttpMessageConverter());
//    	  restTemplate.setMessageConverters(messageConverters);
    	
//    	SpringBootApplication app = new SpringBootApplication();
    	
//        new SpringApplicationBuilder()
//        .showBanner(false)
//        .sources(SpringBootApplication.class)
//        .run(args);
        
        Server.openBrowser("http://localhost:8080/user.html");

    }
    


}
