package patentsearch.service.patent.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import patentsearch.bean.cndescriptionitem.CNDescriptionItem;
import patentsearch.bean.cndescriptionitem.EnDescriptionItem;
import patentsearch.bean.search.requestParameter.DoSearchParameter;
import patentsearch.bean.search.requestParameter.GetGeneralDataParameter;
import patentsearch.bean.util.xml.XMLUtil;
import patentsearch.service.base.impl.DaoSupport;
import patentsearch.service.patent.SearchService;
import patentsearch.util.webservice.WebServiceClientUtil;
import patentsearch.util.webservice.WebServiceLocalClientUtil;
import patentsearch.util.webservice.WebServiceRemoteClientUtil;
import patentsearch.utils.base.ConfigTool;
import patentsearch.utils.base.WebTool;

@Service
public class SearchServiceImpl extends DaoSupport<CNDescriptionItem> implements
		SearchService {
	/*
	 * 本地接口DoSearch
	 */
	public Long handleDoSearch(DoSearchParameter doSearch) {
		// TODO Auto-generated method stub
		String resultString = WebServiceLocalClientUtil.getPatentRecordsNumber(doSearch);
		if (resultString != null && !"ER:检索入口错误".equals(resultString)&&!"ER:申请号校验位错误".equals(resultString)&&!"ER:请求已经超时!".equals(resultString)) {
			return Long.parseLong(resultString);
		}
		return new Long(0);
	}
	/*
	 * 远程接口DoSearch
	 */
	public Long handleDoSearchRemote(DoSearchParameter doSearch) {
		// TODO Auto-generated method stub
		String resultString = WebServiceClientUtil.getPatentRecordsNumber(doSearch);
		System.out.println(resultString);
		if (resultString != null && !"ER:检索入口错误".equals(resultString)) {
			return Long.parseLong(resultString);
		}
		return new Long(0);
	}
	/*
	 * 本地接口GetGeneralData
	 */
	public List<CNDescriptionItem> handleGetGeneralData(
			GetGeneralDataParameter getGeneralData) {
		List<CNDescriptionItem> cNDescriptionItemList = new ArrayList<CNDescriptionItem>();

		List<String> appnoList = WebServiceLocalClientUtil
				.getAppnolistBySearch(getGeneralData);
		if (appnoList != null && appnoList.size() > 0) {
			for (int i = 0; i < appnoList.size(); i++) {
				String appno = appnoList.get(i);
				cNDescriptionItemList.add(XMLUtil
						.getCNDescriptionItemByAppno(appno));

			}
			return cNDescriptionItemList;
		}
		return null;
	}

	/*
	 * 远程接口GetGeneralData
	 */
	public List<EnDescriptionItem> handleGetGeneralDataRemote(
			GetGeneralDataParameter getGeneralData) {
		List<EnDescriptionItem> enDescriptionItemList = new ArrayList<EnDescriptionItem>();

		List<String> pubidList = WebServiceClientUtil.getPubIdListlistBySearch(getGeneralData);
//		System.out.println(pubidList);
		if (pubidList != null && pubidList.size() > 0) {
			for (int i = 0; i < pubidList.size(); i++) {
				String pubId = pubidList.get(i);
				enDescriptionItemList.add(XMLUtil.getENDescriptionItemByPubnr(pubId));    ///////

			}

			return enDescriptionItemList;
		}
		return null;
	}
	/*
	 * 通过远程接口，输入专利申请号，输出说明书、权利要求信息
	 */
	public String getPatentData(String appno, int searchType) {
//		String resultString = WebServiceRemoteClientUtil.getPatentDataByAppno(
				String resultString = WebServiceClientUtil.getPatentDataByAppno(
				appno, searchType);
		return resultString;
	}

	/*
	 * 通过本地接口，输入专利申请号、输出PDF文件URL
	 */
	public String getPdfUrl(String appno) {
		// TODO Auto-generated method stub
		System.out.println("5 "+new Date().getTime());
		String resultString = WebServiceLocalClientUtil.getPdfByAppno(appno);
		System.out.println("5 "+new Date().getTime());
		if("".equals(resultString)){
			
		}
		String resultString0 = "";
		String resultString1 = "";
		System.out.println("5 "+new Date().getTime());
		if (resultString == null) {
			return ConfigTool.getValue("pdf_friendly");
		} else {		
			System.out.println("5 "+new Date().getTime());
			String[] resultStrs = resultString.split("\\|");
			System.out.println("5 "+new Date().getTime());
			for(int i=0; i<resultStrs.length; i++){
				System.out.println("5 "+new Date().getTime());
				if(i>0){
					resultString1 =resultString1+"|";
				}
				System.out.println("5 "+new Date().getTime());
				String temp = resultStrs[i];
				System.out.println("5 "+new Date().getTime());
				temp = "http:" + temp;
				System.out.println("5 "+new Date().getTime());
				resultString0 = temp.replace("\\", "/");
				System.out.println("5 "+new Date().getTime());
				if (!WebTool.urlIsValid(resultString0)) {
					resultString1 = resultString1+ ConfigTool.getValue("pdf_friendly");
				}else{
					resultString1 = resultString1+temp.replace("\\", "/");
				}
				System.out.println("5 "+new Date().getTime());
			}
			resultString = resultString1;
		}
		
		return resultString;
	}
	
	/*
	 * 通过远程接口，输入专利公开号、输出PDF文件URL
	 */
	public String getPdfUrlEn(String pubnr) {
		
		// TODO Auto-generated method stub
		String resultString = WebServiceClientUtil.getPatentDataByAppno(pubnr, 7);
		 
		if (resultString == null) {
			return ConfigTool.getValue("pdf_friendly");
		} else {
			 
			if (!WebTool.urlIsValid(resultString)) {
				resultString = ConfigTool.getValue("pdf_friendly");
			}
		}
		
		return resultString;
	}
	/*
	 * 根据输入专利申请号数组，返回专利著录项集合(国内)
	 */
	public List<CNDescriptionItem> getPatentItemList(List<String> appnoList) {
		//System.out.println("-------------------0000000000000000-"+appnoList.size());
		List<CNDescriptionItem> resultList=new ArrayList<CNDescriptionItem>();
		if(appnoList!=null){
			if(appnoList.size()>0){
				for(String appno:appnoList){
					CNDescriptionItem cnDescriptionItem=XMLUtil.getCNDescriptionItemByAppno(appno);
					resultList.add(cnDescriptionItem);
				}
				return resultList;
			}
		}
		return null;
	}
	/*
	 * 根据输入专利申请号数组，返回专利著录项集合（国外）
	 */
	public List<EnDescriptionItem> getPatentItemListEN(List<String> appnoList) {
		List<EnDescriptionItem> resultList=new ArrayList<EnDescriptionItem>();
		if(appnoList!=null&&appnoList.size()>0){
			for(String pubnr:appnoList){
				EnDescriptionItem enDescriptionItem=XMLUtil.getENDescriptionItemByPubnr(pubnr);
				resultList.add(enDescriptionItem);
			}
			return resultList;
		}
		return null;
	}
	

	public File getPdfFile(String appno) {
	//"http://11.0.0.26:8090/Epds1/CN/2013/203/070/196/000000_20130717_0Y_CN_0.pdf";
		String src=	getPdfUrl(appno);
		String 	s1;
		
		String[] urls = src.split("\\|");
		if(urls.length>1){
			src = urls[1];
		}
		s1=src.substring(src.indexOf("8090")+5);	
		
		String s2=s1.replace("/", "\\\\");
		//String s3="Z:\\\\"+s2;
		String s3=ConfigTool.getValue("raid_disk")+":\\\\"+s2;
		return new File(s3);
	}

	public CNDescriptionItem getPatentItem(String appno) {
		CNDescriptionItem cnDescriptionItem=XMLUtil.getCNDescriptionItemByAppno(appno);
		return cnDescriptionItem;
	}
	public EnDescriptionItem getPatentItemEN(String pubnr) {
		EnDescriptionItem enDescriptionItem=XMLUtil.getENDescriptionItemByPubnr(pubnr);
		return enDescriptionItem;
	}


	public List<Map<String, String>> getPatentBasicInfo(List<String> appnos) {
	 return WebServiceLocalClientUtil.getPatentBasicInfo(appnos);
	}
	public List<Map<String, String>> getPatentApdDate(List<String> appnos) {
		return WebServiceLocalClientUtil.getPatentApdDates(appnos);
	}
	
 
}
