package org.kingdom.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class GPSEeye {
	
   private static Log log = LogFactory.getLog(GPSEeye.class);
	
	private String filepath ="kingdom.E-eye-GPS.log";
	
	private Sqlite sqliteJdbc;
	
	private String timestr;
	
	private int rownum = 500;
	
	public int action(){
		try {
			String encoding = "GBK"; // 字符编码(可解决中文乱码问题 )
			File file = new File(filepath);
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTXT = null;
				Statement stat = sqliteJdbc.newStat();
				int statExNum = 0;
				while ((lineTXT = bufferedReader.readLine()) != null) {
					int beginIndex = lineTXT.indexOf("7E");
					int endIndex = lineTXT.lastIndexOf("7E");
					String txt7e = lineTXT.substring(beginIndex, endIndex);
					String[] byteStr = txt7e.split(" ");
					if(byteStr.length < 40) continue;
					String simid = byteStr[5].substring(1)+byteStr[6]+byteStr[7]+byteStr[8]+byteStr[9]+byteStr[10];
					
					String[] s7E = txt7e.split(timestr);
					for(int i = 1 ;i<s7E.length;i++){
						if(s7E[i].length()<10) continue;
						String time =  timestr+s7E[i].substring(0, 9);
						java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy MM dd hh mm ss",java.util.Locale.US);
						java.util.Date d = sdf.parse(20+time); 
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String sql = " insert into eyelog(simid,gtime) values('"+simid+"','"+formatter.format(d)+"') ";
						stat.execute(sql);
						statExNum++;
						log.info(sql);
						if(statExNum >= rownum){
							sqliteJdbc.connCommit();//提交
							statExNum=0;
						}
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
