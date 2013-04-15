package org.kingdom.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class GPSSocket {
	
    private static Log log = LogFactory.getLog(GPSSocket.class);
	
	private String filepath ="kingdom.E-eye-GPS.socket.log";
	
	private Sqlite sqliteJdbc;
	
	private String timestr;
	
	private int rownum = 500;
	
	public int action(){
		try {
			String encoding = "GBK"; // 字符编码(可解决中文乱码问题 )
			File file = new File(filepath);
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTXT = null;
				Statement stat = sqliteJdbc.newStat();
				int statExNum = 0;
				while ((lineTXT = bufferedReader.readLine()) != null) {
					int simIndex = lineTXT.indexOf("Save……");
					if(simIndex < 0) continue;
					String simid = lineTXT.substring(simIndex+6,simIndex+11+6);
					int timeIndex = lineTXT.indexOf(timestr);
					if(timeIndex < 0 ) continue;
					String time =  lineTXT.substring(timeIndex, timeIndex+17);
					java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy MM dd hh mm ss",java.util.Locale.US);
					java.util.Date d = sdf.parse(20+time); 
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String sql = " insert into eyesocketlog(simid,gtime) values('"+simid+"','"+formatter.format(d)+"') ";
					stat.execute(sql);
					statExNum++;
					log.info(sql);
					if(statExNum >= rownum){
						sqliteJdbc.connCommit();//提交
						statExNum=0;
					}
				}
				sqliteJdbc.connCommit();//提交
				read.close();
			} else {
				log.info("找不到指定的文件！");
			}
		} catch (Exception e) {
			log.info("读取文件内容操作出错");
			e.printStackTrace();
		}
		
		return 1;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public void setSqliteJdbc(Sqlite sqliteJdbc) {
		this.sqliteJdbc = sqliteJdbc;
	}

	public void setTimestr(String timestr) {
		this.timestr = timestr;
	}

	public void setRownum(int rownum) {
		this.rownum = rownum;
	}


	

}
