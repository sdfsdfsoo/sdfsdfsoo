package patentsearch.bean.util.xml;



import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import patentsearch.bean.cndescriptionitem.CNDescriptionItem;
import patentsearch.bean.cndescriptionitem.CNDescriptionItem_HISTORY;
import patentsearch.bean.cndescriptionitem.CNDescriptionItem_IMAGE;
import patentsearch.bean.cndescriptionitem.CNDescriptionItem_PCT;
import patentsearch.bean.cndescriptionitem.CNDescriptionItem_PRI;
import patentsearch.bean.cndescriptionitem.EnDescriptionItem;
import patentsearch.util.webservice.WebServiceClientUtil;
import patentsearch.utils.base.ConfigTool;

public class XMLUtil {

	/*
	 * 根据中国专利申请号来返回中国专利著录项目数据对象
	 */

	public static CNDescriptionItem getCNDescriptionItemByAppno(String appno) {
		String formatAppnp = formatAppno(appno);
		File file = getFileByAppno(formatAppnp);
		CNDescriptionItem CNDescriptionItem = getObjectFormXml(file);
		return CNDescriptionItem;
	}

	/*
	 * 根据世界专利申请号来返回世界专利著录项目数据对象
	 */

	public static EnDescriptionItem getENDescriptionItemByPubnr(String pubnr) {
		EnDescriptionItem enDescriptionItem=null;
		try{
//			String formatPubnr = formatPubnr(pubnr);
//			File file = getFileByPubnr(formatPubnr,pubnr);
			 String xmlString=WebServiceClientUtil.getPatentDataByAppno(pubnr, 6);
			 enDescriptionItem = getObjectFormEnXml(xmlString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return enDescriptionItem;
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

	/*
	 * 国外专利申请号格式化转化成统一18位规则 注意
	 */

	public static String formatPubnr(String pubnr) {
		StringBuilder formatAppno = new StringBuilder();
		if (pubnr != null && !"".equals(pubnr)) {
			formatAppno.append(pubnr.substring(0, 2));
			if (pubnr.length() == 16) {
				formatAppno.append(pubnr);
			} else {
				for (int i = 0; i < 16 - pubnr.length(); i++) {
					formatAppno.append("0");
				}
				formatAppno.append(pubnr);
			}
		}

		return formatAppno.toString();
	}

	/*
	 * 根据专利8或12位申请号生成其校验位Without dot
	 */

	public static String getCheckAppnoWithOutDot(String appno) {
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
				checkAppno = appno + 'x';
			} else if (checkBit < 10) {
				checkAppno = appno + checkBit;
			}
		}
		
		return checkAppno;

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
				checkAppno = appno + ".x";
			} else if (checkBit < 10) {
				checkAppno = appno  +"."+ checkBit;
			}
		}
		return checkAppno;

	}

	/*
	 * 根据中国专利申请号返回中国专利著录XML文件
	 */

	private static File getFileByAppno(String appno) {
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

	/*
	 * 根据国外专利申请号返回国外专利著录XML文件
	 */

	private static File getFileByPubnr(String formatPubnr,String pubnr) {
		// String
		// xmlPath="F:\\第一次来的数据\\CN_XML"+"\\"+appno.substring(0,4)+"\\"+appno.substring(4,9)+"\\"+appno+".xml";
		if (formatPubnr != null && !"".equals(formatPubnr)) {
			StringBuilder xmlPath = new StringBuilder();
			xmlPath.append(ConfigTool.getValue("enxml_pre"));
			xmlPath.append(formatPubnr.substring(0, 2)).append("\\").append(formatPubnr.substring(2, 6)).append("\\")
			.append(formatPubnr.substring(6, 10)).append("\\").append(formatPubnr.substring(10,14))
			.append("\\").append(formatPubnr.substring(14, 18)).append("\\").append(pubnr).append(".xml");
					
			//System.out.println("xmlpath:" + xmlPath);
			File file = new File(xmlPath.toString());
			if (file.exists()) {
				return file;
			}
		}
		return null;

	}

	/*
	 * 根据著录项目XML文件返回中国利著录项目数据对象
	 */

	public static CNDescriptionItem getObjectFormXml(File file) {
		if (file != null) {

			CNDescriptionItem cnDescriptionItem = new CNDescriptionItem();
			cnDescriptionItem.setIpcMinor("");
			SAXReader saxReader = new SAXReader();
			int a =1;
			try {
				Document document = saxReader.read(file);
				a=2;
				Element rootElement = document.getRootElement();
				Iterator elementIterator = rootElement.elements().iterator();
				Boolean isMainIPC = true;
				while (elementIterator.hasNext()) {

					Element element = (Element) elementIterator.next();
					if (element.getName().equals("APNNO")) {
						cnDescriptionItem.setAppno(element.getText().trim());

					} else if (element.getName().equals("PUBNR")) {
						cnDescriptionItem.setPubnr(element.getText());

					} else if (element.getName().equals("APPNR")) {
						cnDescriptionItem.setAppnr(element.getText());

					} else if (element.getName().equals("APD")) {
						cnDescriptionItem.setApd(DateUtil.stringToDate(element
								.getText()));

					} else if (element.getName().equals("PUD")) {
						cnDescriptionItem.setPud(DateUtil.stringToDate(element
								.getText()));

					} else if (element.getName().equals("GRD")) {
						cnDescriptionItem.setGrd(DateUtil.stringToDate(element
								.getText()));

					} else if (element.getName().equals("GRPD")) {
						cnDescriptionItem.setGrpd(DateUtil.stringToDate(element
								.getText()));

					} else if (element.getName().equals("APPD")) {
						String appd=element.getText();
						cnDescriptionItem.setAppd(DateUtil.stringToDate(appd));
//						if(appd.trim().length()>0){
//							String str1=appd.substring(appd.indexOf("年")+1, appd.indexOf("月"));
//							String str2=appd.substring( appd.indexOf("月")+1, appd.indexOf("日"));
//							if(str1.length()==1){
//								str1="0"+str1;
//							}
//							if(str2.length()==1){
//								str2="0"+str2;
//							}
//							appd=appd.substring(0, appd.indexOf("年"))+str1+str2;
//							cnDescriptionItem.setAppdText(appd);
//						}
					} else if (element.getName().equals("NC")) {
						cnDescriptionItem.setNc(element.getText() == null ? ""
								: element.getText().replace("\n", ""));

					} else if (element.getName().equals("PNUM")) {
						if (element.getText() != null
								&& !"".equals(element.getText().trim())) {
							try {
								cnDescriptionItem.setPnum(Integer
										.parseInt(element.getText().trim()));
							} catch (Exception e) {
								cnDescriptionItem.setPnum(null);
								e.printStackTrace();
							}
						}

					} else if (element.getName().equals("FNUM")) {
						/*
						 * cnDescriptionItem.setFnum(Integer.parseInt(element
						 * .getText().trim()));
						 */

					} else if (element.getName().equals("CNUM")) {
						/*
						 * cnDescriptionItem.setCnum(Integer.parseInt(element
						 * .getText().trim()));
						 */
					} else if (element.getName().equals("AGENCY")) {
						cnDescriptionItem.setAgency(element.getText().trim());

					} else if (element.getName().equals("FIELDC")) {
						cnDescriptionItem.setFieldc(element.getText());

					} else if (element.getName().equals("ADDRESS")) {
						cnDescriptionItem.setAddress(element.getText());

					} else if (element.getName().equals("AGENT")) {
						cnDescriptionItem
								.setAgent(element.getText() == null ? ""
										: element.getText());

					} else if (element.getName().equals("TITLE")) {
						cnDescriptionItem.setTitle(element.getText());

					} else if (element.getName().equals("ZIP")) {
						cnDescriptionItem.setZip(element.getText());

					} else if (element.getName().equals("IPC")) {
						// IPC
						if (isMainIPC) {
							cnDescriptionItem.setIpcMain(element.getText());
							isMainIPC = false;
						} else {
							if("".equals(cnDescriptionItem.getIpcMinor())){
								cnDescriptionItem.setIpcMinor(element.getText());
							} else {
								cnDescriptionItem.setIpcMinor(cnDescriptionItem.getIpcMinor()+";"+element.getText());
							}
						}

					} else if (element.getName().equals("PRI")) {
						// PRI
						CNDescriptionItem_PRI pri = new CNDescriptionItem_PRI();
						Iterator priIterator = element.elements().iterator();
						while (priIterator.hasNext())

						{
							Element priElement = (Element) priIterator.next();

							if (priElement.getName().equals("CO")) {
								pri.setCo(priElement.getText());

							}
							if (priElement.getName().equals("NR")) {
								pri.setNr(priElement.getText());
							}
							if (priElement.getName().equals("DATE")) {
								pri.setDate(DateUtil.stringToDate(priElement
										.getText()));
							}

						}
						cnDescriptionItem.addPri(pri);

					} else if (element.getName().equals("KEYWORD")) {
						// KEYWORD
						cnDescriptionItem.addKeyword(element.getText().trim());

					} else if (element.getName().equals("APPL")) {
						cnDescriptionItem.addAppl(element.getText().trim());

					} else if (element.getName().equals("INVENTOR")) {
						// INVENTOR
						cnDescriptionItem.addInventor(element.getText().trim());

					} else if (element.getName().equals("ABSTR")) {
						cnDescriptionItem.setAbstr(element.getText());

					} else if (element.getName().equals("CLAIM")) {
						cnDescriptionItem.setClaim(element.getText());

					} else if (element.getName().equals("HISTORY")) {
						// HISTORY
						CNDescriptionItem_HISTORY history = new CNDescriptionItem_HISTORY();
						Iterator historyIterator = element.elements()
								.iterator();
						while (historyIterator.hasNext())

						{
							Element historyElement = (Element) historyIterator
									.next();

							if (historyElement.getName().equals("LEGAL_MK")) {
								history.setLegal_mk(historyElement.getText());

							}
							if (historyElement.getName().equals("REC_DATE")) {
								history
										.setRec_date(DateUtil
												.stringToDate(historyElement
														.getText()));
							}

						}
						cnDescriptionItem.addHistory(history);

					} else if (element.getName().equals("PCT")) {

						// PCT
						CNDescriptionItem_PCT pct = new CNDescriptionItem_PCT();
						Iterator pctIterator = element.elements().iterator();
						while (pctIterator.hasNext())

						{
							Element pctElement = (Element) pctIterator.next();

							if (pctElement.getName().equals("PSTDA")) {
								pct.setPstda(DateUtil.stringToDate(pctElement
										.getText()));

							}
							if (pctElement.getName().equals("PCTNO")) {
								pct.setPctno(pctElement.getText());
							}
							if (pctElement.getName().equals("PCTDA")) {
								pct.setPctda(DateUtil.stringToDate(pctElement
										.getText()));

							}
							if (pctElement.getName().equals("PPBDO")) {
								pct.setPpbdo(pctElement.getText());
							}
							if (pctElement.getName().equals("PPBDA")) {
								pct.setPpbda(DateUtil.stringToDate(pctElement
										.getText()));

							}
							if (pctElement.getName().equals("PLANG")) {
								pct.setPlang(pctElement.getText().trim());
							}
						}
						cnDescriptionItem.setPct(pct);

					} else if (element.getName().equals("IMAGE")) {
						// IMAGE
						CNDescriptionItem_IMAGE image = new CNDescriptionItem_IMAGE();
						Iterator imageIterator = element.elements().iterator();
						while (imageIterator.hasNext())

						{
							Element imageElement = (Element) imageIterator
									.next();

							if (imageElement.getName().equals("VOL")) {
								image.setVol(imageElement.getText().trim());

							}
							if (imageElement.getName().equals("PAGE")) {
								if (!imageElement.getText().replace("\n", "").trim().equals("")) {
									image.setPage(Integer.parseInt(imageElement
											.getText().trim()));
								} else {
									image.setPage(0);
								}
							}
						}
						cnDescriptionItem.addImage(image);
					}
				}

				return cnDescriptionItem;
			} catch (Exception e) {
				System.out.println(file.getAbsolutePath() + "没有这个著录项文件");
				System.out.println(file.getName() + "没有这个著录荐文件");
				if(a == 2){
					try {
						FileWriter file1 = new FileWriter("d:\\aaa.txt", true);
						file1.write(file.getName() + "\r\n");
						file1.close();
					} catch (IOException e1) {
						e1.printStackTrace();
						return null;
					}
				}
				e.printStackTrace();
				return null;
			} 

		}
		return null;

	}

	/*
	 * 根据著录项目XML文件返回国际专利著录项目数据对象
	 */

	private static EnDescriptionItem getObjectFormEnXml(String transMessage) {
		EnDescriptionItem enDescriptionItem = new EnDescriptionItem();
		if (transMessage != null) {
			SAXReader saxReader = new SAXReader();
			try {
				Document document =  DocumentHelper.parseText(transMessage);
//				Document document = saxReader.read(file);
				Element rootElement = document.getRootElement();
				Iterator rootElementIterator = rootElement.elements()
						.iterator();
				// Boolean isMainIPC = true;
				while (rootElementIterator.hasNext()) {

					Element firElement = (Element) rootElementIterator.next();
					if ("bibliographic-data".equals(firElement.getName())) {
						Iterator bibliographicIterator = firElement.elements()
								.iterator();
						while (bibliographicIterator.hasNext()) {
							Element secElement = (Element) bibliographicIterator
									.next();
							if ("publication-reference".equals(secElement
									.getName())) {
								if ("dcdb".equals(secElement.attribute("data-format").getData().toString())||"docdb".equals(secElement.attribute("data-format").getData().toString())) {
									Element doc_idElement = (Element) secElement
											.elements().get(0);
									Element countryElement = (Element) doc_idElement
											.elements().get(0);
									Element doc_numElement = (Element) doc_idElement
											.elements().get(1);
									Element kindElement = (Element) doc_idElement
											.elements().get(2);
									Element dateElement = (Element) doc_idElement
											.elements().get(3);
									enDescriptionItem.setPubnr(countryElement
											.getText()
											+ doc_numElement.getText()
											+ kindElement.getText());
									enDescriptionItem.setPudText(dateElement
											.getText());

								}else if("original".equals(secElement.attribute("data-format").getData().toString())){
									Element doc_idElement = (Element) secElement.elements().get(0);
									Element doc_numElement = (Element) doc_idElement.elements().get(0);
									enDescriptionItem.setPubnrOriginal(doc_numElement.getText());
								}
							} else if ("classifications-ipcr".equals(secElement
									.getName())) {
								Iterator classificationIterator = secElement
										.elements().iterator();
								while (classificationIterator.hasNext()) {
									Element classificationElement = (Element) classificationIterator
											.next();
									Element textElement = (Element) classificationElement
											.elements().get(0);
									if (enDescriptionItem.getInterIpc() == null) {
										enDescriptionItem.setInterIpc("");
									}
									enDescriptionItem
											.setInterIpc(enDescriptionItem
													.getInterIpc()
													+ textElement.getText());
								}
							} else if ("classification-ecla".equals(secElement////////////////////////////////////////
									.getName())) {
								Element symbolElement = (Element) secElement
										.elements().get(0);
								if ("EC".equals(secElement.attribute(
										"classification-scheme").getData()
										.toString())) {
									if (enDescriptionItem.getEuroIpc() == null) {
										enDescriptionItem.setEuroIpc("");
									}
									enDescriptionItem
											.setEuroIpc(enDescriptionItem
													.getEuroIpc()
													+ symbolElement.getText()
													+ ";");
								}
							} else if ("application-reference"
									.equals(secElement.getName())
									&& ("dcdb"
											.equals(secElement.attribute(
													"data-format").getData()
													.toString())||"docdb".equals(secElement.attribute("data-format").getData().toString()))) {
								Iterator applicationIterator = secElement
										.elements().iterator();
								while (applicationIterator.hasNext()) {
									Element thirElement = (Element) applicationIterator
											.next();
									Element dateElement = (Element) thirElement
											.elements().get(3);
									// System.out.println(doc_numberElement.getText());
									String ssttrr=dateElement.getText();
									enDescriptionItem.setApdText(ssttrr);
								}
							} else if ("application-reference"
									.equals(secElement.getName())
									&& ("ecdb"
											.equals(secElement.attribute(
													"data-format").getData()
													.toString())||"epodoc"
											.equals(secElement.attribute(
													"data-format").getData()
													.toString()))) {
								Iterator applicationIterator = secElement
										.elements().iterator();
								while (applicationIterator.hasNext()) {
									Element thirElement = (Element) applicationIterator
											.next();
									Element doc_numberElement = (Element) thirElement
											.elements().get(0);
									// System.out.println(doc_numberElement.getText());
									enDescriptionItem
											.setAppno(doc_numberElement
													.getText());
								}
							} else if ("application-reference"
									.equals(secElement.getName())
									&& "original"
											.equals(secElement.attribute(
													"data-format").getData()
													.toString())) {
								Iterator applicationIterator = secElement
										.elements().iterator();
								while (applicationIterator.hasNext()) {
									Element thirElement = (Element) applicationIterator
											.next();
									Element doc_numberElement = (Element) thirElement
											.elements().get(0);
									// System.out.println(doc_numberElement.getText());
									enDescriptionItem
											.setOriginalAppno(doc_numberElement
													.getText());
								}
							} else if ("priority-claims".equals(secElement
									.getName())) {
								Iterator priorityIterator = secElement
										.elements().iterator();
								while (priorityIterator.hasNext()) {
									Element priorityElement = (Element) priorityIterator
											.next();
									if ("ecdb".equals(priorityElement
											.attribute("data-format").getData()
											.toString())||"epodoc".equals(priorityElement
													.attribute("data-format").getData()
													.toString())) {
										Element doc_idElement = (Element) priorityElement
												.elements().get(0);
										Element doc_numElement = (Element) doc_idElement
												.elements().get(0);
										enDescriptionItem
												.setPris(doc_numElement
														.getText());
									}
								}
							} else if ("references-cited".equals(secElement  //////////////////////////////////////////
									.getName())) {
								Iterator citationIterator = secElement
										.elements().iterator();
								while (citationIterator.hasNext()) {
									Element citationElement = (Element) citationIterator
											.next();
									Element patcitElement = (Element) citationElement
											.elements().get(0);
									if("patcit".equals(patcitElement.getName().trim())){
										Element docIdElement = (Element) patcitElement
												.elements().get(0);
										Element countryElement = (Element) docIdElement
												.elements().get(0);
										Element docNumElement = (Element) docIdElement
												.elements().get(1);
										Element kindElement = (Element) docIdElement
												.elements().get(2);
										if (enDescriptionItem.getReferences() == null) {
											enDescriptionItem.setReferences("");
										}
										enDescriptionItem
												.setReferences(enDescriptionItem
														.getReferences()
														+ countryElement.getText()
														+ docNumElement.getText()
														+ kindElement.getText()
														+ ";");
									}
								}
							} else if ("parties".equals(secElement.getName())) {
								Iterator partiesIterator = secElement
										.elements().iterator();
								while (partiesIterator.hasNext()) {
									Element partyElement = (Element) partiesIterator
											.next();
									if ("inventors".equals(partyElement
											.getName())) {
										Iterator inventorsIterator = partyElement
												.elements().iterator();
										while (inventorsIterator.hasNext()) {
											Element inventorElement = (Element) inventorsIterator
													.next();
											if ("dcdb".equals(inventorElement
													.attribute("data-format")
													.getData().toString())||"docdb".equals(inventorElement
															.attribute("data-format")
															.getData().toString())) {
												Element inventorNameElement = (Element) inventorElement
														.elements().get(0);
												Element nameElement = (Element) inventorNameElement
														.elements().get(0);
												if (enDescriptionItem
														.getInventor() != null) {
													enDescriptionItem
															.setInventor(enDescriptionItem
																	.getInventor()
																	+ nameElement
																			.getText()
																	+ ";");
												} else {
													enDescriptionItem
															.setInventor(nameElement
																	.getText()
																	+ ";");
												}
											}
										}
									}
									if ("applicants".equals(partyElement
											.getName())) {
										Iterator applicantsIterator = partyElement
												.elements().iterator();
										while (applicantsIterator.hasNext()) {
											Element applicantElement = (Element) applicantsIterator
													.next();
											if("dcdb".equals(applicantElement
													.attribute("data-format")
													.getData().toString())||"docdb".equals(applicantElement
															.attribute("data-format")
															.getData().toString()))  {
												Element applicant_nameElement = (Element) applicantElement
														.elements().get(0);
												Element nameElement = (Element) applicant_nameElement
														.elements().get(0);
												if (enDescriptionItem.getAppl() != null) {
													enDescriptionItem
															.setAppl(enDescriptionItem
																	.getAppl()
																	+ nameElement
																			.getText()
																	+ ";");
												} else {
													enDescriptionItem
															.setAppl(nameElement
																	.getText()
																	+ ";");
												}
											}
										}

									}
								}
							} else if ("dates-of-public-availability"
									.equals(secElement.getName())) {

							}else if("invention-title".equals(secElement.getName())){
								if(secElement.attribute("lang")!=null){
									if("en".equals(secElement.attribute("lang").getData().toString())){
										enDescriptionItem.setTitle(secElement.getText());
									}
								}else if("docdba".equals(secElement.attribute("data-format").getData().toString())){
									enDescriptionItem.setTitle(secElement.getText());
								}
							}
						}
					} else if ("patent-family".equals(firElement.getName())) {
						Iterator patentIterator = firElement.elements()
								.iterator();
						while (patentIterator.hasNext()) {
							Element secElement = (Element) patentIterator
									.next();
							if ("family-member".equals(secElement.getName())) {
								Iterator familyIterator = secElement.elements()
										.iterator();
								while (familyIterator.hasNext()) {
									Element thirElement = (Element) familyIterator
											.next();
									if ("application-reference"
											.equals(secElement.getName())) {

									} else if ("publication-reference"
											.equals(secElement.getName())) {

									}
								}
							} else if ("abstract".equals(secElement.getName())) {
								if ("en".equals(secElement.attribute("lang")
										.getData().toString())) {
									Element pElement = (Element) secElement
											.elements().get(0);
									enDescriptionItem
											.setAbs(pElement.getText());
								}
							}
						}
					} else if ("abstract".equals(firElement.getName())) {
						
						if(firElement.attribute("lang")!=null){
							if("en".equals(firElement.attribute("lang").getData().toString())){
								Element pElement = (Element) firElement.elements()
								.get(0);
						        enDescriptionItem.setAbs(pElement.getText());
							}
						}else if("docdba".equals(firElement.attribute("data-format").getData().toString())){
							Element pElement = (Element) firElement.elements()
							.get(0);
					        enDescriptionItem.setAbs(pElement.getText());

						}
					}

				}
				if (enDescriptionItem.getInventor() != null
						&& enDescriptionItem.getInventor().length() > 1) {
					enDescriptionItem
							.setInventor(enDescriptionItem.getInventor()
									.substring(
											0,
											enDescriptionItem.getInventor()
													.length() - 1));
				}
				if (enDescriptionItem.getAppl() != null
						&& enDescriptionItem.getAppl().length() > 1) {
					enDescriptionItem.setAppl(enDescriptionItem.getAppl()
							.substring(0,
									enDescriptionItem.getAppl().length() - 1));
				}
				if (enDescriptionItem.getInterIpc() != null
						&& enDescriptionItem.getInterIpc().length() > 1) {
					String str = enDescriptionItem.getInterIpc();
					String[] strr = str.split(" ");
					StringBuilder ss = new StringBuilder();
					int j = 0;
					for (int i = 0; i < strr.length; i++) {
						if (strr[i] != null && !"".equals(strr[i])) {
							j++;
							if (j % 3 == 0) {
								ss.append(";");
							} else {
								ss.append(strr[i]);
							}
						}
					}
					ss.deleteCharAt(ss.length() - 1);
					String[] str0 = ss.toString().split(";");
					// System.out.println(str0.length);
					if (strr.length > 1) {
						for (int i = 0; i < str0.length; i++) {
							for (int k = i + 1; k < str0.length; k++) {
								if (str0[k] != null && str0[i] != null) {
									if (str0[i].equals(str0[k])) {
										str0[k] = null;
									}
								}
							}
						}
					}
					StringBuilder s = new StringBuilder();
					for (int i = 0; i < str0.length; i++) {
						if (str0[i] != null) {
							s.append(str0[i]).append(";");
						}
					}
					s.deleteCharAt(s.length() - 1);
					enDescriptionItem.setInterIpc(s.toString());
					// System.out.println(enDescriptionItem.getInterIpc());
				}
				if (enDescriptionItem.getEuroIpc() != null
						&& !"".equals(enDescriptionItem.getEuroIpc())) {
					String str = enDescriptionItem.getEuroIpc();
					enDescriptionItem.setEuroIpc(str.substring(0,
							str.length() - 1));
				}
				if (enDescriptionItem.getReferences() != null
						&& !"".equals(enDescriptionItem.getReferences())) {
					String str = enDescriptionItem.getReferences();
					enDescriptionItem.setReferences(str.substring(0, str
							.length() - 1));
					// System.out.println(enDescriptionItem.getReferences());

				}
				if(enDescriptionItem.getPubnr()!=null&&!"".equals(enDescriptionItem.getPubnr())){
					String str=enDescriptionItem.getPubnr();
					//防止出现WONULL    USnull
					if(enDescriptionItem.getPubnrOriginal()!=null&&!"".equals(enDescriptionItem.getPubnrOriginal())){
						enDescriptionItem.setPubnrOriginal(str.substring(0,2)+enDescriptionItem.getPubnrOriginal());
					}
				}
				if(enDescriptionItem.getPubnr()!=null&&!"".equals(enDescriptionItem.getPubnr())){
					String str=enDescriptionItem.getPubnr();
					if(enDescriptionItem.getOriginalAppno()!=null&&!"".equals(enDescriptionItem.getOriginalAppno())){	
						enDescriptionItem.setOriginalAppno(str.substring(0,2)+enDescriptionItem.getOriginalAppno());
					}	
				}
//				System.out.println(enDescriptionItem);

			} catch (DocumentException e) {
//				System.out.println(file.getAbsolutePath() + "没有这个著录项文件");
//				System.out.println(file.getName() + "没有这个著录荐文件");
				e.printStackTrace();
				return null;
			}

		}
		return enDescriptionItem;

	}

	/*
	 * 根据申请号得到中国专利摘要附图，输入专利申请号，输出摘要附图（国内）
	 */

	public static String getFuTuByAppno(String appno) {
		String formatAppno = null;

		if (appno != null && !"".equals(appno)) {
			if (appno.length() == 12) {
				formatAppno = appno.substring(0, 4) + "/"
						+ appno.substring(4, 9) + "/" + appno + ".gif";
			} else if (appno.length() == 8) {
				formatAppno = appno.substring(0, 5) + "/" + appno + ".gif";
			}
		}

		return ConfigTool.getValue("futu_pre") + formatAppno;
	}
	/*
	 * 根据申请号得到中国专利摘要附图，输入专利申请号，输出摘要附图(国外)
	 */

	public static String getFuTuByPubnr(String pubnr,String pudText) {
		String formatPubnr = null;

		if (pubnr != null && !"".equals(pubnr)) {
			formatPubnr="&date="+pudText+"&CC="+pubnr.substring(0, 2)+"&NR="+pubnr.substring(2, pubnr.length())+"&KD="+pubnr.substring(pubnr.length()-1, pubnr.length());
		}

		return ConfigTool.getValue("futuen_pre") + formatPubnr;
	}

	/*
	 * 递归调用查找指定文件加下所有文件
	 */
	public static int GetAllFiles(String path) {
		File rootDir = new File(path);
		if (!rootDir.isDirectory()) {
			return 0;
		} else {
			return rootDir.list().length;
		}
	}
	
	public static void main(String args[]) {
		
		System.out.println(XMLUtil.getFileByAppno("00100004"));
//		List<String> strList= GetSql("http://112.83.69.145:8090/Design/2817/201130227380x");
//		 File file=new File("Z:\\cn_item\\CN_XML\\2013\\10660\\201310660841.xml");
//		 CNDescriptionItem cnDescriptionItem= getObjectFormXml(file);
//		 System.out.println(cnDescriptionItem.getAddress());
//		 System.out.println(cnDescriptionItem.getAbstr());
//		 System.out.println(cnDescriptionItem.getAppdText());
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
//			
////		 File file=new File("Z:\\En_Xml\\AM\\0000\\0000\\00AM\\170U\\AM170U.xml");
//			EnDescriptionItem enDescriptionItem= XMLUtil.getObjectFormEnXml(xmlString);
//			System.out.println(enDescriptionItem);
//			System.out.println(enDescriptionItem.getTitle());
	}
}
