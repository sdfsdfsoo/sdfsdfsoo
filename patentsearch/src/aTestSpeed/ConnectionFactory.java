package aTestSpeed;
//public class JDBCDemo {
//
//}

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class ConnectionFactory {
 
 private Connection connection = null; 
 private String url = "jdbc:oracle:thin:@11.0.0.26:1521:OCLINSTANCE";  //192.168.1.118
 
 
 /**
  * 创建连接
  * @return
  */
 public Connection createConnection(){  
  try {
   Class.forName("oracle.jdbc.driver.OracleDriver");
   connection = DriverManager.getConnection(url, "scott", "tiger");
   return connection;   
  } catch (ClassNotFoundException e) {
   return null;
  } catch (SQLException e) {
   return null;
  }
 } 
 /**
  * 释放连接
  */
 public void releaseConnection(){  
  if (connection!=null)
   try {
    connection.close();
   } catch (SQLException e) {
   }
 }
}


