package patentsearch.utils.base.jedis;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


import redis.clients.jedis.Jedis;

//rukou
public class ImportJedis {
	public static int total = 0;
	private static Logger logger = Logger.getLogger("DownPic") ;
	private static Jedis jedis = new Jedis("11.0.0.99", 6379,1000000);
	private static String JDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";// SQL数据库引擎
	private static String connectDB = "jdbc:sqlserver://11.0.0.25:1433; DatabaseName=Db_LegalStatus";// 数据源

	/*
	 * 递归调用查找指定文件加下所有文件
	 */
	public static String GetSql(String appno) throws SQLException {
		
		
		try {
			Class.forName(JDriver);// 加载数据库引擎，返回给定字符串名的类
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
			System.out.println("加载数据库引擎失败");
			System.exit(0);
		} 
		
		
		String user = "sa";
		String password = "123456";
		Connection con = DriverManager.getConnection(connectDB, user,password);
		
		System.out.println("连接数据库成功");
		Statement stmt =  con.createStatement(); // 创建SQL命令对象
		
		
		//ResultSet rs = stmt.executeQuery("SELECT *  FROM  PatentStoreInfo where 1=1 order by legalDate desc");
		if(appno.length()<12){
			appno=XMLUtil.formatAppno(appno).substring(0, 12);
		}
		
		String path="Z:\\cn_item\\CN_XML\\"+appno.subSequence(0, 4)+"\\"+appno.subSequence(4, 9)+"\\"+appno+".xml";
		
		File rootDir = new File(path);
		if (!rootDir.isDirectory()) {
			if (rootDir.getName().endsWith("xml")) {
				String rootDirFront = rootDir.getName();
				rootDirFront = rootDirFront.substring(0, rootDirFront
						.indexOf("."));
				if (rootDirFront.matches("\\d+")) {
					CNDescriptionItem cnDescriptionItem = XMLUtil
							.getObjectFormXml(rootDir);
					if (cnDescriptionItem != null) {
						String  appnoWithDot=ImportJedis.getCheckAppnoWithDot(cnDescriptionItem.getAppno());
						try {
							ResultSet rs = stmt
									.executeQuery("SELECT TOP 1  category FROM  LegalStatusDetailView where SHENQINGH='"
											+appnoWithDot
											+ "' order by legalDate desc");
							String category =null;
							if(rs.next()){
								category=rs.getString("category");
							}else{
								category="";
							}
							jedis.set("Jly"+appnoWithDot
									, cnDescriptionItem.getApdText()
									+ "_" + cnDescriptionItem.getPudText()
									+ "_" + cnDescriptionItem.getAppdText()
									+ "_" + cnDescriptionItem.getNc() + "_"
									+ cnDescriptionItem.getIpcMain() + "_"
									+ cnDescriptionItem.getAppl() + "_"
									+ cnDescriptionItem.getInventor()+"_"
									+ category);
							System.out.println(rootDir.getName() + "--导入了"
									+ (++total) + "条");
						} catch (SQLException e) {
							e.printStackTrace();
						}// 返回SQL语句查询结果集(集合)
					}
				}
			}
		} else {
			String[] fileList = rootDir.list();
			for (int i = 0; i < fileList.length; i++) {
				path = rootDir.getAbsolutePath() + "\\" + fileList[i];
				GetSql(path);
			}
		}
		return null;
	}
	/*
	 * 根据专利8或12位申请号生成其校验位Without dot
	 */

	public static String getCheckAppnoWithDot(String appno) {
		int checkBit = 0;
		String checkAppno = "";
		if (appno != null && !"".equals(appno)) {
			char[] cc = appno.toCharArray();
			Integer[] ii = new Integer[appno.length()];
			for (int i = 0; i < appno.length(); i++) {
				ii[i] = Integer.parseInt(cc[i] + "");
			}
			if (appno.length() == 8) {
				checkBit = (ii[0] * 2 + ii[1] * 3 + ii[2] * 4 + ii[3] * 5
						+ ii[4] * 6 + ii[5] * 7 + ii[6] * 8 + ii[7] * 9) % 11;

			}
			if (appno.length() == 12) {
				checkBit = (ii[0] * 2 + ii[1] * 3 + ii[2] * 4 + ii[3] * 5
						+ ii[4] * 6 + ii[5] * 7 + ii[6] * 8 + ii[7] * 9 + ii[8]
						* 2 + ii[9] * 3 + ii[10] * 4 + ii[11] * 5) % 11;
			}
			if (checkBit >= 10) {
				checkAppno = appno  + 'x';
			} else if (checkBit < 10) {
				checkAppno = appno + checkBit;
			}
		}
		return checkAppno;
	}
}
