package org.kingdom.action;

import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class InitSqlite {
	
   private static Log log = LogFactory.getLog(InitSqlite.class);
	
	private Sqlite sqliteJdbc;
	
	public boolean clearInit(){
		
		String[] tabs = {"traction","eyelog","eyesocketlog","eyeservicelog"};
		
		Statement stat = sqliteJdbc.newStat();
		for(String tab :tabs){
			String sql = "DELETE FROM "+tab ;
			try {
				stat.execute(sql);
				log.info(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.info(" SQLException  "+sql);
			}
		}
		sqliteJdbc.connCommit();//提交
		
		return true;
	}

	public void setSqliteJdbc(Sqlite sqliteJdbc) {
		this.sqliteJdbc = sqliteJdbc;
	}
	

	
	
}
