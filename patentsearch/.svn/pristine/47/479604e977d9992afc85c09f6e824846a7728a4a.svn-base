package aTestSpeed;

//public class OutSql {
//
//}
import java.io.File;
/**
 * 

 *自动扫描文件夹下的文件
 *用途：自动扫描脚本输入路径，输出要执行的sql导入数据库；
 */
public class OutSql { 
	public static int total=0;
 /**
  * @param args
  */
 public static void main(String[] args) {
   // TODO Auto-generated method stub
   String path ="E:\\1\\2";
   GetSql(path);
 }
 /*
  * 递归调用查找指定文件加下所有文件
  */
 public static  String GetSql(String path){
  File rootDir = new File(path);
   if(!rootDir.isDirectory()){
	   if(rootDir.getName().endsWith("xml")){
//		    System.out.println("文件名:"+rootDir.getAbsolutePath());
		   CNDescriptionItemForAnalysis cnDescriptionItem= XMLUtilInputMem.getObjectFormXml(rootDir);
		    Test1.getTest().insertData(cnDescriptionItem.getAppno(),cnDescriptionItem.getAppl(),cnDescriptionItem.getInventor(),cnDescriptionItem.getNc(),cnDescriptionItem.getIpcMain(),cnDescriptionItem.getApdText(),cnDescriptionItem.getPudText());
		    System.out.println("--导入了"+(++total)+"条");
	   }
   }else{
    String[] fileList =  rootDir.list();
    for (int i = 0; i < fileList.length; i++) {
     path = rootDir.getAbsolutePath()+"\\"+fileList[i];
     GetSql(path);      
      } 
   }    
  return null;    
 }
 
 
}