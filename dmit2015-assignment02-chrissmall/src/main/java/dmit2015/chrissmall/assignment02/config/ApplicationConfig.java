package dmit2015.chrissmall.assignment02.config;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.annotation.sql.DataSourceDefinitions;
import jakarta.enterprise.context.ApplicationScoped;

@DataSourceDefinitions({
		@DataSourceDefinition(
				name="java:app/datasources/hsqldatabaseDS",
				className="org.hsqldb.jdbc.JDBCDataSource",
      	//url="jdbc:hsqldb:file:~/jdk/databases/dmit2015-assignment02-chrissmall;shutdown=true",
				url="jdbc:hsqldb:mem:dmit2015assignment02chrissmall",
				user="user2015",
				password="Password2015"),

})

@ApplicationScoped
public class ApplicationConfig {

}