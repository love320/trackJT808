package org.kingdom.action;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MainGo {
	
    private static Log log = LogFactory.getLog(MainGo.class);

	public static void main(String[] args) throws IOException {
		
		 log.info("Go……");
		
		 Spring spring = new Spring();
		 
		 int KA = args.length;
		 
		 if(KA < 1){
			 log.info("initSqlite……");
		 //准备初始
		 InitSqlite initSqlite = spring.getSpring().getBean(InitSqlite.class);
		 initSqlite.clearInit();
		 }
		 
		 if(KA < 2){
			 log.info("gpsEeye……");
		 //分析原始码
		 GPSEeye gpsEeye = spring.getSpring().getBean(GPSEeye.class);
		 gpsEeye.action();
		 }
		 
		 if(KA < 3){
			 log.info("eyeSocketGPS……");
		 //分析解析
		 GPSSocket eyeSocketGPS = spring.getSpring().getBean(GPSSocket.class);
		 eyeSocketGPS.action();
		 }
		 
		 if(KA < 4){
			 log.info("gpsService……");
		 //分析业务处理
		 GPSService gpsService = spring.getSpring().getBean(GPSService.class);
		 gpsService.action();
		 }
		 
		 if(KA < 5){
			 log.info("trackTest……");
		 //数据库断点信息
		 TrackTest trackTest = spring.getSpring().getBean(TrackTest.class);
		 String str = trackTest.homePage();
		 }
		 
		 if(KA < 6){
			 log.info("gpsTrackAction……");
		 //分析处理
		 GPSTrackAction gpsTrackAction = spring.getSpring().getBean(GPSTrackAction.class);
		 gpsTrackAction.action();
		 }
		 
		 log.info("End……");
		 
	}

}
