package patentsearch.util.webservice;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.springframework.stereotype.Service;
import org.tempuri.PtDataSvcStub;
import org.tempuri.PtDataSvcStub.DoSearch_ByQuery;
import org.tempuri.PtDataSvcStub.DoSearch_ByQueryResponse;
import org.tempuri.PtDataSvcStub.GeneralDataInfo;
import org.tempuri.PtDataSvcStub.GetGeneralData;
import org.tempuri.PtDataSvcStub.GetGeneralDataResponse;
import org.tempuri.PtDataSvcStub.GetPatentData;
import org.tempuri.PtDataSvcStub.GetPatentDataResponse;
import org.tempuri.PtDataSvcStub.PatentDataType;
import org.tempuri.PtDataSvcStub.SearchDbType;

import patentsearch.bean.search.requestParameter.DoSearchParameter;
import patentsearch.bean.search.requestParameter.GetGeneralDataParameter;
import patentsearch.bean.util.xml.XMLUtil;
import patentsearch.service.legalstatus.LegalStatusService;

@Service
public class WebServiceClientUtil {
	
	
	public static String getPatentRecordsNumber(DoSearchParameter doSearchParameter) {
		 

		try {
			PtDataSvcStub ptDataSvcStub = new PtDataSvcStub();
			DoSearch_ByQuery doSearch = new DoSearch_ByQuery();
			doSearch.set_strUID(doSearchParameter.getuID());
			if("DocDB".equals(doSearchParameter.getDbType())){
				doSearch.set_SDbType(SearchDbType.DocDB);
			}else if("Cn".equals(doSearchParameter.getDbType())){
				doSearch.set_SDbType(SearchDbType.Cn);
			}
//			doSearch.set_SDbType(SearchDbType.Cn);
			doSearch.set_strSID(doSearchParameter.getsID());
			doSearch.set_strSearchQuery(doSearchParameter.getSearchFormula()) ;
			DoSearch_ByQueryResponse getGeneralDataResponse = ptDataSvcStub
					.doSearch_ByQuery(doSearch); 
			return getGeneralDataResponse.getDoSearch_ByQueryResult();
 
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	public static List<String> getAppnolistBySearch(patentsearch.bean.search.requestParameter.GetGeneralDataParameter getGeneralData) {
		List<String> appnoList = new ArrayList<String>();

		try {
			PtDataSvcStub ptDataSvcStub = new PtDataSvcStub();
			GetGeneralData generalData = new GetGeneralData();
			generalData.set_strUID(getGeneralData.getuID());
			if("DocDB".equals(getGeneralData.getDbType())){
				generalData.set_SDbType(SearchDbType.DocDB);
			}else if("Cn".equals(getGeneralData.getDbType())){
				generalData.set_SDbType(SearchDbType.Cn);
			}
				
//			generalData.set_SDbType(SearchDbType.Cn);
			generalData.set_strSID(getGeneralData.getsID());
			generalData.set_pageNo(getGeneralData.getPageNo());
			generalData.set_pageSize(getGeneralData.getPageSize());
			GetGeneralDataResponse getGeneralDataResponse = ptDataSvcStub
					.getGeneralData(generalData);
						if(getGeneralDataResponse!=null&&getGeneralDataResponse
								.getGetGeneralDataResult()!=null&&getGeneralDataResponse
								.getGetGeneralDataResult().getGeneralDataInfo()!=null){
							 
							//System.out.println(getGeneralDataResponse.getGetGeneralDataResult().getGeneralDataInfo()==null);
							for (GeneralDataInfo generalDataInfo : getGeneralDataResponse
									.getGetGeneralDataResult().getGeneralDataInfo()) {
								
									//?????????????????????????????????????注意这里国外的返回公开号待解决
									// 引擎webservice中检索的有ExtensionData、NCPIC、StrTI、StrTrsTI、StrAN、StrAD、StrIPC、NMembers、StrPtCode
									// 因为这里检索的专利信息不全，还得通过中国专利著录项目XML来获得详细，所以这里还得封装CNDescriptionItem
									// System.out.println(generalDataInfo.getStrAN()+":"+generalDataInfo.getStrTI());
									// 测试代码 先对申请号进行截取去取.与最后一位
									String appno = generalDataInfo.getStrAN();
//									String appno = generalDataInfo.getStrAN().substring(0,generalDataInfo.getStrAN().length() - 2);
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
	public static List<String> getPubIdListlistBySearch(patentsearch.bean.search.requestParameter.GetGeneralDataParameter getGeneralData) {
		List<String> pubidList = new ArrayList<String>();

		try {
			PtDataSvcStub ptDataSvcStub = new PtDataSvcStub();
			GetGeneralData generalData = new GetGeneralData();
			generalData.set_strUID(getGeneralData.getuID());
			if("DocDB".equals(getGeneralData.getDbType())){
				generalData.set_SDbType(SearchDbType.DocDB);
			}else if("Cn".equals(getGeneralData.getDbType())){
				generalData.set_SDbType(SearchDbType.Cn);
			}
				
//			generalData.set_SDbType(SearchDbType.Cn);
			generalData.set_strSID(getGeneralData.getsID());
			generalData.set_pageNo(getGeneralData.getPageNo());
			generalData.set_pageSize(getGeneralData.getPageSize());
			GetGeneralDataResponse getGeneralDataResponse = ptDataSvcStub
					.getGeneralData(generalData);
						if(getGeneralDataResponse!=null&&getGeneralDataResponse
								.getGetGeneralDataResult()!=null&&getGeneralDataResponse
								.getGetGeneralDataResult().getGeneralDataInfo()!=null){
							 
							//System.out.println(getGeneralDataResponse.getGetGeneralDataResult().getGeneralDataInfo()==null);
							for (GeneralDataInfo generalDataInfo : getGeneralDataResponse
									.getGetGeneralDataResult().getGeneralDataInfo()) {
								
									String pubId = generalDataInfo.getStrPubID();
//									String appno = generalDataInfo.getStrAN().substring(0,generalDataInfo.getStrAN().length() - 2);
									pubidList.add(pubId);
									 // System.out.println("摘要:"+XMLUtil.getCNDescriptionItemByAppno(appno).getAbstr());
							}
							return pubidList;
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
	
	public static String getPatentDataByAppno(String appno ,int searchType) {
		 

		try {
			PtDataSvcStub ptDataSvcStub = new PtDataSvcStub();
			GetPatentData request = new GetPatentData();
			request.set_strPID(appno);
			switch (searchType) {
			case 1:
				request.set_PdTpe(PatentDataType.CnMabXmlTxt) ;
				break;
			case 2:
				request.set_PdTpe(PatentDataType.CnDesXmlTxt) ;
				break;
			case 3:
				request.set_PdTpe(PatentDataType.CnClmXmlTxt) ;
				break;
			case 4:
				request.set_PdTpe(PatentDataType.CnAbsFuTuUrl) ;
				break;
			case 5:
				request.set_PdTpe(PatentDataType.CnWGImgUrls) ;
				break;
			 
			case 6:
				request.set_PdTpe(PatentDataType.EnMabXmlTxt) ;
				break;
			case 7:
				request.set_PdTpe(PatentDataType.PDFFileUrl) ;
				break;
			default:
				request.set_PdTpe(PatentDataType.CnClmXmlTxt) ;
				break;
			}
			
			GetPatentDataResponse response = ptDataSvcStub.getPatentData(request);
			return response.getGetPatentDataResult();
 
		} catch (AxisFault e) {
			e.printStackTrace();
			return "远程数据调用失败!";
			
		} catch (RemoteException e) {
			e.printStackTrace();
			return "远程数据调用失败!";
		}  

	}
	
	public static void main(String[] args) {
		
//		String ss=WebServiceClientUtil.getPatentDataByAppno("201010185493", 2);
//		System.out.println(ss);
		
		String ss= WebServiceClientUtil.getPatentDataByAppno("US2010057524A1",6); //////////////////////////3333
		//System.out.println(ss);
//		///////////////////////////////////////
//		String interIpcFormula="asdasd;sad;dd;ff";
//		if(interIpcFormula.contains(";")){
//			System.out.println("---0");
//			interIpcFormula=interIpcFormula.replace(';', '+');
//		}	
//		System.out.println(interIpcFormula);
		//////////////////////////////

//		DoSearchParameter doSearchParameter  =new DoSearchParameter("1", "DocDB", "001", "F XX (computer/TI)");
//		System.out.println("---"+getPatentRecordsNumber(doSearchParameter));/////////////////////11111111111
		
		
////		DoSearchParameter doSearchParameter  =new DoSearchParameter("1", "Cn", "001", "F XX (计算机/TI)");
		
//		GetGeneralDataParameter getGeneralDataParameter=new GetGeneralDataParameter("1", "DocDB", "001", 70, 10);
//		System.out.println("+++"+getPubIdListlistBySearch(getGeneralDataParameter) );///////////////222222222
		
		
//		---24083
//		+++[201320561096, 201320554833, 201320532833, 201320527119, 201320520640, 201320509855, 201320497316, 201320469438, 201320444227, 201320406897]
//		---212068
//		+++[US20030632665, JP19990111343, JP19980002464, JP20010161424, JP19860215442, JP19900130361, CN20102543766U, JP20040278262, SU19772459901, FR20050009822]
		
	}

}
