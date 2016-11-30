package patentsearch.utils.base.jedis;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XMLUtil {
	
	private static int  errorNum=0;
	
	/**
	 * 替换掉所有非法字符
	 * @param str
	 * @return
	 */
	public static String stripNonValidXMLChars(String str) {
		  if (str == null || "".equals(str)) {
		    return str;
		  }
		  return str.replaceAll("[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]", "");
		}
	/*
	 * 中国专利申请号格式化转化成统一12位规则
	 */

	public static String formatAppno(String appno) {
		String formatAppno = null;
		if (appno != null && !"".equals(appno)) {
			if (appno.length() == 12) {
				formatAppno = appno;
			} else if (appno.length() == 8) {
				if (appno.startsWith("0")) {

					formatAppno = "20" + appno.substring(0, 3) + "00"
							+ appno.substring(appno.length() - 5);
				} else {
					formatAppno = "19" + appno.substring(0, 3) + "00"
							+ appno.substring(appno.length() - 5);
				}
			}
		}

		return formatAppno;
	}
	
	public static String replaceCh(String str){
		return str.replace("[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]","");
	}
	/*
	 * 根据著录项目XML文件返回中国专利著录项目数据对象
	 */

	public static CNDescriptionItem getObjectFormXml(File file) {
		String temp="";
		
		if (file != null) {
//			 String fileToString="";
//			FileInputStream in=null;
//	
//			fileToString=stripNonValidXMLChars(fileToString);
			CNDescriptionItem cnDescriptionItem = new CNDescriptionItem();
			SAXReader saxReader = new SAXReader();
//			Document document=null;
//			try {
//				document = saxReader.read(file);
//			} catch (DocumentException e2) {
//				// TODO Auto-generated catch block
//				e2.printStackTrace();
//			}
//			String encodingStr= document.getXMLEncoding().trim();
//			System.out.println(encodingStr);
//			 try {
//				  in=new FileInputStream(file);
//				  // size  为字串的长度 ，这里一次性读完
//			        int size;
//					try {
//						size = in.available();
//						   byte[] buffer=new byte[size];
//					        in.read(buffer);
//					        in.close();
//					        fileToString=new String(buffer);
////					        fileToString=new String(buffer,"UTF-8");
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//			     
//			} catch (FileNotFoundException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			try {
//				 document =  DocumentHelper.parseText(fileToString);
				InputStream is = new FileInputStream(file);
				BufferedReader reader = new BufferedReader(new InputStreamReader(is,"gb2312"));
				StringBuilder sb = new StringBuilder();
				String line = null;
				
				try{
					while((line=reader.readLine())!=null){
						sb.append(line.trim());
					}
				}catch(IOException e){
					e.printStackTrace();
				}finally{
					try{
						is.close();
					}catch(IOException e){
						e.printStackTrace();
					}
				}
				
				temp = sb.toString();
				temp = replaceCh(temp);
				//System.out.println(temp);
				is = new ByteArrayInputStream(temp.getBytes("gb2312"));
				
				Document document = saxReader.read(is);
				Element rootElement = document.getRootElement();
				Iterator elementIterator = rootElement.elements().iterator();
				Boolean isMainIPC = true;
				while (elementIterator.hasNext()) {

					Element element = (Element) elementIterator.next();
					if (element.getName().equals("APNNO")) {
						cnDescriptionItem.setAppno(element.getText().trim());

					}else if (element.getName().equals("APD")) {
						if(element.getText().trim()!=null&&!"".equals(element.getText().trim())){
							cnDescriptionItem.setApdText(element.getText().trim().substring(0,4));
						}else {
							cnDescriptionItem.setApdText("");
						}
					} else if (element.getName().equals("PUD")) {
						if(element.getText().trim()!=null&&!"".equals(element.getText().trim())){
							cnDescriptionItem.setPudText(element.getText().trim().substring(0,4));
						}else {
							cnDescriptionItem.setPudText("");
						}
					} else if (element.getName().equals("APPD")) {
						if(element.getText().trim()!=null&&!"".equals(element.getText().trim())){
							cnDescriptionItem.setAppdText(element.getText().trim().substring(0,4));	
						}else {
							cnDescriptionItem.setAppdText("");
						}
					} else if (element.getName().equals("NC")) {
						cnDescriptionItem.setNc(element.getText().trim() == null ? ""
								: element.getText().trim() );
					} else if (element.getName().equals("IPC")) {
						// IPC
//						if (isMainIPC) {
//							cnDescriptionItem.setIpcMain(element.getText().trim().substring(0, element.getText().trim().indexOf(' ')));
//							isMainIPC = false;
//						}else {
						if(element.getText().trim().length()<=4){
							cnDescriptionItem.setIpcMain(element.getText().trim());
						}else{
							cnDescriptionItem.setIpcMain(element.getText().trim().substring(0, 4));
						}
//						}
					} else if (element.getName().equals("APPL")) {
						cnDescriptionItem.addAppl(element.getText().trim());

					} else if (element.getName().equals("INVENTOR")) {
						// INVENTOR
						cnDescriptionItem.addInventor(element.getText().trim());
					}
				}
				return cnDescriptionItem;
			} catch (DocumentException e) {
				if(e.getMessage().indexOf("UTF-8")!=-1){
					return getObjectFormXmlUtf(file);
				}
				else{
					e.printStackTrace();
					errorNum++;
					System.out.println("errorNum:"+errorNum);
					return null;
				}
				//System.out.println(e.getMessage());
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return null;

	}
	
	public static CNDescriptionItem getObjectFormXmlUtf(File file){
String temp="";
		
		if (file != null) {
//			 String fileToString="";
//			FileInputStream in=null;
//	
//			fileToString=stripNonValidXMLChars(fileToString);
			CNDescriptionItem cnDescriptionItem = new CNDescriptionItem();
			SAXReader saxReader = new SAXReader();
//			Document document=null;
//			try {
//				document = saxReader.read(file);
//			} catch (DocumentException e2) {
//				// TODO Auto-generated catch block
//				e2.printStackTrace();
//			}
//			String encodingStr= document.getXMLEncoding().trim();
//			System.out.println(encodingStr);
//			 try {
//				  in=new FileInputStream(file);
//				  // size  为字串的长度 ，这里一次性读完
//			        int size;
//					try {
//						size = in.available();
//						   byte[] buffer=new byte[size];
//					        in.read(buffer);
//					        in.close();
//					        fileToString=new String(buffer);
////					        fileToString=new String(buffer,"UTF-8");
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//			     
//			} catch (FileNotFoundException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			try {
//				 document =  DocumentHelper.parseText(fileToString);
				InputStream is = new FileInputStream(file);
				BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
				StringBuilder sb = new StringBuilder();
				String line = null;
				
				try{
					while((line=reader.readLine())!=null){
						sb.append(line.trim());
					}
				}catch(IOException e){
					e.printStackTrace();
				}finally{
					try{
						is.close();
					}catch(IOException e){
						e.printStackTrace();
					}
				}
				
				temp = sb.toString();
				temp = replaceCh(temp);
				//System.out.println(temp);
				is = new ByteArrayInputStream(temp.getBytes("utf-8"));
				
				Document document = saxReader.read(is);
				Element rootElement = document.getRootElement();
				Iterator elementIterator = rootElement.elements().iterator();
				Boolean isMainIPC = true;
				while (elementIterator.hasNext()) {

					Element element = (Element) elementIterator.next();
					if (element.getName().equals("APNNO")) {
						cnDescriptionItem.setAppno(element.getText().trim());

					}else if (element.getName().equals("APD")) {
						if(element.getText().trim()!=null&&!"".equals(element.getText().trim())){
							cnDescriptionItem.setApdText(element.getText().trim().substring(0,4));
						}else {
							cnDescriptionItem.setApdText("");
						}
					} else if (element.getName().equals("PUD")) {
						if(element.getText().trim()!=null&&!"".equals(element.getText().trim())){
							cnDescriptionItem.setPudText(element.getText().trim().substring(0,4));
						}else {
							cnDescriptionItem.setPudText("");
						}
					} else if (element.getName().equals("APPD")) {
						if(element.getText().trim()!=null&&!"".equals(element.getText().trim())){
							cnDescriptionItem.setAppdText(element.getText().trim().substring(0,4));	
						}else {
							cnDescriptionItem.setAppdText("");
						}
					} else if (element.getName().equals("NC")) {
						cnDescriptionItem.setNc(element.getText().trim() == null ? ""
								: element.getText().trim() );
					} else if (element.getName().equals("IPC")) {
						// IPC
//						if (isMainIPC) {
//							cnDescriptionItem.setIpcMain(element.getText().trim().substring(0, element.getText().trim().indexOf(' ')));
//							isMainIPC = false;
//						}else {
						if(element.getText().trim().length()<=4){
							cnDescriptionItem.setIpcMain(element.getText().trim());
						}else{
							cnDescriptionItem.setIpcMain(element.getText().trim().substring(0, 4));
						}
//						}
					} else if (element.getName().equals("APPL")) {
						cnDescriptionItem.addAppl(element.getText().trim());

					} else if (element.getName().equals("INVENTOR")) {
						// INVENTOR
						cnDescriptionItem.addInventor(element.getText().trim());
					}
				}
				return cnDescriptionItem;
			} catch (DocumentException e) {
				System.out.println(file.getAbsolutePath() + "没有这个著录项文件");
				System.out.println(file.getName() + "没有这个著录荐文件");
				System.out.println(temp);
				errorNum++;
				System.out.println("error num : "+ errorNum);
				e.printStackTrace();
				return null;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return null;
	}

	public static void main(String args[]) {
//		 File file=new File("E:\\1\\2\\1.xml");
		 File file=new File("Z:\\cn_item\\CN_XML\\1985\\10003\\198510003938.xml");
		 CNDescriptionItem cnDescriptionItem= XMLUtil.getObjectFormXml(file);
		 
		 
		System.out.println("--------------------");
		String ss=cnDescriptionItem.getIpcMain();
		String nc=cnDescriptionItem.getNc();
//		ss=ss.substring(0, cnDescriptionItem.getIpcMain().indexOf(' '));
		System.out.println(ss+"---ipc");
		System.out.println(nc+"---nc");
		System.out.println(cnDescriptionItem.getAppno()+"---appno");
		System.out.println(cnDescriptionItem.getApdText());
		System.out.println(cnDescriptionItem.getPudText());
		/*
		 * XMLUtil xmlUtil=new XMLUtil();
		 * System.out.println("201220502908:"+xmlUtil
		 * .formatAppno("201220502908"));
		 * System.out.println("03152428:"+xmlUtil.formatAppno("03152428"));
		 * System.out.println("90104119:"+xmlUtil.formatAppno("90104119"));
		 * getFileByAppno("201220502908");
		 */
		// System.out.println(getFuTuByAppno("201220502908"));
		// System.out.println(getCNDescriptionItemByAppno("03152428"));
		// System.out.println(getCNDescriptionItemByAppno("90104119"));
//		System.out.println(XMLUtil.getFileByPubnr("us0000000us3456789","us3456789"));
		// System.out.println(XMLUtil.getCheckAppnoWithDot("2013300423962"));
		// File file=new File("D:\\EP2000200A1.xml");
		// File file=new File("D:\\SK500792011U1.xml");
//		 File file=new File("Z:\\En_Xml\\WO\\00WO\\2013\\0616\\85A1\\WO2013061685A1.xml");
//		 String xmlString=WebServiceClientUtil.getPatentDataByAppno("UA66116U", 6);
//			System.out.println(xmlString);
			
//		 File file=new File("Z:\\En_Xml\\AM\\0000\\0000\\00AM\\170U\\AM170U.xml");
//			EnDescriptionItem enDescriptionItem= XMLUtil.getObjectFormEnXml(xmlString);
//			System.out.println(enDescriptionItem);
//			System.out.println(enDescriptionItem.getTitle());
	}
}
