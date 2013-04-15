package org.kingdom.action;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
public class FileRW {
	
	public void ReadFile(){
		try {
			String encoding = "GBK"; // 字符编码(可解决中文乱码问题 )
			File file = new File("D:/delete/kingdom.E-eye-GPS/kingdom.E-eye-GPS.log");
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTXT = null;

				BufferedWriter writer = new BufferedWriter(new FileWriter(new File("D:/delete/kingdom.E-eye-GPS/kingdom.E-eye-GPS.log.txt")));
				boolean st = false;
				while ((lineTXT = bufferedReader.readLine()) != null) {
					if (lineTXT.indexOf("01 38 17 62 79 46") >= 0) {
						System.out.println(lineTXT.toString().trim());
						writer.write(lineTXT.toString().trim()+"\n");
						st = true;
					}/*else if(st){
						System.out.println(lineTXT.toString().trim());
						writer.write(lineTXT.toString().trim()+"\n");
						st = false;
					}*/
				}
				read.close();
				writer.close();
			} else {
				System.out.println("找不到指定的文件！");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容操作出错");
			e.printStackTrace();
		}
	}

}
