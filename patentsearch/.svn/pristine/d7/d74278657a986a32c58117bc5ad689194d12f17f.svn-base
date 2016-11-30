package aTestSpeed;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class Test1 {

 static ResultSet rs = null;
 static Statement statement = null;
 static Connection connection=null;
 private static Test1 test;
 private Test1(){
	 ConnectionFactory cFactory = new ConnectionFactory();
	 connection= cFactory.createConnection();
 }
 public static Test1 getTest()
 {
  if(test==null)
	  test=new Test1();
  return test;
 }
 
 /**
  * @param args
  */
 public static void main(String[] args) {
//	 Test1.getTest().insertData("aaa","qqq", "wwww","1","1","1","112");
	 System.out.println("Hello".equals("hello"));
 } 
 
 public static void insertData(String appno ,String appl,String inventor,String nc,String ipcMain,String apd,String pudText){
	  try {
		   PreparedStatement pState = connection.prepareStatement("INSERT INTO ANALYSISTABLE(APPNO,APPL,INVENTOR,NC,IPCMAIN,APDTEXT,PUDTEXT) VALUES('"+appno+"','"+appl+"','"+inventor+"','"+nc+"','"+ipcMain+"','"+apd+"','"+pudText+"')");
		   pState.executeQuery();

		  } catch (SQLException e) {
		       e.printStackTrace();
		  }  
 }
 
}