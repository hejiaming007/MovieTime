package com.jimmystudio.movietime_service.schema_export;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import com.jimmystudio.movietime_service.entity.Comment;
import com.jimmystudio.movietime_service.entity.Group;
import com.jimmystudio.movietime_service.entity.Movie;
import com.jimmystudio.movietime_service.entity.User;


public class ExportSchema {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Configuration configuration = new Configuration();
	
		
		configuration.setProperty(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
		
//		for (Class<?> entityClass : classes) {
		
//		configuration.addPackage("com.jimmystudio.movietime_service.entity");
//		
		configuration.addAnnotatedClass(Comment.class);
		configuration.addAnnotatedClass(User.class);
		configuration.addAnnotatedClass(Movie.class);
		configuration.addAnnotatedClass(Group.class);
//		
//		}
		
		SchemaExport schemaExport = new SchemaExport(configuration);
		schemaExport.setDelimiter(";");
		schemaExport.setFormat(true);
		schemaExport.setOutputFile("target/schema.ddl");
		
		boolean consolePrint = true;
		boolean exportInDatabase = false;
		schemaExport.create(consolePrint, exportInDatabase);
	}

}
