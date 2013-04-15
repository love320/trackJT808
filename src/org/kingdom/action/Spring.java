package org.kingdom.action;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Spring {
	
	private static BeanFactory factory = new FileSystemXmlApplicationContext("classpath:conf/spring-datasource.xml");
	
	public BeanFactory getSpring(){
		return factory;
	}
	
}
