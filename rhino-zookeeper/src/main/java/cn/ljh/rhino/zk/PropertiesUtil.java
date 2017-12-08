package cn.ljh.rhino.zk;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class PropertiesUtil {
	
	static Properties pps;
	static {
		pps = new Properties();
		try {
			pps.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getMessage(String key) throws FileNotFoundException, IOException {
		String value = pps.getProperty(key);
		return value;

	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		String ip = PropertiesUtil.getMessage("ips");
		String timeOut = PropertiesUtil.getMessage("time_out");
		String baseSleepTimeMs = PropertiesUtil.getMessage("base_sleep_timems");
		String maxRetries = PropertiesUtil.getMessage("max_retries");
		System.out.println(ip);
		System.out.println(timeOut);
		System.out.println(baseSleepTimeMs);
		System.out.println(maxRetries);
	}

}