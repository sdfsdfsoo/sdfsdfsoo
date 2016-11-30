package patentsearch.util.webservice;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.axis2.AxisFault;
import org.apache.struts2.ServletActionContext;
import org.junit.Test;
import org.springframework.stereotype.Service;
import org.tempuri.local.CprsGIISWebSvcStub;
import org.tempuri.local.DoSearch;
import org.tempuri.local.DoSearchResponse;
import org.tempuri.local.GeneralDataInfo;
import org.tempuri.local.GetGeneralData;
import org.tempuri.local.GetGeneralDataResponse;
import org.tempuri.local.GetPatentData;
import org.tempuri.local.GetPatentDataResponse;
import org.tempuri.local.PatentDataType;
import org.tempuri.local.SearchDbType;

import patentsearch.bean.search.requestParameter.DoSearchParameter;
import patentsearch.bean.search.requestParameter.GetGeneralDataParameter;
import patentsearch.bean.user.Users;
import patentsearch.bean.util.xml.XMLUtil;
import patentsearch.service.patent.SearchService;
import patentsearch.service.patent.impl.SearchServiceImpl;
import patentsearch.utils.base.ConfigTool;
import patentsearch.utils.base.WebTool;

@Service
public class WebServiceLocalClientUtil {

	public static String getPatentRecordsNumber(
			DoSearchParameter doSearchParameter) {

		try {
			CprsGIISWebSvcStub cprsGIISWebSvcStub = new CprsGIISWebSvcStub();
			DoSearch doSearch = new DoSearch();
			doSearch.set_strUID(doSearchParameter.getuID());
			doSearch.set_SDbType(SearchDbType.Cn);
			doSearch.set_strSID(doSearchParameter.getsID());
			doSearch.set_strSearchQuery(doSearchParameter.getSearchFormula());
			DoSearchResponse getDoSearchResponse = cprsGIISWebSvcStub
					.doSearch(doSearch);
			return getDoSearchResponse.getDoSearchResult();

		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static List<String> getAppnolistBySearch(
			patentsearch.bean.search.requestParameter.GetGeneralDataParameter getGeneralData) {
		List<String> appnoList = new ArrayList<String>();

		try {
			CprsGIISWebSvcStub cprsGIISWebSvcStub = new CprsGIISWebSvcStub();
			GetGeneralData generalData = new GetGeneralData();
			generalData.set_strUID(getGeneralData.getuID());
			generalData.set_SDbType(SearchDbType.Cn);
			generalData.set_strSID(getGeneralData.getsID());
			generalData.set_pageNo(getGeneralData.getPageNo());
			generalData.set_pageSize(getGeneralData.getPageSize());
			GetGeneralDataResponse getGeneralDataResponse = cprsGIISWebSvcStub
					.getGeneralData(generalData);
			if (getGeneralDataResponse != null
					&& getGeneralDataResponse.getGetGeneralDataResult() != null
					&& getGeneralDataResponse.getGetGeneralDataResult()
							.getGeneralDataInfo() != null) {
				for (GeneralDataInfo generalDataInfo : getGeneralDataResponse
						.getGetGeneralDataResult().getGeneralDataInfo()) {

					// 引擎webservice中检索的有ExtensionData、NCPIC、StrTI、StrTrsTI、StrAN、StrAD、StrIPC、NMembers、StrPtCode
					// 因为这里检索的专利信息不全，还得通过中国专利著录项目XML来获得详细，所以这里还得封装CNDescriptionItem
					// System.out.println(generalDataInfo.getStrAN()+":"+generalDataInfo.getStrTI());
					// 测试代码 先对申请号进行截取去取.与最后一位
					String appno = generalDataInfo.getStrAN().substring(0,generalDataInfo.getStrAN().length() - 2);
					//XMLUtil.getCNDescriptionItemByAppno(appno);
					appnoList.add(appno);
					// System.out.println("摘要:"+XMLUtil.getCNDescriptionItemByAppno(appno).getAbstr());

				}
				
				return appnoList;
			}
			System.out.println("结果为null");
			return null;

		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	public static List<String> getAppnolistBySearchWithLastNum(patentsearch.bean.search.requestParameter.GetGeneralDataParameter getGeneralData) {
		List<String> appnoList = new ArrayList<String>();

		try {
			CprsGIISWebSvcStub cprsGIISWebSvcStub = new CprsGIISWebSvcStub();
			GetGeneralData generalData = new GetGeneralData();
			generalData.set_strUID(getGeneralData.getuID());
			generalData.set_SDbType(SearchDbType.Cn);
			generalData.set_strSID(getGeneralData.getsID());
			generalData.set_pageNo(getGeneralData.getPageNo());
			generalData.set_pageSize(getGeneralData.getPageSize());
			GetGeneralDataResponse getGeneralDataResponse = cprsGIISWebSvcStub
					.getGeneralData(generalData);
			if (getGeneralDataResponse != null
					&& getGeneralDataResponse.getGetGeneralDataResult() != null
					&& getGeneralDataResponse.getGetGeneralDataResult()
							.getGeneralDataInfo() != null) {
				for (GeneralDataInfo generalDataInfo : getGeneralDataResponse
						.getGetGeneralDataResult().getGeneralDataInfo()) {
					// 引擎webservice中检索的有ExtensionData、NCPIC、StrTI、StrTrsTI、StrAN、StrAD、StrIPC、NMembers、StrPtCode
					// 因为这里检索的专利信息不全，还得通过中国专利著录项目XML来获得详细，所以这里还得封装CNDescriptionItem
					// System.out.println(generalDataInfo.getStrAN()+":"+generalDataInfo.getStrTI());
					// 测试代码 先对申请号进行截取去取.与最后一位
					String appnoOriginal=generalDataInfo.getStrAN();
					String appno = appnoOriginal.substring(0,appnoOriginal.length() - 2)+appnoOriginal.charAt(appnoOriginal.length()-1);
					appnoList.add(appno);
					// System.out.println("摘要:"+XMLUtil.getCNDescriptionItemByAppno(appno).getAbstr());
				}
				//System.out.println("local engine work");
				return appnoList;
			}
			System.out.println("结果为null");
			return null;

		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	
	
	
	public static String getPatentDataByAppno(String appno, int searchType) {

		try {
			CprsGIISWebSvcStub cprsGIISWebSvcStub = new CprsGIISWebSvcStub();
			GetPatentData request = new GetPatentData();
			request.set_strPID(appno);
			switch (searchType) {
			case 1:
				request.set_PdTpe(PatentDataType.CnMabXmlTxt);
				break;
			case 2:
				request.set_PdTpe(PatentDataType.CnDesXmlTxt);
				break;
			case 3:
				request.set_PdTpe(PatentDataType.CnClmXmlTxt);
				break;
			case 4:
				request.set_PdTpe(PatentDataType.CnAbsFuTuUrl);
				break;
			case 5:
				request.set_PdTpe(PatentDataType.CnWGImgUrls);
				break;

			case 6:
				request.set_PdTpe(PatentDataType.EnMabXmlTxt);
				break;
			case 7:
				request.set_PdTpe(PatentDataType.PDFFileUrl);
				break;
			default:
				request.set_PdTpe(PatentDataType.CnClmXmlTxt);
				break;
			}

			GetPatentDataResponse response = cprsGIISWebSvcStub
					.getPatentData(request);
			//System.out.println("local engine works");
			return response.getGetPatentDataResult();

		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static String getPdfByAppno(String appno) {

		try {
			CprsGIISWebSvcStub cprsGIISWebSvcStub = new CprsGIISWebSvcStub();
			GetPatentData request = new GetPatentData();
			request.set_strPID(appno);
			request.set_PdTpe(PatentDataType.PDFFiles);
			GetPatentDataResponse response = cprsGIISWebSvcStub
					.getPatentData(request);
			String resultString = response.getGetPatentDataResult();
			System.out.println("获取"+appno+"pdf"+"，resultString:"+resultString);
			if (resultString.equals("未查询到此申请号的信息")) {
				return null;
			}
			return response.getGetPatentDataResult();
		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static List<Map<String, String>> getPatentBasicInfo(
			List<String> appnos) {
		List<Map<String, String>> patentList = new ArrayList<Map<String, String>>();
		if (appnos != null && appnos.size() > 0) {
			for (String appno : appnos) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("appno", appno);
				// 这里需要对结果文本进行处理
				String resultText=getPatentDataByAppno(appno, 1);
				int begin= resultText.indexOf("<TITLE>")+7;
				int end= resultText.indexOf("</TITLE>");
				map.put("title", resultText.substring(begin, end));
				patentList.add(map);
			}

			return patentList;
		}
		return null;
	}
	
	public static List<Map<String, String>> getPatentApdDates(List<String> appnos) {
		List<Map<String, String>> patentList = new ArrayList<Map<String, String>>();
		if (appnos != null && appnos.size() > 0) {
			for (String appno : appnos) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("appno", appno);
				// 这里需要对结果文本进行处理
				String resultText=getPatentDataByAppno(appno, 1);
				int begin= resultText.indexOf("<APD>")+7;
				map.put("title", (resultText.substring(begin, begin+4)=="")?"无":resultText.substring(begin, begin+4));
				patentList.add(map);
			}

			return patentList;
		}
		return null;
	}
	
	public static void main(String[] args) {
		
		
//		System.out.println("--+"+(int)(Math.random()*10000)+"+-----");
		
//		DoSearchParameter doSearchParameter  =new DoSearchParameter("1", "Cn", "001", "F XX (计算机$/TI)");
//		System.out.println("---"+getPatentRecordsNumber(doSearchParameter));
		
//		 DoSearchParameter doSearchParameter =new DoSearchParameter("80004",
//		 "Cn", "001", "F XX (计算机/TI)");
		
//		  DoSearchParameter doSearchParameter =new DoSearchParameter("123","Cn", "001", "F XX (计算机/TI*系统/TI)");
//		  GetGeneralDataParameter getGeneralDataParameter=new GetGeneralDataParameter("001", "Cn","001", 70, 10);
//		  System.out.println(getPatentRecordsNumber(doSearchParameter));
//		  System.out.println(getAppnolistBySearch(getGeneralDataParameter) );
//		  System.out.println(getPdfByAppno("201310223269") );
		 
		//System.out.println(ConfigTool.getValue("futu_pre"));

//		List <String> appnos=new ArrayList<String>();
//		appnos.add("201330088788");
//		appnos.add("2014300161343");
		//appnos.add("201330088796");
	//	appnos.add("201330088788");
		//appnos.add("201330059894");
		//System.out.println(getPatentBasicInfo(appnos));
//		String test="<?xml version=1.0 encoding=UTF-8?><Patent> <APNNO>201330088788</APNNO> <PUBNR></PUBNR><APPNR>302504613S</APPNR><APD>2013年3月29日</APD><PUD> </PUD><GRD>2013年7月17日</GRD> <GRPD>2013年7月17日</GRPD> <APPD>2013年7月17日</APPD><NC>44</NC><AGENCY>44215</AGENCY> <ADDRESS>广东省东莞市厚街镇科技工业城东莞市金河田实业有限公司</ADDRESS><AGENT>张明</AGENT> <TITLE>电脑机箱面板（6001）</TITLE> <ZIP>523943</ZIP><ABSTR>1．本外观设计产品的名称：电脑机箱面板（6001）。2．本外观设计产品的用途：一种计算机主机箱的前置面板。3．本外观设计的设计要点：设计要点在于形状。4．最能表明设计要点的图片或者照片：立体图。5．省略后视图。</ABSTR> <CLAIM></CLAIM> <Searcher></Searcher><IPC>14-02</IPC><APPL>东莞市金河田实业有限公司</APPL> <INVENTOR>贺爱余</INVENTOR><PCT /></Patent>"; 
//		int begin= test.indexOf("<TITLE>")+7;
//		int end= test.indexOf("</TITLE>");
//		System.out.println(test.substring(begin, end));
	}
	
	

}
