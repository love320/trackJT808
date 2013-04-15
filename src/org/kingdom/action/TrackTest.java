package org.kingdom.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

public class TrackTest {
	
    private static Log log = LogFactory.getLog(TrackTest.class);
	
	private JdbcTemplate jdbc;
	
	private Sqlite sqliteJdbc;
	
	private String wfile = "track.test.txt";
	
	private String partition = " gps_t_track ";

	private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public String homePage() throws IOException{
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(wfile)));
		
		//查询车辆
		String sql = " select t.id,t.car_no,t.simid  from gps_t_car t  ";
		List<Map<String,Object>> carList = jdbc.queryForList(sql);
		for(Map sing : carList){
			if(sing.get("id") == null || sing.get("id").toString() == null || sing.get("id").toString().trim().equals("")) continue;
			if(sing.get("simid") == null || sing.get("simid").toString() == null || sing.get("simid").toString().trim().equals("")) continue;
			if(sing.get("car_no") == null || sing.get("car_no").toString() == null || sing.get("car_no").toString().trim().equals("")) continue;
			actionSing(sing.get("id").toString(),sing.get("simid").toString(),writer,"车辆 ："+sing.get("car_no"));
		}
		writer.write("End...");
		return null;
	}
	
	public void actionSing(String id,String simid,BufferedWriter writer,String msg) throws IOException{
		String sql = " SELECT t.comptertime,t.gpstime, t.latitude,t.longitude,t.speed,t.gpscar_id FROM "+partition+" t ";
		sql = " SELECT t.comptertime,t.gpstime, t.latitude,t.longitude,t.speed,t.gpscar_id FROM gps_t_track t";
		sql += " WHERE  t.gpsstate = 1 AND t.GPSCAR_ID = ? ";
		sql +="  ORDER BY t.gpstime ASC  ";
		
		List<Map<String,Object>> trackList = jdbc.queryForList(sql,id);
		
		if(trackList.size() <= 0) return;
		
		Date temDate =  (Date)trackList.get(0).get("gpstime");
		long beforeNum = 0;
		Statement stat = sqliteJdbc.newStat();
		int statExNum = 0;
		for(int i = 1;i< trackList.size() ;i++){
			Date time = (Date)trackList.get(i).get("gpstime");
			long num = time.getTime() - temDate.getTime();
			String msgText = msg +" "+ format.format(temDate) +"  "+ format.format(time) +" 差:"+(num/1000)+"秒\n";
			if(num > 11000 && num != 240000 && beforeNum != num) {
				log.info(msgText) ;
				//writer.write(msgText);
				String sqlitesql ="insert into traction(simid,ftime,ltime,type,msg) VALUES ('"+simid+"','"+format.format(temDate) +"','"+format.format(time) +"',0,'"+msgText+"') ";
				try {
					stat.addBatch(sqlitesql);
					statExNum++;
					log.info(sqlitesql);
					if(statExNum >= 500){
						log.info("TrackTest: 500 Row ");
						stat.executeBatch();
						stat.clearBatch();
						statExNum=0;
						sqliteJdbc.connCommit();//提交
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info(" SQLException  "+sqlitesql);
				}
			}
			temDate = time;
			beforeNum = num;//记录上次
		}
		try {
			log.info("TrackTest:  End "+stat.executeBatch().length);
			sqliteJdbc.connCommit();//提交
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setWfile(String wfile) {
		this.wfile = wfile;
	}

	public void setJdbc(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	public void setPartition(String partition) {
		this.partition = partition;
	}

	public void setSqliteJdbc(Sqlite sqliteJdbc) {
		this.sqliteJdbc = sqliteJdbc;
	}
	
	
	

}
