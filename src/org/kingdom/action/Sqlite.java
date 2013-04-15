package org.kingdom.action;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Sqlite {
	
	private String driverClassName;
	private String url;
	private static Statement stat; 
	private static Connection connTest; 
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public Statement newStat(){
		if(stat != null) return stat;
		try {
			Class.forName(driverClassName);
			 connTest = DriverManager.getConnection(url);
			 connTest.setAutoCommit(false);
		     stat = connTest.createStatement();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
		return stat;
	}
	
	public boolean connCommit(){
		try {
			connTest.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	

}
