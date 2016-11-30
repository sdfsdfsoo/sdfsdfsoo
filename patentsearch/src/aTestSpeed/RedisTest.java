package aTestSpeed;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;  
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;

import patentsearch.bean.search.requestParameter.DoSearchParameter;
import patentsearch.bean.search.requestParameter.GetGeneralDataParameter;
import patentsearch.bean.user.AnalysisBean;
import patentsearch.bean.user.Users;
import patentsearch.bean.util.xml.XMLUtil;
import patentsearch.util.webservice.WebServiceLocalClientUtil;
import patentsearch.utils.base.ConfigTool;
import patentsearch.utils.base.WebTool;
  
import redis.clients.jedis.Jedis;  
  
  
public class RedisTest {
	
	
//	 public static void main(String[] args) {  
////		 DoSearchParameter doSearchParameter =new DoSearchParameter("123","Cn", "001", "F XX (计算机/TI)");
////		 System.out.println(WebServiceLocalClientUtil.getPatentRecordsNumber(doSearchParameter));
//		 Date date0=new Date();////////////////////////////////第1个记录时间点
//		  Jedis jedis = new Jedis("localhost",6379);  
//		      jedis.del("apddate");  
//		      jedis.del("puddate");  
//		      jedis.del("accdate");  
//		      jedis.del("area");  
//		  DoSearchParameter doSearch = new DoSearchParameter("123", "Cn", "001","F XX (计算机/TI*显示器/TI)");
//	         int totalSize=new Long(WebServiceLocalClientUtil.getPatentRecordsNumber(doSearch)).intValue();
//	         List<String> appnoList = new ArrayList<String>();
//	         if(totalSize<100000){
//					 int totalPage=totalSize/50+1;
//					for(int i=1;i<=totalPage;i++){
//						GetGeneralDataParameter getGeneralData = new GetGeneralDataParameter("123", "Cn", "001",i , 50);
//						appnoList.addAll(WebServiceLocalClientUtil.getAppnolistBySearchWithLastNum(getGeneralData));
//					}
//	         }else{
//	        	 System.out.println("分析数据不得超过10万条");
//	         }
//	         Date date1=new Date();////////////////////////////////第2个记录时间点
//	         inputToMem(appnoList, jedis);
//	         Date date2=new Date();////////////////////////////////第3个记录时间点
//	         generateTable(jedis);
//	         System.out.println(date0+"------->"+date1+"------->"+date2+"--------->"+new Date());
////	         
//		  
//	 }
	 /**
	  * 将数据存入内存数据库
	  * @param appnoList
	  * @param jedis
	  */
	 public static  void inputToMem( List<String> appnoList, Jedis jedis){
		 int total=0;
		 for(int i=0;i<appnoList.size();i++){
        	 String appno=appnoList.get(i);
        	 appno=XMLUtilInputMem.formatAppno(appno);
        	 File rootDir=XMLUtilInputMem.getFileByAppno(appno);
        	 if(rootDir!=null){
        		 CNDescriptionItemForAnalysis cnDescriptionItem= XMLUtilInputMem.getObjectFormXml(rootDir);
        		 if(cnDescriptionItem.getApdText()!=null&&!"".equals(cnDescriptionItem.getApdText())){
        			 jedis.zadd("apddate", Integer.parseInt(cnDescriptionItem.getApdText()), cnDescriptionItem.getAppno());
        		 }
        		 if(cnDescriptionItem.getPudText()!=null&&!"".equals(cnDescriptionItem.getPudText())){
        			 jedis.zadd("puddate", Integer.parseInt(cnDescriptionItem.getPudText()),  cnDescriptionItem.getAppno());
        		 }
        		 if(cnDescriptionItem.getAppdText()!=null&&!"".equals(cnDescriptionItem.getAppdText())){
        			 jedis.zadd("accdate", Integer.parseInt(cnDescriptionItem.getAppdText()),  cnDescriptionItem.getAppno());
        		 }
        		 if(cnDescriptionItem.getNc()!=null&&!"".equals(cnDescriptionItem.getNc())){   //国省
        			 if(Character.isDigit(cnDescriptionItem.getNc().charAt(0))){    //判断是否为数字
        				 jedis.zadd("area", Integer.parseInt(cnDescriptionItem.getNc()),  cnDescriptionItem.getAppno());
        			 }else{
        				 StringBuilder stbur=new StringBuilder();
        				for(int k=0;k<2;k++){
        					      if("a".equalsIgnoreCase(cnDescriptionItem.getNc().charAt(k)+"")){
        						stbur.append("1");
        					}else if("b".equalsIgnoreCase(cnDescriptionItem.getNc().charAt(k)+"")){
        						stbur.append("2");
        					}else if("c".equalsIgnoreCase(cnDescriptionItem.getNc().charAt(k)+"")){
        						stbur.append("3");
        					}else if("d".equalsIgnoreCase(cnDescriptionItem.getNc().charAt(k)+"")){
        						stbur.append("4");
        					}else if("e".equalsIgnoreCase(cnDescriptionItem.getNc().charAt(k)+"")){
        						stbur.append("5");
        					}else if("f".equalsIgnoreCase(cnDescriptionItem.getNc().charAt(k)+"")){
        						stbur.append("6");
        					}else if("g".equalsIgnoreCase(cnDescriptionItem.getNc().charAt(k)+"")){
        						stbur.append("7");
        					}else if("h".equalsIgnoreCase(cnDescriptionItem.getNc().charAt(k)+"")){
        						stbur.append("8");
        					}else if("i".equalsIgnoreCase(cnDescriptionItem.getNc().charAt(k)+"")){
        						stbur.append("9");
        					}else if("j".equalsIgnoreCase(cnDescriptionItem.getNc().charAt(k)+"")){
        						stbur.append("10");
        					}else if("k".equalsIgnoreCase(cnDescriptionItem.getNc().charAt(k)+"")){
        						stbur.append("11");
        					}else if("l".equalsIgnoreCase(cnDescriptionItem.getNc().charAt(k)+"")){
        						stbur.append("12");
        					}else if("m".equalsIgnoreCase(cnDescriptionItem.getNc().charAt(k)+"")){
        						stbur.append("13");
        					}else if("n".equalsIgnoreCase(cnDescriptionItem.getNc().charAt(k)+"")){
        						stbur.append("14");
        					}else if("o".equalsIgnoreCase(cnDescriptionItem.getNc().charAt(k)+"")){
        						stbur.append("15");
        					}else if("p".equalsIgnoreCase(cnDescriptionItem.getNc().charAt(k)+"")){
        						stbur.append("16");
        					}else if("q".equalsIgnoreCase(cnDescriptionItem.getNc().charAt(k)+"")){
        						stbur.append("17");
        					}else if("r".equalsIgnoreCase(cnDescriptionItem.getNc().charAt(k)+"")){
        						stbur.append("18");
        					}else if("s".equalsIgnoreCase(cnDescriptionItem.getNc().charAt(k)+"")){
        						stbur.append("19");
        					}else if("t".equalsIgnoreCase(cnDescriptionItem.getNc().charAt(k)+"")){
        						stbur.append("20");
        					}else if("u".equalsIgnoreCase(cnDescriptionItem.getNc().charAt(k)+"")){
        						stbur.append("21");
        					}else if("v".equalsIgnoreCase(cnDescriptionItem.getNc().charAt(k)+"")){
        						stbur.append("22");
        					}else if("w".equalsIgnoreCase(cnDescriptionItem.getNc().charAt(k)+"")){
        						stbur.append("23");
        					}else if("x".equalsIgnoreCase(cnDescriptionItem.getNc().charAt(k)+"")){
        						stbur.append("24");
        					}else if("y".equalsIgnoreCase(cnDescriptionItem.getNc().charAt(k)+"")){
        						stbur.append("25");
        					}else if("z".equalsIgnoreCase(cnDescriptionItem.getNc().charAt(k)+"")){
        						stbur.append("26");
        					}	
        					      stbur.append("0");//为了防止所得到数字与 国内省代码重复     
        					
        				}
        				jedis.zadd("area", Integer.parseInt(stbur.toString()),  cnDescriptionItem.getAppno());
        			 }
        		 }
        		 
        	 }   
         }
	 }
	 
	 
	 /**
	  * 内存数据库取出数据进行分析
	  * @param path
	  * @param jedis
	  */
	 public static   List<String> generateTable(Jedis jedis){
//		 jedis.del("apddate");
//		 jedis.del("puddate");
//		 jedis.del("accdate");
		 Long apd1985=jedis.zcount("apddate", 1985, 1985);
		 Long apd1986=jedis.zcount("apddate", 1986, 1986);
		 Long apd1987=jedis.zcount("apddate", 1987, 1987);
		 Long apd1988=jedis.zcount("apddate", 1988, 1988);
		 Long apd1989=jedis.zcount("apddate", 1989, 1989);
		 Long apd1990=jedis.zcount("apddate", 1990, 1990);
		 Long apd1991=jedis.zcount("apddate", 1991, 1991);
		 Long apd1992=jedis.zcount("apddate", 1992, 1992);
		 Long apd1993=jedis.zcount("apddate", 1993, 1993);
		 Long apd1994=jedis.zcount("apddate", 1994, 1994);
		 Long apd1995=jedis.zcount("apddate", 1995, 1995);
		 Long apd1996=jedis.zcount("apddate", 1996, 1996);
		 Long apd1997=jedis.zcount("apddate", 1997, 1997);
		 Long apd1998=jedis.zcount("apddate", 1998, 1998);
		 Long apd1999=jedis.zcount("apddate", 1999, 1999);
		 Long apd2000=jedis.zcount("apddate", 2000, 2000);
		 Long apd2001=jedis.zcount("apddate", 2001, 2001);
		 Long apd2002=jedis.zcount("apddate", 2002, 2002);
		 Long apd2003=jedis.zcount("apddate", 2003, 2003);
		 Long apd2004=jedis.zcount("apddate", 2004, 2004);
		 Long apd2005=jedis.zcount("apddate", 2005, 2005);
		 Long apd2006=jedis.zcount("apddate", 2006, 2006);
		 Long apd2007=jedis.zcount("apddate", 2007, 2007);
		 Long apd2008=jedis.zcount("apddate", 2008, 2008);
		 Long apd2009=jedis.zcount("apddate", 2009, 2009);
		 Long apd2010=jedis.zcount("apddate", 2010, 2010);
		 Long apd2011=jedis.zcount("apddate", 2011, 2011);
		 Long apd2012=jedis.zcount("apddate", 2012, 2012);
		 Long apd2013=jedis.zcount("apddate", 2013, 2013);
		 Long apd2014=jedis.zcount("apddate", 2014, 2014);
		 
		 Long pud1985=jedis.zcount("puddate", 1985, 1985);
		 Long pud1986=jedis.zcount("puddate", 1986, 1986);
		 Long pud1987=jedis.zcount("puddate", 1987, 1987);
		 Long pud1988=jedis.zcount("puddate", 1988, 1988);
		 Long pud1989=jedis.zcount("puddate", 1989, 1989);
		 Long pud1990=jedis.zcount("puddate", 1990, 1990);
		 Long pud1991=jedis.zcount("puddate", 1991, 1991);
		 Long pud1992=jedis.zcount("puddate", 1992, 1992);
		 Long pud1993=jedis.zcount("puddate", 1993, 1993);
		 Long pud1994=jedis.zcount("puddate", 1994, 1994);
		 Long pud1995=jedis.zcount("puddate", 1995, 1995);
		 Long pud1996=jedis.zcount("puddate", 1996, 1996);
		 Long pud1997=jedis.zcount("puddate", 1997, 1997);
		 Long pud1998=jedis.zcount("puddate", 1998, 1998);
		 Long pud1999=jedis.zcount("puddate", 1999, 1999);
		 Long pud2000=jedis.zcount("puddate", 2000, 2000);
		 Long pud2001=jedis.zcount("puddate", 2001, 2001);
		 Long pud2002=jedis.zcount("puddate", 2002, 2002);
		 Long pud2003=jedis.zcount("puddate", 2003, 2003);
		 Long pud2004=jedis.zcount("puddate", 2004, 2004);
		 Long pud2005=jedis.zcount("puddate", 2005, 2005);
		 Long pud2006=jedis.zcount("puddate", 2006, 2006);
		 Long pud2007=jedis.zcount("puddate", 2007, 2007);
		 Long pud2008=jedis.zcount("puddate", 2008, 2008);
		 Long pud2009=jedis.zcount("puddate", 2009, 2009);
		 Long pud2010=jedis.zcount("puddate", 2010, 2010);
		 Long pud2011=jedis.zcount("puddate", 2011, 2011);
		 Long pud2012=jedis.zcount("puddate", 2012, 2012);
		 Long pud2013=jedis.zcount("puddate", 2013, 2013);
		 Long pud2014=jedis.zcount("puddate", 2014, 2014);
		 
		 Long acc1985=jedis.zcount("accdate", 1985, 1985);
		 Long acc1986=jedis.zcount("accdate", 1986, 1986);
		 Long acc1987=jedis.zcount("accdate", 1987, 1987);
		 Long acc1988=jedis.zcount("accdate", 1988, 1988);
		 Long acc1989=jedis.zcount("accdate", 1989, 1989);
		 Long acc1990=jedis.zcount("accdate", 1990, 1990);
		 Long acc1991=jedis.zcount("accdate", 1991, 1991);
		 Long acc1992=jedis.zcount("accdate", 1992, 1992);
		 Long acc1993=jedis.zcount("accdate", 1993, 1993);
		 Long acc1994=jedis.zcount("accdate", 1994, 1994);
		 Long acc1995=jedis.zcount("accdate", 1995, 1995);
		 Long acc1996=jedis.zcount("accdate", 1996, 1996);
		 Long acc1997=jedis.zcount("accdate", 1997, 1997);
		 Long acc1998=jedis.zcount("accdate", 1998, 1998);
		 Long acc1999=jedis.zcount("accdate", 1999, 1999);
		 Long acc2000=jedis.zcount("accdate", 2000, 2000);
		 Long acc2001=jedis.zcount("accdate", 2001, 2001);
		 Long acc2002=jedis.zcount("accdate", 2002, 2002);
		 Long acc2003=jedis.zcount("accdate", 2003, 2003);
		 Long acc2004=jedis.zcount("accdate", 2004, 2004);
		 Long acc2005=jedis.zcount("accdate", 2005, 2005);
		 Long acc2006=jedis.zcount("accdate", 2006, 2006);
		 Long acc2007=jedis.zcount("accdate", 2007, 2007);
		 Long acc2008=jedis.zcount("accdate", 2008, 2008);
		 Long acc2009=jedis.zcount("accdate", 2009, 2009);
		 Long acc2010=jedis.zcount("accdate", 2010, 2010);
		 Long acc2011=jedis.zcount("accdate", 2011, 2011);
		 Long acc2012=jedis.zcount("accdate", 2012, 2012);
		 Long acc2013=jedis.zcount("accdate", 2013, 2013);
		 Long acc2014=jedis.zcount("accdate", 2014, 2014);
		 StringBuilder apdBur=new StringBuilder();
			 apdBur.append(apd2005).append(",").append(apd2006).append(",").append(apd2007).append(",").append(apd2008).append(",").append(apd2009).append(",")
		       .append(apd2010).append(",").append(apd2011).append(",").append(apd2012).append(",").append(apd2013).append(",").append(apd2014);
		 StringBuilder pudBur=new StringBuilder();
			 pudBur.append(pud2005).append(",").append(pud2006).append(",").append(pud2007).append(",").append(pud2008).append(",").append(pud2009).append(",")
		       .append(pud2010).append(",").append(pud2011).append(",").append(pud2012).append(",").append(pud2013).append(",").append(pud2014);
		StringBuilder accBur=new StringBuilder();
			 accBur.append(acc2005).append(",").append(acc2006).append(",").append(acc2007).append(",").append(acc2008).append(",").append(acc2009).append(",")
		       .append(acc2010).append(",").append(acc2011).append(",").append(acc2012).append(",").append(acc2013).append(",").append(acc2014);
		  List<String> dateList = new ArrayList<String>();
		  		dateList.add(apdBur.toString());
		  		dateList.add(pudBur.toString());
		  		dateList.add(accBur.toString());
			 return  dateList;
         
	 }
	 /**
	  * 国省构成分析
	  * @param path
	  * @param jedis
	  * @return
	  */
	 public static  List<String> formCountryCity(Jedis jedis){
		 
		 Long area11=jedis.zcount("area", 11, 11);
		 Long area12=jedis.zcount("area", 12, 12);
		 Long area13=jedis.zcount("area", 13, 13);
		 Long area14=jedis.zcount("area", 14, 14);
		 Long area15=jedis.zcount("area", 15, 15);
		 Long area21=jedis.zcount("area", 21, 21);
		 Long area22=jedis.zcount("area", 22, 22);
		 Long area23=jedis.zcount("area", 23, 23);
		 Long area31=jedis.zcount("area", 31, 31);
		 Long area32=jedis.zcount("area", 32, 32);
		 Long area33=jedis.zcount("area", 33, 33);
		 Long area34=jedis.zcount("area", 34, 34);
		 Long area35=jedis.zcount("area", 35, 35);
		 Long area36=jedis.zcount("area", 36, 36);
		 Long area37=jedis.zcount("area", 37, 37);
		 Long area41=jedis.zcount("area", 41, 41);
		 Long area42=jedis.zcount("area", 42, 42);
		 Long area43=jedis.zcount("area", 43, 43);
		 Long area44=jedis.zcount("area", 44, 44);
		 Long area45=jedis.zcount("area", 45, 45);
		 Long area51=jedis.zcount("area", 51, 51);
		 Long area52=jedis.zcount("area", 52, 52);
		 Long area53=jedis.zcount("area", 53, 53);
		 Long area54=jedis.zcount("area", 54, 54);
		 Long area61=jedis.zcount("area", 61, 61);
		 Long area62=jedis.zcount("area", 62, 62);
		 Long area63=jedis.zcount("area", 63, 63);
		 Long area64=jedis.zcount("area", 64, 64);
		 Long area65=jedis.zcount("area", 65, 65);
		 Long area66=jedis.zcount("area", 66, 66);
		 Long area71=jedis.zcount("area", 71, 71);
		 Long area72=jedis.zcount("area", 72, 72);
		 Long area81=jedis.zcount("area", 81, 81);
		 Long area82=jedis.zcount("area", 82, 82);
		 Long area83=jedis.zcount("area", 83, 83);
		 Long area84=jedis.zcount("area", 84, 84);
		 Long area85=jedis.zcount("area", 85, 85);
		 Long area86=jedis.zcount("area", 86, 86);
		 Long area87=jedis.zcount("area", 87, 87);
		 Long area88=jedis.zcount("area", 88, 88);
		 Long area89=jedis.zcount("area", 89, 89);
		 Long area90=jedis.zcount("area", 90, 90);
		 Long area91=jedis.zcount("area", 91, 91);
		 Long area92=jedis.zcount("area", 92, 92);
		 Long area93=jedis.zcount("area", 93, 93);
		 Long area94=jedis.zcount("area", 94, 94);
		 Long area95=jedis.zcount("area", 95, 95);
		 Long area97=jedis.zcount("area", 97, 97);
		 Long areaAD=jedis.zcount("area", 140, 140);
		 Long areaAE=jedis.zcount("area", 150, 150);
		 Long areaAF=jedis.zcount("area", 160, 160);
		 Long areaAG=jedis.zcount("area", 170, 170);
		 Long areaAI=jedis.zcount("area", 190, 190);
		 Long areaAL=jedis.zcount("area", 1120, 1120);
		 Long areaAM=jedis.zcount("area", 1130, 1130);
		 Long areaAN=jedis.zcount("area", 1140, 1140);
		 Long areaAO=jedis.zcount("area", 1150, 1150);
		 Long areaAR=jedis.zcount("area", 1180, 1180);
		 Long areaAT=jedis.zcount("area", 1200, 1200);
		 Long areaAU=jedis.zcount("area", 1210, 1210);
		 Long areaAW=jedis.zcount("area", 1230, 1230);
		 Long areaAZ=jedis.zcount("area", 1260, 1260);
		 
		 Long areaBB=jedis.zcount("area", 220, 220);
		 Long areaBD=jedis.zcount("area", 240, 240);
		 Long areaBE=jedis.zcount("area", 250, 250);
		 Long areaBF=jedis.zcount("area", 260, 260);
		 Long areaBG=jedis.zcount("area", 270, 270);
		 Long areaBH=jedis.zcount("area", 280, 280);
		 Long areaBI=jedis.zcount("area", 290, 290);
		 Long areaBJ=jedis.zcount("area", 2100, 2100);
		 Long areaBM=jedis.zcount("area", 2130, 2130);
		 Long areaBN=jedis.zcount("area", 2140, 2140);
		 Long areaBO=jedis.zcount("area", 2150, 2150);
		 Long areaBR=jedis.zcount("area", 2180, 2180);
		 Long areaBS=jedis.zcount("area", 2190, 2190);
		 Long areaBT=jedis.zcount("area", 2200, 2200);
		 Long areaBU=jedis.zcount("area", 2210, 2210);
		 Long areaBW=jedis.zcount("area", 2230, 2230);
		 Long areaBY=jedis.zcount("area", 2250, 2250);
		 Long areaBZ=jedis.zcount("area", 2260, 2260);
		 
		  List<String> dateList = new ArrayList<String>();
		  
		 
		 return null;
	 }
	 
	 /*
	  * 递归调用查找指定文件加下所有文件
	  */
	 public static  String GetSql(String path, Jedis jedis){
	  File rootDir = new File(path);
      
    
	   if(!rootDir.isDirectory()){
		   if(rootDir.getName().endsWith("xml")){
//			    System.out.println("文件名:"+rootDir.getAbsolutePath());
			   CNDescriptionItemForAnalysis cnDescriptionItem= XMLUtilInputMem.getObjectFormXml(rootDir);
			    
//			    //map   
//		        Map<String,String> patent = new HashMap<String,String>();  
//		        patent.put("appno", cnDescriptionItem.getAppno());  
//		        patent.put("appl", cnDescriptionItem.getAppl());  
//		        patent.put("inventor", cnDescriptionItem.getInventor());  
//		        patent.put("nc", cnDescriptionItem.getNc());  
//		        patent.put("ipcmain", cnDescriptionItem.getIpcMain());  
//		        patent.put("apdtext", cnDescriptionItem.getApdText());  
//		        patent.put("pudtext", cnDescriptionItem.getPudText());  
//		        //map存入redis   
//		        jedis.hmset("patent", patent); 
		        jedis.lpush("apddate",cnDescriptionItem.getApdText());  
		        jedis.lpush("area",cnDescriptionItem.getNc());  
//			    System.out.println("--导入了"+(++total)+"条");
		   }
	   }else{
	    String[] fileList =  rootDir.list();
	    for (int i = 0; i < fileList.length; i++) {
	     path = rootDir.getAbsolutePath()+"\\"+fileList[i];
	     GetSql(path,jedis);      
	      } 
	   }   
	  return null;    
	 }
	 /**
	  * 获得所有分析使用到的数据
	  * @param appnoList
	  * @param jedis
	  * @return
	  */
	 public static  void selectForAnalysis(Users user, List<String> appnoList, Jedis jedis){
		 //System.out.println("appnoList.size:"+appnoList.size());
		 StringBuilder appnoBur=new StringBuilder();
		 StringBuilder apdBur=new StringBuilder();
		 StringBuilder pubBur=new StringBuilder();
		 StringBuilder appdBur=new StringBuilder();
		 StringBuilder ncBur=new StringBuilder();
		 StringBuilder ipcBur=new StringBuilder();
		 StringBuilder applBur=new StringBuilder();
		 StringBuilder inventorBur=new StringBuilder();
		 StringBuilder lawBur=new StringBuilder();
System.out.println(new Date().getTime()+"=------");
		  for(int i=0;i<appnoList.size();i++){
			  
			  String appno=appnoList.get(i);
			  //System.out.println(appno.length());
			  if(appno.length()==12){
				  appno = XMLUtil.getCheckAppnoWithOutDot(appno);
			  }
			  String appnoValue=jedis.get("Jly"+appno);//将分析用到的这些信息进行连接
			  
			  
			  
			  if(appnoValue!=null&&(!"".equals(appnoValue))){
				  appnoValue=appnoValue+"1";
					  if(appnoValue.contains("_")){   //内存数据库中存储的后7相分析数据，用‘_’隔开
						  String[] patentValue=appnoValue.split("_");
						  if(patentValue.length==8){
							  appnoBur.append("_").append(appno);     //横线在前
							  apdBur.append("_").append(patentValue[0]);
							  pubBur.append("_").append(patentValue[1]);
							  appdBur.append("_").append(patentValue[2]);
							  ncBur.append("_").append(patentValue[3]);
							  ipcBur.append("_").append(patentValue[4]);
							  applBur.append("_").append(patentValue[5]);
							  inventorBur.append("_").append(patentValue[6]);
							  if(patentValue[7].length()>0){
								  lawBur.append("_").append(patentValue[7].substring(0, patentValue[7].length()-1));     
							  }else{
								  //todo
								  
								  //lawBur.append("_").append(patentValue[7].substring(0, patentValue[7].length()-1));     
							  }
							  jedis.del(patentValue[3]+user.getId());         //这里的删除操作 是为下面对单个排序
							  jedis.del(patentValue[4]+user.getId());             //这里的删除操作
							  String strSQR=patentValue[5];
							  String strFMR=patentValue[6];
							  if(strSQR.contains(",")){
									 String[] strs=strSQR.split(",");
									 for(int k=0;k<strs.length;k++){
										 jedis.del(strs[k]+user.getId());   //这里的删除操作
									 }
							 }else{
								 jedis.del(strSQR+user.getId());         //这里的删除操作
							 }
							 if(strFMR.contains(",")){
								 String[] strs=strFMR.split(",");
								 for(int k=0;k<strs.length;k++){
									 jedis.del(strs[k]+user.getId());     //这里的删除操作
								 }
							 }else{
								 jedis.del(strFMR+user.getId());         //这里的删除操作
							 }
						  }
					}
			  }	  
		  }
		  if(appnoBur.toString()==null||("".equals(appnoBur.toString()))){
			  jedis.set(user.getId()+"appnoJly", appnoBur.append("Jly").toString());
			  jedis.expire(user.getId()+"appnoJly", 3000);     //设置key过期时间
		  }else{
			  jedis.set(user.getId()+"appnoJly", appnoBur.append("Jly").toString().substring(1));
			  jedis.expire(user.getId()+"appnoJly", 3000);
		  }
		  if(apdBur.toString()==null||("".equals(apdBur.toString()))){
			  jedis.set(user.getId()+"apdJly", apdBur.append("Jly").toString());
			  jedis.expire(user.getId()+"apdJly", 3000);
			  
		  }else{
			  jedis.set(user.getId()+"apdJly", apdBur.append("Jly").toString().substring(1));
			  jedis.expire(user.getId()+"apdJly", 3000);
		  }
		  if(pubBur.toString()==null||("".equals(pubBur.toString()))){
			  jedis.set(user.getId()+"pubJly", pubBur.append("Jly").toString());
			  jedis.expire(user.getId()+"pubJly", 3000);
		  }else{
			  jedis.set(user.getId()+"pubJly", pubBur.append("Jly").toString().substring(1));
			  jedis.expire(user.getId()+"pubJly", 3000);
		  }
		  if(appdBur.toString()==null||("".equals(appdBur.toString()))){
			  jedis.set(user.getId()+"appdJly", appdBur.append("Jly").toString());
			  jedis.expire(user.getId()+"appdJly", 3000);
		  }else{
			  jedis.set(user.getId()+"appdJly", appdBur.append("Jly").toString().substring(1));
			  jedis.expire(user.getId()+"appdJly", 3000);
		  }
		  if(ncBur.toString()==null||("".equals(ncBur.toString()))){
			  jedis.set(user.getId()+"ncJly", ncBur.append("Jly").toString());
			  jedis.expire(user.getId()+"ncJly", 3000);
		  }else{
			  jedis.set(user.getId()+"ncJly", ncBur.append("Jly").toString().substring(1));
			  jedis.expire(user.getId()+"ncJly", 3000);
		  }
		  if(ipcBur.toString()==null||("".equals(ipcBur.toString()))){
			  jedis.set(user.getId()+"ipcJly", ipcBur.append("Jly").toString());
			  jedis.expire(user.getId()+"ipcJly", 3000);
		  }else{
			  jedis.set(user.getId()+"ipcJly", ipcBur.append("Jly").toString().substring(1));
			  jedis.expire(user.getId()+"ipcJly", 3000);
		  }
		  if(applBur.toString()==null||("".equals(applBur.toString()))){
			  jedis.set(user.getId()+"applJly", applBur.append("Jly").toString());
			  jedis.expire(user.getId()+"applJly", 3000);
		  }else{
			  jedis.set(user.getId()+"applJly", applBur.append("Jly").toString().substring(1));
			  jedis.expire(user.getId()+"applJly", 3000);
		  }
		  if(inventorBur.toString()==null||("".equals(inventorBur.toString()))){
			  jedis.set(user.getId()+"inventorJly", inventorBur.append("Jly").toString());
			  jedis.expire(user.getId()+"inventorJly", 3000);
		  }else{
			  jedis.set(user.getId()+"inventorJly", inventorBur.append("Jly").toString().substring(1));
			  jedis.expire(user.getId()+"inventorJly", 3000);
		  }
		  if(lawBur.toString()==null||("".equals(lawBur.toString()))){
			  jedis.set(user.getId()+"lawJly", lawBur.append("Jly").toString());
			  jedis.expire(user.getId()+"lawJly", 3000);
		  }else{
			  jedis.set(user.getId()+"lawJly", lawBur.append("Jly").toString().substring(1));
			  jedis.expire(user.getId()+"lawJly", 3000);
		  }
		  
		  RedisTest.singleAnalysis(user, jedis);
		  }
//System.out.println(new Date().getTime());
	 
	 /**
	  * 单一条件分析预处理，分别得到nc，ipc，appl，inventor单一分析结果
	  * @param appnoList
	  * @param jedis
	  * @return
	  */
	 public static  void singleAnalysis(Users user, Jedis jedis){
		 Map<String, Integer> mapForanalysisNC = new TreeMap<String, Integer>();
		 Map<String, Integer> mapForanalysisIPC = new TreeMap<String, Integer>();
		 Map<String, Integer> mapForanalysisSQR = new TreeMap<String, Integer>();
		 Map<String, Integer> mapForanalysisFMR = new TreeMap<String, Integer>();
		 String[] ncJly=jedis.get(user.getId()+"ncJly").split("_");
		 ncJly[ncJly.length-1]=ncJly[ncJly.length-1].substring(0, ncJly[ncJly.length-1].length()-3);    //删除末尾的Jly
		 String[] ipcJly=jedis.get(user.getId()+"ipcJly").split("_");
		 ipcJly[ipcJly.length-1]=ipcJly[ipcJly.length-1].substring(0, ipcJly[ipcJly.length-1].length()-3);
		 String[] applJly=jedis.get(user.getId()+"applJly").split("_");
		 applJly[applJly.length-1]=applJly[applJly.length-1].substring(0, applJly[applJly.length-1].length()-3);
		 String[] inventorJly=jedis.get(user.getId()+"inventorJly").split("_");
		 inventorJly[inventorJly.length-1]=inventorJly[inventorJly.length-1].substring(0, inventorJly[inventorJly.length-1].length()-3);
		for(int i=0;i<ncJly.length;i++){    //不要判断第一次是否为空，这里直接设置，在tab中点击时在做判断
			String strNC=ncJly[i];
			String strIPC=ipcJly[i];
			String strSQR=applJly[i];
			String strFMR=inventorJly[i];
			if((strNC!=null)&&(!"".equals(strNC))){
				jedis.append(strNC+user.getId(), "1");          //貌似tab中不用判断，还要在这里判断，不然统计没法做
				int ncLength=jedis.strlen(strNC+user.getId()).intValue();
				mapForanalysisNC.put(strNC, ncLength);
			}
			if((strIPC!=null)&&(!"".equals(strIPC))){
				jedis.append(strIPC+user.getId(), "1");          //貌似tab中不用判断，还要在这里判断，不然统计没法做
				int ipcLength=jedis.strlen(strIPC+user.getId()).intValue();
				mapForanalysisIPC.put(strIPC, ipcLength);
			}
			if((strSQR!=null)&&(!"".equals(strSQR))){
				 if(strSQR.contains(",")){
					 String[] strs=strSQR.split(",");
					 for(int k=0;k<strs.length;k++){
						 jedis.append(strs[k]+user.getId(), "1");          //貌似tab中不用判断，还要在这里判断，不然统计没法做
						 int sqrLength=jedis.strlen(strs[k]+user.getId()).intValue();
						 mapForanalysisSQR.put(strs[k], sqrLength);
					 }
				 }else{
					 jedis.append(strSQR+user.getId(), "1");          //貌似tab中不用判断，还要在这里判断，不然统计没法做
					 int sqrLength=jedis.strlen(strSQR+user.getId()).intValue();
					 mapForanalysisSQR.put(strSQR, sqrLength);
				 }
			}
			if((strFMR!=null)&&(!"".equals(strFMR))){
				 if(strFMR.contains(",")){
					 String[] strs=strFMR.split(",");
					 for(int k=0;k<strs.length;k++){
						 jedis.append(strs[k]+user.getId(), "1");          //貌似tab中不用判断，还要在这里判断，不然统计没法做
						 int fmrLength=jedis.strlen(strs[k]+user.getId()).intValue();
						 mapForanalysisFMR.put(strs[k], fmrLength);
					 }
				 }else{
					 jedis.append(strFMR+user.getId(), "1");          //貌似tab中不用判断，还要在这里判断，不然统计没法做
					 int fmrLength=jedis.strlen(strFMR+user.getId()).intValue();
					 mapForanalysisFMR.put(strFMR, fmrLength);
				 }
			}
			
		}
		  List<Map.Entry<String, Integer>> infoIdsNC = new ArrayList<Map.Entry<String, Integer>>(mapForanalysisNC.entrySet()); 
		  infoIdsNC=RedisTest.sortMapByValue(mapForanalysisNC);
		  List<Map.Entry<String, Integer>> infoIdsIPC = new ArrayList<Map.Entry<String, Integer>>(mapForanalysisIPC.entrySet()); 
		  infoIdsIPC=RedisTest.sortMapByValue(mapForanalysisIPC);
		  List<Map.Entry<String, Integer>> infoIdsSQR = new ArrayList<Map.Entry<String, Integer>>(mapForanalysisSQR.entrySet()); 
		  infoIdsSQR=RedisTest.sortMapByValue(mapForanalysisSQR);
		  List<Map.Entry<String, Integer>> infoIdsFMR = new ArrayList<Map.Entry<String, Integer>>(mapForanalysisFMR.entrySet()); 
		  infoIdsFMR=RedisTest.sortMapByValue(mapForanalysisFMR);
		  String temp=null;
		  StringBuilder ncBuilder=new StringBuilder();  
		  for(int i=0;i<infoIdsNC.size();i++){     //得到nc分析结果
			  temp=infoIdsNC.get(i).toString();
			  ncBuilder.append("_").append(temp);
		  }
		  StringBuilder ipcBuilder=new StringBuilder();  
		  for(int i=0;i<infoIdsIPC.size();i++){     //得到ipc分析结果
			  temp=infoIdsIPC.get(i).toString();
			  ipcBuilder.append("_").append(temp);
		  }
		  StringBuilder sqrBuilder=new StringBuilder();  
		  for(int i=0;i<infoIdsSQR.size();i++){     //得到申请人分析结果
			  temp=infoIdsSQR.get(i).toString();
			  sqrBuilder.append("_").append(temp);
		  }
		  StringBuilder fmrBuilder=new StringBuilder();  
		  for(int i=0;i<infoIdsFMR.size();i++){     //得到发明人分析结果
			  temp=infoIdsFMR.get(i).toString();
			  fmrBuilder.append("_").append(temp);
		  }
		  if(ncBuilder.length()>0){
			  jedis.set(user.getId()+"nc",  ncBuilder.substring(1).toString());  
			  jedis.expire(user.getId()+"nc", 3000);
		  }
		  if(ipcBuilder.length()>0){
			  jedis.set(user.getId()+"ipc", ipcBuilder.substring(1).toString());   
			  jedis.expire(user.getId()+"ipc", 3000);
		  }
		  if(sqrBuilder.length()>0){
			  jedis.set(user.getId()+"sqr", sqrBuilder.substring(1).toString());    
			  jedis.expire(user.getId()+"sqr", 3000);
		  }
		  if(fmrBuilder.length()>0){
			  jedis.set(user.getId()+"fmr", fmrBuilder.substring(1).toString());    
			  jedis.expire(user.getId()+"fmr", 3000);
		  }
	 }
	 /**
	  * main函数
	  * @param args
	  */
	   public static void main(String[] args) {  
//		   Jedis jedis = new Jedis("11.0.0.26",6379);  
//		   getXYData(jedis);
		   String appnoValue="aaaaa___ss_ddddd";
		   appnoValue=appnoValue.replace("_", ""); 
		   //System.out.println(appnoValue);
//		   Jedis jedis = new Jedis("11.0.0.26", Integer.parseInt(ConfigTool.getValue("redisport")));
//		   for(int i=0;i<100;i++){
//			   if(i%8!=0){
//				   System.out.println(i);
//			   }
//		   }
//		   Map<String, Integer> map = new TreeMap<String, Integer>();
//		   map.put("j2se", 20); 
//		   map.put("j2ee", 10); 
//		   map.put("j2me", 30);
//		   map.put("j2me", 40);
//		   List<Map.Entry<String, Integer>> infoIds = new ArrayList<Map.Entry<String, Integer>>(map.entrySet()); 
//		   infoIds= sortMapByValue(map);
//		    //排序后 
//			   for (int i = 0; i < infoIds.size(); i++) { 
//			   String temp = infoIds.get(i).toString(); 
//			   System.out.println(temp); 
//				System.out.println(temp.substring(0, temp.indexOf("=")));
//				System.out.println(temp.substring(temp.indexOf("=")+1, temp.length()));
//			   } 
	   }
		  
			   
			   
	   /**
	    * 
	    * @return
	    */
	   public static   List<String> getXYData(Jedis jedis) {  
		   Date date=new Date();
		   String[] strForAnalysis= jedis.get("28").split("_");
			 Map<String, Integer> mapForanalysis = new TreeMap<String, Integer>();
			for(int i=0;i<strForAnalysis.length/8;i++){
				if(strForAnalysis[6+i*8]!=null&&!"".equals(strForAnalysis[6+i*8])){
					jedis.append(strForAnalysis[6+i*8], "1");
					int strLength=jedis.strlen(strForAnalysis[6+i*8]).intValue();
					mapForanalysis.put(strForAnalysis[6+i*8], strLength);
				}
			}
			  List<Map.Entry<String, Integer>> infoIds = new ArrayList<Map.Entry<String, Integer>>(mapForanalysis.entrySet()); 
			  infoIds=RedisTest.sortMapByValue(mapForanalysis);
			  String[] strBurArrary=new String[10];
			  String[] categorieArrary=new String[10];
			  String temp=null;
			 int flag=10;
			for(int i=0;i<flag;i++){
				temp=infoIds.get(i).toString();
				//System.out.println(temp);///////////////////
				if(temp!=null&&!"".equals(temp)){
					categorieArrary[i]=temp.substring(0, temp.indexOf("="));
					//System.out.println(categorieArrary[i]);
					strBurArrary[i]=temp.substring(temp.indexOf("=")+1, temp.length());
					//System.out.println(strBurArrary[i]);
				}else{
					//System.out.println("flag++");
					flag++;
				}
			}
			Date date1=new Date();
			//System.out.println(date+"--->"+date1);
		   return null;
	   }
	   /**
	    * 
	    * @param map
	    * @return
	    */
	   public static  List<Map.Entry<String, Integer>> sortMapByValue( Map<String, Integer> map) {  
		   List<Map.Entry<String, Integer>> infoIds = new ArrayList<Map.Entry<String, Integer>>( 
				   map.entrySet()); 
				   //排序 
				   Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() { 
				   public int compare(Map.Entry<String, Integer> o1, 
				   Map.Entry<String, Integer> o2) { 
				   return (o2.getValue() - o1.getValue()); 
				   } 
				   }); 
				   return  infoIds;
	   }
	 /**
	  * 
	  * 
	  * 返回国省名称
	  */
	   public static String getAddrNameByCode(String code) {
			String AddrName=new String("");	    
			if(code.equals("11")){
				AddrName = "中国北京";
		    }
			else if(code.equals("12")){
				AddrName = "中国天津";
		    }
			else if(code.equals("13")){
				AddrName = "中国河北";
		    }
			else if(code.equals("14")){
				AddrName = "中国山西";
		    }
			else if(code.equals("15")){
				AddrName = "中国内蒙";
		    }
			else if(code.equals("21")){
				AddrName = "中国辽宁";
		    }
			else if(code.equals("22")){
				AddrName = "中国吉林";
		    }
			else if(code.equals("23")){
				AddrName = "中国黑龙江";
		    }
			else if(code.equals("31")){
				AddrName = "中国上海";
		    }
			else if(code.equals("32")){
				AddrName = "中国江苏";
		    }
			else if(code.equals("33")){
				AddrName = "中国浙江";
		    }
			else if(code.equals("34")){
				AddrName = "中国安徽";
		    }
			else if(code.equals("35")){
				AddrName = "中国福建";
		    }
			else if(code.equals("36")){
				AddrName = "中国江西";
		    }
			else if(code.equals("37")){
				AddrName = "中国山东";
		    }
			else if(code.equals("41")){
				AddrName = "中国河南";
		    }
			else if(code.equals("42")){
				AddrName = "中国湖北";
		    }
			else if(code.equals("43")){
				AddrName = "中国湖南";
		    }
			else if(code.equals("44")){
				AddrName = "中国广东";
		    }
			else if(code.equals("45")){
				AddrName = "中国广西";
		    }
			else if(code.equals("51")){
				AddrName = "中国四川";
		    }
			else if(code.equals("52")){
				AddrName = "中国贵州";
		    }
			else if(code.equals("53")){
				AddrName = "中国云南";
		    }
			else if(code.equals("54")){
				AddrName = "中国西藏";
		    }
			else if(code.equals("61")){
				AddrName = "中国陕西";
		    }		
			if(code.equals("62")){
				AddrName = "中国甘肃";
		    }
			else if(code.equals("63")){
				AddrName = "中国青海";
		    }
			else if(code.equals("64")){
				AddrName = "中国宁夏";
		    }
			else if(code.equals("65")){
				AddrName = "中国新疆";
		    }
			else if(code.equals("66")){
				AddrName = "中国海南";
		    }
			else if(code.equals("71")){
				AddrName = "中国台湾";
		    }
			else if(code.equals("81")){
				AddrName = "中国广州";
		    }
			else if(code.equals("83")){
				AddrName = "中国武汉";
		    }
			else if(code.equals("85")){
				AddrName = "中国重庆";
		    }
			else if(code.equals("87")){
				AddrName = "中国西安";
		    }
			else if(code.equals("89")){
				AddrName = "中国沈阳";
		    }
			else if(code.equals("91")){
				AddrName = "中国大连";
		    }		
			else if(code.equals("93")){
				AddrName = "中国哈尔滨";
		    }
			else if(code.equals("95")){
				AddrName = "中国青岛";
		    }
			else if(code.equals("HK")){
				AddrName = "中国香港";
		    }
			else if(code.equals("AD")){
				AddrName = "安道尔";
		    }
			else if(code.equals("AE")){
				AddrName = "阿拉伯联合酋长国";
		    }
			else if(code.equals("AF")){
				AddrName = "阿富汗";
		    }
			else if(code.equals("AG")){
				AddrName = "安提瓜和巴布达";
		    }
			else if(code.equals("AI")){
				AddrName = "安圭拉";
		    }
			else if(code.equals("AL")){
				AddrName = "阿尔巳尼亚";
		    }
			else if(code.equals("AM")){
				AddrName = "亚美尼亚";
		    }
			else if(code.equals("AN")){
				AddrName = "菏属安的列斯群岛";
		    }
			else if(code.equals("AO")){
				AddrName = "安哥拉";
		    }		
			else if(code.equals("AR")){
				AddrName = "阿根廷";
		    }
			else if(code.equals("AT")){
				AddrName = "奥地利";
		    }
			else if(code.equals("AU")){
				AddrName = "澳大利亚";
		    }
			else if(code.equals("AW")){
				AddrName = "阿鲁巴";
		    }
			else if(code.equals("AZ")){
				AddrName = "阿塞拜疆";
		    }
			else if(code.equals("BB")){
				AddrName = "巴巴多斯";
		    }
			else if(code.equals("BD")){
				AddrName = "孟加拉国";
		    }
			else if(code.equals("BE")){
				AddrName = "比利时";
		    }
			else if(code.equals("BF")){
				AddrName = "布莱基纳法索";
		    }
			else if(code.equals("BG")){
				AddrName = "保加利亚";
		    }
			else if(code.equals("BH")){
				AddrName = "巴林";
		    }
			else if(code.equals("BI")){
				AddrName = "布隆迪";
		    }
			else if(code.equals("BJ")){
				AddrName = "贝宁";
		    }
			else if(code.equals("BM")){
				AddrName = "百慕大";
		    }
			else if(code.equals("BN")){
				AddrName = "文莱";
		    }
			else if(code.equals("BO")){
				AddrName = "玻利维亚";
		    }
			else if(code.equals("BR")){
				AddrName = "巴西";
		    }
			else if(code.equals("BS")){
				AddrName = "巴哈马";
		    }
			else if(code.equals("BT")){
				AddrName = "不丹";
		    }
			else if(code.equals("BU")){
				AddrName = "缅甸";
		    }
			else if(code.equals("BW")){
				AddrName = "博茨瓦纳";
		    }
			else if(code.equals("BY")){
				AddrName = "白俄罗斯";
		    }
			else if(code.equals("BZ")){
				AddrName = "伯利兹";
		    }
			else if(code.equals("CA")){
				AddrName = "加拿大";
		    }		
			else if(code.equals("CF")){
				AddrName = "中非共和国";
		    }
			else if(code.equals("CG")){
				AddrName = "刚果";
		    }
			else if(code.equals("CH")){
				AddrName = "瑞士";
		    }
			else if(code.equals("CI")){
				AddrName = "科特迪瓦";
		    }
			else if(code.equals("CL")){
				AddrName = "智利";
		    }
			else if(code.equals("CM")){
				AddrName = "喀麦隆";
		    }
			else if(code.equals("CN")){
				AddrName = "中国";
		    }
			else if(code.equals("CO")){
				AddrName = "哥伦比亚";
		    }
			else if(code.equals("CR")){
				AddrName = "哥斯达黎加";
		    }
			else if(code.equals("CS")){
				AddrName = "捷克斯洛伐克";
		    }
			else if(code.equals("CU")){
				AddrName = "古巴";
		    }
			else if(code.equals("CV")){
				AddrName = "怫得角";
		    }
			else if(code.equals("CY")){
				AddrName = "塞浦路斯";
		    }
			else if(code.equals("DE")){
				AddrName = "联邦德国";
		    }
			else if(code.equals("DJ")){
				AddrName = "吉布提";
		    }
			else if(code.equals("DK")){
				AddrName = "丹麦";
		    }
			else if(code.equals("DM")){
				AddrName = "多米尼加岛";
		    }
			else if(code.equals("DO")){
				AddrName = "多米尼加共和国";
		    }
			else if(code.equals("DZ")){
				AddrName = "阿尔及利亚";
		    }
			else if(code.equals("EC")){
				AddrName = "厄瓜多尔";
		    }
			else if(code.equals("EE")){
				AddrName = "爱沙尼亚";
		    }
			else if(code.equals("EG")){
				AddrName = "埃及";
		    }
			else if(code.equals("EP")){
				AddrName = "欧洲专利局";
		    }
			else if(code.equals("ES")){
				AddrName = "西班牙";
		    }		
			else if(code.equals("ET")){
				AddrName = "埃塞俄比亚";
		    }
			else if(code.equals("FI")){
				AddrName = "芬兰";
		    }
			else if(code.equals("FJ")){
				AddrName = "斐济";
		    }
			else if(code.equals("FK")){
				AddrName = "马尔维纳斯群岛";
		    }
			else if(code.equals("FR")){
				AddrName = "法国";
		    }
			else if(code.equals("GA")){
				AddrName = "加蓬";
		    }
			else if(code.equals("GB")){
				AddrName = "英国";
		    }
			else if(code.equals("GD")){
				AddrName = "格林纳达";
		    }
			else if(code.equals("GE")){
				AddrName = "格鲁吉亚";
		    }
			else if(code.equals("GH")){
				AddrName = "加纳";
		    }
			else if(code.equals("GI")){
				AddrName = "直布罗陀";
		    }
			else if(code.equals("GM")){
				AddrName = "冈比亚";
		    }
			else if(code.equals("GN")){
				AddrName = "几内亚";
		    }
			else if(code.equals("GQ")){
				AddrName = "赤道几内亚";
		    }
			else if(code.equals("GR")){
				AddrName = "希腊";
		    }
			else if(code.equals("GT")){
				AddrName = "危地马拉";
		    }
			else if(code.equals("GW")){
				AddrName = "几内亚比绍";
		    }
			else if(code.equals("GY")){
				AddrName = "圭亚那";
		    }
			else if(code.equals("HK")){
				AddrName = "香港";
		    }
			else if(code.equals("HN")){
				AddrName = "洪都拉斯";
		    }
			else if(code.equals("HR")){
				AddrName = "克罗地亚";
		    }
			else if(code.equals("HT")){
				AddrName = "海地";
		    }
			else if(code.equals("HU")){
				AddrName = "匈牙利";
		    }
			else if(code.equals("HV")){
				AddrName = "上沃尔特";
		    }		
			else if(code.equals("ID")){
				AddrName = "印度尼西亚";
		    }
			else if(code.equals("IE")){
				AddrName = "爱尔兰";
		    }
			else if(code.equals("IL")){
				AddrName = "以色列";
		    }
			else if(code.equals("IN")){
				AddrName = "印度";
		    }
			else if(code.equals("IQ")){
				AddrName = "伊拉克";
		    }
			else if(code.equals("IR")){
				AddrName = "伊朗";
		    }
			else if(code.equals("IS")){
				AddrName = "冰岛";
		    }
			else if(code.equals("IT")){
				AddrName = "意大利";
		    }
			else if(code.equals("JE")){
				AddrName = "泽西岛";
		    }
			else if(code.equals("JM")){
				AddrName = "牙买加";
		    }
			else if(code.equals("JO")){
				AddrName = "约旦";
		    }
			else if(code.equals("JP")){
				AddrName = "日本";
		    }
			else if(code.equals("KE")){
				AddrName = "肯尼亚";
		    }
			else if(code.equals("KG")){
				AddrName = "吉尔吉斯";
		    }
			else if(code.equals("KH")){
				AddrName = "柬埔寨";
		    }
			else if(code.equals("KI")){
				AddrName = "吉尔伯特群岛";
		    }
			else if(code.equals("KM")){
				AddrName = "科摩罗";
		    }
			else if(code.equals("KN")){
				AddrName = "圣克里斯托弗岛";
		    }
			else if(code.equals("KP")){
				AddrName = "朝鲜";
		    }
			else if(code.equals("KR")){
				AddrName = "韩国";
		    }
			else if(code.equals("KW")){
				AddrName = "科威特";
		    }
			else if(code.equals("KY")){
				AddrName = "开曼群岛";
		    }
			else if(code.equals("KZ")){
				AddrName = "哈萨克";
		    }
			else if(code.equals("LA")){
				AddrName = "老挝";
		    }		
			else if(code.equals("LB")){
				AddrName = "黎巴嫩";
		    }
			else if(code.equals("LC")){
				AddrName = "圣卢西亚岛";
		    }
			else if(code.equals("LI")){
				AddrName = "列支敦士登";
		    }
			else if(code.equals("LK")){
				AddrName = "斯里兰卡";
		    }
			else if(code.equals("LR")){
				AddrName = "利比里亚";
		    }
			else if(code.equals("LS")){
				AddrName = "莱索托";
		    }
			else if(code.equals("LT")){
				AddrName = "立陶宛";
		    }
			else if(code.equals("LU")){
				AddrName = "卢森堡";
		    }
			else if(code.equals("LV")){
				AddrName = "拉脱维亚";
		    }
			else if(code.equals("LY")){
				AddrName = "利比亚";
		    }
			else if(code.equals("MA")){
				AddrName = "摩洛哥";
		    }
			else if(code.equals("MC")){
				AddrName = "摩纳哥";
		    }
			else if(code.equals("MD")){
				AddrName = "莫尔多瓦";
		    }
			else if(code.equals("MG")){
				AddrName = "马达加斯加";
		    }
			else if(code.equals("ML")){
				AddrName = "马里";
		    }
			else if(code.equals("MN")){
				AddrName = "蒙古";
		    }
			else if(code.equals("MO")){
				AddrName = "澳门";
		    }
			else if(code.equals("MR")){
				AddrName = "毛里塔尼亚";
		    }
			else if(code.equals("MS")){
				AddrName = "蒙特塞拉特岛";
		    }
			else if(code.equals("MT")){
				AddrName = "马耳他";
		    }
			else if(code.equals("MU")){
				AddrName = "毛里求斯";
		    }
			else if(code.equals("MV")){
				AddrName = "马尔代夫";
		    }
			else if(code.equals("MW")){
				AddrName = "马拉维";
		    }
			else if(code.equals("MX")){
				AddrName = "墨西哥";
		    }		
			else if(code.equals("MY")){
				AddrName = "马来西亚";
		    }
			else if(code.equals("MZ")){
				AddrName = "莫桑比克";
		    }
			else if(code.equals("NA")){
				AddrName = "纳米比亚";
		    }
			else if(code.equals("NE")){
				AddrName = "尼日尔";
		    }
			else if(code.equals("NG")){
				AddrName = "尼日利亚";
		    }
			else if(code.equals("NH")){
				AddrName = "新赫布里底";
		    }
			else if(code.equals("NI")){
				AddrName = "尼加拉瓜";
		    }
			else if(code.equals("NL")){
				AddrName = "荷兰";
		    }
			else if(code.equals("NO")){
				AddrName = "挪威";
		    }
			else if(code.equals("NP")){
				AddrName = "尼泊尔";
		    }
			else if(code.equals("NR")){
				AddrName = "瑙鲁";
		    }
			else if(code.equals("NZ")){
				AddrName = "新西兰";
		    }
			else if(code.equals("OA")){
				AddrName = "非洲知识产权组织";
		    }
			else if(code.equals("OM")){
				AddrName = "阿曼";
		    }
			else if(code.equals("PA")){
				AddrName = "巴拿马";
		    }
			else if(code.equals("PE")){
				AddrName = "秘鲁";
		    }
			else if(code.equals("PG")){
				AddrName = "巴布亚新几内亚";
		    }
			else if(code.equals("PH")){
				AddrName = "菲律宾";
		    }
			else if(code.equals("PK")){
				AddrName = "巴基斯坦";
		    }
			else if(code.equals("PL")){
				AddrName = "波兰";
		    }
			else if(code.equals("PT")){
				AddrName = "葡萄牙";
		    }
			else if(code.equals("PY")){
				AddrName = "巴拉圭";
		    }
			else if(code.equals("QA")){
				AddrName = "卡塔尔";
		    }
			else if(code.equals("RO")){
				AddrName = "罗马尼亚";
		    }		
			else if(code.equals("RU")){
				AddrName = "俄罗斯联邦";
		    }
			else if(code.equals("RW")){
				AddrName = "卢旺达";
		    }
			else if(code.equals("SA")){
				AddrName = "沙特阿拉伯";
		    }
			else if(code.equals("SB")){
				AddrName = "所罗门群岛";
		    }
			else if(code.equals("SC")){
				AddrName = "塞舌尔";
		    }
			else if(code.equals("SD")){
				AddrName = "苏丹";
		    }
			else if(code.equals("SE")){
				AddrName = "瑞典";
		    }
			else if(code.equals("SG")){
				AddrName = "新加坡";
		    }
			else if(code.equals("SH")){
				AddrName = "圣赫勒拿岛";
		    }
			else if(code.equals("SI")){
				AddrName = "斯洛文尼亚";
		    }
			else if(code.equals("SL")){
				AddrName = "塞拉利昂";
		    }
			else if(code.equals("SM")){
				AddrName = "圣马力诺";
		    }
			else if(code.equals("SN")){
				AddrName = "塞内加尔";
		    }
			else if(code.equals("SO")){
				AddrName = "索马里";
		    }
			else if(code.equals("SR")){
				AddrName = "苏里南";
		    }
			else if(code.equals("ST")){
				AddrName = "圣多美和普林西比岛";
		    }
			else if(code.equals("SU")){
				AddrName = "苏联";
		    }
			else if(code.equals("SV")){
				AddrName = "萨尔瓦多";
		    }
			else if(code.equals("SY")){
				AddrName = "叙利亚";
		    }
			else if(code.equals("SZ")){
				AddrName = "斯威士兰";
		    }
			else if(code.equals("TD")){
				AddrName = "乍得";
		    }
			else if(code.equals("TG")){
				AddrName = "多哥";
		    }
			else if(code.equals("TH")){
				AddrName = "泰国";
		    }
			else if(code.equals("TJ")){
				AddrName = "塔吉克";
		    }		
			else if(code.equals("TT")){
				AddrName = "特立尼达和多巴哥";
		    }
			else if(code.equals("TV")){
				AddrName = "图瓦卢";
		    }
			else if(code.equals("TZ")){
				AddrName = "坦桑尼亚";
		    }
			else if(code.equals("UA")){
				AddrName = "乌克兰";
		    }
			else if(code.equals("UG")){
				AddrName = "乌干达";
		    }
			else if(code.equals("US")){
				AddrName = "美国";
		    }
			else if(code.equals("UY")){
				AddrName = "乌拉圭";
		    }
			else if(code.equals("UZ")){
				AddrName = "乌兹别克";
		    }
			else if(code.equals("VA")){
				AddrName = "梵蒂冈";
		    }
			else if(code.equals("VC")){
				AddrName = "圣文森特岛和格林纳达";
		    }
			else if(code.equals("VE")){
				AddrName = "委内瑞拉";
		    }
			else if(code.equals("VG")){
				AddrName = "维尔京群岛";
		    }
			else if(code.equals("VN")){
				AddrName = "越南";
		    }
			else if(code.equals("VU")){
				AddrName = "瓦努阿图";
		    }
			else if(code.equals("WO")){
				AddrName = "世界知识产权组织";
		    }
			else if(code.equals("WS")){
				AddrName = "萨摩亚";
		    }
			else if(code.equals("YD")){
				AddrName = "民主也门";
		    }
			else if(code.equals("YE")){
				AddrName = "也门";
		    }
			else if(code.equals("YU")){
				AddrName = "南斯拉夫";
		    }
			else if(code.equals("ZA")){
				AddrName = "南非";
		    }
			else if(code.equals("ZM")){
				AddrName = "赞比亚";
		    }
			else if(code.equals("ZR")){
				AddrName = "扎伊尔";
		    }
			else if(code.equals("ZW")){
				AddrName = "津巴布韦";
		    }
			else if(code.equals("97")){
				AddrName = "中国宁波";
		    }		
			else if(code.equals("82")){
				AddrName = "中国长春";
		    }
			else if(code.equals("84")){
				AddrName = "中国南京";
		    }
			else if(code.equals("86")){
				AddrName = "中国杭州";
		    }
			else if(code.equals("88")){
				AddrName = "中国济南";
		    }
			else if(code.equals("90")){
				AddrName = "中国成都";
		    }
			else if(code.equals("92")){
				AddrName = "中国厦门";
		    }
			else if(code.equals("94")){
				AddrName = "中国深圳";
		    }
			else if(code.equals("72")){
				AddrName = "中国香港";
		    }
			return AddrName;
		}
	 
}  