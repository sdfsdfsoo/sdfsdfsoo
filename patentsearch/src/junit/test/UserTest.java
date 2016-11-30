package junit.test;




import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.axis2.AxisFault;
import org.apache.struts2.ServletActionContext;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.tempuri.local.CprsGIISWebSvcStub;
import org.tempuri.local.GeneralDataInfo;
import org.tempuri.local.GetGeneralData;
import org.tempuri.local.GetGeneralDataResponse;
import org.tempuri.local.SearchDbType;

import aTestSpeed.RedisTest;

import patentsearch.bean.base.LegalStatusDetail;
import patentsearch.bean.cndescriptionitem.CNDescriptionItem;
import patentsearch.bean.cndescriptionitem.EnDescriptionItem;
import patentsearch.bean.search.requestParameter.DoSearchParameter;
import patentsearch.bean.search.requestParameter.GetGeneralDataParameter;
import patentsearch.bean.user.Users;
import patentsearch.service.legalstatus.LegalStatusService;
import patentsearch.service.legalstatus.impl.LegalStatusServiceImpl;
import patentsearch.service.patent.SearchService;
import patentsearch.service.patent.impl.SearchServiceImpl;
import patentsearch.util.webservice.WebServiceLocalClientUtil;
import patentsearch.utils.base.ConfigTool;
import patentsearch.utils.base.WebTool;
import redis.clients.jedis.Jedis;



public class UserTest {
	private static  ApplicationContext act ;  
	 @BeforeClass
	public static void setUpBeforeClass() throws Exception {
		  act =  new ClassPathXmlApplicationContext("beans.xml");
		 
		 
	} 
	 
	 private Integer getLegalStatusType(String appno, String pubnr, Date pud, String appnr, Date appd) {
		 LegalStatusService legalStatusService = new LegalStatusServiceImpl();
			List<LegalStatusDetail> legalStatusDetailList = legalStatusService     
					.getLegalStatusDetail(appno);
			Integer LegalStatusType = 2;
			if (legalStatusDetailList != null && legalStatusDetailList.size() > 0     //程序在这里只用第一个，为什么？  //原因是按照倒序排列，状态以第一个为准（也就是时间最靠后的为准）
					&& !"".equals(legalStatusDetailList.get(0).getCategory())) {

				if ("失效".equals(legalStatusDetailList.get(0).getCategory().trim())) {
					LegalStatusType = 3;
				} else if ("审中".equals(legalStatusDetailList.get(0).getCategory()
						.trim())) {
					LegalStatusType = 5;
				} else if ("无效".equals(legalStatusDetailList.get(0).getCategory()
						.trim())) {
					LegalStatusType = 1;
				} else if ("有效".equals(legalStatusDetailList.get(0).getCategory()
						.trim())) {
					LegalStatusType = 4;
				}
			} else if((legalStatusDetailList == null || legalStatusDetailList.size() == 0) && pubnr != null && pud!= null && appnr == null && appd== null){
				LegalStatusType = 5;
			} else if ((legalStatusDetailList == null || legalStatusDetailList.size() == 0) && appnr != null && appd != null){
				LegalStatusType = 4;
			}
			return LegalStatusType;
		}
	 
	 @Test
	 public void testSpeed() throws IOException, RowsExceededException, WriteException{
		 
		 /*GetGeneralDataParameter getGeneralDataParameter = new GetGeneralDataParameter("11", "Cn", "999", page, rows);
		 cNDescriptionItemList = searchService.handleGetGeneralData(getGeneralDataParameter);*/

			String strDateUID=(int)(Math.random()*1000)+"";
			 SearchService searchService = new SearchServiceImpl();
			 DoSearchParameter doSearch = new DoSearchParameter("149"+strDateUID, "Cn", "999","F XX (镇江/DZ)@LX=FM");
				Long total = searchService.handleDoSearch(doSearch);
				List<CNDescriptionItem> cNDescriptionItemList=null;
				List<CNDescriptionItem> cNDescriptionItemList2=new ArrayList<CNDescriptionItem>();
				Integer page = 1;
				
				int numall=Integer.valueOf(total.toString());
				HashMap<String, Integer> hm = new HashMap<String, Integer>();
				
				for(int i=1;i<=numall;i++){
					System.out.println(i);
					GetGeneralDataParameter getGeneralDataParameter = new GetGeneralDataParameter("149"+strDateUID, "Cn", "999", i, 1);
					cNDescriptionItemList = searchService.handleGetGeneralData(getGeneralDataParameter);
					if(cNDescriptionItemList.size()==0){
						continue;
					}
					cNDescriptionItemList2.add(cNDescriptionItemList.get(0));
					
					CNDescriptionItem cNDescriptionItemTemp= new CNDescriptionItem();
					cNDescriptionItemTemp=cNDescriptionItemList.get(0);
					
					int state=getLegalStatusType(cNDescriptionItemTemp.getAppno(),cNDescriptionItemTemp.getPubnr(),cNDescriptionItemTemp.getPud(),cNDescriptionItemTemp.getAppnr(),cNDescriptionItemTemp.getAppd());
					if(state!=4){
						continue;
					}
					
					if(hm.keySet().toString().indexOf(cNDescriptionItemTemp.getAppl())==-1){
						hm.put(cNDescriptionItemTemp.getAppl(), 1);
					}
					else{
						//if(null==cNDescriptionItemTemp.getAppl()){
							//continue;
						//}
						try{
							int tempsum = Integer.valueOf(hm.get(cNDescriptionItemTemp.getAppl()).toString())+1;
							//tempsum
							hm.put(cNDescriptionItemTemp.getAppl(), tempsum);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
					
					
					//System.out.println(hm);
				}
				
				
				strDateUID=(int)(Math.random()*1000)+"";
				 doSearch = new DoSearchParameter("149"+strDateUID, "Cn", "999","F XX (镇江/DZ)@LX=XX,FM,WG");
				total = searchService.handleDoSearch(doSearch);
				cNDescriptionItemList=null;
				cNDescriptionItemList2=new ArrayList<CNDescriptionItem>();
				page = 1;
					
				numall=Integer.valueOf(total.toString());
					HashMap<String, Integer> hm0 = new HashMap<String, Integer>();
					
					for(int i=1;i<=numall;i++){
						System.out.println(i);
						GetGeneralDataParameter getGeneralDataParameter = new GetGeneralDataParameter("149"+strDateUID, "Cn", "999", i, 1);
						cNDescriptionItemList = searchService.handleGetGeneralData(getGeneralDataParameter);
						if(cNDescriptionItemList.size()==0){
							continue;
						}
						cNDescriptionItemList2.add(cNDescriptionItemList.get(0));
						
						CNDescriptionItem cNDescriptionItemTemp= new CNDescriptionItem();
						cNDescriptionItemTemp=cNDescriptionItemList.get(0);
						
						int state=getLegalStatusType(cNDescriptionItemTemp.getAppno(),cNDescriptionItemTemp.getPubnr(),cNDescriptionItemTemp.getPud(),cNDescriptionItemTemp.getAppnr(),cNDescriptionItemTemp.getAppd());
						if(state!=4){
							continue;
						}
						
						if(hm0.keySet().toString().indexOf(cNDescriptionItemTemp.getAppl())==-1){
							hm0.put(cNDescriptionItemTemp.getAppl(), 1);
						}
						else{
							//if(null==cNDescriptionItemTemp.getAppl()){
								//continue;
							//}
							try{
								int tempsum = Integer.valueOf(hm0.get(cNDescriptionItemTemp.getAppl()).toString())+1;
								//tempsum
								hm0.put(cNDescriptionItemTemp.getAppl(), tempsum);
							}catch(Exception e){
								e.printStackTrace();
							}
						}
						
						
						//System.out.println(hm);
					}
					
					
				strDateUID=(int)(Math.random()*1000)+"";
				 doSearch = new DoSearchParameter("149"+strDateUID, "Cn", "999","F XX (镇江/DZ)@LX=XX");
				total = searchService.handleDoSearch(doSearch);
				cNDescriptionItemList=null;
				cNDescriptionItemList2=new ArrayList<CNDescriptionItem>();
				page = 1;
					
				numall=Integer.valueOf(total.toString());
					HashMap<String, Integer> hm2 = new HashMap<String, Integer>();
					
					for(int i=1;i<=numall;i++){
						System.out.println(i);
						GetGeneralDataParameter getGeneralDataParameter = new GetGeneralDataParameter("149"+strDateUID, "Cn", "999", i, 1);
						cNDescriptionItemList = searchService.handleGetGeneralData(getGeneralDataParameter);
						if(cNDescriptionItemList.size()==0){
							continue;
						}
						cNDescriptionItemList2.add(cNDescriptionItemList.get(0));
						
						CNDescriptionItem cNDescriptionItemTemp= new CNDescriptionItem();
						cNDescriptionItemTemp=cNDescriptionItemList.get(0);
						
						int state=getLegalStatusType(cNDescriptionItemTemp.getAppno(),cNDescriptionItemTemp.getPubnr(),cNDescriptionItemTemp.getPud(),cNDescriptionItemTemp.getAppnr(),cNDescriptionItemTemp.getAppd());
						if(state!=4){
							continue;
						}
						
						if(hm2.keySet().toString().indexOf(cNDescriptionItemTemp.getAppl())==-1){
							hm2.put(cNDescriptionItemTemp.getAppl(), 1);
						}
						else{
							//if(null==cNDescriptionItemTemp.getAppl()){
								//continue;
							//}
							try{
								int tempsum = Integer.valueOf(hm2.get(cNDescriptionItemTemp.getAppl()).toString())+1;
								//tempsum
								hm2.put(cNDescriptionItemTemp.getAppl(), tempsum);
							}catch(Exception e){
								e.printStackTrace();
							}
						}
						
						
						//System.out.println(hm);
					}
					
					
					strDateUID=(int)(Math.random()*1000)+"";
					 doSearch = new DoSearchParameter("149"+strDateUID, "Cn", "999","F XX (镇江/DZ)@LX=WG");
					total = searchService.handleDoSearch(doSearch);
					cNDescriptionItemList=null;
					cNDescriptionItemList2=new ArrayList<CNDescriptionItem>();
					page = 1;
						
					numall=Integer.valueOf(total.toString());
						HashMap<String, Integer> hm3 = new HashMap<String, Integer>();
						
						for(int i=1;i<=numall;i++){
							System.out.println(i);
							GetGeneralDataParameter getGeneralDataParameter = new GetGeneralDataParameter("149"+strDateUID, "Cn", "999", i, 1);
							cNDescriptionItemList = searchService.handleGetGeneralData(getGeneralDataParameter);
							if(cNDescriptionItemList.size()==0){
								continue;
							}
							cNDescriptionItemList2.add(cNDescriptionItemList.get(0));
							
							CNDescriptionItem cNDescriptionItemTemp= new CNDescriptionItem();
							cNDescriptionItemTemp=cNDescriptionItemList.get(0);
							
							int state=getLegalStatusType(cNDescriptionItemTemp.getAppno(),cNDescriptionItemTemp.getPubnr(),cNDescriptionItemTemp.getPud(),cNDescriptionItemTemp.getAppnr(),cNDescriptionItemTemp.getAppd());
							if(state!=4){
								continue;
							}
							
							if(hm3.keySet().toString().indexOf(cNDescriptionItemTemp.getAppl())==-1){
								hm3.put(cNDescriptionItemTemp.getAppl(), 1);
							}
							else{
								//if(null==cNDescriptionItemTemp.getAppl()){
									//continue;
								//}
								try{
									int tempsum = Integer.valueOf(hm3.get(cNDescriptionItemTemp.getAppl()).toString())+1;
									//tempsum
									hm3.put(cNDescriptionItemTemp.getAppl(), tempsum);
								}catch(Exception e){
									e.printStackTrace();
								}
							}
							
							
							//System.out.println(hm);
						}
					
					
				Iterator it = hm.entrySet().iterator();
				Iterator it2 = hm2.entrySet().iterator();
				Iterator it3 = hm3.entrySet().iterator();
				Iterator it0 = hm0.entrySet().iterator();
				Label label = null;
				FileOutputStream fos = new FileOutputStream("C:\\test02.xls");
				
				WritableWorkbook workbook = jxl.Workbook.createWorkbook(fos);
				WritableSheet sheet = workbook.createSheet("111", 0);
				label = new Label(0,0,"单位名称");
				sheet.addCell(label);
				label = new Label(1,0,"发明授权");
				sheet.addCell(label);
				label = new Label(3,0,"外观");
				sheet.addCell(label);
				label = new Label(5,0,"实用新型");
				sheet.addCell(label);
				int i=1;
				while(it0.hasNext()){
					Map.Entry entry0 = (Map.Entry)it0.next();
					
					label = new Label(0,i,entry0.getKey().toString());
					sheet.addCell(label);
					
					label = new Label(7,i,entry0.getValue().toString());
					sheet.addCell(label);
					
					while(it.hasNext()){
						Map.Entry entry = (Map.Entry)it.next();
						if(entry0.getKey().toString().trim().indexOf(entry.getKey().toString().trim())!=-1){
							label = new Label(1,i,entry.getValue().toString());
							sheet.addCell(label);
							break;
						}
						
					}
					it = hm.entrySet().iterator();
					
					while(it2.hasNext()){
						Map.Entry entry2 = (Map.Entry)it2.next();
						if(entry0.getKey().toString().trim().indexOf(entry2.getKey().toString().trim())!=-1){
							label = new Label(5,i,entry2.getValue().toString());
							sheet.addCell(label);
							break;
						}
						
					}
					it2 = hm2.entrySet().iterator();
					
					while(it3.hasNext()){
						Map.Entry entry3 = (Map.Entry)it3.next();
						if(entry0.getKey().toString().trim().indexOf(entry3.getKey().toString().trim())!=-1){
							label = new Label(3,i,entry3.getValue().toString());
							sheet.addCell(label);
							break;
						}
						
					}
					it3 = hm3.entrySet().iterator();
					
					
					
					
					
					i++;
				}
				workbook.write();
				workbook.close();
				fos.close();
				System.out.println(hm);
				
				
				
				
				
				
				/*GetGeneralDataParameter getGeneralDataParameter = new GetGeneralDataParameter("149"+strDateUID, "Cn", "999", 1, Integer.valueOf(total.toStrng()));
				cNDescriptionItemList = searchService.handleGetGeneralData(getGeneralDataParameter);*/
				
				System.out.println(total);
	 }
	 
	/* @Test
	 public void testSpeed(){
		 SearchService searchService = new SearchServiceImpl();
		 DoSearchParameter doSearch = new DoSearchParameter("11", "Cn", "999","F XX (计算机/AB+计算机/CL+计算机/TI+计算机/IN+计算机/PA+计算机/AT+计算机/DZ)@LX=WG");
			Long total = searchService.handleDoSearch(doSearch);
			System.out.println(total);
	 }*/
	 
	 
	 
	 public List<String> getappnoValuesBySearchWithLastNum(patentsearch.bean.search.requestParameter.GetGeneralDataParameter getGeneralData){

		 List<String> appnoValues = new ArrayList<String>();
			Jedis jedis = new Jedis("11.0.0.26", Integer.parseInt(ConfigTool.getValue("redisport")));
			
			
			List<String> appnoList = new ArrayList<String>();
			//System.out.println(new Date().getTime());
		     

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
					
					//从jedis中取数据

					//RedisTest.selectForAnalysis(user,appnoList, jedis);  
					
					for(int i=0;i<appnoList.size();i++){
						  
						  String appno=appnoList.get(i);
						  String appnoValue=jedis.get("Jly"+appno);//将分析用到的这些信息进行连接
						  
						  if(appnoValue!=null&&(!"".equals(appnoValue))){
							  appnoValues.add(appnoValue);
						  }
					}
					System.out.println(appnoValues.size());
					
					return appnoValues;
					
				}
				System.out.println("结果为null");
				//return null;

			} catch (AxisFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
	 }
	 
	 /**
		 * 测试从redis中取9W多数据的效率
		 */
		@Test
		public void testjedisSpeed(){
			
			
			SearchService searchService  = new SearchServiceImpl();;
			
			//Users user = WebTool.getLoginedUser(ServletActionContext.getRequest()); 
			String searchFormula= "F XX (句容/DZ)@LX=WG,FM,XX";//
			
			String strUserId=149+""; 
			
			DoSearchParameter doSearch = new DoSearchParameter(strUserId, "Cn", "999",searchFormula);
			int total=new Long(searchService.handleDoSearch(doSearch)).intValue();
			List<String> appnoValues = new ArrayList<String>();
			System.out.println(total);
			int totalPage = total / 2000;
			System.out.println(new Date().getTime());
			if (totalPage < 1) {
				GetGeneralDataParameter getGeneralData = new GetGeneralDataParameter(strUserId, "Cn", "999", 1, total);
				appnoValues.addAll(getappnoValuesBySearchWithLastNum(getGeneralData));	
					
			} else {
				for (int i = 1; i <= totalPage; i++) {
					GetGeneralDataParameter getGeneralData = new GetGeneralDataParameter(strUserId, "Cn", "999", i, 2000);
					appnoValues.addAll(getappnoValuesBySearchWithLastNum(getGeneralData));
					
				}
			}
			System.out.println(appnoValues.size());
			
			
			//GetGeneralDataParameter getGeneralData = new GetGeneralDataParameter(strUserId, "Cn", "999", 1, total);
			
			
			
		}
		
	 
	/*
	@Test
	public void test1(){
		  LegalStatusService legalStatusService  =(LegalStatusService) act.getBean("legalStatusServiceImpl");;
		 System.out.println(legalStatusService.getCnLegalStatusByAppnp("882004115"));
		
	}
	 
	@Test
	public void test2() throws Exception{
		 // ProvinceCityService provinceCityService  =(ProvinceCityService) act.getBean("provinceCityServiceImpl");;
		 //System.out.println(provinceCityService.detailInfo(provinceCityService.getScrollData().getResultlist(),"34"));
		 try {
	           
			 URL url=new URL("http://11.0.0.26:8090/Figure_S/2013/20098/201320098254.gif"); //将此处换成要测试是否连通的地址       
	            HttpURLConnection   httpConnection   =   (HttpURLConnection)url.openConnection(); 
	             
	            int   responseCode=httpConnection.getResponseCode(); 
	            System.out.println(responseCode);
	            if(responseCode==200) {//如果响应码为200说明此地址是通的，则跳到该地址，
	            	System.out.println("http://11.0.0.26:8090/Figure_S/2013/30106/201330106569.gif"); 
	            }else{//否则跳到另外的地址
	            	 System.out.println("201330106569.gif file not exit");
	            }          
	        } catch (MalformedURLException e) {           
	            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
	        } catch (IOException e) {
	           
	            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
	        }
		 
		 
		
	}
	@Test
	public void test3() throws Exception{
		  ProvinceCityService provinceCityService  =(ProvinceCityService) act.getBean("provinceCityServiceImpl");;
		 System.out.println(provinceCityService.getCoListByTerm("国"));
		 
		
	}*/
	
	/*@Test
	public void test4() throws Exception{
		LegalStatusListService legalStatusListService  =(LegalStatusListService) act.getBean("legalStatusListServiceImpl");;
		List<LegalStatusDetail> childList=legalStatusListService. getLegalStatusChildList("专利权的无效宣告");
		 System.out.println(childList);
		 StringBuilder inSet=new StringBuilder("(");
		 for(int i=0;i<childList.size();i++){
			 inSet.append("'"+childList.get(i).getLegalStatusInfo()+"',");
		 }
		 inSet.deleteCharAt(inSet.length()-1);
		 inSet.append(")");
		 
		 System.out.println(inSet);
		 
	}*/
	 @Test
	public void test5() throws Exception{
		SearchService searchService  =(SearchService) act.getBean("searchServiceImpl");
		List<String> appnoList=new ArrayList<String>();
		appnoList.add("200980114629");
		appnoList.add("200880128642");
		appnoList.add("201010275089");
		 System.out.println(searchService.getPatentItemList(appnoList));
		
		 
	} 

}
