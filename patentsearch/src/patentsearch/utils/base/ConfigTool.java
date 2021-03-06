package patentsearch.utils.base;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 读取配置文件信息
 */
public class ConfigTool {
	private static Properties config = new Properties();
	// 静态初始化块
	static {
		try {
			//tomcat安装的服务器路径不能有空格，否则配置文件config.properties内容无法读取
			String path = Thread.currentThread().getContextClassLoader()
					.getResource("").getPath();
			InputStream is = new FileInputStream(path + "/config.properties");
			config.load(is);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void init() {
		try {
			String path = Thread.currentThread().getContextClassLoader()
					.getResource("").getPath();
			InputStream is = new FileInputStream(path + "/config.properties");
			config.load(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	 

	/**
	 * 设置properties属性
	 * 
	 * @return
	 */
	public static void setPorperTyToFile(String propertyKey, String value) {
		StringBuilder str = new StringBuilder();
		String propertyValue = null;
		try {
			for (String propertyName : config.stringPropertyNames()) {
				if (propertyName.equals(propertyKey)) {
					propertyValue = value;
				} else {
					propertyValue = new String(config.getProperty(propertyName)
							.getBytes("ISO-8859-1"), "utf-8");
				}
				str.append(propertyName + "=" + propertyValue).append("\n\r");
			}
			// 将修改过的值写入properties文件
			String filePath = Thread.currentThread().getContextClassLoader()
					.getResource("").getPath()
					+ "/config.properties";
			FileOutputStream fos = new FileOutputStream(filePath);
			//System.out.println(str.toString());
			fos.write(str.toString().getBytes("utf-8"));
			fos.flush();
			fos.close();
			// 更新内存中的数据
			ConfigTool.init();
		} catch (Exception e) {
			//System.out.println("xxx");
			e.printStackTrace();
		}

	}
 
	/**
	 * 在Properties中,根据key,返回value
	 */
	public static String getValue(String key) {
		
		try {
			String value = new String(config.getProperty(key)
					.getBytes("ISO-8859-1"), "utf-8");
			return value;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
