package patentsearch.web.action.front.search;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import patentsearch.bean.cndescriptionitem.CNDescriptionItem;
import patentsearch.bean.util.xml.XMLUtil;
  
  
  
public class Demo {  
  
    /** 
     * @param args 
     * @throws IOException 
     */  
    public static void main(String[] args) throws IOException {  
    	CNDescriptionItem cnDescriptionItem = new CNDescriptionItem();
        //连接redis服务   
//        Jedis jedis = new Jedis("11.0.0.26",6379);  
    	 String JDriver="com.microsoft.sqlserver.jdbc.SQLServerDriver";//SQL数据库引擎
    	  String connectDB=  "jdbc:sqlserver://11.0.0.25:1433; DatabaseName=Db_LegalStatus";//数据源
    	  try
    	  {
    	   Class.forName(JDriver);//加载数据库引擎，返回给定字符串名的类
    	  }catch(ClassNotFoundException e)
    	  {
    	   //e.printStackTrace();
    	   System.out.println("加载数据库引擎失败");
    	   System.exit(0);
    	  } 
    	  System.out.println("数据库驱动成功");
    	  try {
    	   String user="sa";
    	   String password="123456";
			Connection con=DriverManager.getConnection(connectDB,user,password);
    	   System.out.println("连接数据库成功");
    	   Statement stmt=con.createStatement();//创建SQL命令对象
    	   Statement stmt1=con.createStatement();//创建SQL命令对象
    	   ResultSet rs=stmt.executeQuery("select distinct SHENQINGH from CnLegalStatus where zhaiYao IS NULL AND zhuQuanLi IS NULL");//返回SQL语句查询结果集(集合)
    	   String sQH = null;
    	   int length = 1;
    	   int num = 0;
    	   while(rs.next())
    	   {
    	    //输出每个字段
    		   length = rs.getString("SHENQINGH").length();
    		   if(length>0){
    			   sQH = rs.getString("SHENQINGH").substring(0, length-1);
    			   try{
    				   cnDescriptionItem = XMLUtil.getCNDescriptionItemByAppno(sQH);
    			   } catch (Exception e) {
    				   continue;
    			   }

				   if(cnDescriptionItem == null){
					   continue;
				   }
    			   if(cnDescriptionItem!=null){
    				   if(cnDescriptionItem.getAbstr()==null){
    					   cnDescriptionItem.setAbstr("");
    				   }else{
    					   cnDescriptionItem.setAbstr(cnDescriptionItem.getAbstr().replace("\'", "\""));
    				   }
					   if(cnDescriptionItem.getClaim() == null){
						   cnDescriptionItem.setClaim(""); 
					   } else{
						   cnDescriptionItem.setClaim(cnDescriptionItem.getClaim().replace("\'", "\"")); 
					   }
					   if(cnDescriptionItem.getAbstr().length()>4000){
						   continue;
					   }if(cnDescriptionItem.getClaim().length()>4000){
						   continue;
					   }
					   int result = 0;
					   try{
        			   result = stmt1.executeUpdate("update CnLegalStatus set zhaiYao = '"+cnDescriptionItem.getAbstr().trim()+"' , zhuQuanLi='"+cnDescriptionItem.getClaim().trim()+"' where SHENQINGH='"+rs.getString("SHENQINGH")+"'");//返回SQL语句查询结果集(集合)
					   } catch(SQLException e){
						   continue;
					   }
					   num = num + result;
        			   System.out.println(rs.getString("SHENQINGH")+"--"+num);
    			   }
    		   }
    	   }
    	   System.out.println("读取完毕");
    	  } catch (SQLException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}//连接数据库对象
    	  
     
    }  
}
