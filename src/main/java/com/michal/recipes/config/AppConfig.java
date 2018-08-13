package com.michal.recipes.config;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan(basePackages="com.michal.recipes")
@PropertySource("classpath:persistence-mysql.properties")
public class AppConfig implements WebMvcConfigurer {

	@Autowired
	private Environment environment;
	
	@Bean
	public DataSource dataSource() {
		
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		
		try {
			dataSource.setDriverClass(environment.getProperty("jdbc.driver"));
		} catch (PropertyVetoException e) {
			throw new RuntimeException(e);
		}
		
		dataSource.setJdbcUrl(environment.getProperty("jdbc.url"));
		dataSource.setUser(environment.getProperty("jdbc.user"));
		dataSource.setPassword(environment.getProperty("jdbc.password"));
		
		// set connection pool props
		dataSource.setInitialPoolSize(getIntProperty("connection.pool.initialPoolSize"));
		dataSource.setMinPoolSize(getIntProperty("connection.pool.minPoolSize"));
		dataSource.setMaxPoolSize(getIntProperty("connection.pool.maxPoolSize"));		
		dataSource.setMaxIdleTime(getIntProperty("connection.pool.maxIdleTime"));

		return dataSource;
	}
	
	private int getIntProperty(String propName) {
		
		String propVal = environment.getProperty(propName);
		int intPropVal = Integer.parseInt(propVal);
		
		return intPropVal;
	}	
	
	
	private Properties getHibernateProperties() {
		Properties properties = new Properties();

		properties.setProperty("hibernate.dialect", environment.getProperty("hibernate.dialect"));
		properties.setProperty("hibernate.show_sql", environment.getProperty("hibernate.show_sql"));
		
		return properties;				
	}
	
	
	@Bean
	public LocalSessionFactoryBean sessionFactory(){
		
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan(environment.getProperty("hibernate.packagesToScan"));
		sessionFactory.setHibernateProperties(getHibernateProperties());
		
		return sessionFactory;
	}
	
	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory);

		return transactionManager;
	}	
	
	
	
	
}
