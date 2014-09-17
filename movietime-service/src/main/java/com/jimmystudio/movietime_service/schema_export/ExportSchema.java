package com.jimmystudio.movietime_service.schema_export;

import java.util.Set;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.reflections.Reflections;

import com.jimmystudio.movietime_service.entity.AbstractEntity;

public class ExportSchema {

	public static void main(String[] args) {
		Configuration configuration = new Configuration();

		configuration.setProperty(Environment.DIALECT,
				"org.hibernate.dialect.Oracle10gDialect");

		Reflections reflections = new Reflections("com.jimmystudio.movietime_service.entity");
		
		 Set<Class<? extends AbstractEntity>> allClasses = 
		     reflections.getSubTypesOf(AbstractEntity.class);
		
		 for (Class<? extends AbstractEntity> class1 : allClasses) {
			 configuration.addAnnotatedClass(class1);
		}
		 

		SchemaExport schemaExport = new SchemaExport(configuration);
		schemaExport.setDelimiter(";");
		schemaExport.setFormat(true);
		schemaExport.setOutputFile("target/schema.ddl");
		
		boolean consolePrint = true;
		boolean exportInDatabase = false;
		schemaExport.create(consolePrint, exportInDatabase);
	}

}
