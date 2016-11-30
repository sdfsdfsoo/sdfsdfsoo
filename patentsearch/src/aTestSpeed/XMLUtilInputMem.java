package aTestSpeed;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import patentsearch.bean.util.xml.DateUtil;
import patentsearch.utils.base.ConfigTool;

public class XMLUtilInputMem {
	
	/*
	 * 根据中国专利申请号返回中国专利著录XML文件
	 */

	public static File getFileByAppno(String appno) {
		// String
		// xmlPath="F:\\第一次来的数据\\CN_XML"+"\\"+appno.substring(0,4)+"\\"+appno.substring(4,9)+"\\"+appno+".xml";
		if (appno != null && !"".equals(appno)) {
			String xmlPath = ConfigTool.getValue("cnxml_pre")
					+ appno.substring(0, 4) + "\\" + appno.substring(4, 9)
					+ "\\" + appno + ".xml";
			//System.out.println("xmlpath:" + xmlPath);
			File file = new File(xmlPath);
			if (file.exists()) {
				return file;
			}
		}
		return null;

	}
	
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
			if (appno.length() == 13) {
				formatAppno = appno.substring(0, appno.length()-1);
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
	/*
	 * 根据著录项目XML文件返回中国专利著录项目数据对象
	 */

	public static CNDescriptionItemForAnalysis getObjectFormXml(File file) {
		if (file != null) {
			CNDescriptionItemForAnalysis cnDescriptionItem = new CNDescriptionItemForAnalysis();
			SAXReader saxReader = new SAXReader();
			try {
//				Document document =  DocumentHelper.parseText(fileToString);
				Document document = saxReader.read(file);
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
//                        System.out.print(cnDescriptionItem.getAppno());
				return cnDescriptionItem;
			} catch (DocumentException e) {
				System.out.println(file.getAbsolutePath() + "没有这个著录项文件");
				System.out.println(file.getName() + "没有这个著录荐文件");
				e.printStackTrace();
				return null;
			}

		}
		return null;

	}

	public static void main(String args[]) {
		 File file=new File("E:\\1\\2\\1.xml");
		 CNDescriptionItemForAnalysis cnDescriptionItem= XMLUtilInputMem.getObjectFormXml(file);
		//System.out.println("--------------------");
		String ss=cnDescriptionItem.getIpcMain();
		String nc=cnDescriptionItem.getNc();
//		ss=ss.substring(0, cnDescriptionItem.getIpcMain().indexOf(' '));
		//System.out.println(ss+"---ipc");
		//System.out.println(nc+"---nc");
		//System.out.println(cnDescriptionItem.getAppno()+"---appno");
		//System.out.println(cnDescriptionItem.getApdText());
		//System.out.println(cnDescriptionItem.getPudText());
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
