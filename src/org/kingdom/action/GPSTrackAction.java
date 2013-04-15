package org.kingdom.action;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class GPSTrackAction {
	
    private static Log log = LogFactory.getLog(GPSTrackAction.class);
	
	private Sqlite sqliteJdbc;
	
	private int rownum = 500;
	
	public int action(){
		
		try {
		List tractionList = traction();
		List<Map> listMap = (List<Map>)tractionList;
		int statExNum = 0;
		Statement stat = sqliteJdbc.newStat();
		for(Map sing:listMap){
			String sql = actionGo((String)sing.get("id"),(String)sing.get("simid"),(String)sing.get("ftime"),(String)sing.get("ltime"));
				stat.executeUpdate(sql);
				statExNum++;
				log.info(sql);
				if(statExNum >= rownum){
					sqliteJdbc.connCommit();//提交
					stat.clearBatch();
					statExNum=0;
				}
			
		}
		sqliteJdbc.connCommit();//提交
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return 1;
	}
	
	//获取断点，时间点
	public List traction(){
		String sql = " SELECT id,simid,ftime,ltime FROM traction where type = 0  ";
		Statement stat = sqliteJdbc.newStat();
		List list = new ArrayList<Map>();
		try {
			ResultSet result  = stat.executeQuery(sql);
			Map tractionSing = null;
			while(result.next()) {
				tractionSing = new HashMap<String, String>();
				tractionSing.put("id", result.getString("id"));
				tractionSing.put("simid", result.getString("simid"));
				tractionSing.put("ftime", result.getString("ftime"));
				tractionSing.put("ltime", result.getString("ltime"));
				list.add(tractionSing);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info(" SQLException  "+sql);
		}
		
		return list;
	}
	
	//分析日志
	public int actionLog(String logname,String simid,String ftime,String ltime){
		String sql = " SELECT id FROM "+logname+" where 1 = 1";
		sql += " and simid = '"+simid+"' ";
		sql += " and gtime > '"+ftime+"' ";
		sql += " and gtime < '"+ltime+"' ";
		sql += " order by id asc ";
		Statement stat = sqliteJdbc.newStat();
		int num = 0;
		try {
			ResultSet result  = stat.executeQuery(sql);
			while(result.next()) {
				num++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info(" SQLException  "+sql);
		}
		return num;
	}
	
	//综合分析，并更新说明
	public String actionGo(String id,String simid,String ftime,String ltime){
		String body = "  ";
		int type = 1;
		
		String[] tables = {"eyelog","eyesocketlog","eyeservicelog"};
		
		for(String tab : tables){
			int num = actionLog(tab,simid,ftime,ltime);
			if(num>0){
				type++;
				body += tab+" 有日志"+num+"信息。 ";
			}else{
				break;
			}
		}
		
		//更新断点信息说明
		String sql = " update traction set type = "+type+" , body = '"+body+"' where id = "+id;

		return sql;
	}


	public void setSqliteJdbc(Sqlite sqliteJdbc) {
		this.sqliteJdbc = sqliteJdbc;
	}

	public void setRownum(int rownum) {
		this.rownum = rownum;
	}
	
	

}
