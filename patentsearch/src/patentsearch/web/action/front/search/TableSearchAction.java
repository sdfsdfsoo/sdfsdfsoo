package patentsearch.web.action.front.search;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import patentsearch.bean.base.Agency;
import patentsearch.bean.base.LegalStatusDetail;
import patentsearch.bean.base.PdfEntity;
import patentsearch.bean.base.ProvinceCity;
import patentsearch.bean.base.QueryResult;
import patentsearch.bean.cndescriptionitem.CNDescriptionItem;
import patentsearch.bean.cndescriptionitem.CnLegalStatus;
import patentsearch.bean.cndescriptionitem.EnDescriptionItem;
import patentsearch.bean.search.requestParameter.DoSearchParameter;
import patentsearch.bean.search.requestParameter.GetGeneralDataParameter;
import patentsearch.bean.user.PatentCategory;
import patentsearch.bean.user.PatentStoreInfo;
import patentsearch.bean.user.SearchFormula;
import patentsearch.bean.user.SearchHistory;
import patentsearch.bean.user.Users;
import patentsearch.bean.util.xml.DateUtil;
import patentsearch.bean.util.xml.LegalUtil;
import patentsearch.bean.util.xml.XMLUtil;
import patentsearch.service.base.AgencyService;
import patentsearch.service.base.ProvinceCityService;
import patentsearch.service.legalstatus.LegalStatusService;
import patentsearch.service.patent.SearchService;
import patentsearch.service.patent.impl.SearchServiceImpl;
import patentsearch.service.user.CategoryService;
import patentsearch.service.user.PatentStoreInfoService;
import patentsearch.service.user.SearchFormulaService;
import patentsearch.service.user.SearchHistoryService;
import patentsearch.service.user.UserService;
import patentsearch.util.webservice.WebServiceLocalClientUtil;
import patentsearch.utils.base.ConfigTool;
import patentsearch.utils.base.ExcelTool2007;
import patentsearch.utils.base.FileTool;
import patentsearch.utils.base.StringUtil;
import patentsearch.utils.base.WebTool;
import patentsearch.utils.base.jedis.ImportJedis;
import redis.clients.jedis.Jedis;
import aTestSpeed.RedisTest;

import com.opensymphony.xwork2.ActionContext;

/*
 *   表格检索与智能检索控制器
 */
@Controller
@Scope("prototype")
public class TableSearchAction {
	
	@Resource
	CategoryService categoryService;
	@Resource
	PatentStoreInfoService patentStoreInfoService;
	@Resource
	SearchService searchService;
	@Resource
	LegalStatusService legalStatusService;
	@Resource
	ProvinceCityService provinceCityService;
	@Resource
	AgencyService agencyService;
	@Resource
	SearchFormulaService searchFormualService;
	
	@Resource
	SearchHistoryService searchHistoryService;
	
	@Resource
	UserService userService;
	
	private  List<SearchHistory> searchHistorys;
	
	
	
	private static List<ProvinceCity> provinceCityList;
	// 用于接收datagrid查询参数并用于回显以完成翻页请求
	private Long total;
	private Integer rows = 10;
	private Integer page = 1;
	// 检索式参数
	private String searchFormula;
	// 检索类型
	private String searchType;
	//中国检索还是世界检索
	private String searchscope;
	// 智能检索中关键字
	private String keyword;

	private EnDescriptionItem enDescriptionItem = new EnDescriptionItem();
	
	private CNDescriptionItem cnDescriptionItem = new CNDescriptionItem();
	
	// 接收来自智能检索或者叫算式检索中的请求参数
	private SearchFormula searchFormulaEntity = new SearchFormula();
	
	private String searchFormulaOld;
	
	private List<CNDescriptionItem> cNDescriptionItemList;
	private List<EnDescriptionItem> enDescriptionItemList;
	
//	static {
//	JedisPoolConfig jpconfig = new JedisPoolConfig();
//	JedisPool jedispool =   new JedisPool(jpconfig,"11.0.0.26", Integer.parseInt(ConfigTool.getValue("redisport")),10000);
//	}
	
	Jedis jedis = new Jedis("11.0.0.99", Integer.parseInt(ConfigTool.getValue("redisport")));
	
	
	
	//接受上传的xls文件
	private File fileupload;
	/* 获取上传文件的文件名，属性名写法固定 */
	private String fileuploadFileName;
	/* 获取上传文件的类型，写法仍然固定 */
	private String fileuploadContentType;

	/* 文件路径 */
	private String filePath;
	
	
	
	
	
	
	
	public File getFileupload() {
		return fileupload;
	}

	public void setFileupload(File fileupload) {
		this.fileupload = fileupload;
	}

	public String getFileuploadFileName() {
		return fileuploadFileName;
	}

	public void setFileuploadFileName(String fileuploadFileName) {
		this.fileuploadFileName = fileuploadFileName;
	}

	public String getFileuploadContentType() {
		return fileuploadContentType;
	}

	public void setFileuploadContentType(String fileuploadContentType) {
		this.fileuploadContentType = fileuploadContentType;
	}


	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public List<SearchHistory> getSearchHistorys() {
		return searchHistorys;
	}

	public void setSearchHistorys(List<SearchHistory> searchHistorys) {
		this.searchHistorys = searchHistorys;
	}

	public String getSearchFormulaOld() {
		return searchFormulaOld;
	}

	public void setSearchFormulaOld(String searchFormulaOld) {
		this.searchFormulaOld = searchFormulaOld;
	}

	public String getSearchscope() {
		return searchscope;
	}

	public void setSearchscope(String searchscope) {
		this.searchscope = searchscope;
	}

	public EnDescriptionItem getEnDescriptionItem() {
		return enDescriptionItem;
	}

	public void setEnDescriptionItem(EnDescriptionItem enDescriptionItem) {
		this.enDescriptionItem = enDescriptionItem;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getSearchFormula() {
		return searchFormula;
	}

	public void setSearchFormula(String searchFormula) {
		this.searchFormula = searchFormula;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public CNDescriptionItem getCnDescriptionItem() {
		return cnDescriptionItem;
	}

	public void setCnDescriptionItem(CNDescriptionItem cnDescriptionItem) {
		this.cnDescriptionItem = cnDescriptionItem;
	}

	public SearchFormula getSearchFormulaEntity() {
		return searchFormulaEntity;
	}

	public void setSearchFormulaEntity(SearchFormula searchFormulaEntity) {
		this.searchFormulaEntity = searchFormulaEntity;
	}

	/**
	 * 表格检索界面
	 */
	public String tableSearchUI() {
		return "tableSearchUI";
	}

	/**
	 * 智能检索界面
	 */
	public String smartSearchUI() {
		return "smartSearchUI";
	}

	/**
	 * 专家检索界面
	 */
	public String expertSearchUI() { 
		return "expertSearchUI";
	}

	
	public void delHistory(){
		String historyId = ServletActionContext.getRequest().getParameter("historyId");
		searchHistoryService.delById(historyId);
	}
	
	/**
	 * 专家检索界面
	 */
	public String searchHistoryUI() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());
		List<SearchHistory> historyList= searchHistoryService.findAll(String.valueOf(user.getId()));
		this.setSearchHistorys(historyList);
		
		return "searchHistoryUI";
	}
	
	public void getPatentIPCTypeNum() throws IOException{
		String searchFormulaPatentType= ServletActionContext.getRequest().getParameter("searchFormulaPatentTypeNum");
		if(searchFormulaPatentType.contains("_")){
			searchFormulaPatentType = searchFormulaPatentType.replace('_', '+');
		}
		
		Users user=WebTool.getLoginedUser(ServletActionContext.getRequest());
		String strDateUID=(int)(Math.random()*1000)+"";
		
		String searchFormulaPatentTypeNew="";
		String res="";
		
		for(int i=0;i<8;i++){
			if(searchFormulaPatentType.contains("@LX")){
				if(i==0){
					searchFormulaPatentTypeNew = searchFormulaPatentType.substring(0,searchFormulaPatentType.indexOf("@")) +"*(A/IC)" +searchFormulaPatentType.substring(searchFormulaPatentType.indexOf("@"), searchFormulaPatentType.length());
				}
				if(i==1){
					searchFormulaPatentTypeNew = searchFormulaPatentType.substring(0,searchFormulaPatentType.indexOf("@")) +"*(B/IC)" +searchFormulaPatentType.substring(searchFormulaPatentType.indexOf("@"), searchFormulaPatentType.length());
				}
				if(i==2){
					searchFormulaPatentTypeNew = searchFormulaPatentType.substring(0,searchFormulaPatentType.indexOf("@")) +"*(C/IC)" +searchFormulaPatentType.substring(searchFormulaPatentType.indexOf("@"), searchFormulaPatentType.length());
				}
				if(i==3){
					searchFormulaPatentTypeNew = searchFormulaPatentType.substring(0,searchFormulaPatentType.indexOf("@")) +"*(D/IC)" +searchFormulaPatentType.substring(searchFormulaPatentType.indexOf("@"), searchFormulaPatentType.length());
				}
				if(i==4){
					searchFormulaPatentTypeNew = searchFormulaPatentType.substring(0,searchFormulaPatentType.indexOf("@")) +"*(E/IC)" +searchFormulaPatentType.substring(searchFormulaPatentType.indexOf("@"), searchFormulaPatentType.length());
				}
				if(i==5){
					searchFormulaPatentTypeNew = searchFormulaPatentType.substring(0,searchFormulaPatentType.indexOf("@")) +"*(F/IC)" +searchFormulaPatentType.substring(searchFormulaPatentType.indexOf("@"), searchFormulaPatentType.length());
				}
				if(i==6){
					searchFormulaPatentTypeNew = searchFormulaPatentType.substring(0,searchFormulaPatentType.indexOf("@")) +"*(G/IC)" +searchFormulaPatentType.substring(searchFormulaPatentType.indexOf("@"), searchFormulaPatentType.length());
				}
				if(i==7){
					searchFormulaPatentTypeNew = searchFormulaPatentType.substring(0,searchFormulaPatentType.indexOf("@")) +"*(H/IC)" +searchFormulaPatentType.substring(searchFormulaPatentType.indexOf("@"), searchFormulaPatentType.length());
				}
			}
			else{
				if(i==0){
					searchFormulaPatentTypeNew = searchFormulaPatentType +"*(A/IC)";
				}
				if(i==1){
					searchFormulaPatentTypeNew = searchFormulaPatentType +"*(B/IC)";
				}
				if(i==2){
					searchFormulaPatentTypeNew = searchFormulaPatentType +"*(C/IC)";
				}
				if(i==3){
					searchFormulaPatentTypeNew = searchFormulaPatentType +"*(D/IC)";
				}
				if(i==4){
					searchFormulaPatentTypeNew = searchFormulaPatentType +"*(E/IC)";
				}
				if(i==5){
					searchFormulaPatentTypeNew = searchFormulaPatentType +"*(F/IC)";
				}
				if(i==6){
					searchFormulaPatentTypeNew = searchFormulaPatentType +"*(G/IC)";
				}
				if(i==7){
					searchFormulaPatentTypeNew = searchFormulaPatentType +"*(H/IC)";
				}
			}
			
			if (this.searchType != null && "expert".equals(this.searchType)) {
				if("Cn".equals(searchscope)){//本地ii
					DoSearchParameter doSearch = new DoSearchParameter(user.getId()+"", "Cn", "999",searchFormulaPatentTypeNew);
					total = searchService.handleDoSearch(doSearch);
				}else if("DocDB".equals(searchscope)){//远程
					DoSearchParameter doSearch = new DoSearchParameter(user.getId()+"", "DocDB", "999",searchFormulaPatentTypeNew);
					total = searchService.handleDoSearchRemote(doSearch);
				}else{
					DoSearchParameter doSearch = new DoSearchParameter(user.getId()+"", "Cn", "999",searchFormulaPatentTypeNew);
					total = searchService.handleDoSearch(doSearch);
				}
			}else{
				if("Cn".equals(searchscope)){//本地
					DoSearchParameter doSearch = new DoSearchParameter(user.getId()+""+strDateUID, "Cn", "999",searchFormulaPatentTypeNew);
					total = searchService.handleDoSearch(doSearch);
				}else if("DocDB".equals(searchscope)){//远程
					DoSearchParameter doSearch = new DoSearchParameter(user.getId()+""+strDateUID, "DocDB", "999",searchFormulaPatentTypeNew);
					total = searchService.handleDoSearchRemote(doSearch);
				}else{
					DoSearchParameter doSearch = new DoSearchParameter(user.getId()+""+strDateUID, "Cn", "999",searchFormulaPatentTypeNew);
					total = searchService.handleDoSearch(doSearch);
				}
				
				if(i!=0){
					res=res+"_"+total;
				}
				else{
					res=total+"";
				}
				
			}
			
		}
		ServletActionContext.getResponse().setCharacterEncoding("utf-8");
		ServletActionContext.getResponse().getWriter().print(res);
		ServletActionContext.getResponse().flushBuffer();
	}
	
	/**
	 * ajax获取不同类型的专利数量
	 * @throws IOException 
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	
	public void getPatentTypeNum() throws IOException{
		String searchFormulaPatentType= ServletActionContext.getRequest().getParameter("searchFormulaPatentTypeNum");
		if(searchFormulaPatentType.contains("_")){
			searchFormulaPatentType = searchFormulaPatentType.replace('_', '+');
		}
		
		Users user=WebTool.getLoginedUser(ServletActionContext.getRequest());
		String strDateUID=(int)(Math.random()*1000)+"";
		
		String searchFormulaPatentTypeNew="";
		String res="";
		
		
		
		for(int i=0;i<3;i++){
				if(searchFormulaPatentType.contains("@LX")){
					searchFormulaPatentType = searchFormulaPatentType.substring(0, searchFormulaPatentType.indexOf("@LX"));
					if(i==0&&searchFormulaPatentType.contains("FM")){
						searchFormulaPatentTypeNew = searchFormulaPatentType + "@LX=FM";
					}
					if(i==1&&searchFormulaPatentType.contains("WG")){
						searchFormulaPatentTypeNew = searchFormulaPatentType + "@LX=WG";
					}
					if(i==2&&searchFormulaPatentType.contains("XX")){
						searchFormulaPatentTypeNew = searchFormulaPatentType + "@LX=XX";
					}
				}
				else{
					if(i==0){
						searchFormulaPatentTypeNew = searchFormulaPatentType + "@LX=FM";
					}
					if(i==1){
						searchFormulaPatentTypeNew = searchFormulaPatentType + "@LX=WG";
					}
					if(i==2){
						searchFormulaPatentTypeNew = searchFormulaPatentType + "@LX=XX";
					}
				}
				
			
			
			if (this.searchType != null && "expert".equals(this.searchType)) {
				if("Cn".equals(searchscope)){//本地ii
					DoSearchParameter doSearch = new DoSearchParameter(user.getId()+"", "Cn", "999",searchFormulaPatentTypeNew);
					total = searchService.handleDoSearch(doSearch);
				}else if("DocDB".equals(searchscope)){//远程
					DoSearchParameter doSearch = new DoSearchParameter(user.getId()+"", "DocDB", "999",searchFormulaPatentTypeNew);
					total = searchService.handleDoSearchRemote(doSearch);
				}else{
					DoSearchParameter doSearch = new DoSearchParameter(user.getId()+"", "Cn", "999",searchFormulaPatentTypeNew);
					total = searchService.handleDoSearch(doSearch);
				}
			}else{
				if("Cn".equals(searchscope)){//本地
					DoSearchParameter doSearch = new DoSearchParameter(user.getId()+""+strDateUID, "Cn", "999",searchFormulaPatentTypeNew);
					total = searchService.handleDoSearch(doSearch);
				}else if("DocDB".equals(searchscope)){//远程
					DoSearchParameter doSearch = new DoSearchParameter(user.getId()+""+strDateUID, "DocDB", "999",searchFormulaPatentTypeNew);
					total = searchService.handleDoSearchRemote(doSearch);
				}else{
					DoSearchParameter doSearch = new DoSearchParameter(user.getId()+""+strDateUID, "Cn", "999",searchFormulaPatentTypeNew);
					total = searchService.handleDoSearch(doSearch);
				}
				
				if(i!=0){
					res=res+"_"+total;
				}
				else{
					res=total+"";
				}
				
			}
		}
		ServletActionContext.getResponse().setCharacterEncoding("utf-8");
		ServletActionContext.getResponse().getWriter().print(res);
		ServletActionContext.getResponse().flushBuffer();
	}
	
	
	/**
	 * 处理表格检索界面检索请求
	 */
	public String tableSearch() {
		//检索式 F MC  G01N21/17;G01N33/483 变成G01N21/17+G01N33/483	

		/*SearchHistory searchHistory = new SearchHistory();
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest()); 
		
		searchHistory.setSearchformula(searchFormula);
		searchHistory.setUserid(String.valueOf(user.getId()));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		searchHistory.setAddtime(sdf.format(new Date()));
		searchHistory.s*/
		int temp = searchFormula.indexOf("*");
		if(temp!=-1){
			String str1 = searchFormula.substring(0,temp);
			String str2 = searchFormula.substring(temp, searchFormula.length()).replace(" ", "+");
			searchFormula = str1+str2;
		}
		if(searchFormula.contains("_")){
			searchFormula=searchFormula.replaceAll("_", "+");
		}
		
		if(this.searchFormulaOld==null||this.searchFormulaOld.equals("")){
			this.setSearchFormulaOld(this.searchFormula.replaceAll("\\+", "_"));
		}
		
		SearchHistory searchHistory = new SearchHistory();
		Users user=WebTool.getLoginedUser(ServletActionContext.getRequest());
		searchHistory.setUserid(String.valueOf(user.getId()));
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String addtime=sdf.format(new Date());
		searchHistory.setAddtime(addtime);
		
		
		
		searchHistory.setSearchformula(searchFormulaOld);
		String strDateUID=(int)(Math.random()*1000)+"";
		if (this.searchType != null && "expert".equals(this.searchType)) {
			if("Cn".equals(searchscope)){//本地ii
				DoSearchParameter doSearch = new DoSearchParameter(user.getId()+"", "Cn", "999",searchFormula);
				total = searchService.handleDoSearch(doSearch);
			}else if("DocDB".equals(searchscope)){//远程
				DoSearchParameter doSearch = new DoSearchParameter(user.getId()+"", "DocDB", "999",searchFormula);
				total = searchService.handleDoSearchRemote(doSearch);
			}else{
				DoSearchParameter doSearch = new DoSearchParameter(user.getId()+"", "Cn", "999",searchFormula);
				total = searchService.handleDoSearch(doSearch);
			}
		}else{
			if("Cn".equals(searchscope)){//本地
				DoSearchParameter doSearch = new DoSearchParameter(user.getId()+""+strDateUID, "Cn", "999",searchFormula);
				total = searchService.handleDoSearch(doSearch);
			}else if("DocDB".equals(searchscope)){//远程
				DoSearchParameter doSearch = new DoSearchParameter(user.getId()+""+strDateUID, "DocDB", "999",searchFormula);
				total = searchService.handleDoSearchRemote(doSearch);
			}else{
				DoSearchParameter doSearch = new DoSearchParameter(user.getId()+""+strDateUID, "Cn", "999",searchFormula);
				total = searchService.handleDoSearch(doSearch);
			}
		}
		
		searchHistory.setResultnum(String.valueOf(total));
		
		searchHistoryService.save(searchHistory);
		
		
		searchFormula=StringUtil.stringToFormula(searchFormula);
		String patentType= ServletActionContext.getRequest().getParameter("patentType");
		String ipcMType= ServletActionContext.getRequest().getParameter("ipcMType");
		String patentTypeFM="";
		String patentTypeWG="";
		String patentTypeXX="";
		if(patentType!=null){
			if(patentType.contains("FM")){
				patentTypeFM="FM";
			}
			if(patentType.contains("WG")){
				patentTypeWG="WG";
			}
			if(patentType.contains("XX")){
				patentTypeXX="XX";
			}
		}else if(patentType==null){	
			patentTypeFM="FM";
			patentTypeWG="WG";
			patentTypeXX="XX";
		}
		ServletActionContext.getRequest().setAttribute("searchscope",searchscope);
		ServletActionContext.getRequest().setAttribute("patentTypeFM",patentTypeFM);
		ServletActionContext.getRequest().setAttribute("patentTypeWG",patentTypeWG);
		ServletActionContext.getRequest().setAttribute("patentTypeXX",patentTypeXX);
		if(null!=ServletActionContext.getRequest().getParameter("patentType")){
			ServletActionContext.getRequest().setAttribute("patentType", patentType);
		}
		if(null!=ServletActionContext.getRequest().getParameter("ipcMType")){
			ServletActionContext.getRequest().setAttribute("ipcMType", ipcMType);
		}
		
		// 接收检索界面检索式
		if (searchType != null && "table".equals(searchType)) {

		} else if ("smart".equals(searchType)) {
			keyword = ServletActionContext.getRequest().getParameter("keyword");
			if("DocDB".equals(searchscope)){
				enDescriptionItem.setInventor(keyword);
				enDescriptionItem.setAppl(keyword);
				enDescriptionItem.setTitle(keyword);
				enDescriptionItem.setAbs(keyword);
				enDescriptionItem.setReferences(keyword);
			}else{
				cnDescriptionItem.setInventor(keyword);
				cnDescriptionItem.setAppl(keyword);
				cnDescriptionItem.setTitle(keyword);
				cnDescriptionItem.setClaim(keyword);
				cnDescriptionItem.setAddress(keyword);
				cnDescriptionItem.setAbstr(keyword);
			}
		} else if ("expert".equals(searchType)) {
			//searchFormula = ServletActionContext.getRequest().getParameter("searchFormula");
			System.out.println("expert.searchFormula,"+searchFormula);
		}
		
		

		return "searchResultUI";

	}
	
	
	public void getSearchResultList(){
		
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest()); 
		if(searchFormula.contains("_")){
			searchFormula=searchFormula.replaceAll("_", "+");
		}
		//System.out.println("Ajax searchFormula:" + searchFormula);
		// 将著录项查询条件进行封装,来自于查询结果页面
		//检索编号，如果来自算式检索就是id  高级检索为2
		Integer searchFormulaId=new Integer(2);
		
		// 如果是来自检索式的点击检索,需要更新检索相关信息
		if (this.searchType != null && "expert".equals(this.searchType)) {
			String idStr = ServletActionContext.getRequest().getParameter(
					"searchFormulaId");
			if (idStr != null && !"".equals(idStr)) {
				try {
					Integer id = Integer.parseInt(idStr);
					searchFormulaId=id;
					SearchFormula searchFormula = searchFormualService.find(id);
					searchFormula.setAlterTime(new Date());
					searchFormula.setHits(total);
					searchFormualService.update(searchFormula);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		String strDateUID=(int)(Math.random()*1000)+"";
		//此处封装enDescriptionItem和cnDescriptionItem
		buildZhuLuXiang(ServletActionContext.getRequest());  
		if (this.searchType != null && "expert".equals(this.searchType)) {
			if("Cn".equals(searchscope)){//本地ii
				DoSearchParameter doSearch = new DoSearchParameter(user.getId()+"", "Cn", "999",searchFormula);
				total = searchService.handleDoSearch(doSearch);
			}else if("DocDB".equals(searchscope)){//远程
				DoSearchParameter doSearch = new DoSearchParameter(user.getId()+"", "DocDB", "999",searchFormula);
				total = searchService.handleDoSearchRemote(doSearch);
			}else{
				DoSearchParameter doSearch = new DoSearchParameter(user.getId()+"", "Cn", "999",searchFormula);
				total = searchService.handleDoSearch(doSearch);
			}
		}else{
			if("Cn".equals(searchscope)){//本地
				DoSearchParameter doSearch = new DoSearchParameter(user.getId()+""+strDateUID, "Cn", "999",searchFormula);
				total = searchService.handleDoSearch(doSearch);
			}else if("DocDB".equals(searchscope)){//远程
				DoSearchParameter doSearch = new DoSearchParameter(user.getId()+""+strDateUID, "DocDB", "999",searchFormula);
				total = searchService.handleDoSearchRemote(doSearch);
			}else{
				DoSearchParameter doSearch = new DoSearchParameter(user.getId()+""+strDateUID, "Cn", "999",searchFormula);
				total = searchService.handleDoSearch(doSearch);
			}
		}
	
		int rows=20;			
		System.out.println("专利检索记录条数：" + total);
//		List<CNDescriptionItem> cNDescriptionItemList=null
//		 List<EnDescriptionItem> enDescriptionItemList=null;
		 if (this.searchType != null && "expert".equals(this.searchType)) {
			if("Cn".equals(searchscope)){//本地==
				GetGeneralDataParameter getGeneralDataParameter = new GetGeneralDataParameter(user.getId()+"", "Cn", "999", page, rows);
				cNDescriptionItemList = searchService.handleGetGeneralData(getGeneralDataParameter);
			}else if("DocDB".equals(searchscope)){//远程
				GetGeneralDataParameter getGeneralDataParameter = new GetGeneralDataParameter(user.getId()+"", "DocDB", "999", page, rows);
				enDescriptionItemList = searchService.handleGetGeneralDataRemote(getGeneralDataParameter);
			}else{
				GetGeneralDataParameter getGeneralDataParameter = new GetGeneralDataParameter(user.getId()+"", "Cn", "999", page, rows);
				cNDescriptionItemList = searchService.handleGetGeneralData(getGeneralDataParameter);
			}
		 }else{
			 if("Cn".equals(searchscope)){//本地
					GetGeneralDataParameter getGeneralDataParameter = new GetGeneralDataParameter(user.getId()+""+strDateUID, "Cn", "999", page, rows);
					cNDescriptionItemList = searchService.handleGetGeneralData(getGeneralDataParameter);
				}else if("DocDB".equals(searchscope)){//远程
					GetGeneralDataParameter getGeneralDataParameter = new GetGeneralDataParameter(user.getId()+""+strDateUID, "DocDB", "999", page, rows);
					enDescriptionItemList = searchService.handleGetGeneralDataRemote(getGeneralDataParameter);
				}else{
					GetGeneralDataParameter getGeneralDataParameter = new GetGeneralDataParameter(user.getId()+""+strDateUID, "Cn", "999", page, rows);
					cNDescriptionItemList = searchService.handleGetGeneralData(getGeneralDataParameter);
				}	
		}
		 	 
		 
		 
		 
		 
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> records = new ArrayList<Map>();
		if (enDescriptionItemList != null && enDescriptionItemList.size() > 0) {
			
			for (int i = 0; i < enDescriptionItemList.size(); i++) {
				EnDescriptionItem enDescription=enDescriptionItemList.get(i);
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("searchscope",searchscope);
//				m.put("futuURL",ConfigTool.getValue("futu_friendly"));
				this.enDescriptionItem=XMLUtil.getENDescriptionItemByPubnr(enDescription.getPubnr());
				m.put("futuURL", XMLUtil.getFuTuByPubnr(this.enDescriptionItem.getPubnr(),this.enDescriptionItem.getPudText()));
				m.put("title", getHighlighOutput(enDescription.getTitle(),enDescriptionItem.getTitle()));
				m.put("titleValue",enDescription.getTitle());
//				m.put("legalStatus", 1);
				m.put("patentType", "else");
				m.put("appno", getHighlighOutput(enDescription.getAppno(),enDescriptionItem.getAppno()));
				m.put("appnoValue", enDescription.getAppno());
//				m.put("appnoWithDot", getHighlighOutput(enDescription.getAppnoWithDot(), cnDescriptionItem.getAppno()));
//				m.put("apdText", getHighlighOutput(DateUtil
//						.dateToTextString(enDescription.getApd()), DateUtil
//						.formatStrToStr(enDescriptionItem.getApdText())));
				m.put("pubnr", enDescription.getPubnr());
				m.put("apdValue", enDescription.getApdText());
				m.put("apdText", DateUtil.formatStrToStr(enDescription.getApdText()));
//				m.put("pubnr",
//						enDescription.getPud() != null ? getHighlighOutput(
//								enDescription.getPubnr(), enDescriptionItem
//										.getPubnr()) : "无");
//				m.put("pudText",
//						enDescription.getPud() != null ? getHighlighOutput(
//								DateUtil.dateToTextString(enDescription
//										.getPud()), DateUtil
//										.formatStrToStr(enDescriptionItem
//												.getPudText())) : "无");
				m.put("interIpc", enDescription.getInterIpc());
				m.put("interIpcFormula",enDescription.getInterIpc());
				m.put("pudText", DateUtil.formatStrToStr(enDescription.getPudText()));
				m.put("pudValue",enDescription.getPudText());

//				m.put("appnr",enDescription.getAppnr() != null ? getHighlighOutput(enDescription.getAppnr(),enDescriptionItem.getAppnr()): "无");
//				m.put("appdText",enDescription.getPud() != null ? getHighlighOutput(DateUtil.dateToTextString(enDescription.getPud()),DateUtil.formatStrToStr(enDescriptionItem.getPudText())): "无");
//				m.put("appdValue",
//						enDescription.getPud() != null ? DateUtil
//								.dateToValueString(enDescription.getPud())
//								: "无");
				m.put("ipcMain", enDescription.getIpcMain());

				m.put("ipcMainPara", StringUtil.getIpcPara(enDescription
						.getIpcMain()));
				m.put("appl", enDescription.getAppl().split(","));
				m.put("inventor", enDescription.getInventor().split(","));
				// 对摘要进行部分截取
				if (enDescription.getAbs() != null
						&& enDescription.getAbs().length() > 800) {
					m.put("abstr", getHighlighOutput(enDescription
							.getAbs().substring(0, 800)
							+ "<font color=black size=2px>. . . . . .</font>",
							enDescriptionItem.getAbs()));
				} else {
					m.put("abstr", getHighlighOutput(enDescription
							.getAbs(), enDescriptionItem.getAbs()));
				}
				m.put("pdfURL", ConfigTool.getValue("pdf_friendly"));
				records.add(m);
			}
		//远程国外
		}
		System.out.println("2  "+new Date().getTime());
		if (cNDescriptionItemList != null && cNDescriptionItemList.size() > 0) {//本地
			for (int i = 0; i < cNDescriptionItemList.size(); i++) {
				CNDescriptionItem cNDescriptionItem = cNDescriptionItemList
						.get(i);
				
				int stateInt=getLegalStatusTypeSingle(cNDescriptionItem.getAppno(),cNDescriptionItem.getPubnr(),
						cNDescriptionItem.getPud(),cNDescriptionItem.getAppnr(),cNDescriptionItem.getAppd());
				
				String state="";
				if(stateInt==1){
					state="无效";
				}else if(stateInt==2){
					state="暂无";
				}else if(stateInt==3){
					state="失效";
				}else if(stateInt==4){
					state="有效";
				}else if(stateInt==5){
					state="审中";
				}
				cNDescriptionItem.setCnLegalStatusStr(state);
				if(null==cNDescriptionItem.getAppnr()||cNDescriptionItem.getAppnr().trim().equals("")){
					cNDescriptionItem.setAppnr("无");
				}
				if(null==cNDescriptionItem.getAppd()){
					cNDescriptionItem.setAppdText("无");
				}
				else{
					cNDescriptionItem.setAppdText(DateUtil.dateToTextString(cNDescriptionItem.getAppd()));
				}
				
				
				if (cNDescriptionItem.getAbstr() != null&& cNDescriptionItem.getAbstr().length() > 180) {
					cNDescriptionItem.setAbstr(cNDescriptionItem.getAbstr().substring(0, 180)+ "<font color=black size=2px>. . . . . .</font>");
					
				}
				
				
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("searchscope",searchscope);
				m.put("futuURL", cNDescriptionItem.getFutuURL());
				m.put("title", getHighlighOutput(cNDescriptionItem.getTitle(),cnDescriptionItem.getTitle()));
				m.put("titleValue",cNDescriptionItem.getTitle().trim());
				// 返回法律状态
				m.put("legalStatus",stateInt);
				m.put("cnLegalStatusStr",cNDescriptionItem.getCnLegalStatusStr());
//				m.put("legalStatus", 1);
				m.put("patentType", cNDescriptionItem.getPatentType());
				m.put("appno", getHighlighOutput(cNDescriptionItem.getAppno(),
						cnDescriptionItem.getAppno()));
				

				m.put("appnoValue", cNDescriptionItem.getAppno());
				
				m.put("appnoWithDot", getHighlighOutput(cNDescriptionItem.getAppnoWithDot(), cnDescriptionItem.getAppno()));
				m.put("apdText", getHighlighOutput(DateUtil
						.dateToTextString(cNDescriptionItem.getApd()), DateUtil
						.formatStrToStr(cnDescriptionItem.getApdText())));
				m.put("apdValue", DateUtil.dateToValueString(cNDescriptionItem
						.getApd()));
				m.put("pubnr",
						cNDescriptionItem.getPud() != null ? getHighlighOutput(
								cNDescriptionItem.getPubnr(), cnDescriptionItem
										.getPubnr()) : "无");
				m.put("pudText",
						cNDescriptionItem.getPud() != null ? getHighlighOutput(
								DateUtil.dateToTextString(cNDescriptionItem
										.getPud()), DateUtil
										.formatStrToStr(cnDescriptionItem
												.getPudText())) : "无");
				m.put("pudValue", cNDescriptionItem.getPud() != null ? DateUtil
						.dateToValueString(cNDescriptionItem.getPud()) : "无");

				m.put("appnr",(cNDescriptionItem.getAppnr() != null&&!cNDescriptionItem.getAppnr().trim().equals("000000000")) ? getHighlighOutput(cNDescriptionItem.getAppnr(),cnDescriptionItem.getAppnr()): "无");
				m
						.put(
								"appdText",
								cNDescriptionItem.getAppd() != null ? getHighlighOutput(
										DateUtil
												.dateToTextString(cNDescriptionItem
														.getAppd()),
										DateUtil
												.formatStrToStr(cnDescriptionItem
														.getAppdText()))
										: "无");
				m.put("appdValue",
						cNDescriptionItem.getAppd() != null ? DateUtil
								.dateToValueString(cNDescriptionItem.getAppd())
								: "无");
				m.put("ipcMain", cNDescriptionItem.getIpcMain());

				m.put("ipcMainPara", StringUtil.getIpcPara(cNDescriptionItem
						.getIpcMain()));
				m.put("appl", cNDescriptionItem.getAppl().split(","));
				m.put("inventor", cNDescriptionItem.getInventor().split(","));
				// 对摘要进行部分截取
				if (cNDescriptionItem.getAbstr() != null
						&& cNDescriptionItem.getAbstr().length() > 180) {
					m.put("abstr", getHighlighOutput(cNDescriptionItem
							.getAbstr().substring(0, 180)
							+ "<font color=black size=2px>. . . . . .</font>",
							cnDescriptionItem.getAbstr()));
				} else {
					m.put("abstr", getHighlighOutput(cNDescriptionItem
							.getAbstr(), cnDescriptionItem.getAbstr()));
				}
				System.out.println("3  "+new Date().getTime());
				//m.put("pdfURL", searchService.getPdfUrl(cNDescriptionItem.getAppno()));
				System.out.println("3  "+new Date().getTime());
				records.add(m);
			}
		}
		System.out.println("2  "+new Date().getTime());
		map.put("total", total);
		System.out.println("totaltotal=" + total);
		if(total==0){
			System.out.println(records);
			map.put("rows", new ArrayList<Map>());
		}
		else{
			map.put("rows", records);
		}
		
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}



		//ActionContext ct = ActionContext.getContext();
		//ct.put("cNDescriptionItemList", cNDescriptionItemList); 
		//ct.put("enDescriptionItemList", enDescriptionItemList); 
		//ct.put("total", total);
	}
	

	/*
	 * 获取Ajax检索结果
	 */
	public void getSearchResult() throws IOException, RowsExceededException, WriteException {
		
		
		
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		if(searchFormula.contains("_")){
			searchFormula=searchFormula.replaceAll("_", "+");
		}
		//System.out.println("Ajax searchFormula:" + searchFormula);
		// 将著录项查询条件进行封装,来自于查询结果页面
		//检索编号，如果来自算式检索就是id  高级检索为2
		Integer searchFormulaId=new Integer(2);
		
		// 如果是来自检索式的点击检索,需要更新检索相关信息
		if (this.searchType != null && "expert".equals(this.searchType)) {
			String idStr = ServletActionContext.getRequest().getParameter(
					"searchFormulaId");
			if (idStr != null && !"".equals(idStr)) {
				try {
					Integer id = Integer.parseInt(idStr);
					searchFormulaId=id;
					SearchFormula searchFormula = searchFormualService.find(id);
					searchFormula.setAlterTime(new Date());
					searchFormula.setHits(total);
					searchFormualService.update(searchFormula);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		String strDateUID=(int)(Math.random()*1000)+"";
		//此处封装enDescriptionItem和cnDescriptionItem
		buildZhuLuXiang(ServletActionContext.getRequest());  
		if (this.searchType != null && "expert".equals(this.searchType)) {
			if("Cn".equals(searchscope)){//本地ii
				DoSearchParameter doSearch = new DoSearchParameter(user.getId()+"", "Cn", "999",searchFormula);
				total = searchService.handleDoSearch(doSearch);
			}else if("DocDB".equals(searchscope)){//远程
				DoSearchParameter doSearch = new DoSearchParameter(user.getId()+"", "DocDB", "999",searchFormula);
				total = searchService.handleDoSearchRemote(doSearch);
			}else{
				DoSearchParameter doSearch = new DoSearchParameter(user.getId()+"", "Cn", "999",searchFormula);
				total = searchService.handleDoSearch(doSearch);
			}
		}else{
			if("Cn".equals(searchscope)){//本地
				DoSearchParameter doSearch = new DoSearchParameter(user.getId()+""+strDateUID, "Cn", "999",searchFormula);
				total = searchService.handleDoSearch(doSearch);
			}else if("DocDB".equals(searchscope)){//远程
				DoSearchParameter doSearch = new DoSearchParameter(user.getId()+""+strDateUID, "DocDB", "999",searchFormula);
				total = searchService.handleDoSearchRemote(doSearch);
			}else{
				DoSearchParameter doSearch = new DoSearchParameter(user.getId()+""+strDateUID, "Cn", "999",searchFormula);
				total = searchService.handleDoSearch(doSearch);
			}
		}
	
		System.out.println("专利检索记录条数：" + total);
		if(total>500){
			rows=500;
		}
		List<CNDescriptionItem> cNDescriptionItemList=null;
		 List<EnDescriptionItem> enDescriptionItemList=null;
		 if (this.searchType != null && "expert".equals(this.searchType)) {
			if("Cn".equals(searchscope)){//本地==
				GetGeneralDataParameter getGeneralDataParameter = new GetGeneralDataParameter(user.getId()+"", "Cn", "999", page, rows);
				cNDescriptionItemList = searchService.handleGetGeneralData(getGeneralDataParameter);
			}else if("DocDB".equals(searchscope)){//远程
				GetGeneralDataParameter getGeneralDataParameter = new GetGeneralDataParameter(user.getId()+"", "DocDB", "999", page, rows);
				enDescriptionItemList = searchService.handleGetGeneralDataRemote(getGeneralDataParameter);
			}else{
				GetGeneralDataParameter getGeneralDataParameter = new GetGeneralDataParameter(user.getId()+"", "Cn", "999", page, rows);
				cNDescriptionItemList = searchService.handleGetGeneralData(getGeneralDataParameter);
			}
		 }else{
			 if("Cn".equals(searchscope)){//本地
					GetGeneralDataParameter getGeneralDataParameter = new GetGeneralDataParameter(user.getId()+""+strDateUID, "Cn", "999", page, rows);
					cNDescriptionItemList = searchService.handleGetGeneralData(getGeneralDataParameter);
				}else if("DocDB".equals(searchscope)){//远程
					GetGeneralDataParameter getGeneralDataParameter = new GetGeneralDataParameter(user.getId()+""+strDateUID, "DocDB", "999", page, rows);
					enDescriptionItemList = searchService.handleGetGeneralDataRemote(getGeneralDataParameter);
				}else{
					GetGeneralDataParameter getGeneralDataParameter = new GetGeneralDataParameter(user.getId()+""+strDateUID, "Cn", "999", page, rows);
					cNDescriptionItemList = searchService.handleGetGeneralData(getGeneralDataParameter);
				}	
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> records = new ArrayList<Map>();
		if (enDescriptionItemList != null && enDescriptionItemList.size() > 0) {
			
			for (int i = 0; i < enDescriptionItemList.size(); i++) {
				EnDescriptionItem enDescription=enDescriptionItemList.get(i);
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("searchscope",searchscope);
//				m.put("futuURL",ConfigTool.getValue("futu_friendly"));
				this.enDescriptionItem=XMLUtil.getENDescriptionItemByPubnr(enDescription.getPubnr());
				m.put("futuURL", XMLUtil.getFuTuByPubnr(this.enDescriptionItem.getPubnr(),this.enDescriptionItem.getPudText()));
				m.put("title", getHighlighOutput(enDescription.getTitle(),enDescriptionItem.getTitle()));
				m.put("titleValue",enDescription.getTitle());
//				m.put("legalStatus", 1);
				m.put("patentType", "else");
				m.put("appno", getHighlighOutput(enDescription.getAppno(),enDescriptionItem.getAppno()));
				m.put("appnoValue", enDescription.getAppno());
//				m.put("appnoWithDot", getHighlighOutput(enDescription.getAppnoWithDot(), cnDescriptionItem.getAppno()));
//				m.put("apdText", getHighlighOutput(DateUtil
//						.dateToTextString(enDescription.getApd()), DateUtil
//						.formatStrToStr(enDescriptionItem.getApdText())));
				m.put("pubnr", enDescription.getPubnr());
				m.put("apdValue", enDescription.getApdText());
				m.put("apdText", DateUtil.formatStrToStr(enDescription.getApdText()));
//				m.put("pubnr",
//						enDescription.getPud() != null ? getHighlighOutput(
//								enDescription.getPubnr(), enDescriptionItem
//										.getPubnr()) : "无");
//				m.put("pudText",
//						enDescription.getPud() != null ? getHighlighOutput(
//								DateUtil.dateToTextString(enDescription
//										.getPud()), DateUtil
//										.formatStrToStr(enDescriptionItem
//												.getPudText())) : "无");
				m.put("interIpc", enDescription.getInterIpc());
				m.put("interIpcFormula",enDescription.getInterIpc());
				m.put("pudText", DateUtil.formatStrToStr(enDescription.getPudText()));
				m.put("pudValue",enDescription.getPudText());

//				m.put("appnr",enDescription.getAppnr() != null ? getHighlighOutput(enDescription.getAppnr(),enDescriptionItem.getAppnr()): "无");
//				m.put("appdText",enDescription.getPud() != null ? getHighlighOutput(DateUtil.dateToTextString(enDescription.getPud()),DateUtil.formatStrToStr(enDescriptionItem.getPudText())): "无");
//				m.put("appdValue",
//						enDescription.getPud() != null ? DateUtil
//								.dateToValueString(enDescription.getPud())
//								: "无");
				m.put("ipcMain", enDescription.getIpcMain());

				m.put("ipcMainPara", StringUtil.getIpcPara(enDescription
						.getIpcMain()));
				m.put("appl", enDescription.getAppl().split(","));
				m.put("inventor", enDescription.getInventor().split(","));
				// 对摘要进行部分截取
				if (enDescription.getAbs() != null
						&& enDescription.getAbs().length() > 800) {
					m.put("abstr", getHighlighOutput(enDescription
							.getAbs().substring(0, 800)
							+ "<font color=black size=2px>. . . . . .</font>",
							enDescriptionItem.getAbs()));
				} else {
					m.put("abstr", getHighlighOutput(enDescription
							.getAbs(), enDescriptionItem.getAbs()));
				}
				m.put("pdfURL", ConfigTool.getValue("pdf_friendly"));
				records.add(m);
			}
		//远程国外
		}
		
		if (cNDescriptionItemList != null && cNDescriptionItemList.size() > 0) {//本地
			for (int i = 0; i < cNDescriptionItemList.size(); i++) {
				CNDescriptionItem cNDescriptionItem = cNDescriptionItemList
						.get(i);
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("searchscope",searchscope);
				m.put("futuURL", cNDescriptionItem.getFutuURL());
				m.put("title", getHighlighOutput(cNDescriptionItem.getTitle(),cnDescriptionItem.getTitle()));
				m.put("titleValue",cNDescriptionItem.getTitle().trim());
				// 返回法律状态
				m.put("legalStatus",getLegalStatusTypeSingle(cNDescriptionItem.getAppno(),cNDescriptionItem.getPubnr(),
						cNDescriptionItem.getPud(),cNDescriptionItem.getAppnr(),cNDescriptionItem.getAppd()));
//				m.put("legalStatus", 1);
				m.put("patentType", cNDescriptionItem.getPatentType());
				m.put("appno", getHighlighOutput(cNDescriptionItem.getAppno(),
						cnDescriptionItem.getAppno()));
				

				m.put("appnoValue", cNDescriptionItem.getAppno());
				
				m.put("appnoWithDot", getHighlighOutput(cNDescriptionItem.getAppnoWithDot(), cnDescriptionItem.getAppno()));
				m.put("apdText", getHighlighOutput(DateUtil
						.dateToTextString(cNDescriptionItem.getApd()), DateUtil
						.formatStrToStr(cnDescriptionItem.getApdText())));
				m.put("apdValue", DateUtil.dateToValueString(cNDescriptionItem
						.getApd()));
				m.put("pubnr",
						cNDescriptionItem.getPud() != null ? getHighlighOutput(
								cNDescriptionItem.getPubnr(), cnDescriptionItem
										.getPubnr()) : "无");
				m.put("pudText",
						cNDescriptionItem.getPud() != null ? getHighlighOutput(
								DateUtil.dateToTextString(cNDescriptionItem
										.getPud()), DateUtil
										.formatStrToStr(cnDescriptionItem
												.getPudText())) : "无");
				m.put("pudValue", cNDescriptionItem.getPud() != null ? DateUtil
						.dateToValueString(cNDescriptionItem.getPud()) : "无");

				m.put("appnr",(cNDescriptionItem.getAppnr() != null&&!cNDescriptionItem.getAppnr().trim().equals("000000000")) ? getHighlighOutput(cNDescriptionItem.getAppnr(),cnDescriptionItem.getAppnr()): "无");
				m
						.put(
								"appdText",
								cNDescriptionItem.getAppd() != null ? getHighlighOutput(
										DateUtil
												.dateToTextString(cNDescriptionItem
														.getAppd()),
										DateUtil
												.formatStrToStr(cnDescriptionItem
														.getAppdText()))
										: "无");
				m.put("appdValue",
						cNDescriptionItem.getAppd() != null ? DateUtil
								.dateToValueString(cNDescriptionItem.getAppd())
								: "无");
				m.put("ipcMain", cNDescriptionItem.getIpcMain());

				m.put("ipcMainPara", StringUtil.getIpcPara(cNDescriptionItem
						.getIpcMain()));
				m.put("appl", cNDescriptionItem.getAppl().split(","));
				m.put("inventor", cNDescriptionItem.getInventor().split(","));
				// 对摘要进行部分截取
				if (cNDescriptionItem.getAbstr() != null
						&& cNDescriptionItem.getAbstr().length() > 180) {
					m.put("abstr", getHighlighOutput(cNDescriptionItem
							.getAbstr().substring(0, 180)
							+ "<font color=black size=2px>. . . . . .</font>",
							cnDescriptionItem.getAbstr()));
				} else {
					m.put("abstr", getHighlighOutput(cNDescriptionItem
							.getAbstr(), cnDescriptionItem.getAbstr()));
				}
				m.put("pdfURL", searchService.getPdfUrl(cNDescriptionItem
						.getAppno()));
				records.add(m);
			}
		}
		map.put("total", total);
		System.out.println("totaltotal=" + total);
		if(total==0){
			System.out.println(records);
			map.put("rows", new ArrayList<Map>());
		}
		else{
			map.put("rows", records);
		}
		
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	/*public String getLegalSatusNew(){
		
	}*/
	
	private Integer getLegalStatusTypeSingle(String appno, String pubnr, Date pud, String appnr, Date appd) {
		String legalCode = legalStatusService.getLegalStatusDetailSigle(appno);
		String legalStr = LegalUtil.getCategoryByLegalCode(legalCode.trim());
		Integer LegalStatusType = 2;
		if (!legalCode.equals("")) {
			legalStr = LegalUtil.getCategoryByLegalCode(legalCode.trim());
			if ("失效".equals(legalStr)) {
				LegalStatusType = 3;
			} else if ("审中".equals(legalStr)) {
				LegalStatusType = 5;
			} else if ("无效".equals(legalStr)) {
				LegalStatusType = 1;
			} else if ("有效".equals(legalStr)) {
				LegalStatusType = 4;
			}
		} else if(((legalStr.equals("其它"))||(legalStr.equals("")) )&& pubnr != null && pud!= null && appnr == null && appd== null){
			LegalStatusType = 5;
		} else if (((legalStr.equals("其它"))||(legalStr.equals("")) )&& appnr != null && appd != null){
			LegalStatusType = 4;
		}
		return LegalStatusType;
	}
	
	public String pdfShowDiv(){
		String url = ServletActionContext.getRequest().getParameter("url");
		String type = ServletActionContext.getRequest().getParameter("type");
		ServletActionContext.getRequest().setAttribute("type", type);		
		String[] urls = url.split("\\|");
		for(int i=0;i<urls.length;i++){
			ServletActionContext.getRequest().setAttribute("url"+i, urls[i]);		
		}	
		return "pdfShowDiv";
	}

	/**
	 * 专利详情界面
	 */
	public String patentDetailUI() {
		// 为何this.cNDescriptionItem不能自动封装将this.cNDescriptionItem改为this.cnDescriptionItem
		searchFormula = ServletActionContext.getRequest().getParameter(
				"searchFormula");
		return "patentDetailUI";
	}
	public String detailOutLookUI() {
		CNDescriptionItem cnDescription=XMLUtil.getCNDescriptionItemByAppno(this.cnDescriptionItem.getAppno());
		String strTemp=cnDescription.getFutuURL();
		int i=1;
		for(;i<100;i++){
			strTemp=strTemp.replace(i+".jpg", (i+1)+".jpg");
			if(i<9){
				if(WebTool.urlIsValid(strTemp)){
				}else{
					break;
				}
			}
			if(i>=9){
				strTemp=strTemp.replace("00000", "0000");
				if(WebTool.urlIsValid(strTemp)){
				}else{
					break;
				}
			}
		}
		strTemp=strTemp.substring(0, strTemp.length()-6);
		ServletActionContext.getRequest().setAttribute("fuluURL", strTemp);
		ServletActionContext.getRequest().setAttribute("sumOfFulu",i);
		return "detailOutLookUI";
	}
	/**
	 * 专利详情页面->著录项目信息（国内）
	 */
	public String detailTab1UI() {
		this.cnDescriptionItem = XMLUtil
				.getCNDescriptionItemByAppno(this.cnDescriptionItem.getAppno());
		// 每进行一次省市代码转换都需要查一下数据库,应考虑性能
		provinceCityList = provinceCityService.getScrollData().getResultlist();
		ActionContext.getContext().put("futuURL",
				XMLUtil.getFuTuByAppno(this.cnDescriptionItem.getAppno()));
		// 对国省代码信息、代理机构信息进行详情展示
		CNDescriptionItem formatDescriptionItem = handleCodeInfo(this.cnDescriptionItem);
		ActionContext.getContext().put("ncText", formatDescriptionItem.getNc());
		ActionContext.getContext().put("agencyText",
				formatDescriptionItem.getAgency());
		// 对申请日、公开日、授权公告日进行格式化成检索引擎入口参数格式
		ActionContext.getContext().put("apdPara",
				DateUtil.dateToValueString(this.cnDescriptionItem.getApd()));
		ActionContext.getContext().put("pudPara",
				DateUtil.dateToValueString(this.cnDescriptionItem.getPud()));
		ActionContext.getContext().put("grpdPara",
				DateUtil.dateToValueString(this.cnDescriptionItem.getGrpd()));
		
		ActionContext.getContext().put("IpcMainPara",StringUtil.getIpcPara(cnDescriptionItem.getIpcMain()));
		String[] ipcMinors = cnDescriptionItem.getIpcMinor().split(";");
		
		Map<String, String > minors = new HashMap<String, String>();
		for(String s : ipcMinors){
			minors.put(s, StringUtil.getIpcPara(s));
		}
		ActionContext.getContext().put("IpcMinorPara",minors);
		
		// 对申请人、发明人字段进行字符串切割成集合进行迭代
		ActionContext.getContext().put("applicantList",
				this.cnDescriptionItem.getAppl().split(","));
		ActionContext.getContext().put("inventorList",this.cnDescriptionItem.getInventor().split(","));
		ActionContext.getContext().put("title",this.cnDescriptionItem.getTitle());
		return "detailTab1UI";
	}
	/**
	 * 专利详情页面->著录项目信息(国外)
	 */
	public String detailTab1EnUI() {
		this.enDescriptionItem=XMLUtil.getENDescriptionItemByPubnr(this.enDescriptionItem.getPubnr());
		ActionContext.getContext().put("futuURL",
				XMLUtil.getFuTuByAppno(this.enDescriptionItem.getAppno()));
		
//		// 对申请日、公开日、授权公告日进行格式化成检索引擎入口参数格式
//		ActionContext.getContext().put("apdPara",
//				DateUtil.dateToValueString(this.cnDescriptionItem.getApd()));
//		ActionContext.getContext().put("pudPara",
//				DateUtil.dateToValueString(this.cnDescriptionItem.getPud()));
//		ActionContext.getContext().put("grpdPara",
//				DateUtil.dateToValueString(this.cnDescriptionItem.getGrpd()));
		
		// 对申请人、发明人字段进行字符串切割成集合进行迭代
		ActionContext.getContext().put("applicantList",
				this.enDescriptionItem.getAppl().split(","));
		ActionContext.getContext().put("inventorList",
				this.enDescriptionItem.getInventor().split(","));
		
		return "detailTab1EnUI";
	}

	/**
	 * 专利详情页面->全文PDF（国内）
	 */
	public String detailTab2UI() {
		
		String url = searchService.getPdfUrl(this.cnDescriptionItem.getAppno());
		String[] urls = url.split("\\|");
		if(urls.length>1){
			url = urls[1];
		}
		
		
		ActionContext.getContext().put("pdfURL",url);

		return "detailTab2UI";
	}
	
	/**
	 * 专利详情页面->全文PDF(国外)
	 */
	public String detailTab2EnUI() {
		ActionContext.getContext().put("pdfURL",
				searchService.getPdfUrlEn(this.enDescriptionItem.getPubnr()));
		return "detailTab2UI";
	}

	/**
	 * 专利详情页面->说明书
	 */
	public String detailTab3UI() {
		String cnDesXmlTxt = searchService.getPatentData(this.cnDescriptionItem
				.getAppno(), 2);
		ActionContext.getContext().put("cnDesXmlTxt", cnDesXmlTxt);
		return "detailTab3UI";
	}

	/**
	 * 专利详情页面->权利要求
	 */
	public String detailTab4UI() {
		String clmXmlTxt = searchService.getPatentData(this.cnDescriptionItem
				.getAppno(), 3);
//System.out.println("clmXmlTxt="+clmXmlTxt);	

clmXmlTxt=clmXmlTxt.replace("<br />", "");
clmXmlTxt=clmXmlTxt.replace("</claim>", "</claim><br />");

		
//System.out.println("clmXmlTxt="+clmXmlTxt);		
		
		ActionContext.getContext().put("clmXmlTxt", clmXmlTxt);
		return "detailTab4UI";
	}

	/**
	 * 专利详情页面->法律状态 注意此时接收的专利申请号并没有校验位，而在法律状态表中是有校验位的，因些需要对专利申请号进行检验生成
	 */
	public String detailTab5UI() {
		List<CnLegalStatus> cnLegalStatusList = legalStatusService
				.getCnLegalStatusByAppnp(XMLUtil
						.getCheckAppnoWithOutDot(this.cnDescriptionItem
								.getAppno()));
		ActionContext.getContext().put("cnLegalStatusList", cnLegalStatusList);
		ActionContext.getContext().put("shenQingH", this.cnDescriptionItem.getAppno());
		ActionContext.getContext().put("legalstatus_friendly",
				ConfigTool.getValue("legalstatus_friendly"));
		return "detailTab5UI";
	}
	


	/**
	 * 专利详情页面->法律状态 注意此时接收的专利申请号并没有校验位，而在法律状态表中是有校验位的，因些需要对专利申请号进行检验生成
	 */
	public String detailTab6UI() {
		List<CnLegalStatus> cnLegalStatusList = legalStatusService
				.getCnLegalStatusByAppnp(XMLUtil
						.getCheckAppnoWithOutDot(this.cnDescriptionItem
								.getAppno()));
		ActionContext.getContext().put("cnLegalStatusList", cnLegalStatusList);
		ActionContext.getContext().put("shenQingH", this.cnDescriptionItem.getAppno());
		ActionContext.getContext().put("legalstatus_friendly",
				ConfigTool.getValue("legalstatus_friendly"));
		return "detailTab6UI";
	}

	/*
	 * 将国家/省市代码、代理机构代码化信息进行处理
	 */
	private CNDescriptionItem handleCodeInfo(CNDescriptionItem cnDescriptionItem) {
		CNDescriptionItem formatcnDescriptionItem = new CNDescriptionItem();
		provinceCityList = provinceCityService.getScrollData().getResultlist();
		formatcnDescriptionItem.setNc(provinceCityService.detailInfo(
				provinceCityList, cnDescriptionItem.getNc()));
		Agency agency = agencyService.detailInfo(cnDescriptionItem.getAgency());
		formatcnDescriptionItem.setAgency(agency == null ? "无" : agency
				.getName()
				+ "(代码" + agency.getCode() + ")");
		return formatcnDescriptionItem;
	}

	/*
	 * 对字条串的高亮输出显示
	 */
	private String getHighlighOutput(String in, String keyword) {
		String out = "";
		if (in != null && !"".equals(in) && keyword != null
				&& !"".equals(keyword)) {
			keyword=keyword.replaceAll("\\(", "\\\\(");
			keyword=keyword.replaceAll("\\)", "\\\\)");
			Pattern pattern = Pattern.compile(keyword);
			Matcher matcher = pattern.matcher(in);
			out = matcher
					.replaceAll("<span style='background-color:rgb(255,255,0)'>"+ keyword + "</span>");
		} else {
			out = in;
		}

		return out;
	}

	/*
	 * 将著录项进行封装，配合高亮显示功能
	 */

	private void buildZhuLuXiang(HttpServletRequest request) {
		cnDescriptionItem.setTitle(request.getParameter("title"));
		cnDescriptionItem.setAbstr(request.getParameter("abstr"));
		cnDescriptionItem.setClaim(request.getParameter("claim"));
		cnDescriptionItem.setAppl(request.getParameter("appl"));
		cnDescriptionItem.setIpcMain(request.getParameter("ipcMain"));
		cnDescriptionItem.setAppno(request.getParameter("appno"));
		cnDescriptionItem.setApdText(request.getParameter("apd"));
		cnDescriptionItem.setPubnr(request.getParameter("pubnr"));
		cnDescriptionItem.setPudText(request.getParameter("pud"));
		cnDescriptionItem.setAppnr(request.getParameter("appnr"));
		cnDescriptionItem.setAppdText(request.getParameter("appd"));
		cnDescriptionItem.setInventor(request.getParameter("inventor"));
		cnDescriptionItem.setFieldc(request.getParameter("fieldc"));
		cnDescriptionItem.setAddress(request.getParameter("address"));
		cnDescriptionItem.setNc(request.getParameter("nc"));
		cnDescriptionItem.setAgency(request.getParameter("agency"));
		cnDescriptionItem.setIpcMain(request.getParameter("ipcMain"));
		cnDescriptionItem.setAgent(request.getParameter("agent"));
		
		enDescriptionItem.setTitle(request.getParameter("titleEn"));
		enDescriptionItem.setAbs(request.getParameter("abstrEn"));
		enDescriptionItem.setAppl(request.getParameter("applEn"));
		enDescriptionItem.setIpcMain(request.getParameter("ipcMainEn"));
		enDescriptionItem.setAppno(request.getParameter("appnoEn"));
		enDescriptionItem.setApdText(request.getParameter("apdEn"));
		enDescriptionItem.setPubnr(request.getParameter("pubnrEn"));
		enDescriptionItem.setPudText(request.getParameter("pudEn"));
		enDescriptionItem.setInventor(request.getParameter("inventorEn"));
	}

	/*
	 * 异步国省代码加载
	 */

	public void getCoList() {
		String term = ServletActionContext.getRequest().getParameter("term");
		List<ProvinceCity> list = provinceCityService.getCoListByTerm(term);
		JSONArray json = new JSONArray();
		for (ProvinceCity item : list) {
			JSONObject jsonObject = new JSONObject();
			jsonObject
					.put("label", item.getName() + "(" + item.getCode() + ")");
			jsonObject.put("value", item.getCode());
			json.add(jsonObject);
		}
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(
					json.toString());
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 异步代理机构代码加载
	 */

	public void getAgList() {
		String term = ServletActionContext.getRequest().getParameter("term");
		List<Agency> list = agencyService.getAgListByTerm(term);
		JSONArray json = new JSONArray();
		for (Agency item : list) {
			JSONObject jsonObject = new JSONObject();
			jsonObject
					.put("label", item.getName() + "(" + item.getCode() + ")");
			jsonObject.put("value", item.getCode());
			json.add(jsonObject);
		}
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(
					json.toString());
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 根据专利申请号，返回法律状态类型值 1、暂无 2、失效 3、审中 4、有效 5、无效
	 */
	
	//m.put("legalStatus",getLegalStatusType(cNDescriptionItem.getAppno(),cNDescriptionItem.getPubnr(),
	//cNDescriptionItem.getPudText(),cNDescriptionItem.getAppnr(),cNDescriptionItem.getAppdText()));
	private Integer getLegalStatusType(String appno, String pubnr, Date pud, String appnr, Date appd) {
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
	/*
	 * 根据专利申请号，返回法律状态类型值 1、暂无 2、失效 3、审中 4、有效 5、无效
	 */
	
	//m.put("legalStatus",getLegalStatusType(cNDescriptionItem.getAppno(),cNDescriptionItem.getPubnr(),
	//cNDescriptionItem.getPudText(),cNDescriptionItem.getAppnr(),cNDescriptionItem.getAppdText()));
	private Integer getLegalStatusTypePicture(String appno) {
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
		}
		return LegalStatusType;
	}
//	
//	/*
//	 * 根据专利申请号，返回法律状态类型值 1、暂无 2、失效 3、审中 4、有效 5、无效
//	 */
//	private Integer getLegalStatusTypeByAppno(String appno) {
//		String categoryStr=legalStatusService.getLegalStatusCategoryByAppno(appno);
//		Integer LegalStatusType = 1;
//		if (categoryStr != null && !"".equals(categoryStr)) {
//
//			if ("失效".equals(categoryStr.trim())) {
//				LegalStatusType = 2;
//			} else if ("审中".equals(categoryStr
//					.trim())) {
//				LegalStatusType = 3;
//			} else if ("有效".equals(categoryStr
//					.trim())) {
//				LegalStatusType = 4;
//			} else if ("有效".equals(categoryStr
//					.trim())) {
//				LegalStatusType = 5;
//			}
//		}
//		return LegalStatusType;
//	}

	/*
	 * 对导出著录项数据导出前进行处理
	 */
	private List<CNDescriptionItem> formateItemList(List<CNDescriptionItem> list) {
		if (list != null && list.size() > 0) {
			List<CNDescriptionItem> resultList = new ArrayList<CNDescriptionItem>();
			for (CNDescriptionItem item : list) {
				CNDescriptionItem cnDescriptionItem = handleCodeInfo(item);
				item.setNc(cnDescriptionItem.getNc());
				item.setAgency(cnDescriptionItem.getAgency());
				resultList.add(item);
			}
			return resultList;
		}
		return null;
	}

	/**
	 * 导出专利著录项信息到Excel表
	 * @throws IOException 
	 */
	@SuppressWarnings({ "deprecation", "null" })
	public String toExcel() throws IOException {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest()); 
		HttpServletResponse response = ServletActionContext.getResponse();
		String searchscope = ServletActionContext.getRequest().getParameter("searchscope");
		String items = ServletActionContext.getRequest().getParameter("items");
		List<String> appnoList = new ArrayList<String>();
		String appnos = ServletActionContext.getRequest().getParameter("appnos");
		if (appnos != null && !"".equals(appnos)) {
			for (String appno : appnos.split(",")) {
				appnoList.add(appno);
			}
		}
		List<CNDescriptionItem> resultList = null;
		List<CNDescriptionItem> resultListExport = new ArrayList<CNDescriptionItem>();
		List<EnDescriptionItem> resultListEN = null;
		List<EnDescriptionItem> resultListENExport = new ArrayList<EnDescriptionItem>();
		if("DocDB".equals(searchscope)){
			resultListEN=searchService.getPatentItemListEN(appnoList);
		}else{
			resultList=formateItemList(searchService.getPatentItemList(appnoList));
		}
		

		
		int numtemp=0;
		int numold=user.getDowloadnumexcel();
		int maxnum=0;
		String level=user.getLevel();
		if(level.equals("1")){
			maxnum=0;
		}
		else if(level.equals("2")){
			maxnum=Integer.valueOf(ConfigTool.getValue("leval1numexcel").trim());
		}
		else if(level.equals("3")){
			maxnum=Integer.valueOf(ConfigTool.getValue("leval2numexcel").trim());		
		}
		else if(level.equals("4")){
			maxnum=Integer.valueOf(ConfigTool.getValue("leval3numexcel").trim());		
		}
		else{
			maxnum=0;
		}
		if(numold>=maxnum){
			//达到最大下载数
			response.setContentType("text/html;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script>alert('达到今天最大导出数"+maxnum+",请明天再下载');window.close();</script>");
			out.flush();
			out.close();
			return null;
			
		}
		
		
		if(resultListEN!=null){
			for(int i=0;i<resultListEN.size();i++){
				numtemp++;
				user.setDowloadnumexcel(numtemp+numold);
				userService.update(user);
				resultListENExport.add(resultListEN.get(i));
				if(user.getDowloadnumexcel()>=maxnum){
					//达到最大下载数
					break;
				}
			}
		}
		if(resultList!=null){
				for(int i=0;i<resultList.size();i++){
					numtemp++;
					user.setDowloadnumexcel(numtemp+numold);
					userService.update(user);
					
					CNDescriptionItem cnDescriptionItemTemp=resultList.get(i);
					resultList.get(i).setCnLegalStatusStr(getLeagelStatic(cnDescriptionItemTemp)); 
					resultList.get(i).setAppno(XMLUtil.getCheckAppnoWithDot(resultList.get(i).getAppno()));
					resultListExport.add(resultList.get(i));
					if(user.getDowloadnumexcel()>=maxnum){
						//达到最大下载数
						break;
					}
				}
		}
		String fileSavePath = "/upload/excel/";// 生成文件路径
		String fileSaveName = DateUtil.dateToValueString(new Date()) + ".xls";// 生成保存文件名
		// 文件名可以分为用户登录信息与时间信息拼接
		//Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());
		if (user != null) {
			fileSaveName = user.getUsername() + "_"
					+ DateUtil.dateToValueString(new Date()) + ".xls";

		}

		FileTool.generateFile(fileSavePath, fileSaveName);
		try {
			if("DocDB".equals(searchscope)){
				ExcelTool2007.exportPatentsToExcelEN(resultListENExport, fileSavePath, fileSaveName);
			}else{
//System.out.println(items);				
				ExcelTool2007.exportPatentsToExcel(resultListExport, fileSavePath, fileSaveName, items);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ActionContext.getContext().put("fileSaveName",
					URLEncoder.encode(fileSaveName, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "toexcelfile";
	}

	public String getLeagelStatic(CNDescriptionItem cnDescriptionItemTemp) {
		String JDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";// SQL数据库引擎
		String connectDB = "jdbc:sqlserver://11.0.0.25:1433; DatabaseName=Db_LegalStatus";// 数据源
		try {
			Class.forName(JDriver);// 加载数据库引擎，返回给定字符串名的类
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
			System.exit(0);
		}
		String category =null;
		try {
			String user = "sa";
			String password = "123456";
			Connection con = DriverManager.getConnection(connectDB, user,password);
			Statement stmt = con.createStatement(); // 创建SQL命令对象
			String appnoItem=cnDescriptionItemTemp.getAppno();
			String  appnoWithDot=XMLUtil.getCheckAppnoWithOutDot(appnoItem);
			ResultSet rs = stmt
			.executeQuery("SELECT TOP 1  category FROM  LegalStatusDetailView where SHENQINGH='"
					+appnoWithDot
					+ "' order by legalDate desc");
			if(rs.next()){
				category=rs.getString("category");
			}else{
				if(cnDescriptionItemTemp.getPubnr() != null && cnDescriptionItemTemp.getPud()!= null 
						&& cnDescriptionItemTemp.getAppnr() == null && cnDescriptionItemTemp.getAppd() == null){
					category="审中";
				} else if (cnDescriptionItemTemp.getAppnr() != null && cnDescriptionItemTemp.getAppd() != null){
					category="有效";
				}
			}
			cnDescriptionItemTemp.setCnLegalStatusStr(category);
		} catch (SQLException e) {
			e.printStackTrace();
		}// 连接数据库对象
		return category;
	}

	/*
	 * 批量下载专利PDF
	 */
	public String batchDownloadPdfs() throws IOException {
		List<PdfEntity> pdfList = new ArrayList<PdfEntity>();
		String appnos = ServletActionContext.getRequest()
				.getParameter("appnos");
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest()); 
		HttpServletResponse response = ServletActionContext.getResponse();
		if (appnos != null && !"".equals(appnos)) {
			int numtemp=0;
			int numold=user.getDowloadnumpdf();
			int maxnum=0;
			String level=user.getLevel();
			if(level.equals("1")){
				maxnum=0;
			}
			else if(level.equals("2")){
				maxnum=Integer.valueOf(ConfigTool.getValue("leval1numpdf").trim());
			}
			else if(level.equals("3")){
				maxnum=Integer.valueOf(ConfigTool.getValue("leval2numpdf").trim());		
			}
			else if(level.equals("4")){
				maxnum=Integer.valueOf(ConfigTool.getValue("leval3numpdf").trim());		
			}
			else{
				maxnum=0;
			}
			if(numold>=maxnum){
				//达到最大下载数
				response.setContentType("text/html;charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				PrintWriter out = response.getWriter();
				out.print("<script>alert('达到今天最大下载数"+maxnum+",请明天再下载');window.close();</script>");
				out.flush();
				out.close();
				return null;
				
			}
			for (String appno : appnos.split(",")) {
				PdfEntity pdfEntity=new PdfEntity();
				pdfEntity.setAppno(appno);
				
				File pdfFile = searchService.getPdfFile(appno);
				if(pdfFile.getName().equals("epds_nullPdf.pdf")){
					//Users user = WebTool.getLoginedUser(ServletActionContext.getRequest()); 
				}
				else{
					numtemp++;
					user.setDowloadnumpdf(numtemp+numold);
					userService.update(user);
				}
				
				pdfEntity.setPdf(pdfFile);
				pdfList.add(pdfEntity);
				if(user.getDowloadnumpdf()>=maxnum){
					//达到最大下载数
					break;
				}
			}

		}
		//System.out.println("pdfList:"+pdfList);
		String fileSaveName = DateUtil.dateToValueString(new Date()) + ".zip";// 生成保存文件名
		// 文件名可以分为用户登录信息与时间信息拼接
		//Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());
		if (user != null) {
			fileSaveName = user.getUsername() + "_"
					+ DateUtil.dateToValueString(new Date()) + ".zip";

		}
		File zip = FileTool.generateFile("/upload/pdf/", fileSaveName);

		/*
		 * List<File> srcfile = new ArrayList<File>(); srcfile.add(new
		 * File("D:\\test\\1.png")); srcfile.add(new File("D:\\test\\2.png"));
		 * srcfile.add(new File("D:\\test\\3.png")); srcfile.add(new
		 * File("D:\\test\\4.png"));
		 */
		FileTool.zipFiles(pdfList, zip);
		try {
			ActionContext.getContext().put("fileSaveName",
					URLEncoder.encode(fileSaveName, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "topdfzip";

	}
	

	/*
	 * 异步添加专利到企业库
	 */
	public void addPatentIn() {
		List<String> appnoList = new ArrayList<String>();//国内为申请号，国外为公开号
		String searchscope= ServletActionContext.getRequest().getParameter("searchscope");
		String appnos = ServletActionContext.getRequest()
				.getParameter("appnos");
		if (appnos != null && !"".equals(appnos)) {
			for (String appno : appnos.split(",")) {
				appnoList.add(appno);
			}
		}
			List<CNDescriptionItem> resultList=null;
			List<EnDescriptionItem> resultListEN=null;
			if("DocDB".equals(searchscope)){
				resultListEN=searchService.getPatentItemListEN(appnoList);
			}else{
				resultList = formateItemList(searchService.getPatentItemList(appnoList));
			}
		
		
		
//		String appnoValue= ServletActionContext.getRequest().getParameter("appnoValue");
//		String titleValue= ServletActionContext.getRequest().getParameter("title");
		String id= ServletActionContext.getRequest().getParameter("id");
		String text= ServletActionContext.getRequest().getParameter("text");
		
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			if(resultListEN!=null){
				for(int i=0;i<resultListEN.size();i++){
					PatentStoreInfo patent=new PatentStoreInfo();
					if(id!=null){	
						patent.setPatentCategory(categoryService.find(Integer.parseInt(id)));
					}else{
						StringBuilder wherejpql=new StringBuilder();
						wherejpql.append(" o.name = '"+text+"'");
						QueryResult<PatentCategory> categoryResult=  categoryService.getScrollData(-1, -1, wherejpql.toString(), null);
						int totalrecords=(int)categoryResult.getTotalrecord();	
						PatentCategory category= categoryResult.getResultlist().get(totalrecords-1);
						patent.setPatentCategory(category);
					}
					EnDescriptionItem enDescriptionItem=resultListEN.get(i);
					patent.setAppno(enDescriptionItem.getPubnr());//这里PatentStoreInfo 的申请号  就相当于国外专利公开号，便于查询
					patent.setTitle(enDescriptionItem.getTitle());
					
					/*String legalstateold="";
					//String legalstateold="";
					
					if(legalStatusService.getLegalStatusInfoByAppno(XMLUtil.getCheckAppnoWithOutDot(enDescriptionItem.getPubnr())) == null){
						legalstateold="无";
					} else {
						legalstateold= legalStatusService.getLegalStatusInfoByAppno(XMLUtil.getCheckAppnoWithOutDot(enDescriptionItem.getPubnr())).equals("文件的公告送达")?"实质审查的生效":legalStatusService.getLegalStatusInfoByAppno(XMLUtil.getCheckAppnoWithOutDot(enDescriptionItem.getPubnr()));
					}
					
					
					patent.setLegalstateold(legalstateold);*/
					if("DocDB".equals(searchscope)){
						patent.setSearchscope(1);
					}else{
						patent.setSearchscope(0);
					}
					try{	
						patentStoreInfoService.save(patent);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}	
			if(resultList!=null){ 
				for(int i=0;i<resultList.size();i++){
					PatentStoreInfo patent=new PatentStoreInfo();
//					System.out.println("1111---------------"+id);
					
				//	RedisTest.GetSql(path, jedis);
					ImportJedis.GetSql(resultList.get(i).getAppno());
					int totalpatentStoreInfos=0;
					StringBuilder wherejpqlStrBul=new StringBuilder();
					
					if(id!=null&&!"".equals(id)){	
						patent.setPatentCategory(categoryService.find(Integer.parseInt(id)));
						
						wherejpqlStrBul.append(" o.appno = '"+resultList.get(i).getAppno()+"' and o.patentCategory.id = '"+id +"'");
						QueryResult<PatentStoreInfo> patentStoreInfoResult=  patentStoreInfoService.getScrollData(-1, -1, wherejpqlStrBul.toString(), null);
					    totalpatentStoreInfos=(int)patentStoreInfoResult.getTotalrecord();
					    
					    if(totalpatentStoreInfos!=0){
					    	continue;
					    }
					    
					} else {
						StringBuilder wherejpql=new StringBuilder();
						wherejpql.append(" o.name = '"+text+"'");
						QueryResult<PatentCategory> categoryResult=  categoryService.getScrollData(-1, -1, wherejpql.toString(), null);
						int totalrecords=(int)categoryResult.getTotalrecord();	
						PatentCategory category= categoryResult.getResultlist().get(totalrecords-1);
						patent.setPatentCategory(category);
					}
					CNDescriptionItem cnDescriptionItem=resultList.get(i);
					SimpleDateFormat formatDateToString = new SimpleDateFormat(
							"yyyyMMdd");
					patent.setAppno(cnDescriptionItem.getAppno());
					patent.setTitle(cnDescriptionItem.getTitle());
					patent.setApd(formatDateToString.format(cnDescriptionItem
							.getApd()));
					patent.setAppl(cnDescriptionItem.getAppl());
					
					
					int legalStateInt=getLegalStatusType(cnDescriptionItem.getAppno(),cnDescriptionItem.getPubnr(),
							cnDescriptionItem.getPud(),cnDescriptionItem.getAppnr(),cnDescriptionItem.getAppd());
					
					if(legalStateInt==4){
						if(!"".equals(patent.getApd())){
							Date date = DateUtil.formatStrToDate(patent.getApd());
							Calendar ca = Calendar.getInstance();
							ca.setTime(date);
							ca.add(Calendar.YEAR,1);
							patent.setYearfeedate(DateUtil.dateToTextString(ca.getTime()));
						}
						else{
							patent.setYearfeedate("");
						}
					}
					else{
						patent.setYearfeedate("");
					}
					
					
					String legalstateold="";
					//String legalstateold="";
					//设置法律状态
					//legalstateold=getLeagelStatic(cnDescriptionItem);  
					
					if(legalStatusService.getLegalStatusInfoByAppno(XMLUtil.getCheckAppnoWithOutDot(cnDescriptionItem.getAppno())) == null){
						legalstateold="无";
					} else {
						legalstateold= legalStatusService.getLegalStatusInfoByAppno(XMLUtil.getCheckAppnoWithOutDot(cnDescriptionItem.getAppno())).equals("文件的公告送达")?"实质审查的生效":legalStatusService.getLegalStatusInfoByAppno(XMLUtil.getCheckAppnoWithOutDot(cnDescriptionItem.getAppno()));
					}
					
					
					patent.setLegalstateold(legalstateold);
					patent.setLegalstatenew(legalstateold);
					
					if ("DocDB".equals(searchscope)) {
						patent.setSearchscope(1);
					} else {
						patent.setSearchscope(0);
					}
					try {
						patentStoreInfoService.save(patent);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}		
		}
			map.clear();
			map.put("msg", "添加专利成功！");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * 异步获得图片（国内）
	 */
	public void getfutupictureurl() {
		String appnoValue= ServletActionContext.getRequest().getParameter("appnoValue");
		
		String urlStr = XMLUtil.getFuTuByAppno(appnoValue);
		Map<String, Object> map = new HashMap<String, Object>();
		 
		if (WebTool.urlIsValid(urlStr)) {
			map.put("urlStr", urlStr);
		} else {
			map.put("urlStr", ConfigTool.getValue("futu_friendly"));

		}
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/*
	 * 异步获得图片（国外）
	 */
	public void getfutuurl() {
		String rowDatapubnr= ServletActionContext.getRequest().getParameter("rowDatapubnr");
		String rowDatapudText= ServletActionContext.getRequest().getParameter("rowDatapudText");
		
		String urlStr = XMLUtil.getFuTuByPubnr(rowDatapubnr,rowDatapudText);
		Map<String, Object> map = new HashMap<String, Object>();
		 
			map.put("urlStr", urlStr);

		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/*
	 * 异步获得法律状态图片
	 */
	public void getLegalStatusPicture() {
		String appnoValue= ServletActionContext.getRequest().getParameter("appnoValue");
		
		Integer statusTypeNum= getLegalStatusTypePicture(appnoValue);
		Map<String, Object> map = new HashMap<String, Object>();
		 map.put("legalStatusPicture", statusTypeNum);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 
	 * @return
	 */
	public String loadingMessage() {
		String searchFormula= ServletActionContext.getRequest().getParameter("searchFormula");
		ServletActionContext.getRequest().setAttribute("searchFormula",searchFormula);
		ActionContext.getContext().put("message", "请稍后，页面加载中...");
		ActionContext.getContext().put("urladdress", "/front/search/table_analysisUI");
		return "loading";
	}
	/**
	 * 
	 * @return
	 */
	public String analysisUI() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest()); 
		String searchFormula= ServletActionContext.getRequest().getParameter("searchFormula");
		searchFormula=searchFormula.replace("_", "+");                           //慎重单纯这样做会吧大部分的空格造成误解   +不能url传递
		                      ServletActionContext.getRequest().setAttribute("searchFormula",searchFormula);
		String strUserId=user.getId()+"";                      
		DoSearchParameter doSearch = new DoSearchParameter(strUserId, "Cn", "999",searchFormula);
		System.out.println(new Date().getTime());
	     int total=new Long(searchService.handleDoSearch(doSearch)).intValue();
	     System.out.println(new Date().getTime());
	     System.out.println(total);
	     List<String> appnoList = new ArrayList<String>();
	     System.out.println(new Date().getTime() +"+++++");
		if (total < 100000) {
			int totalPage = total / 2000;
			if (totalPage < 1) {
					GetGeneralDataParameter getGeneralData = new GetGeneralDataParameter(
							strUserId, "Cn", "999", 1, total);
					appnoList.addAll(WebServiceLocalClientUtil
							.getAppnolistBySearchWithLastNum(getGeneralData));
			} else {
				for (int i = 1; i <= totalPage; i++) {
					GetGeneralDataParameter getGeneralData = new GetGeneralDataParameter(
							strUserId, "Cn", "999", i, 2000);
					appnoList.addAll(WebServiceLocalClientUtil
							.getAppnolistBySearchWithLastNum(getGeneralData));
				}
			}
		} else {
        	 ActionContext.getContext().put("message", "分析数据不得超过10万条！");
        	 ServletActionContext.getRequest().setAttribute("searchFormula",searchFormula);
        	 ActionContext.getContext().put("urladdress", "/front/search/table_tableSearchUI");
        	return "message";
         }
		System.out.println(new Date().getTime());
        RedisTest.selectForAnalysis(user,appnoList, jedis);  //
        System.out.println(new Date().getTime());
		return "analysisUI";
	}
	/**
	 * 
	 * @return
	 */
	public String analysisUIOwn() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());  
		String ids= ServletActionContext.getRequest().getParameter("ids");
		if (ids != null && ids.length() > 0) {
			String[] idsStr = ids.split(",");
			List<String> appnoList = new ArrayList<String>();
			Collections.addAll(appnoList, idsStr);
			RedisTest.selectForAnalysis(user, appnoList, jedis);
		} else {
			String id=	 ServletActionContext.getRequest().getParameter("id");
			StringBuilder wherejpql=new StringBuilder();
			wherejpql.append("o.patentCategory.id="+id);
			List<String> appnoList= patentStoreInfoService.getAppnoListById(wherejpql.toString());
			RedisTest.selectForAnalysis(user, appnoList, jedis);
		}
//		appnoList.addAll(idsStr);
		
//		} else {
//        	 ActionContext.getContext().put("message", "分析数据不得超过10万条！");
//        	 ServletActionContext.getRequest().setAttribute("searchFormula",searchFormula);
//        	 ActionContext.getContext().put("urladdress", "/front/search/table_tableSearchUI");
//        	return "message";
//         }
//        RedisTest.selectForAnalysis(user,appnoList, jedis);  //
		return "analysisUI";
	}
	
	
	
	
	/**
	 * 
	 * @return
	 */
	public String analysisTab1() {
		String searchFormula= ServletActionContext.getRequest().getParameter("searchFormula");
        ServletActionContext.getRequest().setAttribute("searchFormula",searchFormula);
		return "analysisTab1UI";
	}
	/**
	 * 
	 * @return
	 */
	public String analysisTab2() {
		String searchFormula= ServletActionContext.getRequest().getParameter("searchFormula");
        ServletActionContext.getRequest().setAttribute("searchFormula",searchFormula);
		return "analysisTab2UI";
	}
	/**
	 * 
	 * @return
	 */
	public String analysisTab3() {
		String searchFormula= ServletActionContext.getRequest().getParameter("searchFormula");
        ServletActionContext.getRequest().setAttribute("searchFormula",searchFormula);
		return "analysisTab3UI";
	}
	/**
	 * 
	 * @return
	 */
	public String analysisTab4() {
		String searchFormula= ServletActionContext.getRequest().getParameter("searchFormula");
		if(null!=searchFormula&&!"".equals(searchFormula.trim())){
			searchFormula = searchFormula.substring(0, 6)+searchFormula.substring(6, searchFormula.length()).replace(" ", "+");
			System.out.println("searchFormula"+searchFormula);	
			this.setSearchFormula(searchFormula);
		}
			

        ServletActionContext.getRequest().setAttribute("searchFormula",searchFormula);
		return "analysisTab4UI";
	}
	/**
	 * 
	 * @return
	 */
	public String analysisTab5() {
		String searchFormula= ServletActionContext.getRequest().getParameter("searchFormula");
        ServletActionContext.getRequest().setAttribute("searchFormula",searchFormula);
		return "analysisTab5UI";
	}
	/**
	 * 
	 * @return
	 */
	public String analysisTab6() {
		String searchFormula= ServletActionContext.getRequest().getParameter("searchFormula");
        ServletActionContext.getRequest().setAttribute("searchFormula",searchFormula);
		return "analysisTab6UI";
	}
	/**
	 * 获取统计信息json 形成报表图形(总体趋势分析)
	 */
	public void getPatentSumData() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		 String[] apdJly=jedis.get(user.getId()+"apdJly").split("_");
		 apdJly[apdJly.length-1]=apdJly[apdJly.length-1].substring(0, apdJly[apdJly.length-1].length()-3);
		 String[] pubJly=jedis.get(user.getId()+"pubJly").split("_");
		 pubJly[pubJly.length-1]=pubJly[pubJly.length-1].substring(0, pubJly[pubJly.length-1].length()-3);
		 String[] appdJly=jedis.get(user.getId()+"appdJly").split("_");
		 appdJly[appdJly.length-1]=appdJly[appdJly.length-1].substring(0, appdJly[appdJly.length-1].length()-3);
		 
		int[]  intBurApdArrary=new int[10];
		int[]  intPudBurArrary=new int[10];
		int[]  intAppdBurArrary=new int[10];
		for(int i=0;i<apdJly.length;i++){
			String strApd=apdJly[i]; //获得申请日期
			String strPud=pubJly[i]; //获得公开日期
			String strAppd=appdJly[i];//获得授权日期
			if("2007".equals(strApd)){
				intBurApdArrary[0]++;
			}else if("2008".equals(strApd)){
				intBurApdArrary[1]++;
			}else if("2009".equals(strApd)){
				intBurApdArrary[2]++;
			}else if("2010".equals(strApd)){
				intBurApdArrary[3]++;
			}else if("2011".equals(strApd)){
				intBurApdArrary[4]++;
			}else if("2012".equals(strApd)){
				intBurApdArrary[5]++;
			}else if("2013".equals(strApd)){
				intBurApdArrary[6]++;
			}else if("2014".equals(strApd)){
				intBurApdArrary[7]++;
			}else if("2015".equals(strApd)){
				intBurApdArrary[8]++;
			}else if("2016".equals(strApd)){
				intBurApdArrary[9]++;
			}
			if("2007".equals(strPud)){
				intPudBurArrary[0]++;
			}else if("2008".equals(strPud)){
				intPudBurArrary[1]++;
			}else if("2009".equals(strPud)){
				intPudBurArrary[2]++;
			}else if("2010".equals(strPud)){
				intPudBurArrary[3]++;
			}else if("2011".equals(strPud)){
				intPudBurArrary[4]++;
			}else if("2012".equals(strPud)){
				intPudBurArrary[5]++;
			}else if("2013".equals(strPud)){
				intPudBurArrary[6]++;
			}else if("2014".equals(strPud)){
				intPudBurArrary[7]++;
			}else if("2015".equals(strPud)){
				intPudBurArrary[8]++;
			}else if("2016".equals(strPud)){
				intPudBurArrary[9]++;
			}
			if("2007".equals(strAppd)){
				intAppdBurArrary[0]++;
			}else if("2008".equals(strAppd)){
				intAppdBurArrary[1]++;
			}else if("2009".equals(strAppd)){
				intAppdBurArrary[2]++;
			}else if("2010".equals(strAppd)){
				intAppdBurArrary[3]++;
			}else if("2011".equals(strAppd)){
				intAppdBurArrary[4]++;
			}else if("2012".equals(strAppd)){
				intAppdBurArrary[5]++;
			}else if("2013".equals(strAppd)){
				intAppdBurArrary[6]++;
			}else if("2014".equals(strAppd)){
				intAppdBurArrary[7]++;
			}else if("2015".equals(strAppd)){
				intAppdBurArrary[8]++;
			}else if("2016".equals(strAppd)){
				intAppdBurArrary[9]++;
			}
		}
		StringBuilder apdBur=new StringBuilder();
		StringBuilder pubBur=new StringBuilder();
		StringBuilder appdBur=new StringBuilder();
		for(int i=0;i<10;i++){
			apdBur.append(intBurApdArrary[i]).append(",");
			pubBur.append(intPudBurArrary[i]).append(",");
			appdBur.append(intAppdBurArrary[i]).append(",");
		}
		apdBur.deleteCharAt(apdBur.length()-1);
		pubBur.deleteCharAt(pubBur.length()-1);
		appdBur.deleteCharAt(appdBur.length()-1);
		jedis.set(user.getId()+"apd", apdBur.toString());
		jedis.expire(user.getId()+"apd", 10000);
		jedis.set(user.getId()+"pub", pubBur.toString());
		jedis.expire(user.getId()+"pub", 10000);
		jedis.set(user.getId()+"appd", appdBur.toString());
		jedis.expire(user.getId()+"appd", 10000);
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> series = new ArrayList<Map>();
		
		List<String> categories = new ArrayList<String>();
		for (int year = 2007; year <= 2016; year++) {
			categories.add(year + "");
		}
		for (int i = 1; i <= 3; i++) {
			Map<String, Object> m = new HashMap<String, Object>();
			List<BigInteger> data = new ArrayList<BigInteger>();
			if(i==1){
				for(int j=0;j<intBurApdArrary.length;j++){
					data.add(BigInteger.valueOf(intBurApdArrary[j]));
				}
				m.put("name","申请总数");
			}else if(i==2){
				for(int j=0;j<intPudBurArrary.length;j++){
					data.add(BigInteger.valueOf(intPudBurArrary[j]));
				}
				m.put("name","公开总数");
			}else if(i==3){
				for(int j=0;j<intAppdBurArrary.length;j++){
					data.add(BigInteger.valueOf(intAppdBurArrary[j]));
				}
				m.put("name","授权总数");
			}
			m.put("data", data);
			series.add(m);
		}
		map.put("series", series);
		map.put("categories", categories);
		
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取统计信息json 形成表格
	 */
	public void getPatentSumData_dataGrid() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		String[] strBurArrary=jedis.get(user.getId()+"apd").split(",");
		String[] pubstrBurArrary=jedis.get(user.getId()+"pub").split(",");
		String[] accstrBurArrary=jedis.get(user.getId()+"appd").split(",");
		
		List<Map> records = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		total=new Long((long)strBurArrary.length);
		Map<String, Object> m0 = new HashMap<String, Object>(); 
		m0.put("year","年份");
		m0.put("apdSum","申请总数");
		m0.put("pubSum","公开总数");
		m0.put("appdSum","授权总数");
		records.add(m0);
		for (int i = 0; i < strBurArrary.length; i++) {
			Map<String, Object> m = new HashMap<String, Object>(); 
			m.put("year", 2007+i+"年");
			m.put("apdSum",strBurArrary[i]);
			m.put("pubSum", pubstrBurArrary[i]);
			m.put("appdSum", accstrBurArrary[i]);
			records.add(m);
		}
		map.put("total", total);
		map.put("rows", records);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取统计信息json 形成报表图形（申请总数）
	 */
	public void getPatentSumData_shenqing() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		 String[] apdJly=jedis.get(user.getId()+"apdJly").split("_");
		 apdJly[apdJly.length-1]=apdJly[apdJly.length-1].substring(0, apdJly[apdJly.length-1].length()-3);
		int[]  intBurApdArrary=new int[10];
		for(int i=0;i<apdJly.length;i++){
			String strApd=apdJly[i]; //获得申请日期
			if("2007".equals(strApd)){
				intBurApdArrary[0]++;
			}else if("2008".equals(strApd)){
				intBurApdArrary[1]++;
			}else if("2009".equals(strApd)){
				intBurApdArrary[2]++;
			}else if("2010".equals(strApd)){
				intBurApdArrary[3]++;
			}else if("2011".equals(strApd)){
				intBurApdArrary[4]++;
			}else if("2012".equals(strApd)){
				intBurApdArrary[5]++;
			}else if("2013".equals(strApd)){
				intBurApdArrary[6]++;
			}else if("2014".equals(strApd)){
				intBurApdArrary[7]++;
			}else if("2015".equals(strApd)){
				intBurApdArrary[8]++;
			}else if("2016".equals(strApd)){
				intBurApdArrary[9]++;
			}
		}	
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> series = new ArrayList<Map>();
		
		List<String> categories = new ArrayList<String>();
		for (int year = 2007; year <= 2016; year++) {
			categories.add(year + "");
		}
			Map<String, Object> m = new HashMap<String, Object>();
			List<BigInteger> data = new ArrayList<BigInteger>();
				for(int j=0;j<intBurApdArrary.length;j++){
					data.add(BigInteger.valueOf(intBurApdArrary[j]));
				}
				m.put("name","申请总数");
			m.put("data", data);
			series.add(m);
		map.put("series", series);
		map.put("categories", categories);
		
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取统计信息json 形成表格(申请)
	 */
	public void getPatentSumData_dataGrid_shenqing() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		String[] strBurArrary=jedis.get(user.getId()+"apd").split(",");
		List<Map> records = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		total=new Long((long)strBurArrary.length);
		for (int i = 0; i < strBurArrary.length; i++) {
			Map<String, Object> m = new HashMap<String, Object>(); 
			m.put("year", 2007+i+"年");
			m.put("apdSum",strBurArrary[i]);
			records.add(m);
		}
		map.put("total", total);
		map.put("rows", records);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取统计信息json 形成报表图形（公开总数）
	 */
	public void getPatentSumData_gongkai() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		 String[] pubJly=jedis.get(user.getId()+"pubJly").split("_");
		 pubJly[pubJly.length-1]=pubJly[pubJly.length-1].substring(0, pubJly[pubJly.length-1].length()-3);  //删除末尾的Jly
		int[]  intPudBurArrary=new int[10];
		for(int i=0;i<pubJly.length;i++){
			String strPud=pubJly[i]; //获得公开日期
			if("2007".equals(strPud)){
				intPudBurArrary[0]++;
			}else if("2008".equals(strPud)){
				intPudBurArrary[1]++;
			}else if("2009".equals(strPud)){
				intPudBurArrary[2]++;
			}else if("2010".equals(strPud)){
				intPudBurArrary[3]++;
			}else if("2011".equals(strPud)){
				intPudBurArrary[4]++;
			}else if("2012".equals(strPud)){
				intPudBurArrary[5]++;
			}else if("2013".equals(strPud)){
				intPudBurArrary[6]++;
			}else if("2014".equals(strPud)){
				intPudBurArrary[7]++;
			}else if("2015".equals(strPud)){
				intPudBurArrary[8]++;
			}else if("2016".equals(strPud)){
				intPudBurArrary[9]++;
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> series = new ArrayList<Map>();
		
		List<String> categories = new ArrayList<String>();
		for (int year = 2007; year <= 2016; year++) {
			categories.add(year + "");
		}
			Map<String, Object> m = new HashMap<String, Object>();
			List<BigInteger> data = new ArrayList<BigInteger>();
				for(int j=0;j<intPudBurArrary.length;j++){
					data.add(BigInteger.valueOf(intPudBurArrary[j]));
				}
				m.put("name","公开总数");
			m.put("data", data);
			series.add(m);
		map.put("series", series);
		map.put("categories", categories);
		
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	/**
	 * 获取统计信息json 形成表格(公开)
	 */
	public void getPatentSumData_dataGrid_gongkai() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		String[] pubstrBurArrary=jedis.get(user.getId()+"pub").split(",");
		List<Map> records = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		total=new Long((long)pubstrBurArrary.length);
		for (int i = 0; i < pubstrBurArrary.length; i++) {
			Map<String, Object> m = new HashMap<String, Object>(); 
			m.put("year", 2007+i+"年");
			m.put("pubSum", pubstrBurArrary[i]);
			records.add(m);
		}
		map.put("total", total);
		map.put("rows", records);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取统计信息json 形成报表图形（授权总数）
	 */
	public void getPatentSumData_shouquan() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		 String[] appdJly=jedis.get(user.getId()+"appdJly").split("_");
		 appdJly[appdJly.length-1]=appdJly[appdJly.length-1].substring(0, appdJly[appdJly.length-1].length()-3);    ////删除末尾的Jly
		int[]  intAppdBurArrary=new int[10];
		for(int i=0;i<appdJly.length;i++){
			String strAppd=appdJly[i];//获得授权日期
			if("2007".equals(strAppd)){
				intAppdBurArrary[0]++;
			}else if("2008".equals(strAppd)){
				intAppdBurArrary[1]++;
			}else if("2009".equals(strAppd)){
				intAppdBurArrary[2]++;
			}else if("2010".equals(strAppd)){
				intAppdBurArrary[3]++;
			}else if("2011".equals(strAppd)){
				intAppdBurArrary[4]++;
			}else if("2012".equals(strAppd)){
				intAppdBurArrary[5]++;
			}else if("2013".equals(strAppd)){
				intAppdBurArrary[6]++;
			}else if("2014".equals(strAppd)){
				intAppdBurArrary[7]++;
			}else if("2015".equals(strAppd)){
				intAppdBurArrary[8]++;
			}else if("2016".equals(strAppd)){
				intAppdBurArrary[9]++;
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> series = new ArrayList<Map>();
		List<String> categories = new ArrayList<String>();
		for (int year = 2007; year <= 2016; year++) {
			categories.add(year + "");
		}
			Map<String, Object> m = new HashMap<String, Object>();
			List<BigInteger> data = new ArrayList<BigInteger>();
				for(int j=0;j<intAppdBurArrary.length;j++){
					data.add(BigInteger.valueOf(intAppdBurArrary[j]));
				}
				m.put("name","授权总数");
			m.put("data", data);
			series.add(m);
		map.put("series", series);
		map.put("categories", categories);
		
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取统计信息json 形成表格（授权）
	 */
	public void getPatentSumData_dataGrid_shouquan() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		String[] strBurArrary=jedis.get(user.getId()+"apd").split(",");
		String[] pubstrBurArrary=jedis.get(user.getId()+"pub").split(",");
		String[] accstrBurArrary=jedis.get(user.getId()+"appd").split(",");
		
		List<Map> records = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		total=new Long((long)strBurArrary.length);
		for (int i = 0; i < strBurArrary.length; i++) {
			Map<String, Object> m = new HashMap<String, Object>(); 
			m.put("year", 2007+i+"年");
			m.put("apdSum",strBurArrary[i]);
			m.put("pubSum", pubstrBurArrary[i]);
			m.put("appdSum", accstrBurArrary[i]);
			records.add(m);
		}
		map.put("total", total);
		map.put("rows", records);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 
	 * 
	 * 
	 * 获取统计信息json 形成报表图形（国省）
	 */
	public void getPatentSumData_nc() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> series = new ArrayList<Map>();
		List<String> categories = new ArrayList<String>();
		 String[] ss= jedis.get(user.getId()+"nc").split("_");
		 String temp=null;
		 List<BigInteger> data = new ArrayList<BigInteger>();
		 StringBuilder ncStr=new StringBuilder();
		 StringBuilder sumStr=new StringBuilder();
		 if(ss.length>=10){
			 for(int i=0;i<10;i++){
				 temp=ss[i];
				 if(temp!=null&&!"".equals(temp)){
					 String tempFront=temp.substring(0, temp.indexOf("="));
					 String strNC=RedisTest.getAddrNameByCode(tempFront);
					 String strSUM=temp.substring(temp.indexOf("=")+1, temp.length());
						categories.add(strNC);
						data.add(BigInteger.valueOf(Long.parseLong(strSUM)));
						ncStr.append(strNC).append("(").append(tempFront).append(")").append(",");    //把国省和国省代码都放到内存数据库里面
						sumStr.append(strSUM).append(",");
					}
			 }
		 }else{
			 for(int i=0;i<ss.length;i++){
				 temp=ss[i];
				 if(temp!=null&&!"".equals(temp)){
					 String tempFront=temp.substring(0, temp.indexOf("="));
					 String strNC=RedisTest.getAddrNameByCode(tempFront);
					 String strSUM=temp.substring(temp.indexOf("=")+1, temp.length());
						categories.add(strNC);
						data.add(BigInteger.valueOf(Long.parseLong(strSUM)));
						ncStr.append(strNC).append("(").append(tempFront).append(")").append(",");    //把国省和国省代码都放到内存数据库里面
						sumStr.append(strSUM).append(",");
					}
			 }
		 }
		 ncStr.deleteCharAt(ncStr.length()-1);
		 sumStr.deleteCharAt(sumStr.length()-1);
		 jedis.set(user.getId()+"ncName", ncStr.toString());
		 jedis.expire(user.getId()+"ncName", 10000);
		 jedis.set(user.getId()+"ncSum", sumStr.toString());
		 jedis.expire(user.getId()+"ncSum", 10000);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("name","专利数量");
		m.put("data", data);
		series.add(m);
		map.put("series", series);
		map.put("categories", categories);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取统计信息json 形成表格(国省)
	 */
	public void getPatentSumData_dataGrid_nc() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		String[] ncBurArrary=jedis.get(user.getId()+"ncName").split(",");
		String[] ncSumBurArrary=jedis.get(user.getId()+"ncSum").split(",");
		
		List<Map> records = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		total=new Long((long)ncBurArrary.length);
		if(ncBurArrary.length>=10){
			for (int i = 0; i <10; i++) {
				Map<String, Object> m = new HashMap<String, Object>(); 
				m.put("ncName",ncBurArrary[i]);
				m.put("ncSum", ncSumBurArrary[i]);
				records.add(m);
			}
		}else{
			for (int i = 0; i <ncBurArrary.length; i++) {
				Map<String, Object> m = new HashMap<String, Object>(); 
				m.put("ncName",ncBurArrary[i]);
				m.put("ncSum", ncSumBurArrary[i]);
				records.add(m);
			}
		}
		map.put("total", total);
		map.put("rows", records);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 
	 * 
	 * 
	 * 获取统计信息json 形成报表图形（申请人）
	 */
	public void getPatentSumData_shenqingren() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> series = new ArrayList<Map>();
		List<String> categories = new ArrayList<String>();
		 String[] ss= jedis.get(user.getId()+"sqr").split("_");
		 String temp=null;
		 List<BigInteger> data = new ArrayList<BigInteger>();
		 StringBuilder applStr=new StringBuilder();
		 StringBuilder sumStr=new StringBuilder();
		 if(ss.length>=10){
			 for(int i=0;i<10;i++){
				 temp=ss[i];
				 if(temp!=null&&!"".equals(temp)){
					 String strAPPL=temp.substring(0, temp.indexOf("="));
					 String strSUM=temp.substring(temp.indexOf("=")+1, temp.length());
						categories.add(strAPPL);
						data.add(BigInteger.valueOf(Long.parseLong(strSUM)));
						applStr.append(strAPPL).append(",");
						sumStr.append(strSUM).append(",");
					}
			 }
		 }else{
			 for(int i=0;i<ss.length;i++){
				 temp=ss[i];
				 if(temp!=null&&!"".equals(temp)){
					 String strAPPL=temp.substring(0, temp.indexOf("="));
					 String strSUM=temp.substring(temp.indexOf("=")+1, temp.length());
						categories.add(strAPPL);
						data.add(BigInteger.valueOf(Long.parseLong(strSUM)));
						applStr.append(strAPPL).append(",");
						sumStr.append(strSUM).append(",");
					}
			 } 
		 }
		 applStr.deleteCharAt(applStr.length()-1);
		 sumStr.deleteCharAt(sumStr.length()-1);
		 jedis.set(user.getId()+"applName", applStr.toString());
		 jedis.expire(user.getId()+"applName", 10000);
		 jedis.set(user.getId()+"applSum", sumStr.toString());
		 jedis.expire(user.getId()+"applSum", 10000);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("name","专利数量");
		m.put("data", data);
		series.add(m);
		map.put("series", series);
		map.put("categories", categories);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取统计信息json 形成表格(申请人)
	 */
	public void getPatentSumData_dataGrid_appl() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		String[] applBurArrary=jedis.get(user.getId()+"applName").split(",");
		String[] applSumBurArrary=jedis.get(user.getId()+"applSum").split(",");
		
		List<Map> records = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		total=new Long((long)applBurArrary.length);
		if(applBurArrary.length>=10){
			for (int i = 0; i <10; i++) {
				Map<String, Object> m = new HashMap<String, Object>(); 
				m.put("applName", applBurArrary[i]);
				m.put("applSum", applSumBurArrary[i]);
				records.add(m);
			}
		}else{
			for (int i = 0; i <applBurArrary.length; i++) {
				Map<String, Object> m = new HashMap<String, Object>(); 
				m.put("applName", applBurArrary[i]);
				m.put("applSum", applSumBurArrary[i]);
				records.add(m);
			}
		}
		map.put("total", total);
		map.put("rows", records);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * 
	 * 
	 * 获取统计信息json 形成报表图形（发明人）
	 */
	public void getPatentSumData_famingren() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> series = new ArrayList<Map>();
		List<String> categories = new ArrayList<String>();
		 String[] ss= jedis.get(user.getId()+"fmr").split("_");
		 String temp=null;
		 List<BigInteger> data = new ArrayList<BigInteger>();
		 StringBuilder inventorStr=new StringBuilder();
		 StringBuilder sumStr=new StringBuilder();
		 if(ss.length>=10){
			 for(int i=0;i<10;i++){
				 temp=ss[i];
				 if(temp!=null&&!"".equals(temp)){
					 String strINVENTOR=temp.substring(0, temp.indexOf("="));
					 String strSUM=temp.substring(temp.indexOf("=")+1, temp.length());
						categories.add(strINVENTOR);
						data.add(BigInteger.valueOf(Long.parseLong(strSUM)));
						inventorStr.append(strINVENTOR).append(",");
						sumStr.append(strSUM).append(",");
					}
			 }
		 }else{
			 for(int i=0;i<ss.length;i++){
				 temp=ss[i];
				 if(temp!=null&&!"".equals(temp)){
					 String strINVENTOR=temp.substring(0, temp.indexOf("="));
					 String strSUM=temp.substring(temp.indexOf("=")+1, temp.length());
						categories.add(strINVENTOR);
						data.add(BigInteger.valueOf(Long.parseLong(strSUM)));
						inventorStr.append(strINVENTOR).append(",");
						sumStr.append(strSUM).append(",");
					}
			 }
		 }
		 inventorStr.deleteCharAt(inventorStr.length()-1);
		 sumStr.deleteCharAt(sumStr.length()-1);
		 jedis.set(user.getId()+"inventorName", inventorStr.toString());
		 jedis.expire(user.getId()+"inventorName", 10000);
		 jedis.set(user.getId()+"inventorSum", sumStr.toString());
		 jedis.expire(user.getId()+"inventorSum", 10000);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("name","专利数量");
		m.put("data", data);
		series.add(m);
		map.put("series", series);
		map.put("categories", categories);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取统计信息json 形成表格(发明人)
	 */
	public void getPatentSumData_dataGrid_inventor() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		String[] inventorBurArrary=jedis.get(user.getId()+"inventorName").split(",");
		String[] inventorSumBurArrary=jedis.get(user.getId()+"inventorSum").split(",");
		
		List<Map> records = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		total=new Long((long)inventorBurArrary.length);
		if(inventorBurArrary.length>=10){
			for (int i = 0; i <10; i++) {
				Map<String, Object> m = new HashMap<String, Object>(); 
				m.put("inventorName", inventorBurArrary[i]);
				m.put("inventorSum", inventorSumBurArrary[i]);
				records.add(m);
			}
		}else{
			for (int i = 0; i <inventorBurArrary.length; i++) {
				Map<String, Object> m = new HashMap<String, Object>(); 
				m.put("inventorName", inventorBurArrary[i]);
				m.put("inventorSum", inventorSumBurArrary[i]);
				records.add(m);
			}
		}
		map.put("total", total);
		map.put("rows", records);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 
	 * 
	 * 
	 * 获取统计信息json 形成报表图形（技术分类）
	 */
	public void getPatentSumData_ipc() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> series = new ArrayList<Map>();
		List<String> categories = new ArrayList<String>();
		 String[] ss= jedis.get(user.getId()+"ipc").split("_");
		 String temp=null;
		 List<BigInteger> data = new ArrayList<BigInteger>();
		 StringBuilder ipcStr=new StringBuilder();
		 StringBuilder sumStr=new StringBuilder();
		 if(ss.length>=10){
			 for(int i=0;i<10;i++){
				 temp=ss[i];
				 if(temp!=null&&!"".equals(temp)){
					 String strIPC=temp.substring(0, temp.indexOf("="));
					 String strSUM=temp.substring(temp.indexOf("=")+1, temp.length());
					  categories.add(strIPC);
					  data.add(BigInteger.valueOf(Long.parseLong(strSUM)));
					    ipcStr.append(strIPC).append(",");
						sumStr.append(strSUM).append(",");
				 }
			 }
		 }else{
			 for(int i=0;i<ss.length;i++){
				 temp=ss[i];
				 if(temp!=null&&!"".equals(temp)){
					 String strIPC=temp.substring(0, temp.indexOf("="));
					 String strSUM=temp.substring(temp.indexOf("=")+1, temp.length());
					  categories.add(strIPC);
					  data.add(BigInteger.valueOf(Long.parseLong(strSUM)));
					    ipcStr.append(strIPC).append(",");
						sumStr.append(strSUM).append(",");
				 }
			 }
		 }
		 ipcStr.deleteCharAt(ipcStr.length()-1);
		 sumStr.deleteCharAt(sumStr.length()-1);
		 jedis.set(user.getId()+"ipcName", ipcStr.toString());
		 jedis.expire(user.getId()+"ipcName", 10000);
		 jedis.set(user.getId()+"ipcSum", sumStr.toString());
		 jedis.expire(user.getId()+"ipcSum", 10000);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("name","专利数量");
		m.put("data", data);
		series.add(m);
		map.put("series", series);
		map.put("categories", categories);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取统计信息json 形成表格(ipc)
	 */
	public void getPatentSumData_dataGrid_ipc() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		String[] ipcBurArrary=jedis.get(user.getId()+"ipcName").split(",");
		String[] ipcSumBurArrary=jedis.get(user.getId()+"ipcSum").split(",");
		List<Map> records = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		total=new Long((long)ipcBurArrary.length);
		if(ipcBurArrary.length>=10){
			for (int i = 0; i <10; i++) {
				Map<String, Object> m = new HashMap<String, Object>(); 
				m.put("ipcName", ipcBurArrary[i]);
				m.put("ipcSum", ipcSumBurArrary[i]);
				records.add(m);
			}
		}else{
			for (int i = 0; i <ipcBurArrary.length; i++) {
				Map<String, Object> m = new HashMap<String, Object>(); 
				m.put("ipcName", ipcBurArrary[i]);
				m.put("ipcSum", ipcSumBurArrary[i]);
				records.add(m);
			}
		}
		map.put("total", total);
		map.put("rows", records);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取统计信息json 形成报表图形（法律状态）
	 */
	public void getPatentSumData_law() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		 String[] lawJly=jedis.get(user.getId()+"lawJly").split("_");
		int	lawSum1=0;
		int	lawSum2=0;
		int	lawSum3=0;
		for(int i=0;i<lawJly.length;i++){
			String lawStatus=lawJly[i];//获得授权日期
			if(("有效".equals(lawStatus))){
				lawSum1++;
			}
			else if(("审中".equals(lawStatus))||("".equals(lawStatus))){
				lawSum2++;
			}
			else if(("失效".equals(lawStatus))){
				lawSum3++;
			}
		}
		jedis.set(user.getId()+"lawSum1", lawSum1+"");
		jedis.expire(user.getId()+"lawSum1", 10000);
		jedis.set(user.getId()+"lawSum2", lawSum2+"");
		jedis.expire(user.getId()+"lawSum2", 10000);
		jedis.set(user.getId()+"lawSum3", lawSum3+"");
		jedis.expire(user.getId()+"lawSum3", 10000);
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> series = new ArrayList<Map>();
		List<String> categories = new ArrayList<String>();
			categories.add("无效专利");
			categories.add("有效专利");
			categories.add("审中专利");
			Map<String, Object> m = new HashMap<String, Object>();
			List<BigInteger> data = new ArrayList<BigInteger>();
			data.add(BigInteger.valueOf(lawSum1));
			data.add(BigInteger.valueOf(lawSum2));
			data.add(BigInteger.valueOf(lawSum3));
			
			
			
			m.put("name","数量");
			m.put("data", data);
			series.add(m);
		map.put("series", series);
		map.put("categories", categories);
		
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取统计信息json 形成表格(law)
	 */
	public void getPatentSumData_dataGrid_law() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		String lawSum1=jedis.get(user.getId()+"lawSum1");
		String lawSum2=jedis.get(user.getId()+"lawSum2");
		String lawSum3=jedis.get(user.getId()+"lawSum3");
		List<Map> records = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		total=new Long(1+"");
			Map<String, Object> m = new HashMap<String, Object>(); 
			m.put("lawName", "无效专利");
			m.put("lawSum", lawSum1);
			records.add(m);
			
			m = new HashMap<String, Object>(); 
			m.put("lawName", "有效专利");
			m.put("lawSum", lawSum2);
			records.add(m);
			
			m = new HashMap<String, Object>(); 
			m.put("lawName", "审中专利");
			m.put("lawSum", lawSum3);
			records.add(m);
		map.put("total", total);
		map.put("rows", records);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取统计信息json 形成报表图形（国省趋势分析）
	 * 
	 * 
	 */
	public void getPatentSumData_ncQS() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> series = new ArrayList<Map>();
		List<String> categories = new ArrayList<String>();
		 String[] ss= jedis.get(user.getId()+"nc").split("_");
		 String temp=null;
		 String[] strBurArrary=new String[10];
		 if(ss.length>=10){
			 for(int i=0;i<10;i++){
				 temp=ss[i];
				 if(temp!=null&&!"".equals(temp)){
					    strBurArrary[i]=temp.substring(0, temp.indexOf("="));
						jedis.del(user.getId()+strBurArrary[i]+"2007");
						jedis.del(user.getId()+strBurArrary[i]+"2008");
						jedis.del(user.getId()+strBurArrary[i]+"2009");
						jedis.del(user.getId()+strBurArrary[i]+"2010");
						jedis.del(user.getId()+strBurArrary[i]+"2011");
						jedis.del(user.getId()+strBurArrary[i]+"2012");
						jedis.del(user.getId()+strBurArrary[i]+"2013");
						jedis.del(user.getId()+strBurArrary[i]+"2014");
						jedis.del(user.getId()+strBurArrary[i]+"2015");
						jedis.del(user.getId()+strBurArrary[i]+"2016");
					}
			 }
		 }else{
			 for(int i=0;i<ss.length;i++){
				 temp=ss[i];
				 if(temp!=null&&!"".equals(temp)){
					    strBurArrary[i]=temp.substring(0, temp.indexOf("="));
						jedis.del(user.getId()+strBurArrary[i]+"2007");
						jedis.del(user.getId()+strBurArrary[i]+"2008");
						jedis.del(user.getId()+strBurArrary[i]+"2009");
						jedis.del(user.getId()+strBurArrary[i]+"2010");
						jedis.del(user.getId()+strBurArrary[i]+"2011");
						jedis.del(user.getId()+strBurArrary[i]+"2012");
						jedis.del(user.getId()+strBurArrary[i]+"2013");
						jedis.del(user.getId()+strBurArrary[i]+"2014");
						jedis.del(user.getId()+strBurArrary[i]+"2015");
						jedis.del(user.getId()+strBurArrary[i]+"2016");
					}
			 }
		 }
		 for(int i=0;i<10;i++){
			 categories.add((2007+i)+"");     //目录
		 }
		 String[] apdJly=jedis.get(user.getId()+"apdJly").split("_");
		 apdJly[apdJly.length-1]=apdJly[apdJly.length-1].substring(0, apdJly[apdJly.length-1].length()-3);    ////删除末尾的Jly
		 String[] ncJly=jedis.get(user.getId()+"ncJly").split("_");
		 ncJly[ncJly.length-1]=ncJly[ncJly.length-1].substring(0, ncJly[ncJly.length-1].length()-3);    ////删除末尾的Jly
		 for(int i=0;i<ncJly.length;i++){
			String srtSQR=ncJly[i];//获得申请人
			if((srtSQR!=null)&&(!"".equals(srtSQR))){    //申请人不空
				if(srtSQR.contains(",")){      //多个申请人
					String[] strs=srtSQR.split(",");
					for(int j=0;j<strs.length;j++){
						 if(ss.length>=10){
							for(int k=0;k<10;k++){   //申请人跟10个要统计的进行比对
								if(strBurArrary[k].equals(strs[j])){   //比对成功
									String strYear=apdJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
									if((strYear!=null)&&(!"".equals(strYear))){
										if("2007".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2007", "1");
										}else if("2008".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2008", "1");
										}else if("2009".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2009", "1");
										}else if("2010".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2010", "1");
										}else if("2011".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2011", "1");
										}else if("2012".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2012", "1");
										}else if("2013".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2013", "1");
										}else if("2014".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2014", "1");
										}else if("2015".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2015", "1");
										}else if("2016".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2016", "1");
										}
									}
								}
							}
						}else{
							for(int k=0;k<ss.length;k++){   //申请人跟10个要统计的进行比对
								if(strBurArrary[k].equals(strs[j])){   //比对成功
									String strYear=apdJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
									if((strYear!=null)&&(!"".equals(strYear))){
										if("2007".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2007", "1");
										}else if("2008".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2008", "1");
										}else if("2009".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2009", "1");
										}else if("2010".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2010", "1");
										}else if("2011".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2011", "1");
										}else if("2012".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2012", "1");
										}else if("2013".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2013", "1");
										}else if("2014".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2014", "1");
										}else if("2015".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2015", "1");
										}else if("2016".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2016", "1");
										}
									}
								}
							}
						}
					}
				}else{  //单个申请人
					if(ss.length>=10){
						for(int k=0;k<10;k++){   //申请人跟10个要统计的进行比对
							if(strBurArrary[k].equals(srtSQR)){   //比对成功
								String strYear=apdJly[i];       //这里可以jedis.append(srtSHR+strYear,"1");
								if((strYear!=null)&&(!"".equals(strYear))){
									if("2007".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2007", "1");
									}else if("2008".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2008", "1");
									}else if("2009".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2009", "1");
									}else if("2010".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2010", "1");
									}else if("2011".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2011", "1");
									}else if("2012".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2012", "1");
									}else if("2013".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2013", "1");
									}else if("2014".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2014", "1");
									}else if("2015".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2015", "1");
									}else if("2016".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2016", "1");
									}
								}
							}
						}
					}else{
						for(int k=0;k<ss.length;k++){   //申请人跟10个要统计的进行比对
							if(strBurArrary[k].equals(srtSQR)){   //比对成功
								String strYear=apdJly[i];       //这里可以jedis.append(srtSHR+strYear,"1");
								if((strYear!=null)&&(!"".equals(strYear))){
									if("2007".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2007", "1");
									}else if("2008".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2008", "1");
									}else if("2009".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2009", "1");
									}else if("2010".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2010", "1");
									}else if("2011".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2011", "1");
									}else if("2012".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2012", "1");
									}else if("2013".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2013", "1");
									}else if("2014".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2014", "1");
									}else if("2015".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2015", "1");
									}else if("2016".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2016", "1");
									}
								}
							}
						}
					}
				}
			}
		}
		StringBuilder ncName=new StringBuilder();
		StringBuilder ncName0=new StringBuilder();
		StringBuilder ncName1=new StringBuilder();
		StringBuilder ncName2=new StringBuilder();
		StringBuilder ncName3=new StringBuilder();
		StringBuilder ncName4=new StringBuilder();
		StringBuilder ncName5=new StringBuilder();
		StringBuilder ncName6=new StringBuilder();
		StringBuilder ncName7=new StringBuilder();
		StringBuilder ncName8=new StringBuilder();
		StringBuilder ncName9=new StringBuilder();
		if(ss.length>=10){
			for(int i=0;i<10;i++){
				Map<String, Object> m = new HashMap<String, Object>();
				 List<BigInteger> data = new ArrayList<BigInteger>();
				 ncName.append(strBurArrary[i]).append(",");
				 for(int j=0;j<10;j++){
					 Long  strLong=jedis.strlen(user.getId()+strBurArrary[i]+(2007+j));
					 data.add(BigInteger.valueOf(strLong));
					 if(i==0){
						 ncName0.append(strLong).append(",");
					 }else if(i==1){
						 ncName1.append(strLong).append(",");
					 }else if(i==2){
						 ncName2.append(strLong).append(",");
					 }else if(i==3){
						 ncName3.append(strLong).append(",");
					 }else if(i==4){
						 ncName4.append(strLong).append(",");
					 }else if(i==5){
						 ncName5.append(strLong).append(",");
					 }else if(i==6){
						 ncName6.append(strLong).append(",");
					 }else if(i==7){
						 ncName7.append(strLong).append(",");
					 }else if(i==8){
						 ncName8.append(strLong).append(",");
					 }else if(i==9){
						 ncName9.append(strLong).append(",");
					 }
				 }
				 m.put("name",RedisTest.getAddrNameByCode(strBurArrary[i])+"("+strBurArrary[i]+")");
				 m.put("data", data);
				 series.add(m);
			}
		}else{
			for(int i=0;i<ss.length;i++){
				Map<String, Object> m = new HashMap<String, Object>();
				 List<BigInteger> data = new ArrayList<BigInteger>();
				 ncName.append(strBurArrary[i]).append(",");
				 for(int j=0;j<10;j++){
					 Long  strLong=jedis.strlen(user.getId()+strBurArrary[i]+(2007+j));
					 data.add(BigInteger.valueOf(strLong));
					 if(i==0){
						 ncName0.append(strLong).append(",");
					 }else if(i==1){
						 ncName1.append(strLong).append(",");
					 }else if(i==2){
						 ncName2.append(strLong).append(",");
					 }else if(i==3){
						 ncName3.append(strLong).append(",");
					 }else if(i==4){
						 ncName4.append(strLong).append(",");
					 }else if(i==5){
						 ncName5.append(strLong).append(",");
					 }else if(i==6){
						 ncName6.append(strLong).append(",");
					 }else if(i==7){
						 ncName7.append(strLong).append(",");
					 }else if(i==8){
						 ncName8.append(strLong).append(",");
					 }else if(i==9){
						 ncName9.append(strLong).append(",");
					 }
				 }
				 m.put("name",RedisTest.getAddrNameByCode(strBurArrary[i])+"("+strBurArrary[i]+")");
				 m.put("data", data);
				 series.add(m);
			}
		}
		if(ncName.length()>0){
			 ncName.deleteCharAt(ncName.length()-1);
		}
		if(ncName0.length()>0){
			 ncName0.deleteCharAt(ncName0.length()-1);
		}
		if(ncName1.length()>0){
			 ncName1.deleteCharAt(ncName1.length()-1);
		}
		if(ncName2.length()>0){
			 ncName2.deleteCharAt(ncName2.length()-1);
		}
		if(ncName3.length()>0){
			 ncName3.deleteCharAt(ncName3.length()-1);
		}
		if(ncName4.length()>0){
			 ncName4.deleteCharAt(ncName4.length()-1);
		}
		if(ncName5.length()>0){
			 ncName5.deleteCharAt(ncName5.length()-1);
		}
		if(ncName6.length()>0){
			 ncName6.deleteCharAt(ncName6.length()-1);
		}
		if(ncName7.length()>0){
			 ncName7.deleteCharAt(ncName7.length()-1);
		}
		if(ncName8.length()>0){
			 ncName8.deleteCharAt(ncName8.length()-1);
		}
		if(ncName9.length()>0){
			 ncName9.deleteCharAt(ncName9.length()-1);
		}
		 jedis.set(user.getId()+"ncQSName", ncName.toString());
		 jedis.expire(user.getId()+"ncQSName", 10000);
		 jedis.set(user.getId()+"ncQS0", ncName0.toString());
		 jedis.expire(user.getId()+"ncQS0", 10000);
		 jedis.set(user.getId()+"ncQS1", ncName1.toString());
		 jedis.expire(user.getId()+"ncQS1", 10000);
		 jedis.set(user.getId()+"ncQS2", ncName2.toString());
		 jedis.expire(user.getId()+"ncQS2", 10000);
		 jedis.set(user.getId()+"ncQS3", ncName3.toString());
		 jedis.expire(user.getId()+"ncQS3", 10000);
		 jedis.set(user.getId()+"ncQS4", ncName4.toString());
		 jedis.expire(user.getId()+"ncQS4", 10000);
		 jedis.set(user.getId()+"ncQS5", ncName5.toString());
		 jedis.expire(user.getId()+"ncQS5", 10000);
		 jedis.set(user.getId()+"ncQS6", ncName6.toString());
		 jedis.expire(user.getId()+"ncQS6", 10000);
		 jedis.set(user.getId()+"ncQS7", ncName7.toString());
		 jedis.expire(user.getId()+"ncQS7", 10000);
		 jedis.set(user.getId()+"ncQS8", ncName8.toString());
		 jedis.expire(user.getId()+"ncQS8", 10000);
		 jedis.set(user.getId()+"ncQS9", ncName9.toString());
		 jedis.expire(user.getId()+"ncQS9", 10000);
		map.put("series", series);
		map.put("categories", categories);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取统计信息json 形成表格（国省趋势）
	 */
	public void getPatentSumData_dataGrid_ncQS() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] ncQSName=jedis.get(user.getId()+"ncQSName").split(",");
		String[] ncQS0=jedis.get(user.getId()+"ncQS0").split(",");
		String[] ncQS1=jedis.get(user.getId()+"ncQS1").split(",");
		String[] ncQS2=jedis.get(user.getId()+"ncQS2").split(",");
		String[] ncQS3=jedis.get(user.getId()+"ncQS3").split(",");
		String[] ncQS4=jedis.get(user.getId()+"ncQS4").split(",");
		String[] ncQS5=jedis.get(user.getId()+"ncQS5").split(",");
		String[] ncQS6=jedis.get(user.getId()+"ncQS6").split(",");
		String[] ncQS7=jedis.get(user.getId()+"ncQS7").split(",");
		String[] ncQS8=jedis.get(user.getId()+"ncQS8").split(",");
		String[] ncQS9=jedis.get(user.getId()+"ncQS9").split(",");
		List<Map> records = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		total=new Long((long)ncQS0.length);
		Map<String, Object> m0 = new HashMap<String, Object>(); 
		m0.put("year", "年份");
		
		if(ncQSName.length==1){
			m0.put("ncName0",RedisTest.getAddrNameByCode(ncQSName[0])+"("+ncQSName[0]+")_");
			m0.put("ncName1","-");
			m0.put("ncName2","-");
			m0.put("ncName3","-");
			m0.put("ncName4","-");
			m0.put("ncName5","-");
			m0.put("ncName6","-");
			m0.put("ncName7","-");
			m0.put("ncName8","-");
			m0.put("ncName9","-");
		}
		if(ncQSName.length==2){
			m0.put("ncName0",RedisTest.getAddrNameByCode(ncQSName[0])+"("+ncQSName[0]+")_");
			m0.put("ncName1",RedisTest.getAddrNameByCode(ncQSName[1])+"("+ncQSName[1]+")_");
			m0.put("ncName2","-");
			m0.put("ncName3","-");
			m0.put("ncName4","-");
			m0.put("ncName5","-");
			m0.put("ncName6","-");
			m0.put("ncName7","-");
			m0.put("ncName8","-");
			m0.put("ncName9","-");
		}
		if(ncQSName.length==3){
			m0.put("ncName0",RedisTest.getAddrNameByCode(ncQSName[0])+"("+ncQSName[0]+")_");
			m0.put("ncName1",RedisTest.getAddrNameByCode(ncQSName[1])+"("+ncQSName[1]+")_");
			m0.put("ncName2",RedisTest.getAddrNameByCode(ncQSName[2])+"("+ncQSName[2]+")_");
			m0.put("ncName3","-");
			m0.put("ncName4","-");
			m0.put("ncName5","-");
			m0.put("ncName6","-");
			m0.put("ncName7","-");
			m0.put("ncName8","-");
			m0.put("ncName9","-");
		}
		if(ncQSName.length>=4){
			m0.put("ncName0",RedisTest.getAddrNameByCode(ncQSName[0])+"("+ncQSName[0]+")_");
			m0.put("ncName1",RedisTest.getAddrNameByCode(ncQSName[1])+"("+ncQSName[1]+")_");
			m0.put("ncName2",RedisTest.getAddrNameByCode(ncQSName[2])+"("+ncQSName[2]+")_");
			m0.put("ncName3",RedisTest.getAddrNameByCode(ncQSName[3])+"("+ncQSName[3]+")_");
			m0.put("ncName4","-");
			m0.put("ncName5","-");
			m0.put("ncName6","-");
			m0.put("ncName7","-");
			m0.put("ncName8","-");
			m0.put("ncName9","-");
		}
		if(ncQSName.length==5){
			m0.put("ncName0",RedisTest.getAddrNameByCode(ncQSName[0])+"("+ncQSName[0]+")_");
			m0.put("ncName1",RedisTest.getAddrNameByCode(ncQSName[1])+"("+ncQSName[1]+")_");
			m0.put("ncName2",RedisTest.getAddrNameByCode(ncQSName[2])+"("+ncQSName[2]+")_");
			m0.put("ncName3",RedisTest.getAddrNameByCode(ncQSName[3])+"("+ncQSName[3]+")_");
			m0.put("ncName4",RedisTest.getAddrNameByCode(ncQSName[4])+"("+ncQSName[4]+")_");
			m0.put("ncName5","-");
			m0.put("ncName6","-");
			m0.put("ncName7","-");
			m0.put("ncName8","-");
			m0.put("ncName9","-");
		}
		if(ncQSName.length==6){
			m0.put("ncName0",RedisTest.getAddrNameByCode(ncQSName[0])+"("+ncQSName[0]+")_");
			m0.put("ncName1",RedisTest.getAddrNameByCode(ncQSName[1])+"("+ncQSName[1]+")_");
			m0.put("ncName2",RedisTest.getAddrNameByCode(ncQSName[2])+"("+ncQSName[2]+")_");
			m0.put("ncName3",RedisTest.getAddrNameByCode(ncQSName[3])+"("+ncQSName[3]+")_");
			m0.put("ncName4",RedisTest.getAddrNameByCode(ncQSName[4])+"("+ncQSName[4]+")_");
			m0.put("ncName5",RedisTest.getAddrNameByCode(ncQSName[5])+"("+ncQSName[5]+")_");
			m0.put("ncName6","-");
			m0.put("ncName7","-");
			m0.put("ncName8","-");
			m0.put("ncName9","-");
		}
		if(ncQSName.length==7){
			m0.put("ncName0",RedisTest.getAddrNameByCode(ncQSName[0])+"("+ncQSName[0]+")_");
			m0.put("ncName1",RedisTest.getAddrNameByCode(ncQSName[1])+"("+ncQSName[1]+")_");
			m0.put("ncName2",RedisTest.getAddrNameByCode(ncQSName[2])+"("+ncQSName[2]+")_");
			m0.put("ncName3",RedisTest.getAddrNameByCode(ncQSName[3])+"("+ncQSName[3]+")_");
			m0.put("ncName4",RedisTest.getAddrNameByCode(ncQSName[4])+"("+ncQSName[4]+")_");
			m0.put("ncName5",RedisTest.getAddrNameByCode(ncQSName[5])+"("+ncQSName[5]+")_");
			m0.put("ncName6",RedisTest.getAddrNameByCode(ncQSName[6])+"("+ncQSName[6]+")_");
			m0.put("ncName7","-");
			m0.put("ncName8","-");
			m0.put("ncName9","-");
		}
		if(ncQSName.length==8){
			m0.put("ncName0",RedisTest.getAddrNameByCode(ncQSName[0])+"("+ncQSName[0]+")_");
			m0.put("ncName1",RedisTest.getAddrNameByCode(ncQSName[1])+"("+ncQSName[1]+")_");
			m0.put("ncName2",RedisTest.getAddrNameByCode(ncQSName[2])+"("+ncQSName[2]+")_");
			m0.put("ncName3",RedisTest.getAddrNameByCode(ncQSName[3])+"("+ncQSName[3]+")_");
			m0.put("ncName4",RedisTest.getAddrNameByCode(ncQSName[4])+"("+ncQSName[4]+")_");
			m0.put("ncName5",RedisTest.getAddrNameByCode(ncQSName[5])+"("+ncQSName[5]+")_");
			m0.put("ncName6",RedisTest.getAddrNameByCode(ncQSName[6])+"("+ncQSName[6]+")_");
			m0.put("ncName7",RedisTest.getAddrNameByCode(ncQSName[7])+"("+ncQSName[7]+")_");
			m0.put("ncName8","-");
			m0.put("ncName9","-");
		}
		if(ncQSName.length==9){
			m0.put("ncName0",RedisTest.getAddrNameByCode(ncQSName[0])+"("+ncQSName[0]+")_");
			m0.put("ncName1",RedisTest.getAddrNameByCode(ncQSName[1])+"("+ncQSName[1]+")_");
			m0.put("ncName2",RedisTest.getAddrNameByCode(ncQSName[2])+"("+ncQSName[2]+")_");
			m0.put("ncName3",RedisTest.getAddrNameByCode(ncQSName[3])+"("+ncQSName[3]+")_");
			m0.put("ncName4",RedisTest.getAddrNameByCode(ncQSName[4])+"("+ncQSName[4]+")_");
			m0.put("ncName5",RedisTest.getAddrNameByCode(ncQSName[5])+"("+ncQSName[5]+")_");
			m0.put("ncName6",RedisTest.getAddrNameByCode(ncQSName[6])+"("+ncQSName[6]+")_");
			m0.put("ncName7",RedisTest.getAddrNameByCode(ncQSName[7])+"("+ncQSName[7]+")_");
			m0.put("ncName8",RedisTest.getAddrNameByCode(ncQSName[8])+"("+ncQSName[8]+")_");
			m0.put("ncName9","-");
		}
		if(ncQSName.length==10){
			m0.put("ncName0",RedisTest.getAddrNameByCode(ncQSName[0])+"("+ncQSName[0]+")_");
			m0.put("ncName1",RedisTest.getAddrNameByCode(ncQSName[1])+"("+ncQSName[1]+")_");
			m0.put("ncName2",RedisTest.getAddrNameByCode(ncQSName[2])+"("+ncQSName[2]+")_");
			m0.put("ncName3",RedisTest.getAddrNameByCode(ncQSName[3])+"("+ncQSName[3]+")_");
			m0.put("ncName4",RedisTest.getAddrNameByCode(ncQSName[4])+"("+ncQSName[4]+")_");
			m0.put("ncName5",RedisTest.getAddrNameByCode(ncQSName[5])+"("+ncQSName[5]+")_");
			m0.put("ncName6",RedisTest.getAddrNameByCode(ncQSName[6])+"("+ncQSName[6]+")_");
			m0.put("ncName7",RedisTest.getAddrNameByCode(ncQSName[7])+"("+ncQSName[7]+")_");
			m0.put("ncName8",RedisTest.getAddrNameByCode(ncQSName[8])+"("+ncQSName[8]+")_");
			m0.put("ncName9",RedisTest.getAddrNameByCode(ncQSName[9])+"("+ncQSName[9]+")_");
		}
		records.add(m0);
		for (int i = 0; i < ncQS0.length; i++) {
			Map<String, Object> m = new HashMap<String, Object>(); 
			m.put("year", 2007+i+"年");   //把名称也传过去
			if(ncQSName.length==1){
				m.put("ncName0",ncQS0[i]+"_"+ncQSName[0]);
				m.put("ncName1","-_");
				m.put("ncName2","-_");
				m.put("ncName3","-_");
				m.put("ncName4","-_");
				m.put("ncName5","-_");
				m.put("ncName6","-_");
				m.put("ncName7","-_");
				m.put("ncName8","-_");
				m.put("ncName9","-_");
			}
			if(ncQSName.length==2){
				m.put("ncName0",ncQS0[i]+"_"+ncQSName[0]);
				m.put("ncName1",ncQS1[i]+"_"+ncQSName[1]);
				m.put("ncName2","-_");
				m.put("ncName3","-_");
				m.put("ncName4","-_");
				m.put("ncName5","-_");
				m.put("ncName6","-_");
				m.put("ncName7","-_");
				m.put("ncName8","-_");
				m.put("ncName9","-_");
			}
			if(ncQSName.length==3){
				m.put("ncName0",ncQS0[i]+"_"+ncQSName[0]);
				m.put("ncName1",ncQS1[i]+"_"+ncQSName[1]);
				m.put("ncName2",ncQS2[i]+"_"+ncQSName[2]);
				m.put("ncName3","-_");
				m.put("ncName4","-_");
				m.put("ncName5","-_");
				m.put("ncName6","-_");
				m.put("ncName7","-_");
				m.put("ncName8","-_");
				m.put("ncName9","-_");
			}
			if(ncQSName.length==4){
				m.put("ncName0",ncQS0[i]+"_"+ncQSName[0]);
				m.put("ncName1",ncQS1[i]+"_"+ncQSName[1]);
				m.put("ncName2",ncQS2[i]+"_"+ncQSName[2]);
				m.put("ncName3",ncQS3[i]+"_"+ncQSName[3]);
				m.put("ncName4","-_");
				m.put("ncName5","-_");
				m.put("ncName6","-_");
				m.put("ncName7","-_");
				m.put("ncName8","-_");
				m.put("ncName9","-_");
			}
			if(ncQSName.length==5){
				m.put("ncName0",ncQS0[i]+"_"+ncQSName[0]);
				m.put("ncName1",ncQS1[i]+"_"+ncQSName[1]);
				m.put("ncName2",ncQS2[i]+"_"+ncQSName[2]);
				m.put("ncName3",ncQS3[i]+"_"+ncQSName[3]);
				m.put("ncName4",ncQS4[i]+"_"+ncQSName[4]);
				m.put("ncName5","-_");
				m.put("ncName6","-_");
				m.put("ncName7","-_");
				m.put("ncName8","-_");
				m.put("ncName9","-_");
			}
			if(ncQSName.length==6){
				m.put("ncName0",ncQS0[i]+"_"+ncQSName[0]);
				m.put("ncName1",ncQS1[i]+"_"+ncQSName[1]);
				m.put("ncName2",ncQS2[i]+"_"+ncQSName[2]);
				m.put("ncName3",ncQS3[i]+"_"+ncQSName[3]);
				m.put("ncName4",ncQS4[i]+"_"+ncQSName[4]);
				m.put("ncName5",ncQS5[i]+"_"+ncQSName[5]);
				m.put("ncName6","-_");
				m.put("ncName7","-_");
				m.put("ncName8","-_");
				m.put("ncName9","-_");
			}
			if(ncQSName.length==7){
				m.put("ncName0",ncQS0[i]+"_"+ncQSName[0]);
				m.put("ncName1",ncQS1[i]+"_"+ncQSName[1]);
				m.put("ncName2",ncQS2[i]+"_"+ncQSName[2]);
				m.put("ncName3",ncQS3[i]+"_"+ncQSName[3]);
				m.put("ncName4",ncQS4[i]+"_"+ncQSName[4]);
				m.put("ncName5",ncQS5[i]+"_"+ncQSName[5]);
				m.put("ncName6",ncQS6[i]+"_"+ncQSName[6]);
				m.put("ncName7","-_");
				m.put("ncName8","-_");
				m.put("ncName9","-_");
			}
			if(ncQSName.length==8){
				m.put("ncName0",ncQS0[i]+"_"+ncQSName[0]);
				m.put("ncName1",ncQS1[i]+"_"+ncQSName[1]);
				m.put("ncName2",ncQS2[i]+"_"+ncQSName[2]);
				m.put("ncName3",ncQS3[i]+"_"+ncQSName[3]);
				m.put("ncName4",ncQS4[i]+"_"+ncQSName[4]);
				m.put("ncName5",ncQS5[i]+"_"+ncQSName[5]);
				m.put("ncName6",ncQS6[i]+"_"+ncQSName[6]);
				m.put("ncName7",ncQS7[i]+"_"+ncQSName[7]);
				m.put("ncName8","-_");
				m.put("ncName9","-_");
			}
			if(ncQSName.length==9){
				m.put("ncName0",ncQS0[i]+"_"+ncQSName[0]);
				m.put("ncName1",ncQS1[i]+"_"+ncQSName[1]);
				m.put("ncName2",ncQS2[i]+"_"+ncQSName[2]);
				m.put("ncName3",ncQS3[i]+"_"+ncQSName[3]);
				m.put("ncName4",ncQS4[i]+"_"+ncQSName[4]);
				m.put("ncName5",ncQS5[i]+"_"+ncQSName[5]);
				m.put("ncName6",ncQS6[i]+"_"+ncQSName[6]);
				m.put("ncName7",ncQS7[i]+"_"+ncQSName[7]);
				m.put("ncName8",ncQS8[i]+"_"+ncQSName[8]);
				m.put("ncName9","-_");
			}
			if(ncQSName.length==10){
				m.put("ncName0",ncQS0[i]+"_"+ncQSName[0]);
				m.put("ncName1",ncQS1[i]+"_"+ncQSName[1]);
				m.put("ncName2",ncQS2[i]+"_"+ncQSName[2]);
				m.put("ncName3",ncQS3[i]+"_"+ncQSName[3]);
				m.put("ncName4",ncQS4[i]+"_"+ncQSName[4]);
				m.put("ncName5",ncQS5[i]+"_"+ncQSName[5]);
				m.put("ncName6",ncQS6[i]+"_"+ncQSName[6]);
				m.put("ncName7",ncQS7[i]+"_"+ncQSName[7]);
				m.put("ncName8",ncQS8[i]+"_"+ncQSName[8]);
				m.put("ncName9",ncQS9[i]+"_"+ncQSName[9]);
			}
			records.add(m);
		}
		map.put("total", total);
		map.put("rows", records);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取统计信息json 形成报表图形（申请人趋势）
	 * 
	 * 
	 * 
	 */
	public void getPatentSumData_shenqingrenQS() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> series = new ArrayList<Map>();
		List<String> categories = new ArrayList<String>();
		 String[] ss= jedis.get(user.getId()+"sqr").split("_");
		 String temp=null;
		 String[] strBurArrary=new String[10];
		 if(ss.length>=10){
			 for(int i=0;i<10;i++){
				 temp=ss[i];
				 if(temp!=null&&!"".equals(temp)){
					    strBurArrary[i]=temp.substring(0, temp.indexOf("="));
						jedis.del(user.getId()+strBurArrary[i]+"2007");
						jedis.del(user.getId()+strBurArrary[i]+"2008");
						jedis.del(user.getId()+strBurArrary[i]+"2009");
						jedis.del(user.getId()+strBurArrary[i]+"2010");
						jedis.del(user.getId()+strBurArrary[i]+"2011");
						jedis.del(user.getId()+strBurArrary[i]+"2012");
						jedis.del(user.getId()+strBurArrary[i]+"2013");
						jedis.del(user.getId()+strBurArrary[i]+"2014");
						jedis.del(user.getId()+strBurArrary[i]+"2015");
						jedis.del(user.getId()+strBurArrary[i]+"2016");
					}
			 }
		 }else{
			 for(int i=0;i<ss.length;i++){
				 temp=ss[i];
				 if(temp!=null&&!"".equals(temp)){
					    strBurArrary[i]=temp.substring(0, temp.indexOf("="));
						jedis.del(user.getId()+strBurArrary[i]+"2007");
						jedis.del(user.getId()+strBurArrary[i]+"2008");
						jedis.del(user.getId()+strBurArrary[i]+"2009");
						jedis.del(user.getId()+strBurArrary[i]+"2010");
						jedis.del(user.getId()+strBurArrary[i]+"2011");
						jedis.del(user.getId()+strBurArrary[i]+"2012");
						jedis.del(user.getId()+strBurArrary[i]+"2013");
						jedis.del(user.getId()+strBurArrary[i]+"2014");
						jedis.del(user.getId()+strBurArrary[i]+"2015");
						jedis.del(user.getId()+strBurArrary[i]+"2016");
					}
			 }
		 }
		 for(int i=0;i<10;i++){
			 categories.add((2007+i)+"");     //目录
		 }
		 String[] applJly=jedis.get(user.getId()+"applJly").split("_");
		 applJly[applJly.length-1]=applJly[applJly.length-1].substring(0, applJly[applJly.length-1].length()-3);    ////删除末尾的Jly
		 String[] apdJly=jedis.get(user.getId()+"apdJly").split("_");
		 apdJly[apdJly.length-1]=apdJly[apdJly.length-1].substring(0, apdJly[apdJly.length-1].length()-3);    ////删除末尾的Jly
		 for(int i=0;i<applJly.length;i++){
			String srtSQR=applJly[i];//获得申请人
			if((srtSQR!=null)&&(!"".equals(srtSQR))){    //申请人不空
				if(srtSQR.contains(",")){      //多个申请人
					String[] strs=srtSQR.split(",");
					for(int j=0;j<strs.length;j++){
						 if(ss.length>=10){
							for(int k=0;k<10;k++){   //申请人跟10个要统计的进行比对
								if(strBurArrary[k].equals(strs[j])){   //比对成功
									String strYear=apdJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
									if((strYear!=null)&&(!"".equals(strYear))){
										if("2007".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2007", "1");
										}else if("2008".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2008", "1");
										}else if("2009".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2009", "1");
										}else if("2010".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2010", "1");
										}else if("2011".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2011", "1");
										}else if("2012".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2012", "1");
										}else if("2013".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2013", "1");
										}else if("2014".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2014", "1");
										}else if("2015".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2015", "1");
										}else if("2016".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2016", "1");
										}
									}
								}
							}
					    }else{
					    	for(int k=0;k<ss.length;k++){   //申请人跟10个要统计的进行比对
								if(strBurArrary[k].equals(strs[j])){   //比对成功
									String strYear=apdJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
									if((strYear!=null)&&(!"".equals(strYear))){
										if("2007".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2007", "1");
										}else if("2008".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2008", "1");
										}else if("2009".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2009", "1");
										}else if("2010".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2010", "1");
										}else if("2011".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2011", "1");
										}else if("2012".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2012", "1");
										}else if("2013".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2013", "1");
										}else if("2014".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2014", "1");
										}else if("2015".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2015", "1");
										}else if("2016".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2016", "1");
										}
									}
								}
							}
					    }
					}
				}else{  //单个申请人
					if(ss.length>=10){
						for(int k=0;k<10;k++){   //申请人跟10个要统计的进行比对
							if(strBurArrary[k].equals(srtSQR)){   //比对成功
								String strYear=apdJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
								if((strYear!=null)&&(!"".equals(strYear))){
									if("2007".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2007", "1");
									}else if("2008".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2008", "1");
									}else if("2009".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2009", "1");
									}else if("2010".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2010", "1");
									}else if("2011".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2011", "1");
									}else if("2012".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2012", "1");
									}else if("2013".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2013", "1");
									}else if("2014".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2014", "1");
									}else if("2015".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"20153", "1");
									}else if("2016".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2016", "1");
									}
								}
							}
						}
					}else{
						for(int k=0;k<ss.length;k++){   //申请人跟10个要统计的进行比对
							if(strBurArrary[k].equals(srtSQR)){   //比对成功
								String strYear=apdJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
								if((strYear!=null)&&(!"".equals(strYear))){
									if("2007".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2007", "1");
									}else if("2008".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2008", "1");
									}else if("2009".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2009", "1");
									}else if("2010".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2010", "1");
									}else if("2011".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2011", "1");
									}else if("2012".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2012", "1");
									}else if("2013".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2013", "1");
									}else if("2014".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2014", "1");
									}else if("2015".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2015", "1");
									}else if("2016".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2016", "1");
									}
								}
							}
						}
					}
				}
			}
		}
		StringBuilder applName=new StringBuilder();//申请人名称
		StringBuilder applName0=new StringBuilder();
		StringBuilder applName1=new StringBuilder();
		StringBuilder applName2=new StringBuilder();
		StringBuilder applName3=new StringBuilder();
		StringBuilder applName4=new StringBuilder();
		StringBuilder applName5=new StringBuilder();
		StringBuilder applName6=new StringBuilder();
		StringBuilder applName7=new StringBuilder();
		StringBuilder applName8=new StringBuilder();
		StringBuilder applName9=new StringBuilder();
		 if(ss.length>=10){
			for(int i=0;i<10;i++){
				Map<String, Object> m = new HashMap<String, Object>();
				 List<BigInteger> data = new ArrayList<BigInteger>();
				 applName.append(strBurArrary[i]).append(",");
				 for(int j=0;j<10;j++){
					 Long  strLong=jedis.strlen(user.getId()+strBurArrary[i]+(2007+j));
					 data.add(BigInteger.valueOf(strLong));
					 if(i==0){
						 applName0.append(strLong).append(",");
					 }else if(i==1){
						 applName1.append(strLong).append(",");
					 }else if(i==2){
						 applName2.append(strLong).append(",");
					 }else if(i==3){
						 applName3.append(strLong).append(",");
					 }else if(i==4){
						 applName4.append(strLong).append(",");
					 }else if(i==5){
						 applName5.append(strLong).append(",");
					 }else if(i==6){
						 applName6.append(strLong).append(",");
					 }else if(i==7){
						 applName7.append(strLong).append(",");
					 }else if(i==8){
						 applName8.append(strLong).append(",");
					 }else if(i==9){
						 applName9.append(strLong).append(",");
					 }
				 }
				 m.put("name",strBurArrary[i]);
				 m.put("data", data);
				 series.add(m);
			}
		 }else{
				for(int i=0;i<ss.length;i++){
					Map<String, Object> m = new HashMap<String, Object>();
					 List<BigInteger> data = new ArrayList<BigInteger>();
					 applName.append(strBurArrary[i]).append(",");
					 for(int j=0;j<10;j++){
						 Long  strLong=jedis.strlen(user.getId()+strBurArrary[i]+(2007+j));
						 data.add(BigInteger.valueOf(strLong));
						 if(i==0){
							 applName0.append(strLong).append(",");
						 }else if(i==1){
							 applName1.append(strLong).append(",");
						 }else if(i==2){
							 applName2.append(strLong).append(",");
						 }else if(i==3){
							 applName3.append(strLong).append(",");
						 }else if(i==4){
							 applName4.append(strLong).append(",");
						 }else if(i==5){
							 applName5.append(strLong).append(",");
						 }else if(i==6){
							 applName6.append(strLong).append(",");
						 }else if(i==7){
							 applName7.append(strLong).append(",");
						 }else if(i==8){
							 applName8.append(strLong).append(",");
						 }else if(i==9){
							 applName9.append(strLong).append(",");
						 }
					 }
					 m.put("name",strBurArrary[i]);
					 m.put("data", data);
					 series.add(m);
				} 
		 }
		if(applName.length()>0){
			applName.deleteCharAt(applName.length()-1);
		}
		if(applName0.length()>0){
			applName0.deleteCharAt(applName0.length()-1);
		}
		if(applName1.length()>0){
			applName1.deleteCharAt(applName1.length()-1);
		}
		if(applName2.length()>0){
			applName2.deleteCharAt(applName2.length()-1);
		}
		if(applName3.length()>0){
			applName3.deleteCharAt(applName3.length()-1);
		}
		if(applName4.length()>0){
			applName4.deleteCharAt(applName4.length()-1);
		}
		if(applName5.length()>0){
			applName5.deleteCharAt(applName5.length()-1);
		}
		if(applName6.length()>0){
			applName6.deleteCharAt(applName6.length()-1);
		}
		if(applName7.length()>0){
			applName7.deleteCharAt(applName7.length()-1);
		}
		if(applName8.length()>0){
			applName8.deleteCharAt(applName8.length()-1);
		}
		if(applName9.length()>0){
			applName9.deleteCharAt(applName9.length()-1);
		}
		 jedis.set(user.getId()+"applQSName", applName.toString());
		 jedis.expire(user.getId()+"applQSName", 10000);
		 jedis.set(user.getId()+"applQS0", applName0.toString());
		 jedis.expire(user.getId()+"applQS0", 10000);
		 jedis.set(user.getId()+"applQS1", applName1.toString());
		 jedis.expire(user.getId()+"applQS1", 10000);
		 jedis.set(user.getId()+"applQS2", applName2.toString());
		 jedis.expire(user.getId()+"applQS2", 10000);
		 jedis.set(user.getId()+"applQS3", applName3.toString());
		 jedis.expire(user.getId()+"applQS3", 10000);
		 jedis.set(user.getId()+"applQS4", applName4.toString());
		 jedis.expire(user.getId()+"applQS4", 10000);
		 jedis.set(user.getId()+"applQS5", applName5.toString());
		 jedis.expire(user.getId()+"applQS5", 10000);
		 jedis.set(user.getId()+"applQS6", applName6.toString());
		 jedis.expire(user.getId()+"applQS6", 10000);
		 jedis.set(user.getId()+"applQS7", applName7.toString());
		 jedis.expire(user.getId()+"applQS7", 10000);
		 jedis.set(user.getId()+"applQS8", applName8.toString());
		 jedis.expire(user.getId()+"applQS8", 10000);
		 jedis.set(user.getId()+"applQS9", applName9.toString());
		 jedis.expire(user.getId()+"applQS9", 10000);
		map.put("series", series);
		map.put("categories", categories);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取统计信息json 形成表格（申请人趋势）
	 */
	public void getPatentSumData_dataGrid_sqrQS() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] applQSName=jedis.get(user.getId()+"applQSName").split(",");
		String[] applQS0=jedis.get(user.getId()+"applQS0").split(",");
		String[] applQS1=jedis.get(user.getId()+"applQS1").split(",");
		String[] applQS2=jedis.get(user.getId()+"applQS2").split(",");
		String[] applQS3=jedis.get(user.getId()+"applQS3").split(",");
		String[] applQS4=jedis.get(user.getId()+"applQS4").split(",");
		String[] applQS5=jedis.get(user.getId()+"applQS5").split(",");
		String[] applQS6=jedis.get(user.getId()+"applQS6").split(",");
		String[] applQS7=jedis.get(user.getId()+"applQS7").split(",");
		String[] applQS8=jedis.get(user.getId()+"applQS8").split(",");
		String[] applQS9=jedis.get(user.getId()+"applQS9").split(",");
		
		List<Map> records = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		total=new Long((long)applQS0.length);
		Map<String, Object> m0 = new HashMap<String, Object>(); 
		m0.put("year", "年份");
		if(applQSName.length==1){
			m0.put("applName0",applQSName[0]+"_");
			m0.put("applName1","-");
			m0.put("applName2","-");
			m0.put("applName3","-");
			m0.put("applName4","-");
			m0.put("applName5","-");
			m0.put("applName6","-");
			m0.put("applName7","-");
			m0.put("applName8","-");
			m0.put("applName9","-");
		}
		if(applQSName.length==2){
			m0.put("applName0",applQSName[0]+"_");
			m0.put("applName1",applQSName[1]+"_");
			m0.put("applName2","-");
			m0.put("applName3","-");
			m0.put("applName4","-");
			m0.put("applName5","-");
			m0.put("applName6","-");
			m0.put("applName7","-");
			m0.put("applName8","-");
			m0.put("applName9","-");
		}
		if(applQSName.length==3){
			m0.put("applName0",applQSName[0]+"_");
			m0.put("applName1",applQSName[1]+"_");
			m0.put("applName2",applQSName[2]+"_");
			m0.put("applName3","-");
			m0.put("applName4","-");
			m0.put("applName5","-");
			m0.put("applName6","-");
			m0.put("applName7","-");
			m0.put("applName8","-");
			m0.put("applName9","-");
		}
		if(applQSName.length==4){
			m0.put("applName0",applQSName[0]+"_");
			m0.put("applName1",applQSName[1]+"_");
			m0.put("applName2",applQSName[2]+"_");
			m0.put("applName3",applQSName[3]+"_");
			m0.put("applName4","-");
			m0.put("applName5","-");
			m0.put("applName6","-");
			m0.put("applName7","-");
			m0.put("applName8","-");
			m0.put("applName9","-");
		}
		if(applQSName.length==5){
			m0.put("applName0",applQSName[0]+"_");
			m0.put("applName1",applQSName[1]+"_");
			m0.put("applName2",applQSName[2]+"_");
			m0.put("applName3",applQSName[3]+"_");
			m0.put("applName4",applQSName[4]+"_");
			m0.put("applName5","-");
			m0.put("applName6","-");
			m0.put("applName7","-");
			m0.put("applName8","-");
			m0.put("applName9","-");
		}
		if(applQSName.length==6){
			m0.put("applName0",applQSName[0]+"_");
			m0.put("applName1",applQSName[1]+"_");
			m0.put("applName2",applQSName[2]+"_");
			m0.put("applName3",applQSName[3]+"_");
			m0.put("applName4",applQSName[4]+"_");
			m0.put("applName5",applQSName[5]+"_");
			m0.put("applName6","-");
			m0.put("applName7","-");
			m0.put("applName8","-");
			m0.put("applName9","-");
		}
		if(applQSName.length==7){
			m0.put("applName0",applQSName[0]+"_");
			m0.put("applName1",applQSName[1]+"_");
			m0.put("applName2",applQSName[2]+"_");
			m0.put("applName3",applQSName[3]+"_");
			m0.put("applName4",applQSName[4]+"_");
			m0.put("applName5",applQSName[5]+"_");
			m0.put("applName6",applQSName[6]+"_");
			m0.put("applName7","-");
			m0.put("applName8","-");
			m0.put("applName9","-");
		}
		if(applQSName.length==8){
			m0.put("applName0",applQSName[0]+"_");
			m0.put("applName1",applQSName[1]+"_");
			m0.put("applName2",applQSName[2]+"_");
			m0.put("applName3",applQSName[3]+"_");
			m0.put("applName4",applQSName[4]+"_");
			m0.put("applName5",applQSName[5]+"_");
			m0.put("applName6",applQSName[6]+"_");
			m0.put("applName7",applQSName[7]+"_");
			m0.put("applName8","-");
			m0.put("applName9","-");
		}
		if(applQSName.length==9){
			m0.put("applName0",applQSName[0]+"_");
			m0.put("applName1",applQSName[1]+"_");
			m0.put("applName2",applQSName[2]+"_");
			m0.put("applName3",applQSName[3]+"_");
			m0.put("applName4",applQSName[4]+"_");
			m0.put("applName5",applQSName[5]+"_");
			m0.put("applName6",applQSName[6]+"_");
			m0.put("applName7",applQSName[7]+"_");
			m0.put("applName8",applQSName[8]+"_");
			m0.put("applName9","-");
		}
		if(applQSName.length==10){
			m0.put("applName0",applQSName[0]+"_");
			m0.put("applName1",applQSName[1]+"_");
			m0.put("applName2",applQSName[2]+"_");
			m0.put("applName3",applQSName[3]+"_");
			m0.put("applName4",applQSName[4]+"_");
			m0.put("applName5",applQSName[5]+"_");
			m0.put("applName6",applQSName[6]+"_");
			m0.put("applName7",applQSName[7]+"_");
			m0.put("applName8",applQSName[8]+"_");
			m0.put("applName9",applQSName[9]+"_");
		}
		records.add(m0);
		for (int i = 0; i < applQS0.length; i++) {
			Map<String, Object> m = new HashMap<String, Object>(); 
			m.put("year", 2007+i+"年");
			if(applQSName.length==1){
				m.put("applName0",applQS0[i]+"_"+applQSName[0]);
				m.put("applName1","-_");
				m.put("applName2","-_");
				m.put("applName3","-_");
				m.put("applName4","-_");
				m.put("applName5","-_");
				m.put("applName6","-_");
				m.put("applName7","-_");
				m.put("applName8","-_");
				m.put("applName9","-_");
			}
			if(applQSName.length==2){
				m.put("applName0",applQS0[i]+"_"+applQSName[0]);
				m.put("applName1",applQS1[i]+"_"+applQSName[1]);
				m.put("applName2","-_");
				m.put("applName3","-_");
				m.put("applName4","-_");
				m.put("applName5","-_");
				m.put("applName6","-_");
				m.put("applName7","-_");
				m.put("applName8","-_");
				m.put("applName9","-_");
			}
			if(applQSName.length==3){
				m.put("applName0",applQS0[i]+"_"+applQSName[0]);
				m.put("applName1",applQS1[i]+"_"+applQSName[1]);
				m.put("applName2",applQS2[i]+"_"+applQSName[2]);
				m.put("applName3","-_");
				m.put("applName4","-_");
				m.put("applName5","-_");
				m.put("applName6","-_");
				m.put("applName7","-_");
				m.put("applName8","-_");
				m.put("applName9","-_");
			}
			if(applQSName.length==4){
				m.put("applName0",applQS0[i]+"_"+applQSName[0]);
				m.put("applName1",applQS1[i]+"_"+applQSName[1]);
				m.put("applName2",applQS2[i]+"_"+applQSName[2]);
				m.put("applName3",applQS3[i]+"_"+applQSName[3]);
				m.put("applName4","-_");
				m.put("applName5","-_");
				m.put("applName6","-_");
				m.put("applName7","-_");
				m.put("applName8","-_");
				m.put("applName9","-_");
			}
			if(applQSName.length==5){
				m.put("applName0",applQS0[i]+"_"+applQSName[0]);
				m.put("applName1",applQS1[i]+"_"+applQSName[1]);
				m.put("applName2",applQS2[i]+"_"+applQSName[2]);
				m.put("applName3",applQS3[i]+"_"+applQSName[3]);
				m.put("applName4",applQS4[i]+"_"+applQSName[4]);
				m.put("applName5","-_");
				m.put("applName6","-_");
				m.put("applName7","-_");
				m.put("applName8","-_");
				m.put("applName9","-_");
			}
			if(applQSName.length==6){
				m.put("applName0",applQS0[i]+"_"+applQSName[0]);
				m.put("applName1",applQS1[i]+"_"+applQSName[1]);
				m.put("applName2",applQS2[i]+"_"+applQSName[2]);
				m.put("applName3",applQS3[i]+"_"+applQSName[3]);
				m.put("applName4",applQS4[i]+"_"+applQSName[4]);
				m.put("applName5",applQS5[i]+"_"+applQSName[5]);
				m.put("applName6","-_");
				m.put("applName7","-_");
				m.put("applName8","-_");
				m.put("applName9","-_");
			}
			if(applQSName.length==7){
				m.put("applName0",applQS0[i]+"_"+applQSName[0]);
				m.put("applName1",applQS1[i]+"_"+applQSName[1]);
				m.put("applName2",applQS2[i]+"_"+applQSName[2]);
				m.put("applName3",applQS3[i]+"_"+applQSName[3]);
				m.put("applName4",applQS4[i]+"_"+applQSName[4]);
				m.put("applName5",applQS5[i]+"_"+applQSName[5]);
				m.put("applName6",applQS6[i]+"_"+applQSName[6]);
				m.put("applName7","-_");
				m.put("applName8","-_");
				m.put("applName9","-_");
			}
			if(applQSName.length==8){
				m.put("applName0",applQS0[i]+"_"+applQSName[0]);
				m.put("applName1",applQS1[i]+"_"+applQSName[1]);
				m.put("applName2",applQS2[i]+"_"+applQSName[2]);
				m.put("applName3",applQS3[i]+"_"+applQSName[3]);
				m.put("applName4",applQS4[i]+"_"+applQSName[4]);
				m.put("applName5",applQS5[i]+"_"+applQSName[5]);
				m.put("applName6",applQS6[i]+"_"+applQSName[6]);
				m.put("applName7",applQS7[i]+"_"+applQSName[7]);
				m.put("applName8","-_");
				m.put("applName9","-_");
			}
			if(applQSName.length==9){
				m.put("applName0",applQS0[i]+"_"+applQSName[0]);
				m.put("applName1",applQS1[i]+"_"+applQSName[1]);
				m.put("applName2",applQS2[i]+"_"+applQSName[2]);
				m.put("applName3",applQS3[i]+"_"+applQSName[3]);
				m.put("applName4",applQS4[i]+"_"+applQSName[4]);
				m.put("applName5",applQS5[i]+"_"+applQSName[5]);
				m.put("applName6",applQS6[i]+"_"+applQSName[6]);
				m.put("applName7",applQS7[i]+"_"+applQSName[7]);
				m.put("applName8",applQS8[i]+"_"+applQSName[8]);
				m.put("applName9","-_");
			}
			if(applQSName.length==10){
				m.put("applName0",applQS0[i]+"_"+applQSName[0]);
				m.put("applName1",applQS1[i]+"_"+applQSName[1]);
				m.put("applName2",applQS2[i]+"_"+applQSName[2]);
				m.put("applName3",applQS3[i]+"_"+applQSName[3]);
				m.put("applName4",applQS4[i]+"_"+applQSName[4]);
				m.put("applName5",applQS5[i]+"_"+applQSName[5]);
				m.put("applName6",applQS6[i]+"_"+applQSName[6]);
				m.put("applName7",applQS7[i]+"_"+applQSName[7]);
				m.put("applName8",applQS8[i]+"_"+applQSName[8]);
				m.put("applName9",applQS9[i]+"_"+applQSName[9]);
			}
			records.add(m);
		}
		map.put("total", total);
		map.put("rows", records);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取统计信息json 形成报表图形（技术分类趋势分析）
	 * 
	 * 
	 */
	public void getPatentSumData_ipcQS() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> series = new ArrayList<Map>();
		List<String> categories = new ArrayList<String>();
		 String[] ss= jedis.get(user.getId()+"ipc").split("_");
		 String temp=null;
		 String[] strBurArrary=new String[10];
		 if(ss.length>=10){
			 for(int i=0;i<10;i++){
				 temp=ss[i];
				 if(temp!=null&&!"".equals(temp)){
					    strBurArrary[i]=temp.substring(0, temp.indexOf("="));
						jedis.del(user.getId()+strBurArrary[i]+"2007");
						jedis.del(user.getId()+strBurArrary[i]+"2008");
						jedis.del(user.getId()+strBurArrary[i]+"2009");
						jedis.del(user.getId()+strBurArrary[i]+"2010");
						jedis.del(user.getId()+strBurArrary[i]+"2011");
						jedis.del(user.getId()+strBurArrary[i]+"2012");
						jedis.del(user.getId()+strBurArrary[i]+"2013");
						jedis.del(user.getId()+strBurArrary[i]+"2014");
						jedis.del(user.getId()+strBurArrary[i]+"2015");
						jedis.del(user.getId()+strBurArrary[i]+"2016");
					}
			 }
		 }else{
			 for(int i=0;i<ss.length;i++){
				 temp=ss[i];
				 if(temp!=null&&!"".equals(temp)){
					    strBurArrary[i]=temp.substring(0, temp.indexOf("="));
						jedis.del(user.getId()+strBurArrary[i]+"2007");
						jedis.del(user.getId()+strBurArrary[i]+"2008");
						jedis.del(user.getId()+strBurArrary[i]+"2009");
						jedis.del(user.getId()+strBurArrary[i]+"2010");
						jedis.del(user.getId()+strBurArrary[i]+"2011");
						jedis.del(user.getId()+strBurArrary[i]+"2012");
						jedis.del(user.getId()+strBurArrary[i]+"2013");
						jedis.del(user.getId()+strBurArrary[i]+"2014");
						jedis.del(user.getId()+strBurArrary[i]+"2015");
						jedis.del(user.getId()+strBurArrary[i]+"2016");
					}
			 }
		 }
		 for(int i=0;i<=10;i++){
			 categories.add((2007+i)+"");
		 }
		 String[] ipcJly=jedis.get(user.getId()+"ipcJly").split("_");
		 ipcJly[ipcJly.length-1]=ipcJly[ipcJly.length-1].substring(0, ipcJly[ipcJly.length-1].length()-3);    ////删除末尾的Jly
		 String[] apdJly=jedis.get(user.getId()+"apdJly").split("_");
		 apdJly[apdJly.length-1]=apdJly[apdJly.length-1].substring(0, apdJly[apdJly.length-1].length()-3);    ////删除末尾的Jly
		for(int i=0;i<ipcJly.length;i++){
			String srtSQR=ipcJly[i];//获得技术分类
			if((srtSQR!=null)&&(!"".equals(srtSQR))){    //申请人不空
				if(srtSQR.contains(",")){      //多个申请人
					String[] strs=srtSQR.split(",");
					for(int j=0;j<strs.length;j++){
						 if(ss.length>=10){
							for(int k=0;k<10;k++){   //申请人跟10个要统计的进行比对
								if(strBurArrary[k].equals(strs[j])){   //比对成功
									String strYear=apdJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
									if((strYear!=null)&&(!"".equals(strYear))){
										if("2007".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2007", "1");
										}else if("2008".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2008", "1");
										}else if("2009".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2009", "1");
										}else if("2010".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2010", "1");
										}else if("2011".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2011", "1");
										}else if("2012".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2012", "1");
										}else if("2013".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2013", "1");
										}else if("2014".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2014", "1");
										}else if("2015".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2015", "1");
										}else if("2016".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2016", "1");
										}
									}
								}
							}
						 }else{
							 for(int k=0;k<ss.length;k++){   //申请人跟10个要统计的进行比对
									if(strBurArrary[k].equals(strs[j])){   //比对成功
										String strYear=apdJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
										if((strYear!=null)&&(!"".equals(strYear))){
											if("2007".equals(strYear)){
												jedis.append(user.getId()+strs[j]+"2007", "1");
											}else if("2008".equals(strYear)){
												jedis.append(user.getId()+strs[j]+"2008", "1");
											}else if("2009".equals(strYear)){
												jedis.append(user.getId()+strs[j]+"2009", "1");
											}else if("2010".equals(strYear)){
												jedis.append(user.getId()+strs[j]+"2010", "1");
											}else if("2011".equals(strYear)){
												jedis.append(user.getId()+strs[j]+"2011", "1");
											}else if("2012".equals(strYear)){
												jedis.append(user.getId()+strs[j]+"2012", "1");
											}else if("2013".equals(strYear)){
												jedis.append(user.getId()+strs[j]+"2013", "1");
											}else if("2014".equals(strYear)){
												jedis.append(user.getId()+strs[j]+"2014", "1");
											}else if("2015".equals(strYear)){
												jedis.append(user.getId()+strs[j]+"2015", "1");
											}else if("2016".equals(strYear)){
												jedis.append(user.getId()+strs[j]+"2016", "1");
											}
										}
									}
								}
						 }
					}
				}else{  //单个申请人
					if(ss.length>=10){
						for(int k=0;k<10;k++){   //申请人跟10个要统计的进行比对
							if(strBurArrary[k].equals(srtSQR)){   //比对成功
								String strYear=apdJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
								if((strYear!=null)&&(!"".equals(strYear))){
									if("2007".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2007", "1");
									}else if("2008".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2008", "1");
									}else if("2009".equals(strYear)){  
										jedis.append(user.getId()+srtSQR+"2009", "1");
									}else if("2010".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2010", "1");
									}else if("2011".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2011", "1");
									}else if("2012".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2012", "1");
									}else if("2013".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2013", "1");
									}else if("2014".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2014", "1");
									}else if("2015".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2015", "1");
									}else if("2016".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2016", "1");
									}
								}
							}
						}
					}else{
						for(int k=0;k<ss.length;k++){   //申请人跟10个要统计的进行比对
							if(strBurArrary[k].equals(srtSQR)){   //比对成功
								String strYear=apdJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
								if((strYear!=null)&&(!"".equals(strYear))){
									if("2007".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2007", "1");
									}else if("2008".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2008", "1");
									}else if("2009".equals(strYear)){  
										jedis.append(user.getId()+srtSQR+"2009", "1");
									}else if("2010".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2010", "1");
									}else if("2011".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2011", "1");
									}else if("2012".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2012", "1");
									}else if("2013".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2013", "1");
									}else if("2014".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2014", "1");
									}else if("2015".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2015", "1");
									}else if("2016".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2016", "1");
									}
								}
							}
						}
					}
				}
			}
		}
		StringBuilder ipcName=new StringBuilder();//ipc名称
		StringBuilder ipcName0=new StringBuilder();
		StringBuilder ipcName1=new StringBuilder();
		StringBuilder ipcName2=new StringBuilder();
		StringBuilder ipcName3=new StringBuilder();
		StringBuilder ipcName4=new StringBuilder();
		StringBuilder ipcName5=new StringBuilder();
		StringBuilder ipcName6=new StringBuilder();
		StringBuilder ipcName7=new StringBuilder();
		StringBuilder ipcName8=new StringBuilder();
		StringBuilder ipcName9=new StringBuilder();
		if(ss.length>=10){
			for(int i=0;i<10;i++){
				Map<String, Object> m = new HashMap<String, Object>();
				 List<BigInteger> data = new ArrayList<BigInteger>();
				 ipcName.append(strBurArrary[i]).append(",");
				 for(int j=0;j<10;j++){
					 Long  strLong=jedis.strlen(user.getId()+strBurArrary[i]+(2007+j));
					 data.add(BigInteger.valueOf(strLong));
					 if(i==0){
						 ipcName0.append(strLong).append(",");
					 }else if(i==1){
						 ipcName1.append(strLong).append(",");
					 }else if(i==2){
						 ipcName2.append(strLong).append(",");
					 }else if(i==3){
						 ipcName3.append(strLong).append(",");
					 }else if(i==4){
						 ipcName4.append(strLong).append(",");
					 }else if(i==5){
						 ipcName5.append(strLong).append(",");
					 }else if(i==6){
						 ipcName6.append(strLong).append(",");
					 }else if(i==7){
						 ipcName7.append(strLong).append(",");
					 }else if(i==8){
						 ipcName8.append(strLong).append(",");
					 }else if(i==9){
						 ipcName9.append(strLong).append(",");
					 }
				 }
				 m.put("name",strBurArrary[i]);
				 m.put("data", data);
				 series.add(m);
			}
		}else{
			for(int i=0;i<ss.length;i++){
				Map<String, Object> m = new HashMap<String, Object>();
				 List<BigInteger> data = new ArrayList<BigInteger>();
				 ipcName.append(strBurArrary[i]).append(",");
				 for(int j=0;j<10;j++){
					 Long  strLong=jedis.strlen(user.getId()+strBurArrary[i]+(2007+j));
					 data.add(BigInteger.valueOf(strLong));
					 if(i==0){
						 ipcName0.append(strLong).append(",");
					 }else if(i==1){
						 ipcName1.append(strLong).append(",");
					 }else if(i==2){
						 ipcName2.append(strLong).append(",");
					 }else if(i==3){
						 ipcName3.append(strLong).append(",");
					 }else if(i==4){
						 ipcName4.append(strLong).append(",");
					 }else if(i==5){
						 ipcName5.append(strLong).append(",");
					 }else if(i==6){
						 ipcName6.append(strLong).append(",");
					 }else if(i==7){
						 ipcName7.append(strLong).append(",");
					 }else if(i==8){
						 ipcName8.append(strLong).append(",");
					 }else if(i==9){
						 ipcName9.append(strLong).append(",");
					 }
				 }
				 m.put("name",strBurArrary[i]);
				 m.put("data", data);
				 series.add(m);
			}
		}
		if(ipcName.length()>0){
			 ipcName.deleteCharAt(ipcName.length()-1);
		}
		if(ipcName0.length()>0){
			 ipcName0.deleteCharAt(ipcName0.length()-1);
		}
		if(ipcName1.length()>0){
			ipcName1.deleteCharAt(ipcName1.length()-1);
		}
		if(ipcName2.length()>0){
			ipcName2.deleteCharAt(ipcName2.length()-1);
		}
		if(ipcName3.length()>0){
			ipcName3.deleteCharAt(ipcName3.length()-1);
		}
		if(ipcName4.length()>0){
			ipcName4.deleteCharAt(ipcName4.length()-1);
		}
		if(ipcName5.length()>0){
			ipcName5.deleteCharAt(ipcName5.length()-1);
		}
		if(ipcName6.length()>0){
			ipcName6.deleteCharAt(ipcName6.length()-1);
		}
		if(ipcName7.length()>0){
			ipcName7.deleteCharAt(ipcName7.length()-1);
		}
		if(ipcName8.length()>0){
			ipcName8.deleteCharAt(ipcName8.length()-1);
		}
		if(ipcName9.length()>0){
			ipcName9.deleteCharAt(ipcName9.length()-1);
		}
		 jedis.set(user.getId()+"ipcQSName", ipcName.toString());
		 jedis.expire(user.getId()+"ipcQSName", 10000);
		 jedis.set(user.getId()+"ipcQS0", ipcName0.toString());
		 jedis.expire(user.getId()+"ipcQS0", 10000);
		 jedis.set(user.getId()+"ipcQS1", ipcName1.toString());
		 jedis.expire(user.getId()+"ipcQS1", 10000);
		 jedis.set(user.getId()+"ipcQS2", ipcName2.toString());
		 jedis.expire(user.getId()+"ipcQS2", 10000);
		 jedis.set(user.getId()+"ipcQS3", ipcName3.toString());
		 jedis.expire(user.getId()+"ipcQS3", 10000);
		 jedis.set(user.getId()+"ipcQS4", ipcName4.toString());
		 jedis.expire(user.getId()+"ipcQS4", 10000);
		 jedis.set(user.getId()+"ipcQS5", ipcName5.toString());
		 jedis.expire(user.getId()+"ipcQS5", 10000);
		 jedis.set(user.getId()+"ipcQS6", ipcName6.toString());
		 jedis.expire(user.getId()+"ipcQS6", 10000);
		 jedis.set(user.getId()+"ipcQS7", ipcName7.toString());
		 jedis.expire(user.getId()+"ipcQS7", 10000);
		 jedis.set(user.getId()+"ipcQS8", ipcName8.toString());
		 jedis.expire(user.getId()+"ipcQS8", 10000);
		 jedis.set(user.getId()+"ipcQS9", ipcName9.toString());
		 jedis.expire(user.getId()+"ipcQS9", 10000);
		map.put("series", series);
		map.put("categories", categories);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取统计信息json 形成表格（ipc趋势）
	 */
	public void getPatentSumData_dataGrid_ipcQS() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] ipcQSName=jedis.get(user.getId()+"ipcQSName").split(",");
		String[] ipcQS0=jedis.get(user.getId()+"ipcQS0").split(",");
		String[] ipcQS1=jedis.get(user.getId()+"ipcQS1").split(",");
		String[] ipcQS2=jedis.get(user.getId()+"ipcQS2").split(",");
		String[] ipcQS3=jedis.get(user.getId()+"ipcQS3").split(",");
		String[] ipcQS4=jedis.get(user.getId()+"ipcQS4").split(",");
		String[] ipcQS5=jedis.get(user.getId()+"ipcQS5").split(",");
		String[] ipcQS6=jedis.get(user.getId()+"ipcQS6").split(",");
		String[] ipcQS7=jedis.get(user.getId()+"ipcQS7").split(",");
		String[] ipcQS8=jedis.get(user.getId()+"ipcQS8").split(",");
		String[] ipcQS9=jedis.get(user.getId()+"ipcQS9").split(",");
		List<Map> records = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		total=new Long((long)ipcQS0.length);
		Map<String, Object> m0 = new HashMap<String, Object>(); 
		m0.put("year", "年份");
		if(ipcQSName.length==1){
			m0.put("ipcName0",ipcQSName[0]+"_");
			m0.put("ipcName1","-");
			m0.put("ipcName2","-");
			m0.put("ipcName3","-");
			m0.put("ipcName4","-");
			m0.put("ipcName5","-");
			m0.put("ipcName6","-");
			m0.put("ipcName7","-");
			m0.put("ipcName8","-");
			m0.put("ipcName9","-");
		}
		if(ipcQSName.length==2){
			m0.put("ipcName0",ipcQSName[0]+"_");
			m0.put("ipcName1",ipcQSName[1]+"_");
			m0.put("ipcName2","-");
			m0.put("ipcName3","-");
			m0.put("ipcName4","-");
			m0.put("ipcName5","-");
			m0.put("ipcName6","-");
			m0.put("ipcName7","-");
			m0.put("ipcName8","-");
			m0.put("ipcName9","-");
		}
		if(ipcQSName.length==3){
			m0.put("ipcName0",ipcQSName[0]+"_");
			m0.put("ipcName1",ipcQSName[1]+"_");
			m0.put("ipcName2",ipcQSName[2]+"_");
			m0.put("ipcName3","-");
			m0.put("ipcName4","-");
			m0.put("ipcName5","-");
			m0.put("ipcName6","-");
			m0.put("ipcName7","-");
			m0.put("ipcName8","-");
			m0.put("ipcName9","-");
		}
		if(ipcQSName.length==4){
			m0.put("ipcName0",ipcQSName[0]+"_");
			m0.put("ipcName1",ipcQSName[1]+"_");
			m0.put("ipcName2",ipcQSName[2]+"_");
			m0.put("ipcName3",ipcQSName[3]+"_");
			m0.put("ipcName4","-");
			m0.put("ipcName5","-");
			m0.put("ipcName6","-");
			m0.put("ipcName7","-");
			m0.put("ipcName8","-");
			m0.put("ipcName9","-");
		}
		if(ipcQSName.length==5){
			m0.put("ipcName0",ipcQSName[0]+"_");
			m0.put("ipcName1",ipcQSName[1]+"_");
			m0.put("ipcName2",ipcQSName[2]+"_");
			m0.put("ipcName3",ipcQSName[3]+"_");
			m0.put("ipcName4",ipcQSName[4]+"_");
			m0.put("ipcName5","-");
			m0.put("ipcName6","-");
			m0.put("ipcName7","-");
			m0.put("ipcName8","-");
			m0.put("ipcName9","-");
		}
		if(ipcQSName.length==6){
			m0.put("ipcName0",ipcQSName[0]+"_");
			m0.put("ipcName1",ipcQSName[1]+"_");
			m0.put("ipcName2",ipcQSName[2]+"_");
			m0.put("ipcName3",ipcQSName[3]+"_");
			m0.put("ipcName4",ipcQSName[4]+"_");
			m0.put("ipcName5",ipcQSName[5]+"_");
			m0.put("ipcName6","-");
			m0.put("ipcName7","-");
			m0.put("ipcName8","-");
			m0.put("ipcName9","-");
		}
		if(ipcQSName.length==7){
			m0.put("ipcName0",ipcQSName[0]+"_");
			m0.put("ipcName1",ipcQSName[1]+"_");
			m0.put("ipcName2",ipcQSName[2]+"_");
			m0.put("ipcName3",ipcQSName[3]+"_");
			m0.put("ipcName4",ipcQSName[4]+"_");
			m0.put("ipcName5",ipcQSName[5]+"_");
			m0.put("ipcName6",ipcQSName[6]+"_");
			m0.put("ipcName7","-");
			m0.put("ipcName8","-");
			m0.put("ipcName9","-");
		}
		if(ipcQSName.length==8){
			m0.put("ipcName0",ipcQSName[0]+"_");
			m0.put("ipcName1",ipcQSName[1]+"_");
			m0.put("ipcName2",ipcQSName[2]+"_");
			m0.put("ipcName3",ipcQSName[3]+"_");
			m0.put("ipcName4",ipcQSName[4]+"_");
			m0.put("ipcName5",ipcQSName[5]+"_");
			m0.put("ipcName6",ipcQSName[6]+"_");
			m0.put("ipcName7",ipcQSName[7]+"_");
			m0.put("ipcName8","-");
			m0.put("ipcName9","-");
		}
		if(ipcQSName.length==9){
			m0.put("ipcName0",ipcQSName[0]+"_");
			m0.put("ipcName1",ipcQSName[1]+"_");
			m0.put("ipcName2",ipcQSName[2]+"_");
			m0.put("ipcName3",ipcQSName[3]+"_");
			m0.put("ipcName4",ipcQSName[4]+"_");
			m0.put("ipcName5",ipcQSName[5]+"_");
			m0.put("ipcName6",ipcQSName[6]+"_");
			m0.put("ipcName7",ipcQSName[7]+"_");
			m0.put("ipcName8",ipcQSName[8]+"_");
			m0.put("ipcName9","-");
		}
		if(ipcQSName.length==10){
			m0.put("ipcName0",ipcQSName[0]+"_");
			m0.put("ipcName1",ipcQSName[1]+"_");
			m0.put("ipcName2",ipcQSName[2]+"_");
			m0.put("ipcName3",ipcQSName[3]+"_");
			m0.put("ipcName4",ipcQSName[4]+"_");
			m0.put("ipcName5",ipcQSName[5]+"_");
			m0.put("ipcName6",ipcQSName[6]+"_");
			m0.put("ipcName7",ipcQSName[7]+"_");
			m0.put("ipcName8",ipcQSName[8]+"_");
			m0.put("ipcName9",ipcQSName[9]+"_");
		}
		records.add(m0);
		for (int i = 0; i < ipcQS0.length; i++) {
			Map<String, Object> m = new HashMap<String, Object>(); 
			m.put("year", 2007+i+"年");
			if(ipcQSName.length==1){
				m.put("ipcName0",ipcQS0[i]+"_"+ipcQSName[0]);
				m.put("ipcName1","-_");
				m.put("ipcName2","-_");
				m.put("ipcName3","-_");
				m.put("ipcName4","-_");
				m.put("ipcName5","-_");
				m.put("ipcName6","-_");
				m.put("ipcName7","-_");
				m.put("ipcName8","-_");
				m.put("ipcName9","-_");
			}
			if(ipcQSName.length==2){
				m.put("ipcName0",ipcQS0[i]+"_"+ipcQSName[0]);
				m.put("ipcName1",ipcQS1[i]+"_"+ipcQSName[1]);
				m.put("ipcName2","-_");
				m.put("ipcName3","-_");
				m.put("ipcName4","-_");
				m.put("ipcName5","-_");
				m.put("ipcName6","-_");
				m.put("ipcName7","-_");
				m.put("ipcName8","-_");
				m.put("ipcName9","-_");
			}
			if(ipcQSName.length==3){
				m.put("ipcName0",ipcQS0[i]+"_"+ipcQSName[0]);
				m.put("ipcName1",ipcQS1[i]+"_"+ipcQSName[1]);
				m.put("ipcName2",ipcQS2[i]+"_"+ipcQSName[2]);
				m.put("ipcName3","-_");
				m.put("ipcName4","-_");
				m.put("ipcName5","-_");
				m.put("ipcName6","-_");
				m.put("ipcName7","-_");
				m.put("ipcName8","-_");
				m.put("ipcName9","-_");
			}
			if(ipcQSName.length==4){
				m.put("ipcName0",ipcQS0[i]+"_"+ipcQSName[0]);
				m.put("ipcName1",ipcQS1[i]+"_"+ipcQSName[1]);
				m.put("ipcName2",ipcQS2[i]+"_"+ipcQSName[2]);
				m.put("ipcName3",ipcQS3[i]+"_"+ipcQSName[3]);
				m.put("ipcName4","-_");
				m.put("ipcName5","-_");
				m.put("ipcName6","-_");
				m.put("ipcName7","-_");
				m.put("ipcName8","-_");
				m.put("ipcName9","-_");
			}
			if(ipcQSName.length==5){
				m.put("ipcName0",ipcQS0[i]+"_"+ipcQSName[0]);
				m.put("ipcName1",ipcQS1[i]+"_"+ipcQSName[1]);
				m.put("ipcName2",ipcQS2[i]+"_"+ipcQSName[2]);
				m.put("ipcName3",ipcQS3[i]+"_"+ipcQSName[3]);
				m.put("ipcName4",ipcQS4[i]+"_"+ipcQSName[4]);
				m.put("ipcName5","-_");
				m.put("ipcName6","-_");
				m.put("ipcName7","-_");
				m.put("ipcName8","-_");
				m.put("ipcName9","-_");
			}
			if(ipcQSName.length==6){
				m.put("ipcName0",ipcQS0[i]+"_"+ipcQSName[0]);
				m.put("ipcName1",ipcQS1[i]+"_"+ipcQSName[1]);
				m.put("ipcName2",ipcQS2[i]+"_"+ipcQSName[2]);
				m.put("ipcName3",ipcQS3[i]+"_"+ipcQSName[3]);
				m.put("ipcName4",ipcQS4[i]+"_"+ipcQSName[4]);
				m.put("ipcName5",ipcQS5[i]+"_"+ipcQSName[5]);
				m.put("ipcName6","-_");
				m.put("ipcName7","-_");
				m.put("ipcName8","-_");
				m.put("ipcName9","-_");
			}
			if(ipcQSName.length==7){
				m.put("ipcName0",ipcQS0[i]+"_"+ipcQSName[0]);
				m.put("ipcName1",ipcQS1[i]+"_"+ipcQSName[1]);
				m.put("ipcName2",ipcQS2[i]+"_"+ipcQSName[2]);
				m.put("ipcName3",ipcQS3[i]+"_"+ipcQSName[3]);
				m.put("ipcName4",ipcQS4[i]+"_"+ipcQSName[4]);
				m.put("ipcName5",ipcQS5[i]+"_"+ipcQSName[5]);
				m.put("ipcName6",ipcQS6[i]+"_"+ipcQSName[6]);
				m.put("ipcName7","-_");
				m.put("ipcName8","-_");
				m.put("ipcName9","-_");
			}
			if(ipcQSName.length==8){
				m.put("ipcName0",ipcQS0[i]+"_"+ipcQSName[0]);
				m.put("ipcName1",ipcQS1[i]+"_"+ipcQSName[1]);
				m.put("ipcName2",ipcQS2[i]+"_"+ipcQSName[2]);
				m.put("ipcName3",ipcQS3[i]+"_"+ipcQSName[3]);
				m.put("ipcName4",ipcQS4[i]+"_"+ipcQSName[4]);
				m.put("ipcName5",ipcQS5[i]+"_"+ipcQSName[5]);
				m.put("ipcName6",ipcQS6[i]+"_"+ipcQSName[6]);
				m.put("ipcName7",ipcQS7[i]+"_"+ipcQSName[7]);
				m.put("ipcName8","-_");
				m.put("ipcName9","-_");
			}
			if(ipcQSName.length==9){
				m.put("ipcName0",ipcQS0[i]+"_"+ipcQSName[0]);
				m.put("ipcName1",ipcQS1[i]+"_"+ipcQSName[1]);
				m.put("ipcName2",ipcQS2[i]+"_"+ipcQSName[2]);
				m.put("ipcName3",ipcQS3[i]+"_"+ipcQSName[3]);
				m.put("ipcName4",ipcQS4[i]+"_"+ipcQSName[4]);
				m.put("ipcName5",ipcQS5[i]+"_"+ipcQSName[5]);
				m.put("ipcName6",ipcQS6[i]+"_"+ipcQSName[6]);
				m.put("ipcName7",ipcQS7[i]+"_"+ipcQSName[7]);
				m.put("ipcName8",ipcQS8[i]+"_"+ipcQSName[8]);
				m.put("ipcName9","-_");
			}
			if(ipcQSName.length==10){
				m.put("ipcName0",ipcQS0[i]+"_"+ipcQSName[0]);
				m.put("ipcName1",ipcQS1[i]+"_"+ipcQSName[1]);
				m.put("ipcName2",ipcQS2[i]+"_"+ipcQSName[2]);
				m.put("ipcName3",ipcQS3[i]+"_"+ipcQSName[3]);
				m.put("ipcName4",ipcQS4[i]+"_"+ipcQSName[4]);
				m.put("ipcName5",ipcQS5[i]+"_"+ipcQSName[5]);
				m.put("ipcName6",ipcQS6[i]+"_"+ipcQSName[6]);
				m.put("ipcName7",ipcQS7[i]+"_"+ipcQSName[7]);
				m.put("ipcName8",ipcQS8[i]+"_"+ipcQSName[8]);
				m.put("ipcName9",ipcQS9[i]+"_"+ipcQSName[9]);
			}
			
			records.add(m);
		}
		map.put("total", total);
		map.put("rows", records);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取统计信息json 形成报表图形（发明人趋势）
	 * 
	 * 
	 * 
	 */
	public void getPatentSumData_famingrenQS() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> series = new ArrayList<Map>();
		List<String> categories = new ArrayList<String>();
		 String[] ss= jedis.get(user.getId()+"fmr").split("_");
		 String temp=null;
		 String[] strBurArrary=new String[10];
		 if(ss.length>=10){
			 for(int i=0;i<10;i++){
				 temp=ss[i];
				 if(temp!=null&&!"".equals(temp)){
					    strBurArrary[i]=temp.substring(0, temp.indexOf("="));
						jedis.del(user.getId()+strBurArrary[i]+"2007");
						jedis.del(user.getId()+strBurArrary[i]+"2008");
						jedis.del(user.getId()+strBurArrary[i]+"2009");
						jedis.del(user.getId()+strBurArrary[i]+"2010");
						jedis.del(user.getId()+strBurArrary[i]+"2011");
						jedis.del(user.getId()+strBurArrary[i]+"2012");
						jedis.del(user.getId()+strBurArrary[i]+"2013");
						jedis.del(user.getId()+strBurArrary[i]+"2014");
						jedis.del(user.getId()+strBurArrary[i]+"2015");
						jedis.del(user.getId()+strBurArrary[i]+"2016");
					}
			 }
		 }else{
			 for(int i=0;i<ss.length;i++){
				 temp=ss[i];
				 if(temp!=null&&!"".equals(temp)){
					    strBurArrary[i]=temp.substring(0, temp.indexOf("="));
						jedis.del(user.getId()+strBurArrary[i]+"2007");
						jedis.del(user.getId()+strBurArrary[i]+"2008");
						jedis.del(user.getId()+strBurArrary[i]+"2009");
						jedis.del(user.getId()+strBurArrary[i]+"2010");
						jedis.del(user.getId()+strBurArrary[i]+"2011");
						jedis.del(user.getId()+strBurArrary[i]+"2012");
						jedis.del(user.getId()+strBurArrary[i]+"2013");
						jedis.del(user.getId()+strBurArrary[i]+"2014");
						jedis.del(user.getId()+strBurArrary[i]+"2015");
						jedis.del(user.getId()+strBurArrary[i]+"2016");
					}
			 }
		 }
		 for(int i=0;i<10;i++){
			 categories.add((2007+i)+"");
		 }
		 String[] inventorJly=jedis.get(user.getId()+"inventorJly").split("_");
		 inventorJly[inventorJly.length-1]=inventorJly[inventorJly.length-1].substring(0, inventorJly[inventorJly.length-1].length()-3);    ////删除末尾的Jly
		 String[] apdJly=jedis.get(user.getId()+"apdJly").split("_");
		 apdJly[apdJly.length-1]=apdJly[apdJly.length-1].substring(0, apdJly[apdJly.length-1].length()-3);    ////删除末尾的Jly
		for(int i=0;i<inventorJly.length;i++){
			String srtSQR=inventorJly[i];//获得申请人
			if((srtSQR!=null)&&(!"".equals(srtSQR))){    //申请人不空
				if(srtSQR.contains(",")){      //多个申请人
					String[] strs=srtSQR.split(",");
					for(int j=0;j<strs.length;j++){
						if(ss.length>=10){
							for(int k=0;k<10;k++){   //申请人跟10个要统计的进行比对
								if(strBurArrary[k].equals(strs[j])){   //比对成功
									String strYear=apdJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
									if((strYear!=null)&&(!"".equals(strYear))){
										if("2007".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2007", "1");
										}else if("2008".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2008", "1");
										}else if("2009".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2009", "1");
										}else if("2010".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2010", "1");
										}else if("2011".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2011", "1");
										}else if("2012".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2012", "1");
										}else if("2013".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2013", "1");
										}else if("2014".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2014", "1");
										}else if("2015".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2015", "1");
										}else if("2016".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2016", "1");
										}
									}
								}
							}
						}else{
							for(int k=0;k<ss.length;k++){   //申请人跟10个要统计的进行比对
								if(strBurArrary[k].equals(strs[j])){   //比对成功
									String strYear=apdJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
									if((strYear!=null)&&(!"".equals(strYear))){
										if("2007".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2007", "1");
										}else if("2008".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2008", "1");
										}else if("2009".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2009", "1");
										}else if("2010".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2010", "1");
										}else if("2011".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2011", "1");
										}else if("2012".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2012", "1");
										}else if("2013".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2013", "1");
										}else if("2014".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2014", "1");
										}else if("2015".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2015", "1");
										}else if("2016".equals(strYear)){
											jedis.append(user.getId()+strs[j]+"2016", "1");
										}
									}
								}
							}
						}
					}
				}else{  //单个申请人
					if(ss.length>=10){
						for(int k=0;k<10;k++){   //申请人跟10个要统计的进行比对
							if(strBurArrary[k].equals(srtSQR)){   //比对成功
								String strYear=apdJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
								if((strYear!=null)&&(!"".equals(strYear))){
									if("2007".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2007", "1");
									}else if("2008".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2008", "1");
									}else if("2009".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2009", "1");
									}else if("2010".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2010", "1");
									}else if("2011".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2011", "1");
									}else if("2012".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2012", "1");
									}else if("2013".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2013", "1");
									}else if("2014".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2014", "1");
									}else if("2015".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2015", "1");
									}else if("2016".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2016", "1");
									}
								}
							}
						}
					}else{
						for(int k=0;k<ss.length;k++){   //申请人跟10个要统计的进行比对
							if(strBurArrary[k].equals(srtSQR)){   //比对成功
								String strYear=apdJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
								if((strYear!=null)&&(!"".equals(strYear))){
									if("2007".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2007", "1");
									}else if("2008".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2008", "1");
									}else if("2009".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2009", "1");
									}else if("2010".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2010", "1");
									}else if("2011".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2011", "1");
									}else if("2012".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2012", "1");
									}else if("2013".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2013", "1");
									}else if("2014".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2014", "1");
									}else if("2015".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2015", "1");
									}else if("2016".equals(strYear)){
										jedis.append(user.getId()+srtSQR+"2016", "1");
									}
								}
							}
						}
					}
				}
			}
		}
		StringBuilder fmrName=new StringBuilder();//发明人名称
		StringBuilder fmrName0=new StringBuilder();
		StringBuilder fmrName1=new StringBuilder();
		StringBuilder fmrName2=new StringBuilder();
		StringBuilder fmrName3=new StringBuilder();
		StringBuilder fmrName4=new StringBuilder();
		StringBuilder fmrName5=new StringBuilder();
		StringBuilder fmrName6=new StringBuilder();
		StringBuilder fmrName7=new StringBuilder();
		StringBuilder fmrName8=new StringBuilder();
		StringBuilder fmrName9=new StringBuilder();
		if(ss.length>=10){
			for(int i=0;i<10;i++){
				Map<String, Object> m = new HashMap<String, Object>();
				 List<BigInteger> data = new ArrayList<BigInteger>();
				 fmrName.append(strBurArrary[i]).append(",");
				 for(int j=0;j<10;j++){
					 Long  strLong=jedis.strlen(user.getId()+strBurArrary[i]+(2007+j));
					 data.add(BigInteger.valueOf(strLong));
					 if(i==0){
						 fmrName0.append(strLong).append(",");
					 }else if(i==1){
						 fmrName1.append(strLong).append(",");
					 }else if(i==2){
						 fmrName2.append(strLong).append(",");
					 }else if(i==3){
						 fmrName3.append(strLong).append(",");
					 }else if(i==4){
						 fmrName4.append(strLong).append(",");
					 }else if(i==5){
						 fmrName5.append(strLong).append(",");
					 }else if(i==6){
						 fmrName6.append(strLong).append(",");
					 }else if(i==7){
						 fmrName7.append(strLong).append(",");
					 }else if(i==8){
						 fmrName8.append(strLong).append(",");
					 }else if(i==9){
						 fmrName9.append(strLong).append(",");
					 }
				 }
				 m.put("name",strBurArrary[i]);
				 m.put("data", data);
				 series.add(m);
			}
		}else{
			for(int i=0;i<ss.length;i++){
				Map<String, Object> m = new HashMap<String, Object>();
				 List<BigInteger> data = new ArrayList<BigInteger>();
				 fmrName.append(strBurArrary[i]).append(",");
				 for(int j=0;j<10;j++){
					 Long  strLong=jedis.strlen(user.getId()+strBurArrary[i]+(2007+j));
					 data.add(BigInteger.valueOf(strLong));
					 if(i==0){
						 fmrName0.append(strLong).append(",");
					 }else if(i==1){
						 fmrName1.append(strLong).append(",");
					 }else if(i==2){
						 fmrName2.append(strLong).append(",");
					 }else if(i==3){
						 fmrName3.append(strLong).append(",");
					 }else if(i==4){
						 fmrName4.append(strLong).append(",");
					 }else if(i==5){
						 fmrName5.append(strLong).append(",");
					 }else if(i==6){
						 fmrName6.append(strLong).append(",");
					 }else if(i==7){
						 fmrName7.append(strLong).append(",");
					 }else if(i==8){
						 fmrName8.append(strLong).append(",");
					 }else if(i==9){
						 fmrName9.append(strLong).append(",");
					 }
				 }
				 m.put("name",strBurArrary[i]);
				 m.put("data", data);
				 series.add(m);
			}
		}
		if(fmrName.length()>0){
			 fmrName.deleteCharAt(fmrName.length()-1);
		}
		if(fmrName0.length()>0){
			 fmrName0.deleteCharAt(fmrName0.length()-1);
		}
		if(fmrName1.length()>0){
			fmrName1.deleteCharAt(fmrName1.length()-1);
		}
		if(fmrName2.length()>0){
			fmrName2.deleteCharAt(fmrName2.length()-1);
		}
		if(fmrName3.length()>0){
			fmrName3.deleteCharAt(fmrName3.length()-1);
		}
		if(fmrName4.length()>0){
			fmrName4.deleteCharAt(fmrName4.length()-1);
		}
		if(fmrName5.length()>0){
			fmrName5.deleteCharAt(fmrName5.length()-1);
		}
		if(fmrName6.length()>0){
			fmrName6.deleteCharAt(fmrName6.length()-1);
		}
		if(fmrName7.length()>0){
			fmrName7.deleteCharAt(fmrName7.length()-1);
		}
		if(fmrName8.length()>0){
			fmrName8.deleteCharAt(fmrName8.length()-1);
		}
		if(fmrName9.length()>0){
			fmrName9.deleteCharAt(fmrName9.length()-1);
		}
		 jedis.set(user.getId()+"fmrQSName", fmrName.toString());
		 jedis.expire(user.getId()+"fmrQSName", 10000);
		 jedis.set(user.getId()+"fmrQS0", fmrName0.toString());
		 jedis.expire(user.getId()+"fmrQS0", 10000);
		 jedis.set(user.getId()+"fmrQS1", fmrName1.toString());
		 jedis.expire(user.getId()+"fmrQS1", 10000);
		 jedis.set(user.getId()+"fmrQS2", fmrName2.toString());
		 jedis.expire(user.getId()+"fmrQS2", 10000);
		 jedis.set(user.getId()+"fmrQS3", fmrName3.toString());
		 jedis.expire(user.getId()+"fmrQS3", 10000);
		 jedis.set(user.getId()+"fmrQS4", fmrName4.toString());
		 jedis.expire(user.getId()+"fmrQS4", 10000);
		 jedis.set(user.getId()+"fmrQS5", fmrName5.toString());
		 jedis.expire(user.getId()+"fmrQS5", 10000);
		 jedis.set(user.getId()+"fmrQS6", fmrName6.toString());
		 jedis.expire(user.getId()+"fmrQS6", 10000);
		 jedis.set(user.getId()+"fmrQS7", fmrName7.toString());
		 jedis.expire(user.getId()+"fmrQS7", 10000);
		 jedis.set(user.getId()+"fmrQS8", fmrName8.toString());
		 jedis.expire(user.getId()+"fmrQS8", 10000);
		 jedis.set(user.getId()+"fmrQS9", fmrName9.toString());
		 jedis.expire(user.getId()+"fmrQS9", 10000);
		map.put("series", series);
		map.put("categories", categories);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取统计信息json 形成表格（发明人趋势）
	 */
	public void getPatentSumData_dataGrid_fmrQS() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] fmrQSName=jedis.get(user.getId()+"fmrQSName").split(",");
		String[] fmrQS0=jedis.get(user.getId()+"fmrQS0").split(",");
		String[] fmrQS1=jedis.get(user.getId()+"fmrQS1").split(",");
		String[] fmrQS2=jedis.get(user.getId()+"fmrQS2").split(",");
		String[] fmrQS3=jedis.get(user.getId()+"fmrQS3").split(",");
		String[] fmrQS4=jedis.get(user.getId()+"fmrQS4").split(",");
		String[] fmrQS5=jedis.get(user.getId()+"fmrQS5").split(",");
		String[] fmrQS6=jedis.get(user.getId()+"fmrQS6").split(",");
		String[] fmrQS7=jedis.get(user.getId()+"fmrQS7").split(",");
		String[] fmrQS8=jedis.get(user.getId()+"fmrQS8").split(",");
		String[] fmrQS9=jedis.get(user.getId()+"fmrQS9").split(",");
		
		List<Map> records = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		total=new Long((long)fmrQS0.length);
		Map<String, Object> m0 = new HashMap<String, Object>(); 
		m0.put("year", "年份");
		if(fmrQSName.length==1){
			m0.put("fmrName0",fmrQSName[0]+"_");
			m0.put("fmrName1","-");
			m0.put("fmrName2","-");
			m0.put("fmrName3","-");
			m0.put("fmrName4","-");
			m0.put("fmrName5","-");
			m0.put("fmrName6","-");
			m0.put("fmrName7","-");
			m0.put("fmrName8","-");
			m0.put("fmrName9","-");
		}
		if(fmrQSName.length==2){
			m0.put("fmrName0",fmrQSName[0]+"_");
			m0.put("fmrName1",fmrQSName[1]+"_");
			m0.put("fmrName2","-");
			m0.put("fmrName3","-");
			m0.put("fmrName4","-");
			m0.put("fmrName5","-");
			m0.put("fmrName6","-");
			m0.put("fmrName7","-");
			m0.put("fmrName8","-");
			m0.put("fmrName9","-");
		}
		if(fmrQSName.length==3){
			m0.put("fmrName0",fmrQSName[0]+"_");
			m0.put("fmrName1",fmrQSName[1]+"_");
			m0.put("fmrName2",fmrQSName[2]+"_");
			m0.put("fmrName3","-");
			m0.put("fmrName4","-");
			m0.put("fmrName5","-");
			m0.put("fmrName6","-");
			m0.put("fmrName7","-");
			m0.put("fmrName8","-");
			m0.put("fmrName9","-");
		}
		if(fmrQSName.length==4){
			m0.put("fmrName0",fmrQSName[0]+"_");
			m0.put("fmrName1",fmrQSName[1]+"_");
			m0.put("fmrName2",fmrQSName[2]+"_");
			m0.put("fmrName3",fmrQSName[3]+"_");
			m0.put("fmrName4","-");
			m0.put("fmrName5","-");
			m0.put("fmrName6","-");
			m0.put("fmrName7","-");
			m0.put("fmrName8","-");
			m0.put("fmrName9","-");
		}
		if(fmrQSName.length==5){
			m0.put("fmrName0",fmrQSName[0]+"_");
			m0.put("fmrName1",fmrQSName[1]+"_");
			m0.put("fmrName2",fmrQSName[2]+"_");
			m0.put("fmrName3",fmrQSName[3]+"_");
			m0.put("fmrName4",fmrQSName[4]+"_");
			m0.put("fmrName5","-");
			m0.put("fmrName6","-");
			m0.put("fmrName7","-");
			m0.put("fmrName8","-");
			m0.put("fmrName9","-");
		}
		if(fmrQSName.length==6){
			m0.put("fmrName0",fmrQSName[0]+"_");
			m0.put("fmrName1",fmrQSName[1]+"_");
			m0.put("fmrName2",fmrQSName[2]+"_");
			m0.put("fmrName3",fmrQSName[3]+"_");
			m0.put("fmrName4",fmrQSName[4]+"_");
			m0.put("fmrName5",fmrQSName[5]+"_");
			m0.put("fmrName6","-");
			m0.put("fmrName7","-");
			m0.put("fmrName8","-");
			m0.put("fmrName9","-");
		}
		if(fmrQSName.length==7){
			m0.put("fmrName0",fmrQSName[0]+"_");
			m0.put("fmrName1",fmrQSName[1]+"_");
			m0.put("fmrName2",fmrQSName[2]+"_");
			m0.put("fmrName3",fmrQSName[3]+"_");
			m0.put("fmrName4",fmrQSName[4]+"_");
			m0.put("fmrName5",fmrQSName[5]+"_");
			m0.put("fmrName6",fmrQSName[6]+"_");
			m0.put("fmrName7","-");
			m0.put("fmrName8","-");
			m0.put("fmrName9","-");
		}
		if(fmrQSName.length==8){
			m0.put("fmrName0",fmrQSName[0]+"_");
			m0.put("fmrName1",fmrQSName[1]+"_");
			m0.put("fmrName2",fmrQSName[2]+"_");
			m0.put("fmrName3",fmrQSName[3]+"_");
			m0.put("fmrName4",fmrQSName[4]+"_");
			m0.put("fmrName5",fmrQSName[5]+"_");
			m0.put("fmrName6",fmrQSName[6]+"_");
			m0.put("fmrName7",fmrQSName[7]+"_");
			m0.put("fmrName8","-");
			m0.put("fmrName9","-");
		}
		if(fmrQSName.length==9){
			m0.put("fmrName0",fmrQSName[0]+"_");
			m0.put("fmrName1",fmrQSName[1]+"_");
			m0.put("fmrName2",fmrQSName[2]+"_");
			m0.put("fmrName3",fmrQSName[3]+"_");
			m0.put("fmrName4",fmrQSName[4]+"_");
			m0.put("fmrName5",fmrQSName[5]+"_");
			m0.put("fmrName6",fmrQSName[6]+"_");
			m0.put("fmrName7",fmrQSName[7]+"_");
			m0.put("fmrName8",fmrQSName[8]+"_");
			m0.put("fmrName9","-");
		}
		if(fmrQSName.length==10){
			m0.put("fmrName0",fmrQSName[0]+"_");
			m0.put("fmrName1",fmrQSName[1]+"_");
			m0.put("fmrName2",fmrQSName[2]+"_");
			m0.put("fmrName3",fmrQSName[3]+"_");
			m0.put("fmrName4",fmrQSName[4]+"_");
			m0.put("fmrName5",fmrQSName[5]+"_");
			m0.put("fmrName6",fmrQSName[6]+"_");
			m0.put("fmrName7",fmrQSName[7]+"_");
			m0.put("fmrName8",fmrQSName[8]+"_");
			m0.put("fmrName9",fmrQSName[9]+"_");
		}
		records.add(m0);
		for (int i = 0; i < fmrQS0.length; i++) {
			Map<String, Object> m = new HashMap<String, Object>(); 
			m.put("year", 2007+i+"年");
			if(fmrQSName.length==1){
				m.put("fmrName0",fmrQS0[i]+"_"+fmrQSName[0]);
				m.put("fmrName1","-_");
				m.put("fmrName2","-_");
				m.put("fmrName3","-_");
				m.put("fmrName4","-_");
				m.put("fmrName5","-_");
				m.put("fmrName6","-_");
				m.put("fmrName7","-_");
				m.put("fmrName8","-_");
				m.put("fmrName9","-_");
			}
			if(fmrQSName.length==2){
				m.put("fmrName0",fmrQS0[i]+"_"+fmrQSName[0]);
				m.put("fmrName1",fmrQS1[i]+"_"+fmrQSName[1]);
				m.put("fmrName2","-_");
				m.put("fmrName3","-_");
				m.put("fmrName4","-_");
				m.put("fmrName5","-_");
				m.put("fmrName6","-_");
				m.put("fmrName7","-_");
				m.put("fmrName8","-_");
				m.put("fmrName9","-_");
			}
			if(fmrQSName.length==3){
				m.put("fmrName0",fmrQS0[i]+"_"+fmrQSName[0]);
				m.put("fmrName1",fmrQS1[i]+"_"+fmrQSName[1]);
				m.put("fmrName2",fmrQS2[i]+"_"+fmrQSName[2]);
				m.put("fmrName3","-_");
				m.put("fmrName4","-_");
				m.put("fmrName5","-_");
				m.put("fmrName6","-_");
				m.put("fmrName7","-_");
				m.put("fmrName8","-_");
				m.put("fmrName9","-_");
			}
			if(fmrQSName.length==4){
				m.put("fmrName0",fmrQS0[i]+"_"+fmrQSName[0]);
				m.put("fmrName1",fmrQS1[i]+"_"+fmrQSName[1]);
				m.put("fmrName2",fmrQS2[i]+"_"+fmrQSName[2]);
				m.put("fmrName3",fmrQS3[i]+"_"+fmrQSName[3]);
				m.put("fmrName4","-_");
				m.put("fmrName5","-_");
				m.put("fmrName6","-_");
				m.put("fmrName7","-_");
				m.put("fmrName8","-_");
				m.put("fmrName9","-_");
			}
			if(fmrQSName.length==5){
				m.put("fmrName0",fmrQS0[i]+"_"+fmrQSName[0]);
				m.put("fmrName1",fmrQS1[i]+"_"+fmrQSName[1]);
				m.put("fmrName2",fmrQS2[i]+"_"+fmrQSName[2]);
				m.put("fmrName3",fmrQS3[i]+"_"+fmrQSName[3]);
				m.put("fmrName4",fmrQS4[i]+"_"+fmrQSName[4]);
				m.put("fmrName5","-_");
				m.put("fmrName6","-_");
				m.put("fmrName7","-_");
				m.put("fmrName8","-_");
				m.put("fmrName9","-_");
			}
			if(fmrQSName.length==6){
				m.put("fmrName0",fmrQS0[i]+"_"+fmrQSName[0]);
				m.put("fmrName1",fmrQS1[i]+"_"+fmrQSName[1]);
				m.put("fmrName2",fmrQS2[i]+"_"+fmrQSName[2]);
				m.put("fmrName3",fmrQS3[i]+"_"+fmrQSName[3]);
				m.put("fmrName4",fmrQS4[i]+"_"+fmrQSName[4]);
				m.put("fmrName5",fmrQS5[i]+"_"+fmrQSName[5]);
				m.put("fmrName6","-_");
				m.put("fmrName7","-_");
				m.put("fmrName8","-_");
				m.put("fmrName9","-_");
			}
			if(fmrQSName.length==7){
				m.put("fmrName0",fmrQS0[i]+"_"+fmrQSName[0]);
				m.put("fmrName1",fmrQS1[i]+"_"+fmrQSName[1]);
				m.put("fmrName2",fmrQS2[i]+"_"+fmrQSName[2]);
				m.put("fmrName3",fmrQS3[i]+"_"+fmrQSName[3]);
				m.put("fmrName4",fmrQS4[i]+"_"+fmrQSName[4]);
				m.put("fmrName5",fmrQS5[i]+"_"+fmrQSName[5]);
				m.put("fmrName6",fmrQS6[i]+"_"+fmrQSName[6]);
				m.put("fmrName7","-_");
				m.put("fmrName8","-_");
				m.put("fmrName9","-_");
			}
			if(fmrQSName.length==8){
				m.put("fmrName0",fmrQS0[i]+"_"+fmrQSName[0]);
				m.put("fmrName1",fmrQS1[i]+"_"+fmrQSName[1]);
				m.put("fmrName2",fmrQS2[i]+"_"+fmrQSName[2]);
				m.put("fmrName3",fmrQS3[i]+"_"+fmrQSName[3]);
				m.put("fmrName4",fmrQS4[i]+"_"+fmrQSName[4]);
				m.put("fmrName5",fmrQS5[i]+"_"+fmrQSName[5]);
				m.put("fmrName6",fmrQS6[i]+"_"+fmrQSName[6]);
				m.put("fmrName7",fmrQS7[i]+"_"+fmrQSName[7]);
				m.put("fmrName8","-_");
				m.put("fmrName9","-_");
			}
			if(fmrQSName.length==9){
				m.put("fmrName0",fmrQS0[i]+"_"+fmrQSName[0]);
				m.put("fmrName1",fmrQS1[i]+"_"+fmrQSName[1]);
				m.put("fmrName2",fmrQS2[i]+"_"+fmrQSName[2]);
				m.put("fmrName3",fmrQS3[i]+"_"+fmrQSName[3]);
				m.put("fmrName4",fmrQS4[i]+"_"+fmrQSName[4]);
				m.put("fmrName5",fmrQS5[i]+"_"+fmrQSName[5]);
				m.put("fmrName6",fmrQS6[i]+"_"+fmrQSName[6]);
				m.put("fmrName7",fmrQS7[i]+"_"+fmrQSName[7]);
				m.put("fmrName8",fmrQS8[i]+"_"+fmrQSName[8]);
				m.put("fmrName9","-_");
			}
			if(fmrQSName.length==10){
				m.put("fmrName0",fmrQS0[i]+"_"+fmrQSName[0]);
				m.put("fmrName1",fmrQS1[i]+"_"+fmrQSName[1]);
				m.put("fmrName2",fmrQS2[i]+"_"+fmrQSName[2]);
				m.put("fmrName3",fmrQS3[i]+"_"+fmrQSName[3]);
				m.put("fmrName4",fmrQS4[i]+"_"+fmrQSName[4]);
				m.put("fmrName5",fmrQS5[i]+"_"+fmrQSName[5]);
				m.put("fmrName6",fmrQS6[i]+"_"+fmrQSName[6]);
				m.put("fmrName7",fmrQS7[i]+"_"+fmrQSName[7]);
				m.put("fmrName8",fmrQS8[i]+"_"+fmrQSName[8]);
				m.put("fmrName9",fmrQS9[i]+"_"+fmrQSName[9]);
			}
			records.add(m);
		}
		map.put("total", total);
		map.put("rows", records);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取统计信息json 形成报表图形（国省申请人分析）
	 * 
	 * 
	 */
	public void getPatentSumData_ncSQR() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> series = new ArrayList<Map>();
		List<String> categories = new ArrayList<String>();
		 String[] ss= jedis.get(user.getId()+"nc").split("_");
		 String[] ssSQR= jedis.get(user.getId()+"sqr").split("_");
		 String temp=null;
		 String tempSQR=null;
		 String[] strBurArrary=new String[10];   //不止一个地方用得着
		 String[] strSQRBurArrary=new String[10];   //不止一个地方用得着
		 if(ssSQR.length>=10){
			 for(int i=0;i<10;i++){
				 tempSQR=ssSQR[i];
				 strSQRBurArrary[i]=tempSQR.substring(0, tempSQR.indexOf("="));
				 categories.add(strSQRBurArrary[i]);
			 }
		 }else{
			 for(int i=0;i<ssSQR.length;i++){
				 tempSQR=ssSQR[i];
				 strSQRBurArrary[i]=tempSQR.substring(0, tempSQR.indexOf("="));
				 categories.add(strSQRBurArrary[i]);
			 }
		 }
		 if(ss.length>=10){
			 for(int i=0;i<10;i++){
				 temp=ss[i];
				 if(temp!=null&&!"".equals(temp)){
				    strBurArrary[i]=temp.substring(0, temp.indexOf("="));
				    if(ssSQR.length>=10){
						for(int j=0;j<10;j++){
							jedis.del(user.getId()+strBurArrary[i]+strSQRBurArrary[j]);
						}
				    }else{
				    	for(int j=0;j<ssSQR.length;j++){
							jedis.del(user.getId()+strBurArrary[i]+strSQRBurArrary[j]);
						}
				    }
				}
			 }
		 }else{
			 for(int i=0;i<ss.length;i++){
				 temp=ss[i];
				 if(temp!=null&&!"".equals(temp)){
				    strBurArrary[i]=temp.substring(0, temp.indexOf("="));
				    if(ssSQR.length>=10){
						for(int j=0;j<10;j++){
							jedis.del(user.getId()+strBurArrary[i]+strSQRBurArrary[j]);
						}
				    }else{
				    	for(int j=0;j<ssSQR.length;j++){
							jedis.del(user.getId()+strBurArrary[i]+strSQRBurArrary[j]);
						}
				    }
				}
			 }
		 }
		 String[] ncJly=jedis.get(user.getId()+"ncJly").split("_");
		 ncJly[ncJly.length-1]=ncJly[ncJly.length-1].substring(0, ncJly[ncJly.length-1].length()-3);    ////删除末尾的Jly
		 String[] applJly=jedis.get(user.getId()+"applJly").split("_");
		 applJly[applJly.length-1]=applJly[applJly.length-1].substring(0, applJly[applJly.length-1].length()-3);    ////删除末尾的Jly
		for(int i=0;i<ncJly.length;i++){
			String strNC=ncJly[i];//获得nc
			if((strNC!=null)&&(!"".equals(strNC))){    //nc不空
				if(ss.length>=10){
					for(int k=0;k<10;k++){   //nc跟10个要统计的进行比对
						if(strBurArrary[k].equals(strNC)){   //比对成功
							String strSQR=applJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
							if((strSQR!=null)&&(!"".equals(strSQR))){
								if(strSQR.contains(",")){
									String[] strSQRs=strSQR.split(",");
									for(int jj=0;jj<strSQRs.length;jj++){
										if(ssSQR.length>=10){
											for(int ii=0;ii<10;ii++){
												if(strSQRBurArrary[ii].equals(strSQRs[jj])){
													jedis.append(user.getId()+strNC+strSQRBurArrary[ii], "1");
												}
											}
										}else{
											for(int ii=0;ii<ssSQR.length;ii++){
												if(strSQRBurArrary[ii].equals(strSQRs[jj])){
													jedis.append(user.getId()+strNC+strSQRBurArrary[ii], "1");
												}
											}
										}
									}
								}else{
									if(ssSQR.length>=10){
										for(int ii=0;ii<10;ii++){
											if(strSQRBurArrary[ii].equals(strSQR)){
												jedis.append(user.getId()+strNC+strSQRBurArrary[ii], "1");
											}
										}
									}else{
										for(int ii=0;ii<ssSQR.length;ii++){
											if(strSQRBurArrary[ii].equals(strSQR)){
												jedis.append(user.getId()+strNC+strSQRBurArrary[ii], "1");
											}
										}
									}
								}
							}
						}
					}
				}else{
					for(int k=0;k<ss.length;k++){   //nc跟10个要统计的进行比对
						if(strBurArrary[k].equals(strNC)){   //比对成功
							String strSQR=applJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
							if((strSQR!=null)&&(!"".equals(strSQR))){
								if(strSQR.contains(",")){
									String[] strSQRs=strSQR.split(",");
									for(int jj=0;jj<strSQRs.length;jj++){
										if(ssSQR.length>=10){
											for(int ii=0;ii<10;ii++){
												if(strSQRBurArrary[ii].equals(strSQRs[jj])){
													jedis.append(user.getId()+strNC+strSQRBurArrary[ii], "1");
												}
											}
										}else{
											for(int ii=0;ii<ssSQR.length;ii++){
												if(strSQRBurArrary[ii].equals(strSQRs[jj])){
													jedis.append(user.getId()+strNC+strSQRBurArrary[ii], "1");
												}
											}
										}
									}
								}else{
									if(ssSQR.length>=10){
										for(int ii=0;ii<10;ii++){
											if(strSQRBurArrary[ii].equals(strSQR)){
												jedis.append(user.getId()+strNC+strSQRBurArrary[ii], "1");
											}
										}
									}else{
										for(int ii=0;ii<ssSQR.length;ii++){
											if(strSQRBurArrary[ii].equals(strSQR)){
												jedis.append(user.getId()+strNC+strSQRBurArrary[ii], "1");
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		StringBuilder ncName=new StringBuilder();//国省名称
		StringBuilder SqrName=new StringBuilder();//申请人名称
		StringBuilder ncSqrName0=new StringBuilder();
		StringBuilder ncSqrName1=new StringBuilder();
		StringBuilder ncSqrName2=new StringBuilder();
		StringBuilder ncSqrName3=new StringBuilder();
		StringBuilder ncSqrName4=new StringBuilder();
		StringBuilder ncSqrName5=new StringBuilder();
		StringBuilder ncSqrName6=new StringBuilder();
		StringBuilder ncSqrName7=new StringBuilder();
		StringBuilder ncSqrName8=new StringBuilder();
		StringBuilder ncSqrName9=new StringBuilder();
		if(ss.length>=10){
			for(int i=0;i<10;i++){
				Map<String, Object> m = new HashMap<String, Object>();
				 List<BigInteger> data = new ArrayList<BigInteger>();
				 ncName.append(strBurArrary[i]).append(",");
				 SqrName.append(strSQRBurArrary[i]).append(",");
				 if(ssSQR.length>=10){
					 for(int j=0;j<10;j++){
						 Long  strLong=jedis.strlen(user.getId()+strBurArrary[i]+strSQRBurArrary[j]);
						 data.add(BigInteger.valueOf(strLong));
						 if(i==0){
							 ncSqrName0.append(strLong).append(",");
						 }else if(i==1){
							 ncSqrName1.append(strLong).append(",");
						 }else if(i==2){
							 ncSqrName2.append(strLong).append(",");
						 }else if(i==3){
							 ncSqrName3.append(strLong).append(",");
						 }else if(i==4){
							 ncSqrName4.append(strLong).append(",");
						 }else if(i==5){
							 ncSqrName5.append(strLong).append(",");
						 }else if(i==6){
							 ncSqrName6.append(strLong).append(",");
						 }else if(i==7){
							 ncSqrName7.append(strLong).append(",");
						 }else if(i==8){
							 ncSqrName8.append(strLong).append(",");
						 }else if(i==9){
							 ncSqrName9.append(strLong).append(",");
						 }
					 }
				 }else{
					 for(int j=0;j<ssSQR.length;j++){
						 Long  strLong=jedis.strlen(user.getId()+strBurArrary[i]+strSQRBurArrary[j]);
						 data.add(BigInteger.valueOf(strLong));
						 if(i==0){
							 ncSqrName0.append(strLong).append(",");
						 }else if(i==1){
							 ncSqrName1.append(strLong).append(",");
						 }else if(i==2){
							 ncSqrName2.append(strLong).append(",");
						 }else if(i==3){
							 ncSqrName3.append(strLong).append(",");
						 }else if(i==4){
							 ncSqrName4.append(strLong).append(",");
						 }else if(i==5){
							 ncSqrName5.append(strLong).append(",");
						 }else if(i==6){
							 ncSqrName6.append(strLong).append(",");
						 }else if(i==7){
							 ncSqrName7.append(strLong).append(",");
						 }else if(i==8){
							 ncSqrName8.append(strLong).append(",");
						 }else if(i==9){
							 ncSqrName9.append(strLong).append(",");
						 }
					 }
				 }
				 m.put("name",RedisTest.getAddrNameByCode(strBurArrary[i])+"("+strBurArrary[i]+")");
				 m.put("data", data);
				 series.add(m);
			}
		}else{
			for(int i=0;i<ss.length;i++){
				Map<String, Object> m = new HashMap<String, Object>();
				 List<BigInteger> data = new ArrayList<BigInteger>();
				 ncName.append(strBurArrary[i]).append(",");
				 SqrName.append(strSQRBurArrary[i]).append(",");
				 if(ssSQR.length>=10){
					 for(int j=0;j<10;j++){
						 Long  strLong=jedis.strlen(user.getId()+strBurArrary[i]+strSQRBurArrary[j]);
						 data.add(BigInteger.valueOf(strLong));
						 if(i==0){
							 ncSqrName0.append(strLong).append(",");
						 }else if(i==1){
							 ncSqrName1.append(strLong).append(",");
						 }else if(i==2){
							 ncSqrName2.append(strLong).append(",");
						 }else if(i==3){
							 ncSqrName3.append(strLong).append(",");
						 }else if(i==4){
							 ncSqrName4.append(strLong).append(",");
						 }else if(i==5){
							 ncSqrName5.append(strLong).append(",");
						 }else if(i==6){
							 ncSqrName6.append(strLong).append(",");
						 }else if(i==7){
							 ncSqrName7.append(strLong).append(",");
						 }else if(i==8){
							 ncSqrName8.append(strLong).append(",");
						 }else if(i==9){
							 ncSqrName9.append(strLong).append(",");
						 }
					 }
				 }else{
					 for(int j=0;j<ssSQR.length;j++){
						 Long  strLong=jedis.strlen(user.getId()+strBurArrary[i]+strSQRBurArrary[j]);
						 data.add(BigInteger.valueOf(strLong));
						 if(i==0){
							 ncSqrName0.append(strLong).append(",");
						 }else if(i==1){
							 ncSqrName1.append(strLong).append(",");
						 }else if(i==2){
							 ncSqrName2.append(strLong).append(",");
						 }else if(i==3){
							 ncSqrName3.append(strLong).append(",");
						 }else if(i==4){
							 ncSqrName4.append(strLong).append(",");
						 }else if(i==5){
							 ncSqrName5.append(strLong).append(",");
						 }else if(i==6){
							 ncSqrName6.append(strLong).append(",");
						 }else if(i==7){
							 ncSqrName7.append(strLong).append(",");
						 }else if(i==8){
							 ncSqrName8.append(strLong).append(",");
						 }else if(i==9){
							 ncSqrName9.append(strLong).append(",");
						 }
					 }
				 }
				 m.put("name",RedisTest.getAddrNameByCode(strBurArrary[i])+"("+strBurArrary[i]+")");
				 m.put("data", data);
				 series.add(m);
			}
		}
		if(ncName.length()>0){
			ncName.deleteCharAt(ncName.length()-1);
		}	
		if(SqrName.length()>0){
		    SqrName.deleteCharAt(SqrName.length()-1);
		}
		if(ncSqrName0.length()>0){
			ncSqrName0.deleteCharAt(ncSqrName0.length()-1);
		}
		if(ncSqrName1.length()>0){
			ncSqrName1.deleteCharAt(ncSqrName1.length()-1);
		}
		if(ncSqrName2.length()>0){
			ncSqrName2.deleteCharAt(ncSqrName2.length()-1);
		}
		if(ncSqrName3.length()>0){
			ncSqrName3.deleteCharAt(ncSqrName3.length()-1);
		}
		if(ncSqrName4.length()>0){
			ncSqrName4.deleteCharAt(ncSqrName4.length()-1);
		}
		if(ncSqrName5.length()>0){
			ncSqrName5.deleteCharAt(ncSqrName5.length()-1);
		}
		if(ncSqrName6.length()>0){
			ncSqrName6.deleteCharAt(ncSqrName6.length()-1);
		}
		if(ncSqrName7.length()>0){
			ncSqrName7.deleteCharAt(ncSqrName7.length()-1);
		}
		if(ncSqrName8.length()>0){
			ncSqrName8.deleteCharAt(ncSqrName8.length()-1);
		}
		if(ncSqrName9.length()>0){
			ncSqrName9.deleteCharAt(ncSqrName9.length()-1);
		}
		 jedis.set(user.getId()+"ncsqrNCName", ncName.toString());
		 jedis.expire(user.getId()+"ncsqrNCName", 10000);
		 jedis.set(user.getId()+"ncsqrSQRName", SqrName.toString());
		 jedis.expire(user.getId()+"ncsqrSQRName", 10000);
		 jedis.set(user.getId()+"ncsqr0", ncSqrName0.toString());
		 jedis.expire(user.getId()+"ncsqr0", 10000);
		 jedis.set(user.getId()+"ncsqr1", ncSqrName1.toString());
		 jedis.expire(user.getId()+"ncsqr1", 10000);
		 jedis.set(user.getId()+"ncsqr2", ncSqrName2.toString());
		 jedis.expire(user.getId()+"ncsqr2", 10000);
		 jedis.set(user.getId()+"ncsqr3", ncSqrName3.toString());
		 jedis.expire(user.getId()+"ncsqr3", 10000);
		 jedis.set(user.getId()+"ncsqr4", ncSqrName4.toString());
		 jedis.expire(user.getId()+"ncsqr4", 10000);
		 jedis.set(user.getId()+"ncsqr5", ncSqrName5.toString());
		 jedis.expire(user.getId()+"ncsqr5", 10000);
		 jedis.set(user.getId()+"ncsqr6", ncSqrName6.toString());
		 jedis.expire(user.getId()+"ncsqr6", 10000);
		 jedis.set(user.getId()+"ncsqr7", ncSqrName7.toString());
		 jedis.expire(user.getId()+"ncsqr7", 10000);
		 jedis.set(user.getId()+"ncsqr8", ncSqrName8.toString());
		 jedis.expire(user.getId()+"ncsqr8", 10000);
		 jedis.set(user.getId()+"ncsqr9", ncSqrName9.toString());
		 jedis.expire(user.getId()+"ncsqr9", 10000);
		map.put("series", series);
		map.put("categories", categories);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取统计信息json 形成表格（国省申请人）
	 */
	public void getPatentSumData_dataGrid_ncSQR() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] ncsqrNCName=jedis.get(user.getId()+"ncsqrNCName").split(",");
//		String[] ncsqrSQRName=jedis.get(user.getId()+"ncsqrSQRName").split(",");
		String[] ssSQR= jedis.get(user.getId()+"sqr").split("_");
		 String tempSQR=null;
		 String[] ncsqrSQRName=new String[ssSQR.length<10?ssSQR.length:10];   //不止一个地方用得着
		List<String> categories = new ArrayList<String>();
		if(ssSQR.length>=10){
			 for(int i=0;i<10;i++){
				 tempSQR=ssSQR[i];
				 ncsqrSQRName[i]=tempSQR.substring(0, tempSQR.indexOf("="));
				 categories.add(ncsqrSQRName[i]);
			 }
		}else{
			 for(int i=0;i<ssSQR.length;i++){
				 tempSQR=ssSQR[i];
				 ncsqrSQRName[i]=tempSQR.substring(0, tempSQR.indexOf("="));
				 categories.add(ncsqrSQRName[i]);
			 }
		}
		String[] ncsqr0=jedis.get(user.getId()+"ncsqr0").split(",");
		String[] ncsqr1=jedis.get(user.getId()+"ncsqr1").split(",");
		String[] ncsqr2=jedis.get(user.getId()+"ncsqr2").split(",");
		String[] ncsqr3=jedis.get(user.getId()+"ncsqr3").split(",");
		String[] ncsqr4=jedis.get(user.getId()+"ncsqr4").split(",");
		String[] ncsqr5=jedis.get(user.getId()+"ncsqr5").split(",");
		String[] ncsqr6=jedis.get(user.getId()+"ncsqr6").split(",");
		String[] ncsqr7=jedis.get(user.getId()+"ncsqr7").split(",");
		String[] ncsqr8=jedis.get(user.getId()+"ncsqr8").split(",");
		String[] ncsqr9=jedis.get(user.getId()+"ncsqr9").split(",");
		List<Map> records = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		total=new Long((long)ncsqr0.length);
		Map<String, Object> m0 = new HashMap<String, Object>(); 
		m0.put("sqr", "申请人");
		if(ncsqrNCName.length==1){
			m0.put("ncsqrName0",RedisTest.getAddrNameByCode(ncsqrNCName[0])+"("+ncsqrNCName[0]+")_");
			m0.put("ncsqrName1","");
			m0.put("ncsqrName2","");
			m0.put("ncsqrName3","");
			m0.put("ncsqrName4","");
			m0.put("ncsqrName5","");
			m0.put("ncsqrName6","");
			m0.put("ncsqrName7","");
			m0.put("ncsqrName8","");
			m0.put("ncsqrName9","");
		}
		if(ncsqrNCName.length==2){
			m0.put("ncsqrName0",RedisTest.getAddrNameByCode(ncsqrNCName[0])+"("+ncsqrNCName[0]+")_");
			m0.put("ncsqrName1",RedisTest.getAddrNameByCode(ncsqrNCName[1])+"("+ncsqrNCName[1]+")_");
			m0.put("ncsqrName2","");
			m0.put("ncsqrName3","");
			m0.put("ncsqrName4","");
			m0.put("ncsqrName5","");
			m0.put("ncsqrName6","");
			m0.put("ncsqrName7","");
			m0.put("ncsqrName8","");
			m0.put("ncsqrName9","");
		}
		if(ncsqrNCName.length==3){
			m0.put("ncsqrName0",RedisTest.getAddrNameByCode(ncsqrNCName[0])+"("+ncsqrNCName[0]+")_");
			m0.put("ncsqrName1",RedisTest.getAddrNameByCode(ncsqrNCName[1])+"("+ncsqrNCName[1]+")_");
			m0.put("ncsqrName2",RedisTest.getAddrNameByCode(ncsqrNCName[2])+"("+ncsqrNCName[2]+")_");
			m0.put("ncsqrName3","");
			m0.put("ncsqrName4","");
			m0.put("ncsqrName5","");
			m0.put("ncsqrName6","");
			m0.put("ncsqrName7","");
			m0.put("ncsqrName8","");
			m0.put("ncsqrName9","");
		}
		if(ncsqrNCName.length==4){
			m0.put("ncsqrName0",RedisTest.getAddrNameByCode(ncsqrNCName[0])+"("+ncsqrNCName[0]+")_");
			m0.put("ncsqrName1",RedisTest.getAddrNameByCode(ncsqrNCName[1])+"("+ncsqrNCName[1]+")_");
			m0.put("ncsqrName2",RedisTest.getAddrNameByCode(ncsqrNCName[2])+"("+ncsqrNCName[2]+")_");
			m0.put("ncsqrName3",RedisTest.getAddrNameByCode(ncsqrNCName[3])+"("+ncsqrNCName[3]+")_");
			m0.put("ncsqrName4","");
			m0.put("ncsqrName5","");
			m0.put("ncsqrName6","");
			m0.put("ncsqrName7","");
			m0.put("ncsqrName8","");
			m0.put("ncsqrName9","");
		}
		if(ncsqrNCName.length==5){
			m0.put("ncsqrName0",RedisTest.getAddrNameByCode(ncsqrNCName[0])+"("+ncsqrNCName[0]+")_");
			m0.put("ncsqrName1",RedisTest.getAddrNameByCode(ncsqrNCName[1])+"("+ncsqrNCName[1]+")_");
			m0.put("ncsqrName2",RedisTest.getAddrNameByCode(ncsqrNCName[2])+"("+ncsqrNCName[2]+")_");
			m0.put("ncsqrName3",RedisTest.getAddrNameByCode(ncsqrNCName[3])+"("+ncsqrNCName[3]+")_");
			m0.put("ncsqrName4",RedisTest.getAddrNameByCode(ncsqrNCName[4])+"("+ncsqrNCName[4]+")_");
			m0.put("ncsqrName5","");
			m0.put("ncsqrName6","");
			m0.put("ncsqrName7","");
			m0.put("ncsqrName8","");
			m0.put("ncsqrName9","");
		}
		if(ncsqrNCName.length==6){
			m0.put("ncsqrName0",RedisTest.getAddrNameByCode(ncsqrNCName[0])+"("+ncsqrNCName[0]+")_");
			m0.put("ncsqrName1",RedisTest.getAddrNameByCode(ncsqrNCName[1])+"("+ncsqrNCName[1]+")_");
			m0.put("ncsqrName2",RedisTest.getAddrNameByCode(ncsqrNCName[2])+"("+ncsqrNCName[2]+")_");
			m0.put("ncsqrName3",RedisTest.getAddrNameByCode(ncsqrNCName[3])+"("+ncsqrNCName[3]+")_");
			m0.put("ncsqrName4",RedisTest.getAddrNameByCode(ncsqrNCName[4])+"("+ncsqrNCName[4]+")_");
			m0.put("ncsqrName5",RedisTest.getAddrNameByCode(ncsqrNCName[5])+"("+ncsqrNCName[5]+")_");
			m0.put("ncsqrName6","");
			m0.put("ncsqrName7","");
			m0.put("ncsqrName8","");
			m0.put("ncsqrName9","");
		}
		if(ncsqrNCName.length==7){
			m0.put("ncsqrName0",RedisTest.getAddrNameByCode(ncsqrNCName[0])+"("+ncsqrNCName[0]+")_");
			m0.put("ncsqrName1",RedisTest.getAddrNameByCode(ncsqrNCName[1])+"("+ncsqrNCName[1]+")_");
			m0.put("ncsqrName2",RedisTest.getAddrNameByCode(ncsqrNCName[2])+"("+ncsqrNCName[2]+")_");
			m0.put("ncsqrName3",RedisTest.getAddrNameByCode(ncsqrNCName[3])+"("+ncsqrNCName[3]+")_");
			m0.put("ncsqrName4",RedisTest.getAddrNameByCode(ncsqrNCName[4])+"("+ncsqrNCName[4]+")_");
			m0.put("ncsqrName5",RedisTest.getAddrNameByCode(ncsqrNCName[5])+"("+ncsqrNCName[5]+")_");
			m0.put("ncsqrName6",RedisTest.getAddrNameByCode(ncsqrNCName[6])+"("+ncsqrNCName[6]+")_");
			m0.put("ncsqrName7","");
			m0.put("ncsqrName8","");
			m0.put("ncsqrName9","");
		}
		if(ncsqrNCName.length==8){
			m0.put("ncsqrName0",RedisTest.getAddrNameByCode(ncsqrNCName[0])+"("+ncsqrNCName[0]+")_");
			m0.put("ncsqrName1",RedisTest.getAddrNameByCode(ncsqrNCName[1])+"("+ncsqrNCName[1]+")_");
			m0.put("ncsqrName2",RedisTest.getAddrNameByCode(ncsqrNCName[2])+"("+ncsqrNCName[2]+")_");
			m0.put("ncsqrName3",RedisTest.getAddrNameByCode(ncsqrNCName[3])+"("+ncsqrNCName[3]+")_");
			m0.put("ncsqrName4",RedisTest.getAddrNameByCode(ncsqrNCName[4])+"("+ncsqrNCName[4]+")_");
			m0.put("ncsqrName5",RedisTest.getAddrNameByCode(ncsqrNCName[5])+"("+ncsqrNCName[5]+")_");
			m0.put("ncsqrName6",RedisTest.getAddrNameByCode(ncsqrNCName[6])+"("+ncsqrNCName[6]+")_");
			m0.put("ncsqrName7",RedisTest.getAddrNameByCode(ncsqrNCName[7])+"("+ncsqrNCName[7]+")_");
			m0.put("ncsqrName8","");
			m0.put("ncsqrName9","");
		}
		if(ncsqrNCName.length==9){
			m0.put("ncsqrName0",RedisTest.getAddrNameByCode(ncsqrNCName[0])+"("+ncsqrNCName[0]+")_");
			m0.put("ncsqrName1",RedisTest.getAddrNameByCode(ncsqrNCName[1])+"("+ncsqrNCName[1]+")_");
			m0.put("ncsqrName2",RedisTest.getAddrNameByCode(ncsqrNCName[2])+"("+ncsqrNCName[2]+")_");
			m0.put("ncsqrName3",RedisTest.getAddrNameByCode(ncsqrNCName[3])+"("+ncsqrNCName[3]+")_");
			m0.put("ncsqrName4",RedisTest.getAddrNameByCode(ncsqrNCName[4])+"("+ncsqrNCName[4]+")_");
			m0.put("ncsqrName5",RedisTest.getAddrNameByCode(ncsqrNCName[5])+"("+ncsqrNCName[5]+")_");
			m0.put("ncsqrName6",RedisTest.getAddrNameByCode(ncsqrNCName[6])+"("+ncsqrNCName[6]+")_");
			m0.put("ncsqrName7",RedisTest.getAddrNameByCode(ncsqrNCName[7])+"("+ncsqrNCName[7]+")_");
			m0.put("ncsqrName8",RedisTest.getAddrNameByCode(ncsqrNCName[8])+"("+ncsqrNCName[8]+")_");
			m0.put("ncsqrName9","-");
		}
		if(ncsqrNCName.length==10){
			m0.put("ncsqrName0",RedisTest.getAddrNameByCode(ncsqrNCName[0])+"("+ncsqrNCName[0]+")_");
			m0.put("ncsqrName1",RedisTest.getAddrNameByCode(ncsqrNCName[1])+"("+ncsqrNCName[1]+")_");
			m0.put("ncsqrName2",RedisTest.getAddrNameByCode(ncsqrNCName[2])+"("+ncsqrNCName[2]+")_");
			m0.put("ncsqrName3",RedisTest.getAddrNameByCode(ncsqrNCName[3])+"("+ncsqrNCName[3]+")_");
			m0.put("ncsqrName4",RedisTest.getAddrNameByCode(ncsqrNCName[4])+"("+ncsqrNCName[4]+")_");
			m0.put("ncsqrName5",RedisTest.getAddrNameByCode(ncsqrNCName[5])+"("+ncsqrNCName[5]+")_");
			m0.put("ncsqrName6",RedisTest.getAddrNameByCode(ncsqrNCName[6])+"("+ncsqrNCName[6]+")_");
			m0.put("ncsqrName7",RedisTest.getAddrNameByCode(ncsqrNCName[7])+"("+ncsqrNCName[7]+")_");
			m0.put("ncsqrName8",RedisTest.getAddrNameByCode(ncsqrNCName[8])+"("+ncsqrNCName[8]+")_");
			m0.put("ncsqrName9",RedisTest.getAddrNameByCode(ncsqrNCName[9])+"("+ncsqrNCName[9]+")_");
		}
		records.add(m0);
		if(ncsqrSQRName.length>=10){
			for (int i = 0; i < 10; i++) {
				Map<String, Object> m = new HashMap<String, Object>(); 
				m.put("sqr", ncsqrSQRName[i]);
				if(ncsqr0.length>1){
					m.put("ncsqrName0",ncsqr0[i]+"_"+ncsqrNCName[0]);
				}
				if(ncsqr1.length>1){
				    m.put("ncsqrName1",ncsqr1[i]+"_"+ncsqrNCName[1]);
				}
				if(ncsqr2.length>1){
					m.put("ncsqrName2",ncsqr2[i]+"_"+ncsqrNCName[2]);
				}
				if(ncsqr3.length>1){
					m.put("ncsqrName3",ncsqr3[i]+"_"+ncsqrNCName[3]);
				}
				if(ncsqr4.length>1){
					m.put("ncsqrName4",ncsqr4[i]+"_"+ncsqrNCName[4]);
				}
				if(ncsqr5.length>1){
					m.put("ncsqrName5",ncsqr5[i]+"_"+ncsqrNCName[5]);
				}
				if(ncsqr6.length>1){
					m.put("ncsqrName6",ncsqr6[i]+"_"+ncsqrNCName[6]);
				}
				if(ncsqr7.length>1){
					m.put("ncsqrName7",ncsqr7[i]+"_"+ncsqrNCName[7]);
				}
				if(ncsqr8.length>1){
					m.put("ncsqrName8",ncsqr8[i]+"_"+ncsqrNCName[8]);
				}
				if(ncsqr9.length>1){
					m.put("ncsqrName9",ncsqr9[i]+"_"+ncsqrNCName[9]);
				}
				records.add(m);
			}
		}else{
			for (int i = 0; i < ncsqrSQRName.length; i++) {
				Map<String, Object> m = new HashMap<String, Object>(); 
				m.put("sqr", ncsqrSQRName[i]);
				if(!ncsqr0[0].equals("")){
					m.put("ncsqrName0",ncsqr0[i]+"_"+ncsqrNCName[0]);
				}else{
					m.put("ncsqrName0","-_");
				}
				if(!ncsqr1[0].equals("")){
				    m.put("ncsqrName1",ncsqr1[i]+"_"+ncsqrNCName[1]);
				}else{
					m.put("ncsqrName1","-_");
				}
				if(!ncsqr2[0].equals("")){
					m.put("ncsqrName2",ncsqr2[i]+"_"+ncsqrNCName[2]);
				}else{
					m.put("ncsqrName2","-_");
				}
				if(!ncsqr3[0].equals("")){
					m.put("ncsqrName3",ncsqr3[i]+"_"+ncsqrNCName[3]);
				}else{
					m.put("ncsqrName3","-_");
				}
				if(!ncsqr4[0].equals("")){
					m.put("ncsqrName4",ncsqr4[i]+"_"+ncsqrNCName[4]);
				}else{
					m.put("ncsqrName4","-_");
				}
				if(!ncsqr5[0].equals("")){
					m.put("ncsqrName5",ncsqr5[i]+"_"+ncsqrNCName[5]);
				}else{
					m.put("ncsqrName5","-_");
				}
				if(!ncsqr6[0].equals("")){
					m.put("ncsqrName6",ncsqr6[i]+"_"+ncsqrNCName[6]);
				}else{
					m.put("ncsqrName6","-_");
				}
				if(!ncsqr7[0].equals("")){
					m.put("ncsqrName7",ncsqr7[i]+"_"+ncsqrNCName[7]);
				}else{
					m.put("ncsqrName7","-_");
				}
				if(!ncsqr8[0].equals("")){
					m.put("ncsqrName8",ncsqr8[i]+"_"+ncsqrNCName[8]);
				}else{
					m.put("ncsqrName8","-_");
				}
				if(!ncsqr9[0].equals("")){
					m.put("ncsqrName9",ncsqr9[i]+"_"+ncsqrNCName[9]);
				}else{
					m.put("ncsqrName9","-_");
				}
				records.add(m);
			}
		}
		map.put("total", total);
		map.put("rows", records);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取统计信息json 形成报表图形（申请人国省分析）
	 * 
	 * 
	 */
	public void getPatentSumData_shenqingrenNC() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> series = new ArrayList<Map>();
		List<String> categories = new ArrayList<String>();
		 String[] ss= jedis.get(user.getId()+"sqr").split("_");
		 String[] ssNC= jedis.get(user.getId()+"nc").split("_");
		 String temp=null;
		 String tempNC=null;
		 String[] strBurArrary=new String[10];   //不止一个地方用得着
		 String[] strNCBurArrary=new String[10];   //不止一个地方用得着
		 for(int i=0;i<(ssNC.length<10?ssNC.length:10);i++){
			 tempNC=ssNC[i];
			 strNCBurArrary[i]=tempNC.substring(0, tempNC.indexOf("="));
			 categories.add(RedisTest.getAddrNameByCode(strNCBurArrary[i])+"("+strNCBurArrary[i]+")");
		 }
		 for(int i=0;i<(ss.length<10?ss.length:10);i++){
		    temp=ss[i];
		    strBurArrary[i]=temp.substring(0, temp.indexOf("="));
			for(int j=0;j<10;j++){
				jedis.del(user.getId()+strBurArrary[i]+strNCBurArrary[j]);
			}
		 }
		 String[] applJly=jedis.get(user.getId()+"applJly").split("_");
		 applJly[applJly.length-1]=applJly[applJly.length-1].substring(0, applJly[applJly.length-1].length()-3);    ////删除末尾的Jly
		 String[] ncJly=jedis.get(user.getId()+"ncJly").split("_");
		 ncJly[ncJly.length-1]=ncJly[ncJly.length-1].substring(0, ncJly[ncJly.length-1].length()-3);    ////删除末尾的Jly
		for(int i=0;i<applJly.length;i++){
			String srtSQR=applJly[i];//获得申请人
			if((srtSQR!=null)&&(!"".equals(srtSQR))){    //申请人不空
				if(srtSQR.contains(",")){      //多个申请人
					String[] strs=srtSQR.split(",");
					for(int j=0;j<strs.length;j++){
						for(int k=0;k<(ss.length<10?ss.length:10);k++){   //申请人跟10个要统计的进行比对
							if(strBurArrary[k].equals(strs[j])){   //比对成功
								String strNC=ncJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
								if((strNC!=null)&&(!"".equals(strNC))){
									for(int ii=0;ii<(ssNC.length<10?ssNC.length:10);ii++){
										if(strNCBurArrary[ii].equals(strNC)){
											jedis.append(user.getId()+strBurArrary[k]+strNCBurArrary[ii], "1");
										}
									}
								}
							}
						}
					}
				}else{  //单个申请人
					for(int k=0;k<(ss.length<10?ss.length:10);k++){   //申请人跟10个要统计的进行比对
						if(strBurArrary[k].equals(srtSQR)){   //比对成功
							String strNC=ncJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
							if((strNC!=null)&&(!"".equals(strNC))){
								for(int ii=0;ii<(ssNC.length<10?ssNC.length:10);ii++){
									if(strNCBurArrary[ii].equals(strNC)){
										jedis.append(user.getId()+srtSQR+strNCBurArrary[ii], "1");
									}
								}
							}
						}
					}
				}
			}
		}
		StringBuilder sqrName=new StringBuilder();//申请人名称
		StringBuilder ncName=new StringBuilder();//nc名称
		StringBuilder sqrNcName0=new StringBuilder();
		StringBuilder sqrNcName1=new StringBuilder();
		StringBuilder sqrNcName2=new StringBuilder();
		StringBuilder sqrNcName3=new StringBuilder();
		StringBuilder sqrNcName4=new StringBuilder();
		StringBuilder sqrNcName5=new StringBuilder();
		StringBuilder sqrNcName6=new StringBuilder();
		StringBuilder sqrNcName7=new StringBuilder();
		StringBuilder sqrNcName8=new StringBuilder();
		StringBuilder sqrNcName9=new StringBuilder();
		for(int i=0;i<(ss.length<10?ss.length:10);i++){
			Map<String, Object> m = new HashMap<String, Object>();
			 List<BigInteger> data = new ArrayList<BigInteger>();
			 sqrName.append(strBurArrary[i]).append(",");
			 ncName.append(strNCBurArrary[i]).append(",");
			 for(int j=0;j<(ssNC.length<10?ssNC.length:10);j++){
				 Long  strLong=jedis.strlen(user.getId()+strBurArrary[i]+strNCBurArrary[j]);
				 data.add(BigInteger.valueOf(strLong));
				 if(i==0){
					 sqrNcName0.append(strLong).append(",");
				 }else if(i==1){
					 sqrNcName1.append(strLong).append(",");
				 }else if(i==2){
					 sqrNcName2.append(strLong).append(",");
				 }else if(i==3){
					 sqrNcName3.append(strLong).append(",");
				 }else if(i==4){
					 sqrNcName4.append(strLong).append(",");
				 }else if(i==5){
					 sqrNcName5.append(strLong).append(",");
				 }else if(i==6){
					 sqrNcName6.append(strLong).append(",");
				 }else if(i==7){
					 sqrNcName7.append(strLong).append(",");
				 }else if(i==8){
					 sqrNcName8.append(strLong).append(",");
				 }else if(i==9){
					 sqrNcName9.append(strLong).append(",");
				 }
			 }
			 m.put("name",strBurArrary[i]);
			 m.put("data", data);
			 series.add(m);
		}
		if(sqrName.length()>0){
			sqrName.deleteCharAt(sqrName.length()-1);
		}
		if(ncName.length()>0){
			ncName.deleteCharAt(ncName.length()-1);
		}
		if(sqrNcName0.length()>0){
			sqrNcName0.deleteCharAt(sqrNcName0.length()-1);
		}
		if(sqrNcName1.length()>0){
			sqrNcName1.deleteCharAt(sqrNcName1.length()-1);
		}
		if(sqrNcName2.length()>0){
			sqrNcName2.deleteCharAt(sqrNcName2.length()-1);
		}
		if(sqrNcName3.length()>0){
			sqrNcName3.deleteCharAt(sqrNcName3.length()-1);
		}
		if(sqrNcName4.length()>0){
			sqrNcName4.deleteCharAt(sqrNcName4.length()-1);
		}
		if(sqrNcName5.length()>0){
			sqrNcName5.deleteCharAt(sqrNcName5.length()-1);
		}
		if(sqrNcName6.length()>0){
			sqrNcName6.deleteCharAt(sqrNcName6.length()-1);
		}
		if(sqrNcName7.length()>0){
			sqrNcName7.deleteCharAt(sqrNcName7.length()-1);
		}
		if(sqrNcName8.length()>0){
			sqrNcName8.deleteCharAt(sqrNcName8.length()-1);
		}
		if(sqrNcName9.length()>0){
			sqrNcName9.deleteCharAt(sqrNcName9.length()-1);
		}
		 jedis.set(user.getId()+"sqrNcSQRName", sqrName.toString());
		 jedis.expire(user.getId()+"sqrNcSQRName", 10000);
		 jedis.set(user.getId()+"sqrNcNCName", ncName.toString());
		 jedis.expire(user.getId()+"sqrNcNCName", 10000);
		 jedis.set(user.getId()+"sqrNc0", sqrNcName0.toString());
		 jedis.expire(user.getId()+"sqrNc0", 10000);
		 jedis.set(user.getId()+"sqrNc1", sqrNcName1.toString());
		 jedis.expire(user.getId()+"sqrNc1", 10000);
		 jedis.set(user.getId()+"sqrNc2", sqrNcName2.toString());
		 jedis.expire(user.getId()+"sqrNc2", 10000);
		 jedis.set(user.getId()+"sqrNc3", sqrNcName3.toString());
		 jedis.expire(user.getId()+"sqrNc3", 10000);
		 jedis.set(user.getId()+"sqrNc4", sqrNcName4.toString());
		 jedis.expire(user.getId()+"sqrNc4", 10000);
		 jedis.set(user.getId()+"sqrNc5", sqrNcName5.toString());
		 jedis.expire(user.getId()+"sqrNc5", 10000);
		 jedis.set(user.getId()+"sqrNc6", sqrNcName6.toString());
		 jedis.expire(user.getId()+"sqrNc6", 10000);
		 jedis.set(user.getId()+"sqrNc7", sqrNcName7.toString());
		 jedis.expire(user.getId()+"sqrNc7", 10000);
		 jedis.set(user.getId()+"sqrNc8", sqrNcName8.toString());
		 jedis.expire(user.getId()+"sqrNc8", 10000);
		 jedis.set(user.getId()+"sqrNc9", sqrNcName9.toString());
		 jedis.expire(user.getId()+"sqrNc9", 10000);
		map.put("series", series);
		map.put("categories", categories);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取统计信息json 形成表格（申请人国省）
	 */
	public void getPatentSumData_dataGrid_sqrNC() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] ncsqrNCName=jedis.get(user.getId()+"sqrNcSQRName").split(",");
//		String[] ncsqrSQRName=jedis.get(user.getId()+"sqrNcNCName").split(",");

		List<String> categories = new ArrayList<String>();
		String[] ssNC = jedis.get(user.getId() + "nc").split("_");
		String tempNC = null;
		String[] ncsqrSQRName = new String[ssNC.length<10?ssNC.length:10]; // 不止一个地方用得着
		for (int i = 0; i < (ssNC.length < 10 ? ssNC.length : 10); i++) {
			tempNC = ssNC[i];
			ncsqrSQRName[i] = tempNC.substring(0, tempNC.indexOf("="));
			categories.add(RedisTest.getAddrNameByCode(ncsqrSQRName[i]) + "("
					+ ncsqrSQRName[i] + ")");
		}
		String[] ncsqr0=jedis.get(user.getId()+"sqrNc0").split(",");
		String[] ncsqr1=jedis.get(user.getId()+"sqrNc1").split(",");
		String[] ncsqr2=jedis.get(user.getId()+"sqrNc2").split(",");
		String[] ncsqr3=jedis.get(user.getId()+"sqrNc3").split(",");
		String[] ncsqr4=jedis.get(user.getId()+"sqrNc4").split(",");
		String[] ncsqr5=jedis.get(user.getId()+"sqrNc5").split(",");
		String[] ncsqr6=jedis.get(user.getId()+"sqrNc6").split(",");
		String[] ncsqr7=jedis.get(user.getId()+"sqrNc7").split(",");
		String[] ncsqr8=jedis.get(user.getId()+"sqrNc8").split(",");
		String[] ncsqr9=jedis.get(user.getId()+"sqrNc9").split(",");
		List<Map> records = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		total=new Long((long)ncsqr0.length);
		Map<String, Object> m0 = new HashMap<String, Object>(); 
		m0.put("nc", "国省");
		for(int j=0;j<ncsqrNCName.length;j++){
			m0.put("sqrNCName"+j,ncsqrNCName[j]+"_");
		}
		for(int j=9;j>=ncsqrNCName.length;j--){
			m0.put("sqrNCName"+j,"_");
		}
//		m0.put("sqrNCName0",ncsqrNCName[0]+"_");
//		m0.put("sqrNCName1",ncsqrNCName[1]+"_");
//		m0.put("sqrNCName2",ncsqrNCName[2]+"_");
//		m0.put("sqrNCName3",ncsqrNCName[3]+"_");
//		m0.put("sqrNCName4",ncsqrNCName[4]+"_");
//		m0.put("sqrNCName5",ncsqrNCName[5]+"_");
//		m0.put("sqrNCName6",ncsqrNCName[6]+"_");
//		m0.put("sqrNCName7",ncsqrNCName[7]+"_");
//		m0.put("sqrNCName8",ncsqrNCName[8]+"_");
//		m0.put("sqrNCName9",ncsqrNCName[9]+"_");
		records.add(m0);
		for (int i = 0; i < (ncsqrSQRName.length<10?ncsqrSQRName.length:10); i++) {
			Map<String, Object> m = new HashMap<String, Object>(); 
			m.put("nc",RedisTest.getAddrNameByCode(ncsqrSQRName[i])+"("+ncsqrSQRName[i]+")");

			if(ncsqr0.length>i && !"".equals(ncsqr0[0])){
				m.put("sqrNCName0",ncsqr0[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("sqrNCName0","-_");
			}
			if(ncsqr1.length>i && !"".equals(ncsqr1[0])){
				m.put("sqrNCName1",ncsqr1[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("sqrNCName1","-_");
			}
			if(ncsqr2.length>i && !"".equals(ncsqr2[0])){
				m.put("sqrNCName2",ncsqr2[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("sqrNCName2","-_");
			}
			if(ncsqr3.length>i && !"".equals(ncsqr3[0])){
				m.put("sqrNCName3",ncsqr3[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("sqrNCName3","-_");
			}
			if(ncsqr4.length>i && !"".equals(ncsqr4[0])){
				m.put("sqrNCName4",ncsqr4[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("sqrNCName4","-_");
			}
			if(ncsqr5.length>i && !"".equals(ncsqr5[0])){
				m.put("sqrNCName5",ncsqr5[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("sqrNCName5","-_");
			}
			if(ncsqr6.length>i && !"".equals(ncsqr6[0])){
				m.put("sqrNCName6",ncsqr6[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("sqrNCName6","-_");
			}
			if(ncsqr7.length>i && !"".equals(ncsqr7[0])){
				m.put("sqrNCName7",ncsqr7[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("sqrNCName7","-_");
			}
			if(ncsqr8.length>i && !"".equals(ncsqr8[0])){
				m.put("sqrNCName8",ncsqr8[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("sqrNCName8","-_");
			}
			if(ncsqr9.length>i && !"".equals(ncsqr9[0])){
				m.put("sqrNCName9",ncsqr9[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("sqrNCName9","-_");
			}
//			m.put("sqrNCName0",ncsqr0[i]+"_"+ncsqrNCName[0]);
//			m.put("sqrNCName1",ncsqr1[i]+"_"+ncsqrNCName[1]);
//			m.put("sqrNCName2",ncsqr2[i]+"_"+ncsqrNCName[2]);
//			m.put("sqrNCName3",ncsqr3[i]+"_"+ncsqrNCName[3]);
//			m.put("sqrNCName4",ncsqr4[i]+"_"+ncsqrNCName[4]);
//			m.put("sqrNCName5",ncsqr5[i]+"_"+ncsqrNCName[5]);
//			m.put("sqrNCName6",ncsqr6[i]+"_"+ncsqrNCName[6]);
//			m.put("sqrNCName7",ncsqr7[i]+"_"+ncsqrNCName[7]);
//			m.put("sqrNCName8",ncsqr8[i]+"_"+ncsqrNCName[8]);
//			m.put("sqrNCName9",ncsqr9[i]+"_"+ncsqrNCName[9]);
			records.add(m);
		}
		map.put("total", total);
		map.put("rows", records);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取统计信息json 形成报表图形（发明人国省分析）
	 * 
	 * 
	 */
	public void getPatentSumData_famingrenNC() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> series = new ArrayList<Map>();
		List<String> categories = new ArrayList<String>();
		 String[] ss= jedis.get(user.getId()+"fmr").split("_");
		 String[] ssNC= jedis.get(user.getId()+"nc").split("_");
		 String temp=null;
		 String tempNC=null;
		 String[] strBurArrary=new String[10];   //不止一个地方用得着
		 String[] strNCBurArrary=new String[10];   //不止一个地方用得着
		 for(int i=0;i<(ssNC.length<10?ssNC.length:10);i++){
			 tempNC=ssNC[i];
			 strNCBurArrary[i]=tempNC.substring(0, tempNC.indexOf("="));
			 categories.add(RedisTest.getAddrNameByCode(strNCBurArrary[i])+"("+strNCBurArrary[i]+")");
		 }
		 for(int i=0;i<(ss.length<10?ss.length:10);i++){
		    temp=ss[i];
		    strBurArrary[i]=temp.substring(0, temp.indexOf("="));
			for(int j=0;j<10;j++){
				jedis.del(user.getId()+strBurArrary[i]+strNCBurArrary[j]);
			}
		 }
		 String[] inventorJly=jedis.get(user.getId()+"inventorJly").split("_");
		 inventorJly[inventorJly.length-1]=inventorJly[inventorJly.length-1].substring(0, inventorJly[inventorJly.length-1].length()-3);    ////删除末尾的Jly
		 String[] ncJly=jedis.get(user.getId()+"ncJly").split("_");
		 ncJly[ncJly.length-1]=ncJly[ncJly.length-1].substring(0, ncJly[ncJly.length-1].length()-3);    ////删除末尾的Jly
		for(int i=0;i<inventorJly.length;i++){
			String srtSQR=inventorJly[i];//获得发明人
			if((srtSQR!=null)&&(!"".equals(srtSQR))){    //发明人不空
				if(srtSQR.contains(",")){      //多个发明人
					String[] strs=srtSQR.split(",");
					for(int j=0;j<strs.length;j++){
						for(int k=0;k<(ss.length<10?ss.length:10);k++){   //发明人跟10个要统计的进行比对
							if(strBurArrary[k].equals(strs[j])){   //比对成功
								String strNC=ncJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
								if((strNC!=null)&&(!"".equals(strNC))){
									for(int ii=0;ii<(ssNC.length<10?ssNC.length:10);ii++){
										if(strNCBurArrary[ii].equals(strNC)){
											jedis.append(user.getId()+strBurArrary[k]+strNCBurArrary[ii], "1");
										}
									}
								}
							}
						}
					}
				}else{  //单个申请人
					for(int k=0;k<(ss.length<10?ss.length:10);k++){   //申请人跟10个要统计的进行比对
						if(strBurArrary[k].equals(srtSQR)){   //比对成功
							String strNC=ncJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
							if((strNC!=null)&&(!"".equals(strNC))){
								for(int ii=0;ii<(ssNC.length<10?ssNC.length:10);ii++){
									if(strNCBurArrary[ii].equals(strNC)){
										jedis.append(user.getId()+srtSQR+strNCBurArrary[ii], "1");
									}
								}
							}
						}
					}
				}
			}
		}
		StringBuilder sqrName=new StringBuilder();//申请人名称
		StringBuilder ncName=new StringBuilder();//nc名称
		StringBuilder sqrNcName0=new StringBuilder();
		StringBuilder sqrNcName1=new StringBuilder();
		StringBuilder sqrNcName2=new StringBuilder();
		StringBuilder sqrNcName3=new StringBuilder();
		StringBuilder sqrNcName4=new StringBuilder();
		StringBuilder sqrNcName5=new StringBuilder();
		StringBuilder sqrNcName6=new StringBuilder();
		StringBuilder sqrNcName7=new StringBuilder();
		StringBuilder sqrNcName8=new StringBuilder();
		StringBuilder sqrNcName9=new StringBuilder();
		for(int i=0;i<(ss.length<10?ss.length:10);i++){
			Map<String, Object> m = new HashMap<String, Object>();
			 List<BigInteger> data = new ArrayList<BigInteger>();
			 sqrName.append(strBurArrary[i]).append(",");
			 ncName.append(strNCBurArrary[i]).append(",");
			 for(int j=0;j<(ssNC.length<10?ssNC.length:10);j++){
				 Long  strLong=jedis.strlen(user.getId()+strBurArrary[i]+strNCBurArrary[j]);
				 data.add(BigInteger.valueOf(strLong));
				 if(i==0){
					 sqrNcName0.append(strLong).append(",");
				 }else if(i==1){
					 sqrNcName1.append(strLong).append(",");
				 }else if(i==2){
					 sqrNcName2.append(strLong).append(",");
				 }else if(i==3){
					 sqrNcName3.append(strLong).append(",");
				 }else if(i==4){
					 sqrNcName4.append(strLong).append(",");
				 }else if(i==5){
					 sqrNcName5.append(strLong).append(",");
				 }else if(i==6){
					 sqrNcName6.append(strLong).append(",");
				 }else if(i==7){
					 sqrNcName7.append(strLong).append(",");
				 }else if(i==8){
					 sqrNcName8.append(strLong).append(",");
				 }else if(i==9){
					 sqrNcName9.append(strLong).append(",");
				 }
			 }
			 m.put("name",strBurArrary[i]);
			 m.put("data", data);
			 series.add(m);
		}
		if(sqrName.length()>0){
			sqrName.deleteCharAt(sqrName.length()-1);
		}
		if(ncName.length()>0){
			ncName.deleteCharAt(ncName.length()-1);
		}
		if(sqrNcName0.length()>0){
			sqrNcName0.deleteCharAt(sqrNcName0.length()-1);
		}
		if(sqrNcName1.length()>0){
			sqrNcName1.deleteCharAt(sqrNcName1.length()-1);
		}
		if(sqrNcName2.length()>0){
			sqrNcName2.deleteCharAt(sqrNcName2.length()-1);
		}
		if(sqrNcName3.length()>0){
			sqrNcName3.deleteCharAt(sqrNcName3.length()-1);
		}
		if(sqrNcName4.length()>0){
			sqrNcName4.deleteCharAt(sqrNcName4.length()-1);
		}
		if(sqrNcName5.length()>0){
			sqrNcName5.deleteCharAt(sqrNcName5.length()-1);
		}
		if(sqrNcName6.length()>0){
			sqrNcName6.deleteCharAt(sqrNcName6.length()-1);
		}
		if(sqrNcName7.length()>0){
			sqrNcName7.deleteCharAt(sqrNcName7.length()-1);
		}
		if(sqrNcName8.length()>0){
			sqrNcName8.deleteCharAt(sqrNcName8.length()-1);
		}
		if(sqrNcName9.length()>0){
			sqrNcName9.deleteCharAt(sqrNcName9.length()-1);
		}
		 jedis.set(user.getId()+"sqrNcSQRName", sqrName.toString());
		 jedis.expire(user.getId()+"sqrNcSQRName", 10000);
		 jedis.set(user.getId()+"sqrNcNCName", ncName.toString());
		 jedis.expire(user.getId()+"sqrNcNCName", 10000);
		 jedis.set(user.getId()+"sqrNc0", sqrNcName0.toString());
		 jedis.expire(user.getId()+"sqrNc0", 10000);
		 jedis.set(user.getId()+"sqrNc1", sqrNcName1.toString());
		 jedis.expire(user.getId()+"sqrNc1", 10000);
		 jedis.set(user.getId()+"sqrNc2", sqrNcName2.toString());
		 jedis.expire(user.getId()+"sqrNc2", 10000);
		 jedis.set(user.getId()+"sqrNc3", sqrNcName3.toString());
		 jedis.expire(user.getId()+"sqrNc3", 10000);
		 jedis.set(user.getId()+"sqrNc4", sqrNcName4.toString());
		 jedis.expire(user.getId()+"sqrNc4", 10000);
		 jedis.set(user.getId()+"sqrNc5", sqrNcName5.toString());
		 jedis.expire(user.getId()+"sqrNc5", 10000);
		 jedis.set(user.getId()+"sqrNc6", sqrNcName6.toString());
		 jedis.expire(user.getId()+"sqrNc6", 10000);
		 jedis.set(user.getId()+"sqrNc7", sqrNcName7.toString());
		 jedis.expire(user.getId()+"sqrNc7", 10000);
		 jedis.set(user.getId()+"sqrNc8", sqrNcName8.toString());
		 jedis.expire(user.getId()+"sqrNc8", 10000);
		 jedis.set(user.getId()+"sqrNc9", sqrNcName9.toString());
		 jedis.expire(user.getId()+"sqrNc9", 10000);
		map.put("series", series);
		map.put("categories", categories);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取统计信息json 形成表格（发明人国省）
	 */
	public void getPatentSumData_dataGrid_fmrNC() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] ncsqrNCName=jedis.get(user.getId()+"sqrNcSQRName").split(",");
//		String[] ncsqrSQRName=jedis.get(user.getId()+"sqrNcNCName").split(",");
		List<String> categories = new ArrayList<String>();
		String[] ssNC = jedis.get(user.getId() + "nc").split("_");
		String tempNC = null;
		String[] ncsqrSQRName = new String[ssNC.length < 10 ? ssNC.length : 10]; // 不止一个地方用得着
		for (int i = 0; i < (ssNC.length < 10 ? ssNC.length : 10); i++) {
			tempNC = ssNC[i];
			ncsqrSQRName[i] = tempNC.substring(0, tempNC.indexOf("="));
			categories.add(RedisTest.getAddrNameByCode(ncsqrSQRName[i]) + "("
					+ ncsqrSQRName[i] + ")");
		}
		String[] ncsqr0=jedis.get(user.getId()+"sqrNc0").split(",");
		String[] ncsqr1=jedis.get(user.getId()+"sqrNc1").split(",");
		String[] ncsqr2=jedis.get(user.getId()+"sqrNc2").split(",");
		String[] ncsqr3=jedis.get(user.getId()+"sqrNc3").split(",");
		String[] ncsqr4=jedis.get(user.getId()+"sqrNc4").split(",");
		String[] ncsqr5=jedis.get(user.getId()+"sqrNc5").split(",");
		String[] ncsqr6=jedis.get(user.getId()+"sqrNc6").split(",");
		String[] ncsqr7=jedis.get(user.getId()+"sqrNc7").split(",");
		String[] ncsqr8=jedis.get(user.getId()+"sqrNc8").split(",");
		String[] ncsqr9=jedis.get(user.getId()+"sqrNc9").split(",");
		List<Map> records = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		total=new Long((long)ncsqr0.length);
		Map<String, Object> m0 = new HashMap<String, Object>(); 
		m0.put("nc", "国省");
		for(int j=0;j<ncsqrNCName.length;j++){
			m0.put("sqrNCName"+j,ncsqrNCName[j]+"_");
		}
		for(int j=9;j>=ncsqrNCName.length;j--){
			m0.put("sqrNCName"+j,"_");
		}
//		m0.put("sqrNCName0",ncsqrNCName[0]+"_");
//		m0.put("sqrNCName1",ncsqrNCName[1]+"_");
//		m0.put("sqrNCName2",ncsqrNCName[2]+"_");
//		m0.put("sqrNCName3",ncsqrNCName[3]+"_");
//		m0.put("sqrNCName4",ncsqrNCName[4]+"_");
//		m0.put("sqrNCName5",ncsqrNCName[5]+"_");
//		m0.put("sqrNCName6",ncsqrNCName[6]+"_");
//		m0.put("sqrNCName7",ncsqrNCName[7]+"_");
//		m0.put("sqrNCName8",ncsqrNCName[8]+"_");
//		m0.put("sqrNCName9",ncsqrNCName[9]+"_");
		records.add(m0);
		for (int i = 0; i < (ncsqrSQRName.length<10?ncsqrSQRName.length:10); i++) {
			Map<String, Object> m = new HashMap<String, Object>(); 
			m.put("nc",RedisTest.getAddrNameByCode(ncsqrSQRName[i])+"("+ncsqrSQRName[i]+")");
			if(ncsqr0.length>i && !"".equals(ncsqr0[0])){
				m.put("sqrNCName0",ncsqr0[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("sqrNCName0","-_");
			}
			if(ncsqr1.length>i && !"".equals(ncsqr1[0])){
				m.put("sqrNCName1",ncsqr1[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("sqrNCName1","-_");
			}
			if(ncsqr2.length>i && !"".equals(ncsqr2[0])){
				m.put("sqrNCName2",ncsqr2[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("sqrNCName2","-_");
			}
			if(ncsqr3.length>i && !"".equals(ncsqr3[0])){
				m.put("sqrNCName3",ncsqr3[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("sqrNCName3","-_");
			}
			if(ncsqr4.length>i && !"".equals(ncsqr4[0])){
				m.put("sqrNCName4",ncsqr4[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("sqrNCName4","-_");
			}
			if(ncsqr5.length>i && !"".equals(ncsqr5[0])){
				m.put("sqrNCName5",ncsqr5[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("sqrNCName5","-_");
			}
			if(ncsqr6.length>i && !"".equals(ncsqr6[0])){
				m.put("sqrNCName6",ncsqr6[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("sqrNCName6","-_");
			}
			if(ncsqr7.length>i && !"".equals(ncsqr7[0])){
				m.put("sqrNCName7",ncsqr7[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("sqrNCName7","-_");
			}
			if(ncsqr8.length>i && !"".equals(ncsqr8[0])){
				m.put("sqrNCName8",ncsqr8[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("sqrNCName8","-_");
			}
			if(ncsqr9.length>i && !"".equals(ncsqr9[0])){
				m.put("sqrNCName9",ncsqr9[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("sqrNCName9","-_");
			}
//			m.put("sqrNCName0",ncsqr0[i]+"_"+ncsqrNCName[0]);
//			m.put("sqrNCName1",ncsqr1[i]+"_"+ncsqrNCName[1]);
//			m.put("sqrNCName2",ncsqr2[i]+"_"+ncsqrNCName[2]);
//			m.put("sqrNCName3",ncsqr3[i]+"_"+ncsqrNCName[3]);
//			m.put("sqrNCName4",ncsqr4[i]+"_"+ncsqrNCName[4]);
//			m.put("sqrNCName5",ncsqr5[i]+"_"+ncsqrNCName[5]);
//			m.put("sqrNCName6",ncsqr6[i]+"_"+ncsqrNCName[6]);
//			m.put("sqrNCName7",ncsqr7[i]+"_"+ncsqrNCName[7]);
//			m.put("sqrNCName8",ncsqr8[i]+"_"+ncsqrNCName[8]);
//			m.put("sqrNCName9",ncsqr9[i]+"_"+ncsqrNCName[9]);
			records.add(m);
		}
		map.put("total", total);
		map.put("rows", records);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取统计信息json 形成报表图形（技术分类国省分析）
	 * 
	 * 
	 */
	public void getPatentSumData_ipcNC() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> series = new ArrayList<Map>();
		List<String> categories = new ArrayList<String>();
		String[] ss = jedis.get(user.getId() + "ipc").split("_");
		String[] ssNC = jedis.get(user.getId() + "nc").split("_");
		String temp = null;
		String tempNC = null;
		String[] strBurArrary = new String[10]; // 不止一个地方用得着
		String[] strNCBurArrary = new String[10]; // 不止一个地方用得着
		for (int i = 0; i < (ssNC.length < 10 ? ssNC.length : 10); i++) {
			tempNC = ssNC[i];
			strNCBurArrary[i] = tempNC.substring(0, tempNC.indexOf("="));
			categories.add(RedisTest.getAddrNameByCode(strNCBurArrary[i]) + "("
					+ strNCBurArrary[i] + ")");
		}
		 for(int i=0;i<(ss.length<10?ss.length:10);i++){
		    temp=ss[i];
		    strBurArrary[i]=temp.substring(0, temp.indexOf("="));
			for(int j=0;j<10;j++){
				jedis.del(user.getId()+strBurArrary[i]+strNCBurArrary[j]);
			}
		 }
		 String[] ipcJly=jedis.get(user.getId()+"ipcJly").split("_");
		 ipcJly[ipcJly.length-1]=ipcJly[ipcJly.length-1].substring(0, ipcJly[ipcJly.length-1].length()-3);    ////删除末尾的Jly
		 String[] ncJly=jedis.get(user.getId()+"ncJly").split("_");
		 ncJly[ncJly.length-1]=ncJly[ncJly.length-1].substring(0, ncJly[ncJly.length-1].length()-3);    ////删除末尾的Jly
		for(int i=0;i<ipcJly.length;i++){
			String srtSQR=ipcJly[i];//获得技术分类
			if((srtSQR!=null)&&(!"".equals(srtSQR))){    //发明人不空
				for(int k=0;k<(ss.length<10?ss.length:10);k++){   //申请人跟10个要统计的进行比对
					if(strBurArrary[k].equals(srtSQR)){   //比对成功
						String strNC=ncJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
						if((strNC!=null)&&(!"".equals(strNC))){
							for(int ii=0;ii<(ssNC.length<10?ssNC.length:10);ii++){
								if(strNCBurArrary[ii].equals(strNC)){
									jedis.append(user.getId()+srtSQR+strNCBurArrary[ii], "1");
								}
							}
						}
					}
				}
			}
		}
		StringBuilder sqrName=new StringBuilder();//申请人名称
		StringBuilder ncName=new StringBuilder();//nc名称
		StringBuilder sqrNcName0=new StringBuilder();
		StringBuilder sqrNcName1=new StringBuilder();
		StringBuilder sqrNcName2=new StringBuilder();
		StringBuilder sqrNcName3=new StringBuilder();
		StringBuilder sqrNcName4=new StringBuilder();
		StringBuilder sqrNcName5=new StringBuilder();
		StringBuilder sqrNcName6=new StringBuilder();
		StringBuilder sqrNcName7=new StringBuilder();
		StringBuilder sqrNcName8=new StringBuilder();
		StringBuilder sqrNcName9=new StringBuilder();
		for(int i=0;i<(ss.length<10?ss.length:10);i++){
			Map<String, Object> m = new HashMap<String, Object>();
			 List<BigInteger> data = new ArrayList<BigInteger>();
			 sqrName.append(strBurArrary[i]).append(",");
			 ncName.append(strNCBurArrary[i]).append(",");
			 for(int j=0;j<(ssNC.length<10?ssNC.length:10);j++){
				 Long  strLong=jedis.strlen(user.getId()+strBurArrary[i]+strNCBurArrary[j]);
				 data.add(BigInteger.valueOf(strLong));
				 if(i==0){
					 sqrNcName0.append(strLong).append(",");
				 }else if(i==1){
					 sqrNcName1.append(strLong).append(",");
				 }else if(i==2){
					 sqrNcName2.append(strLong).append(",");
				 }else if(i==3){
					 sqrNcName3.append(strLong).append(",");
				 }else if(i==4){
					 sqrNcName4.append(strLong).append(",");
				 }else if(i==5){
					 sqrNcName5.append(strLong).append(",");
				 }else if(i==6){
					 sqrNcName6.append(strLong).append(",");
				 }else if(i==7){
					 sqrNcName7.append(strLong).append(",");
				 }else if(i==8){
					 sqrNcName8.append(strLong).append(",");
				 }else if(i==9){
					 sqrNcName9.append(strLong).append(",");
				 }
			 }
			 m.put("name",strBurArrary[i]);
			 m.put("data", data);
			 series.add(m);
		}
		if(sqrName.length()>0){
			sqrName.deleteCharAt(sqrName.length()-1);
		}
		if(ncName.length()>0){
			ncName.deleteCharAt(ncName.length()-1);
		}
		if(sqrNcName0.length()>0){
			sqrNcName0.deleteCharAt(sqrNcName0.length()-1);
		}
		if(sqrNcName1.length()>0){
			sqrNcName1.deleteCharAt(sqrNcName1.length()-1);
		}
		if(sqrNcName2.length()>0){
			sqrNcName2.deleteCharAt(sqrNcName2.length()-1);
		}
		if(sqrNcName3.length()>0){
			sqrNcName3.deleteCharAt(sqrNcName3.length()-1);
		}
		if(sqrNcName4.length()>0){
			sqrNcName4.deleteCharAt(sqrNcName4.length()-1);
		}
		if(sqrNcName5.length()>0){
			sqrNcName5.deleteCharAt(sqrNcName5.length()-1);
		}
		if(sqrNcName6.length()>0){
			sqrNcName6.deleteCharAt(sqrNcName6.length()-1);
		}
		if(sqrNcName7.length()>0){
			sqrNcName7.deleteCharAt(sqrNcName7.length()-1);
		}
		if(sqrNcName8.length()>0){
			sqrNcName8.deleteCharAt(sqrNcName8.length()-1);
		}
		if(sqrNcName9.length()>0){
			sqrNcName9.deleteCharAt(sqrNcName9.length()-1);
		}
		 jedis.set(user.getId()+"ipcNcIPCName", sqrName.toString());
		 jedis.expire(user.getId()+"ipcNcIPCName", 10000);
		 jedis.set(user.getId()+"ipcNcNCName", ncName.toString());
		 jedis.expire(user.getId()+"ipcNcNCName", 10000);
		 jedis.set(user.getId()+"ipcNc0", sqrNcName0.toString());
		 jedis.expire(user.getId()+"ipcNc0", 10000);
		 jedis.set(user.getId()+"ipcNc1", sqrNcName1.toString());
		 jedis.expire(user.getId()+"ipcNc1", 10000);
		 jedis.set(user.getId()+"ipcNc2", sqrNcName2.toString());
		 jedis.expire(user.getId()+"ipcNc2", 10000);
		 jedis.set(user.getId()+"ipcNc3", sqrNcName3.toString());
		 jedis.expire(user.getId()+"ipcNc3", 10000);
		 jedis.set(user.getId()+"ipcNc4", sqrNcName4.toString());
		 jedis.expire(user.getId()+"ipcNc4", 10000);
		 jedis.set(user.getId()+"ipcNc5", sqrNcName5.toString());
		 jedis.expire(user.getId()+"ipcNc5", 10000);
		 jedis.set(user.getId()+"ipcNc6", sqrNcName6.toString());
		 jedis.expire(user.getId()+"ipcNc6", 10000);
		 jedis.set(user.getId()+"ipcNc7", sqrNcName7.toString());
		 jedis.expire(user.getId()+"ipcNc7", 10000);
		 jedis.set(user.getId()+"ipcNc8", sqrNcName8.toString());
		 jedis.expire(user.getId()+"ipcNc8", 10000);
		 jedis.set(user.getId()+"ipcNc9", sqrNcName9.toString());
		 jedis.expire(user.getId()+"ipcNc9", 10000);
		map.put("series", series);
		map.put("categories", categories);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取统计信息json 形成表格（ipc国省）
	 * @throws InterruptedException 
	 */
	public void getPatentSumData_dataGrid_ipcNC() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] ncsqrNCName=jedis.get(user.getId()+"ipcNcIPCName").split(",");
//		String[] ncsqrSQRName=jedis.get(user.getId()+"ipcNcNCName").split(",");
		List<String> categories = new ArrayList<String>();
		String[] ssNC = jedis.get(user.getId() + "nc").split("_");
		String tempNC = null;
		String[] ncsqrSQRName = new String[ssNC.length < 10 ? ssNC.length : 10]; // 不止一个地方用得着
		for (int i = 0; i < (ssNC.length < 10 ? ssNC.length : 10); i++) {
			tempNC = ssNC[i];
			ncsqrSQRName[i] = tempNC.substring(0, tempNC.indexOf("="));
			categories.add(RedisTest.getAddrNameByCode(ncsqrSQRName[i]) + "("
					+ ncsqrSQRName[i] + ")");
		}
		StringBuilder sqrNcName0=new StringBuilder();
		StringBuilder sqrNcName1=new StringBuilder();
		StringBuilder sqrNcName2=new StringBuilder();
		StringBuilder sqrNcName3=new StringBuilder();
		StringBuilder sqrNcName4=new StringBuilder();
		StringBuilder sqrNcName5=new StringBuilder();
		StringBuilder sqrNcName6=new StringBuilder();
		StringBuilder sqrNcName7=new StringBuilder();
		StringBuilder sqrNcName8=new StringBuilder();
		StringBuilder sqrNcName9=new StringBuilder();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		for(int i=0;i<(ncsqrNCName.length<10?ncsqrNCName.length:10);i++){
			 for(int j=0;j<(ncsqrSQRName.length<10?ncsqrSQRName.length:10);j++){
				 Long  strLong=jedis.strlen(user.getId()+ncsqrNCName[i]+ncsqrSQRName[j]);
				 if(i==0){
					 sqrNcName0.append(strLong).append(",");
				 }else if(i==1){
					 sqrNcName1.append(strLong).append(",");
				 }else if(i==2){
					 sqrNcName2.append(strLong).append(",");
				 }else if(i==3){
					 sqrNcName3.append(strLong).append(",");
				 }else if(i==4){
					 sqrNcName4.append(strLong).append(",");
				 }else if(i==5){
					 sqrNcName5.append(strLong).append(",");
				 }else if(i==6){
					 sqrNcName6.append(strLong).append(",");
				 }else if(i==7){
					 sqrNcName7.append(strLong).append(",");
				 }else if(i==8){
					 sqrNcName8.append(strLong).append(",");
				 }else if(i==9){
					 sqrNcName9.append(strLong).append(",");
				 }
			 }
		}
		
		String[] ncsqr0=sqrNcName0.toString().split(",");
		String[] ncsqr1=sqrNcName1.toString().split(",");
		String[] ncsqr2=sqrNcName2.toString().split(",");
		String[] ncsqr3=sqrNcName3.toString().split(",");
		String[] ncsqr4=sqrNcName4.toString().split(",");
		String[] ncsqr5=sqrNcName5.toString().split(",");
		String[] ncsqr6=sqrNcName6.toString().split(",");
		String[] ncsqr7=sqrNcName7.toString().split(",");
		String[] ncsqr8=sqrNcName8.toString().split(",");
		String[] ncsqr9=sqrNcName9.toString().split(",");
		
		
		
//		String[] ncsqr0=jedis.get(user.getId()+"sqrNc0").split(",");
//		String[] ncsqr1=jedis.get(user.getId()+"sqrNc1").split(",");
//		String[] ncsqr2=jedis.get(user.getId()+"sqrNc2").split(",");
//		String[] ncsqr3=jedis.get(user.getId()+"sqrNc3").split(",");
//		String[] ncsqr4=jedis.get(user.getId()+"sqrNc4").split(",");
//		String[] ncsqr5=jedis.get(user.getId()+"sqrNc5").split(",");
//		String[] ncsqr6=jedis.get(user.getId()+"sqrNc6").split(",");
//		String[] ncsqr7=jedis.get(user.getId()+"sqrNc7").split(",");
//		String[] ncsqr8=jedis.get(user.getId()+"sqrNc8").split(",");
//		String[] ncsqr9=jedis.get(user.getId()+"sqrNc9").split(",");
		List<Map> records = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		total=new Long((long)ncsqr0.length);
		Map<String, Object> m0 = new HashMap<String, Object>(); 
		m0.put("nc", "国省");
		for(int j=0;j<ncsqrNCName.length;j++){
			m0.put("ipcNCName"+j,ncsqrNCName[j]+"_");
		}
		for(int j=9;j>=ncsqrNCName.length;j--){
			m0.put("ipcNCName"+j,"_");
		}
//		m0.put("ipcNCName0",ncsqrNCName[0]+"_");
//		m0.put("ipcNCName1",ncsqrNCName[1]+"_");
//		m0.put("ipcNCName2",ncsqrNCName[2]+"_");
//		m0.put("ipcNCName3",ncsqrNCName[3]+"_");
//		m0.put("ipcNCName4",ncsqrNCName[4]+"_");
//		m0.put("ipcNCName5",ncsqrNCName[5]+"_");
//		m0.put("ipcNCName6",ncsqrNCName[6]+"_");
//		m0.put("ipcNCName7",ncsqrNCName[7]+"_");
//		m0.put("ipcNCName8",ncsqrNCName[8]+"_");
//		m0.put("ipcNCName9",ncsqrNCName[9]+"_");
		records.add(m0);
		for (int i = 0; i < (ncsqrSQRName.length<10?ncsqrSQRName.length:10); i++) {
			Map<String, Object> m = new HashMap<String, Object>(); 
			m.put("nc",RedisTest.getAddrNameByCode(ncsqrSQRName[i])+"("+ncsqrSQRName[i]+")");
			if(ncsqr0.length>i && !"".equals(ncsqr0[0])){
				m.put("ipcNCName0",ncsqr0[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("ipcNCName0","-_");
			}
			if(ncsqr1.length>i && !"".equals(ncsqr1[0])){
				m.put("ipcNCName1",ncsqr1[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("ipcNCName1","-_");
			}
			if(ncsqr2.length>i && !"".equals(ncsqr2[0])){
				m.put("ipcNCName2",ncsqr2[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("ipcNCName2","-_");
			}
			if(ncsqr3.length>i && !"".equals(ncsqr3[0])){
				m.put("ipcNCName3",ncsqr3[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("ipcNCName3","-_");
			}
			if(ncsqr4.length>i && !"".equals(ncsqr4[0])){
				m.put("ipcNCName4",ncsqr4[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("ipcNCName4","-_");
			}
			if(ncsqr5.length>i && !"".equals(ncsqr5[0])){
				m.put("ipcNCName5",ncsqr5[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("ipcNCName5","-_");
			}
			if(ncsqr6.length>i && !"".equals(ncsqr6[0])){
				m.put("ipcNCName6",ncsqr6[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("ipcNCName6","-_");
			}
			if(ncsqr7.length>i && !"".equals(ncsqr7[0])){
				m.put("ipcNCName7",ncsqr7[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("ipcNCName7","-_");
			}
			if(ncsqr8.length>i && !"".equals(ncsqr8[0])){
				m.put("ipcNCName8",ncsqr8[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("ipcNCName8","-_");
			}
			if(ncsqr9.length>i && !"".equals(ncsqr9[0])){
				m.put("ipcNCName9",ncsqr9[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("ipcNCName9","-_");
			}
//			m.put("ipcNCName0",ncsqr0[i]+"_"+ncsqrNCName[0]);
//			m.put("ipcNCName1",ncsqr1[i]+"_"+ncsqrNCName[1]);
//			m.put("ipcNCName2",ncsqr2[i]+"_"+ncsqrNCName[2]);
//			m.put("ipcNCName3",ncsqr3[i]+"_"+ncsqrNCName[3]);
//			m.put("ipcNCName4",ncsqr4[i]+"_"+ncsqrNCName[4]);
//			m.put("ipcNCName5",ncsqr5[i]+"_"+ncsqrNCName[5]);
//			m.put("ipcNCName6",ncsqr6[i]+"_"+ncsqrNCName[6]);
//			m.put("ipcNCName7",ncsqr7[i]+"_"+ncsqrNCName[7]);
//			m.put("ipcNCName8",ncsqr8[i]+"_"+ncsqrNCName[8]);
//			m.put("ipcNCName9",ncsqr9[i]+"_"+ncsqrNCName[9]);
			records.add(m);
		}
		map.put("total", total);
		map.put("rows", records);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取统计信息json 形成报表图形（国省技术分析）
	 * 
	 * 
	 */
	public void getPatentSumData_ncIPC() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> series = new ArrayList<Map>();
		List<String> categories = new ArrayList<String>();
		 String[] ss= jedis.get(user.getId()+"nc").split("_");
		 String[] ssIPC= jedis.get(user.getId()+"ipc").split("_");
		 String temp=null;
		 String tempIPC=null;
		 String[] strBurArrary=new String[10];   //不止一个地方用得着
		 String[] strIPCBurArrary=new String[10];   //不止一个地方用得着
		 for(int i=0;i<(ssIPC.length<10?ssIPC.length:10);i++){
			 tempIPC=ssIPC[i];
			 strIPCBurArrary[i]=tempIPC.substring(0, tempIPC.indexOf("="));
			 categories.add(strIPCBurArrary[i]);
		 }
		 for(int i=0;i<(ss.length<10?ss.length:10);i++){
			 temp=ss[i];
			 if(temp!=null&&!"".equals(temp)){
				    strBurArrary[i]=temp.substring(0, temp.indexOf("="));
					for(int j=0;j<(ssIPC.length<10?ssIPC.length:10);j++){
						jedis.del(user.getId()+strBurArrary[i]+strIPCBurArrary[j]);
					}
				}
		 }
		 String[] ncJly=jedis.get(user.getId()+"ncJly").split("_");
		 ncJly[ncJly.length-1]=ncJly[ncJly.length-1].substring(0, ncJly[ncJly.length-1].length()-3);    ////删除末尾的Jly
		 String[] ipcJly=jedis.get(user.getId()+"ipcJly").split("_");
		 ipcJly[ipcJly.length-1]=ipcJly[ipcJly.length-1].substring(0, ipcJly[ipcJly.length-1].length()-3);    ////删除末尾的Jly
		for(int i=0;i<ncJly.length;i++){
			String strNC=ncJly[i];//获得nc
			if((strNC!=null)&&(!"".equals(strNC))){    //nc不空
				for(int k=0;k<(ss.length<10?ss.length:10);k++){   //申请人跟10个要统计的进行比对
					if(strBurArrary[k].equals(strNC)){   //比对成功
						String strIPC=ipcJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
						if((strIPC!=null)&&(!"".equals(strIPC))){
							for(int ii=0;ii<(ssIPC.length<10?ssIPC.length:10);ii++){
								if(strIPCBurArrary[ii].equals(strIPC)){
									jedis.append(user.getId()+strNC+strIPCBurArrary[ii], "1");
								}
							}
						}
					}
				}
			}
		}
		StringBuilder ncName=new StringBuilder();//国省名称
		StringBuilder ipcName=new StringBuilder();//ipc名称
		StringBuilder ncIpcName0=new StringBuilder();
		StringBuilder ncIpcName1=new StringBuilder();
		StringBuilder ncIpcName2=new StringBuilder();
		StringBuilder ncIpcName3=new StringBuilder();
		StringBuilder ncIpcName4=new StringBuilder();
		StringBuilder ncIpcName5=new StringBuilder();
		StringBuilder ncIpcName6=new StringBuilder();
		StringBuilder ncIpcName7=new StringBuilder();
		StringBuilder ncIpcName8=new StringBuilder();
		StringBuilder ncIpcName9=new StringBuilder();
		for(int i=0;i<(ss.length<10?ss.length:10);i++){
			Map<String, Object> m = new HashMap<String, Object>();
			 List<BigInteger> data = new ArrayList<BigInteger>();
			 ncName.append(strBurArrary[i]).append(",");
			 ipcName.append(strIPCBurArrary[i]).append(",");
			 for(int j=0;j<(ssIPC.length<10?ssIPC.length:10);j++){
				 Long  strLong=jedis.strlen(user.getId()+strBurArrary[i]+strIPCBurArrary[j]);
				 data.add(BigInteger.valueOf(strLong));
				 if(i==0){
					 ncIpcName0.append(strLong).append(",");
				 }else if(i==1){
					 ncIpcName1.append(strLong).append(",");
				 }else if(i==2){
					 ncIpcName2.append(strLong).append(",");
				 }else if(i==3){
					 ncIpcName3.append(strLong).append(",");
				 }else if(i==4){
					 ncIpcName4.append(strLong).append(",");
				 }else if(i==5){
					 ncIpcName5.append(strLong).append(",");
				 }else if(i==6){
					 ncIpcName6.append(strLong).append(",");
				 }else if(i==7){
					 ncIpcName7.append(strLong).append(",");
				 }else if(i==8){
					 ncIpcName8.append(strLong).append(",");
				 }else if(i==9){
					 ncIpcName9.append(strLong).append(",");
				 }
			 }
			 m.put("name",RedisTest.getAddrNameByCode(strBurArrary[i]));
			 m.put("data", data);
			 series.add(m);
		}
		if(ncName.length()>0){
			ncName.deleteCharAt(ncName.length()-1);
		}
		if(ipcName.length()>0){
			ipcName.deleteCharAt(ipcName.length()-1);
		}
		if(ncIpcName0.length()>0){
			ncIpcName0.deleteCharAt(ncIpcName0.length()-1);
		}
		if(ncIpcName1.length()>0){
			ncIpcName1.deleteCharAt(ncIpcName1.length()-1);
		}
		if(ncIpcName2.length()>0){
			ncIpcName2.deleteCharAt(ncIpcName2.length()-1);
		}
		if(ncIpcName3.length()>0){
			ncIpcName3.deleteCharAt(ncIpcName3.length()-1);
		}
		if(ncIpcName4.length()>0){
			ncIpcName4.deleteCharAt(ncIpcName4.length()-1);
		}
		if(ncIpcName5.length()>0){
			ncIpcName5.deleteCharAt(ncIpcName5.length()-1);
		}
		if(ncIpcName6.length()>0){
			ncIpcName6.deleteCharAt(ncIpcName6.length()-1);
		}
		if(ncIpcName7.length()>0){
			ncIpcName7.deleteCharAt(ncIpcName7.length()-1);
		}
		if(ncIpcName8.length()>0){
			ncIpcName8.deleteCharAt(ncIpcName8.length()-1);
		}
		if(ncIpcName9.length()>0){
			ncIpcName9.deleteCharAt(ncIpcName9.length()-1);
		}
		 jedis.set(user.getId()+"ncipcNCName", ncName.toString());
		 jedis.expire(user.getId()+"ncipcNCName", 10000);
		 jedis.set(user.getId()+"ncipcIPCName", ipcName.toString());
		 jedis.expire(user.getId()+"ncipcIPCName", 10000);
		 jedis.set(user.getId()+"ncipc0", ncIpcName0.toString());
		 jedis.expire(user.getId()+"ncipc0", 10000);
		 jedis.set(user.getId()+"ncipc1", ncIpcName1.toString());
		 jedis.expire(user.getId()+"ncipc1", 10000);
		 jedis.set(user.getId()+"ncipc2", ncIpcName2.toString());
		 jedis.expire(user.getId()+"ncipc2", 10000);
		 jedis.set(user.getId()+"ncipc3", ncIpcName3.toString());
		 jedis.expire(user.getId()+"ncipc3", 10000);
		 jedis.set(user.getId()+"ncipc4", ncIpcName4.toString());
		 jedis.expire(user.getId()+"ncipc4", 10000);
		 jedis.set(user.getId()+"ncipc5", ncIpcName5.toString());
		 jedis.expire(user.getId()+"ncipc5", 10000);
		 jedis.set(user.getId()+"ncipc6", ncIpcName6.toString());
		 jedis.expire(user.getId()+"ncipc6", 10000);
		 jedis.set(user.getId()+"ncipc7", ncIpcName7.toString());
		 jedis.expire(user.getId()+"ncipc7", 10000);
		 jedis.set(user.getId()+"ncipc8", ncIpcName8.toString());
		 jedis.expire(user.getId()+"ncipc8", 10000);
		 jedis.set(user.getId()+"ncipc9", ncIpcName9.toString());
		 jedis.expire(user.getId()+"ncipc9", 10000);
		map.put("series", series);
		map.put("categories", categories);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取统计信息json 形成表格（国省技术分类）
	 */
	public void getPatentSumData_dataGrid_ncIPC() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] ncsqrNCName=jedis.get(user.getId()+"ncipcNCName").split(",");
//		String[] ncsqrSQRName=jedis.get(user.getId()+"ncipcIPCName").split(",");

		List<String> categories = new ArrayList<String>();
		String[] ssIPC= jedis.get(user.getId()+"ipc").split("_");
		String tempIPC=null;
		String[] ncsqrSQRName=new String[ssIPC.length<10?ssIPC.length:10];   //不止一个地方用得着
		for(int i=0;i<(ssIPC.length<10?ssIPC.length:10);i++){
			 tempIPC=ssIPC[i];
			 ncsqrSQRName[i]=tempIPC.substring(0, tempIPC.indexOf("="));
			 categories.add(ncsqrSQRName[i]);
		}
		String[] ncsqr0=jedis.get(user.getId()+"ncipc0").split(",");
		String[] ncsqr1=jedis.get(user.getId()+"ncipc1").split(",");
		String[] ncsqr2=jedis.get(user.getId()+"ncipc2").split(",");
		String[] ncsqr3=jedis.get(user.getId()+"ncipc3").split(",");
		String[] ncsqr4=jedis.get(user.getId()+"ncipc4").split(",");
		String[] ncsqr5=jedis.get(user.getId()+"ncipc5").split(",");
		String[] ncsqr6=jedis.get(user.getId()+"ncipc6").split(",");
		String[] ncsqr7=jedis.get(user.getId()+"ncipc7").split(",");
		String[] ncsqr8=jedis.get(user.getId()+"ncipc8").split(",");
		String[] ncsqr9=jedis.get(user.getId()+"ncipc9").split(",");
		List<Map> records = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		total=new Long((long)ncsqr0.length);
		Map<String, Object> m0 = new HashMap<String, Object>(); 
		m0.put("ipc", "技术分类");
		for(int j=0;j<ncsqrNCName.length;j++){
			m0.put("ncipcName"+j,RedisTest.getAddrNameByCode(ncsqrNCName[j])+"("+ncsqrNCName[j]+")_");
		}
		for(int j=9;j>=ncsqrNCName.length;j--){
			m0.put("ncipcName"+j,RedisTest.getAddrNameByCode("_"));
		}
//		m0.put("ncipcName0",RedisTest.getAddrNameByCode(ncsqrNCName[0])+"("+ncsqrNCName[0]+")_");
//		m0.put("ncipcName1",RedisTest.getAddrNameByCode(ncsqrNCName[1])+"("+ncsqrNCName[1]+")_");
//		m0.put("ncipcName2",RedisTest.getAddrNameByCode(ncsqrNCName[2])+"("+ncsqrNCName[2]+")_");
//		m0.put("ncipcName3",RedisTest.getAddrNameByCode(ncsqrNCName[3])+"("+ncsqrNCName[3]+")_");
//		m0.put("ncipcName4",RedisTest.getAddrNameByCode(ncsqrNCName[4])+"("+ncsqrNCName[4]+")_");
//		m0.put("ncipcName5",RedisTest.getAddrNameByCode(ncsqrNCName[5])+"("+ncsqrNCName[5]+")_");
//		m0.put("ncipcName6",RedisTest.getAddrNameByCode(ncsqrNCName[6])+"("+ncsqrNCName[6]+")_");
//		m0.put("ncipcName7",RedisTest.getAddrNameByCode(ncsqrNCName[7])+"("+ncsqrNCName[7]+")_");
//		m0.put("ncipcName8",RedisTest.getAddrNameByCode(ncsqrNCName[8])+"("+ncsqrNCName[8]+")_");
//		m0.put("ncipcName9",RedisTest.getAddrNameByCode(ncsqrNCName[9])+"("+ncsqrNCName[9]+")_");
		records.add(m0);
		for (int i = 0; i < ncsqrSQRName.length; i++) {
			Map<String, Object> m = new HashMap<String, Object>(); 
			m.put("ipc", ncsqrSQRName[i]);
			if(ncsqr0.length>i && !"".equals(ncsqr0[0])){
				m.put("ncipcName0",ncsqr0[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("ncipcName0","-_");
			}
			if(ncsqr1.length>i && !"".equals(ncsqr1[0]) ){
				m.put("ncipcName1",ncsqr1[i]+"_"+ncsqrNCName[1]);
			} else{
				m.put("ncipcName1","-_");
			}
			if (ncsqr2.length > i && !"".equals(ncsqr2[0]) ){
				m.put("ncipcName2",ncsqr2[i]+"_"+ncsqrNCName[2]);
			} else{
				m.put("ncipcName2","-_");
			}
			if(ncsqr3.length>i && !"".equals(ncsqr3[0]) ){
				m.put("ncipcName3",ncsqr3[i]+"_"+ncsqrNCName[3]);
			} else{
				m.put("ncipcName3","-_");
			}
			if(ncsqr4.length>i && !"".equals(ncsqr4[0]) ){
				m.put("ncipcName4",ncsqr4[i]+"_"+ncsqrNCName[4]);
			} else{
				m.put("ncipcName4","-_");
			}
			if(ncsqr5.length>i && !"".equals(ncsqr5[0]) ){
				m.put("ncipcName5",ncsqr5[i]+"_"+ncsqrNCName[5]);
			} else{
				m.put("ncipcName5","-_");
			}
			if(ncsqr6.length>i &&  !"".equals(ncsqr6[0])){
				m.put("ncipcName6",ncsqr6[i]+"_"+ncsqrNCName[6]);
			} else{
				m.put("ncipcName6","-_");
			}
			if(ncsqr7.length>i &&!"".equals(ncsqr7[0]) ){
				m.put("ncipcName7",ncsqr7[i]+"_"+ncsqrNCName[7]);
			} else{
				m.put("ncipcName7","-_");
			}
			if(ncsqr8.length>i && !"".equals(ncsqr8[0]) ){
				m.put("ncipcName8",ncsqr8[i]+"_"+ncsqrNCName[8]);
			} else{
				m.put("ncipcName8","-_");
			}
			if(ncsqr9.length>i && !"".equals(ncsqr9[0]) ){
				m.put("ncipcName9",ncsqr9[i]+"_"+ncsqrNCName[9]);
			} else{
				m.put("ncipcName9","-_");
			}
			records.add(m);
		}
		map.put("total", total);
		map.put("rows", records);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取统计信息json 形成报表图形（申请人技术分析）
	 * 
	 * 
	 */
	public void getPatentSumData_shenqingrenIPC() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> series = new ArrayList<Map>();
		List<String> categories = new ArrayList<String>();
		 String[] ss= jedis.get(user.getId()+"sqr").split("_");
		 String[] ssIPC= jedis.get(user.getId()+"ipc").split("_");
		 String temp=null;
		 String tempIPC=null;
		 String[] strBurArrary=new String[10];   //不止一个地方用得着
		 String[] strIPCBurArrary=new String[10];   //不止一个地方用得着
		 for(int i=0;i<(ssIPC.length<10?ssIPC.length:10);i++){
			 tempIPC=ssIPC[i];
			 strIPCBurArrary[i]=tempIPC.substring(0, tempIPC.indexOf("="));
			 categories.add(strIPCBurArrary[i]);
		 }
		 for(int i=0;i<(ss.length<10?ss.length:10);i++){
			 temp=ss[i];
			 if(temp!=null&&!"".equals(temp)){
				    strBurArrary[i]=temp.substring(0, temp.indexOf("="));
					for(int j=0;j<10;j++){
						jedis.del(user.getId()+strBurArrary[i]+strIPCBurArrary[j]);
					}
				}
		 }
		 String[] applJly=jedis.get(user.getId()+"applJly").split("_");
		 applJly[applJly.length-1]=applJly[applJly.length-1].substring(0, applJly[applJly.length-1].length()-3);    ////删除末尾的Jly
		 String[] ipcJly=jedis.get(user.getId()+"ipcJly").split("_");
		 ipcJly[ipcJly.length-1]=ipcJly[ipcJly.length-1].substring(0, ipcJly[ipcJly.length-1].length()-3);    ////删除末尾的Jly
		for(int i=0;i<applJly.length;i++){
			String srtSQR=applJly[i];//获得申请人
			if((srtSQR!=null)&&(!"".equals(srtSQR))){    //申请人不空
				if(srtSQR.contains(",")){      //多个申请人
					String[] strs=srtSQR.split(",");
					for(int j=0;j<strs.length;j++){
						for(int k=0;k<(ss.length<10?ss.length:10);k++){   //申请人跟10个要统计的进行比对
							if(strBurArrary[k].equals(strs[j])){   //比对成功
								String strIPC=ipcJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
								if((strIPC!=null)&&(!"".equals(strIPC))){
									for(int ii=0;ii<(ssIPC.length<10?ssIPC.length:10);ii++){
										if(strIPCBurArrary[ii].equals(strIPC)){
											jedis.append(user.getId()+strBurArrary[k]+strIPCBurArrary[ii], "1");
										}
									}
								}
							}
						}
					}
				}else{  //单个申请人
					int jly=0;
					for(int k=0;k<(ss.length<10?ss.length:10);k++){   //申请人跟10个要统计的进行比对
						if(strBurArrary[k].equals(srtSQR)){   //比对成功
							String strIPC=ipcJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
							if((strIPC!=null)&&(!"".equals(strIPC))){
								for(int ii=0;ii<(ssIPC.length<10?ssIPC.length:10);ii++){
									if(strIPCBurArrary[ii].equals(strIPC)){
										jedis.append(user.getId()+srtSQR+strIPCBurArrary[ii], "1");
									}
								}
							}
						}
					}
				}
			}
		}
		StringBuilder sqrName=new StringBuilder();    //申请人名称
		StringBuilder ipcName=new StringBuilder();    //ipc名称
		StringBuilder sqrIpcName0=new StringBuilder();
		StringBuilder sqrIpcName1=new StringBuilder();
		StringBuilder sqrIpcName2=new StringBuilder();
		StringBuilder sqrIpcName3=new StringBuilder();
		StringBuilder sqrIpcName4=new StringBuilder();
		StringBuilder sqrIpcName5=new StringBuilder();
		StringBuilder sqrIpcName6=new StringBuilder();
		StringBuilder sqrIpcName7=new StringBuilder();
		StringBuilder sqrIpcName8=new StringBuilder();
		StringBuilder sqrIpcName9=new StringBuilder();
		for(int i=0;i<(ss.length<10?ss.length:10);i++){
			Map<String, Object> m = new HashMap<String, Object>();
			 List<BigInteger> data = new ArrayList<BigInteger>();
			 sqrName.append(strBurArrary[i]).append(",");
			 ipcName.append(strIPCBurArrary[i]).append(",");
			 for(int j=0;j<(ssIPC.length<10?ssIPC.length:10);j++){
				 Long  strLong=jedis.strlen(user.getId()+strBurArrary[i]+strIPCBurArrary[j]);
				 data.add(BigInteger.valueOf(strLong));
				 if(i==0){
					 sqrIpcName0.append(strLong).append(",");
				 }else if(i==1){
					 sqrIpcName1.append(strLong).append(",");
				 }else if(i==2){
					 sqrIpcName2.append(strLong).append(",");
				 }else if(i==3){
					 sqrIpcName3.append(strLong).append(",");
				 }else if(i==4){
					 sqrIpcName4.append(strLong).append(",");
				 }else if(i==5){
					 sqrIpcName5.append(strLong).append(",");
				 }else if(i==6){
					 sqrIpcName6.append(strLong).append(",");
				 }else if(i==7){
					 sqrIpcName7.append(strLong).append(",");
				 }else if(i==8){
					 sqrIpcName8.append(strLong).append(",");
				 }else if(i==9){
					 sqrIpcName9.append(strLong).append(",");
				 }
			 }
			 m.put("name",strBurArrary[i]);
			 m.put("data", data);
			 series.add(m);
		}
		if(sqrName.length()>0){
			sqrName.deleteCharAt(sqrName.length()-1);
		}
		if(ipcName.length()>0){
			ipcName.deleteCharAt(ipcName.length()-1);
		}
		if(sqrIpcName0.length()>0){
			sqrIpcName0.deleteCharAt(sqrIpcName0.length()-1);
		}
		if(sqrIpcName1.length()>0){
			sqrIpcName1.deleteCharAt(sqrIpcName1.length()-1);
		}
		if(sqrIpcName2.length()>0){
			sqrIpcName2.deleteCharAt(sqrIpcName2.length()-1);
		}
		if(sqrIpcName3.length()>0){
			sqrIpcName3.deleteCharAt(sqrIpcName3.length()-1);
		}
		if(sqrIpcName4.length()>0){
			sqrIpcName4.deleteCharAt(sqrIpcName4.length()-1);
		}
		if(sqrIpcName5.length()>0){
			sqrIpcName5.deleteCharAt(sqrIpcName5.length()-1);
		}
		if(sqrIpcName6.length()>0){
			sqrIpcName6.deleteCharAt(sqrIpcName6.length()-1);
		}
		if(sqrIpcName7.length()>0){
			sqrIpcName7.deleteCharAt(sqrIpcName7.length()-1);
		}
		if(sqrIpcName8.length()>0){
			sqrIpcName8.deleteCharAt(sqrIpcName8.length()-1);
		}
		if(sqrIpcName9.length()>0){
			sqrIpcName9.deleteCharAt(sqrIpcName9.length()-1);
		}
		 jedis.set(user.getId()+"sqripcNCName", sqrName.toString());
		 jedis.expire(user.getId()+"sqripcNCName", 10000);
		 jedis.set(user.getId()+"sqripcIPCName", ipcName.toString());
		 jedis.expire(user.getId()+"sqripcIPCName", 10000);
		 jedis.set(user.getId()+"sqripc0", sqrIpcName0.toString());
		 jedis.expire(user.getId()+"sqripc0", 10000);
		 jedis.set(user.getId()+"sqripc1", sqrIpcName1.toString());
		 jedis.expire(user.getId()+"sqripc1", 10000);
		 jedis.set(user.getId()+"sqripc2", sqrIpcName2.toString());
		 jedis.expire(user.getId()+"sqripc2", 10000);
		 jedis.set(user.getId()+"sqripc3", sqrIpcName3.toString());
		 jedis.expire(user.getId()+"sqripc3", 10000);
		 jedis.set(user.getId()+"sqripc4", sqrIpcName4.toString());
		 jedis.expire(user.getId()+"sqripc4", 10000);
		 jedis.set(user.getId()+"sqripc5", sqrIpcName5.toString());
		 jedis.expire(user.getId()+"sqripc5", 10000);
		 jedis.set(user.getId()+"sqripc6", sqrIpcName6.toString());
		 jedis.expire(user.getId()+"sqripc6", 10000);
		 jedis.set(user.getId()+"sqripc7", sqrIpcName7.toString());
		 jedis.expire(user.getId()+"sqripc7", 10000);
		 jedis.set(user.getId()+"sqripc8", sqrIpcName8.toString());
		 jedis.expire(user.getId()+"sqripc8", 10000);
		 jedis.set(user.getId()+"sqripc9", sqrIpcName9.toString());
		 jedis.expire(user.getId()+"sqripc9", 10000);
		map.put("series", series);
		map.put("categories", categories);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取统计信息json 形成表格（申请人技术分类）
	 */
	public void getPatentSumData_dataGrid_sqrIPC() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] ncsqrNCName=jedis.get(user.getId()+"sqripcNCName").split(",");
//		String[] ncsqrSQRName=jedis.get(user.getId()+"sqripcIPCName").split(",");

		List<String> categories = new ArrayList<String>();
		String[] ssIPC= jedis.get(user.getId()+"ipc").split("_");
		String tempIPC=null;
		String[] ncsqrSQRName=new String[ssIPC.length<10?ssIPC.length:10];   //不止一个地方用得着
		for(int i=0;i<(ssIPC.length<10?ssIPC.length:10);i++){
			tempIPC=ssIPC[i];
			ncsqrSQRName[i]=tempIPC.substring(0, tempIPC.indexOf("="));
			categories.add(ncsqrSQRName[i]);
		}
		String[] ncsqr0=jedis.get(user.getId()+"sqripc0").split(",");
		String[] ncsqr1=jedis.get(user.getId()+"sqripc1").split(",");
		String[] ncsqr2=jedis.get(user.getId()+"sqripc2").split(",");
		String[] ncsqr3=jedis.get(user.getId()+"sqripc3").split(",");
		String[] ncsqr4=jedis.get(user.getId()+"sqripc4").split(",");
		String[] ncsqr5=jedis.get(user.getId()+"sqripc5").split(",");
		String[] ncsqr6=jedis.get(user.getId()+"sqripc6").split(",");
		String[] ncsqr7=jedis.get(user.getId()+"sqripc7").split(",");
		String[] ncsqr8=jedis.get(user.getId()+"sqripc8").split(",");
		String[] ncsqr9=jedis.get(user.getId()+"sqripc9").split(",");
		List<Map> records = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		total=new Long((long)ncsqr0.length);
		Map<String, Object> m0 = new HashMap<String, Object>(); 
		m0.put("ipc", "技术分类");
		for(int j=0;j<ncsqrNCName.length;j++){
			m0.put("sqripcName"+j,ncsqrNCName[j]+"_");
		}
		for(int j=9;j>=ncsqrNCName.length;j--){
			m0.put("sqripcName"+j,"_");
		}
//		m0.put("sqripcName0",ncsqrNCName[0]+"_");
//		m0.put("sqripcName1",ncsqrNCName[1]+"_");
//		m0.put("sqripcName2",ncsqrNCName[2]+"_");
//		m0.put("sqripcName3",ncsqrNCName[3]+"_");
//		m0.put("sqripcName4",ncsqrNCName[4]+"_");
//		m0.put("sqripcName5",ncsqrNCName[5]+"_");
//		m0.put("sqripcName6",ncsqrNCName[6]+"_");
//		m0.put("sqripcName7",ncsqrNCName[7]+"_");
//		m0.put("sqripcName8",ncsqrNCName[8]+"_");
//		m0.put("sqripcName9",ncsqrNCName[9]+"_");
		records.add(m0);
		for (int i = 0; i < (ncsqrSQRName.length<10?ncsqrSQRName.length:10); i++) {
			Map<String, Object> m = new HashMap<String, Object>(); 
			m.put("ipc", ncsqrSQRName[i]);
			if(ncsqr0.length>i && !"".equals(ncsqr0[0])){
				m.put("sqripcName0",ncsqr0[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("sqripcName0","-_");
			}
			if(ncsqr1.length>i && !"".equals(ncsqr1[0]) ){
				m.put("sqripcName1",ncsqr1[i]+"_"+ncsqrNCName[1]);
			} else{
				m.put("sqripcName1","-_");
			}
			if (ncsqr2.length > i && !"".equals(ncsqr2[0]) ){
				m.put("sqripcName2",ncsqr2[i]+"_"+ncsqrNCName[2]);
			} else{
				m.put("sqripcName2","-_");
			}
			if(ncsqr3.length>i && !"".equals(ncsqr3[0]) ){
				m.put("sqripcName3",ncsqr3[i]+"_"+ncsqrNCName[3]);
			} else{
				m.put("sqripcName3","-_");
			}
			if(ncsqr4.length>i && !"".equals(ncsqr4[0]) ){
				m.put("sqripcName4",ncsqr4[i]+"_"+ncsqrNCName[4]);
			} else{
				m.put("sqripcName4","-_");
			}
			if(ncsqr5.length>i && !"".equals(ncsqr5[0]) ){
				m.put("sqripcName5",ncsqr5[i]+"_"+ncsqrNCName[5]);
			} else{
				m.put("sqripcName5","-_");
			}
			if(ncsqr6.length>i &&  !"".equals(ncsqr6[0])){
				m.put("sqripcName6",ncsqr6[i]+"_"+ncsqrNCName[6]);
			} else{
				m.put("sqripcName6","-_");
			}
			if(ncsqr7.length>i &&!"".equals(ncsqr7[0]) ){
				m.put("sqripcName7",ncsqr7[i]+"_"+ncsqrNCName[7]);
			} else{
				m.put("sqripcName7","-_");
			}
			if(ncsqr8.length>i && !"".equals(ncsqr8[0]) ){
				m.put("sqripcName8",ncsqr8[i]+"_"+ncsqrNCName[8]);
			} else{
				m.put("sqripcName8","-_");
			}
			if(ncsqr9.length>i && !"".equals(ncsqr9[0]) ){
				m.put("sqripcName9",ncsqr9[i]+"_"+ncsqrNCName[9]);
			} else{
				m.put("sqripcName9","-_");
			}
//			m.put("sqripcName0",ncsqr0[i]+"_"+ncsqrNCName[0]);
//			m.put("sqripcName1",ncsqr1[i]+"_"+ncsqrNCName[1]);
//			m.put("sqripcName2",ncsqr2[i]+"_"+ncsqrNCName[2]);
//			m.put("sqripcName3",ncsqr3[i]+"_"+ncsqrNCName[3]);
//			m.put("sqripcName4",ncsqr4[i]+"_"+ncsqrNCName[4]);
//			m.put("sqripcName5",ncsqr5[i]+"_"+ncsqrNCName[5]);
//			m.put("sqripcName6",ncsqr6[i]+"_"+ncsqrNCName[6]);
//			m.put("sqripcName7",ncsqr7[i]+"_"+ncsqrNCName[7]);
//			m.put("sqripcName8",ncsqr8[i]+"_"+ncsqrNCName[8]);
//			m.put("sqripcName9",ncsqr9[i]+"_"+ncsqrNCName[9]);
			records.add(m);
		}
		map.put("total", total);
		map.put("rows", records);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取统计信息json 形成报表图形（法律状态发明人分析）
	 * 
	 * 
	 */
	public void getPatentSumData_lawFMR() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> series = new ArrayList<Map>();
		List<String> categories = new ArrayList<String>();
		 String[] ssFMR= jedis.get(user.getId()+"fmr").split("_");
		 String temp=null;
		 String tempFMR=null;
		 String[] strFMRBurArrary=new String[10];   //不止一个地方用得着
		 for(int i=0;i<10;i++){
			 tempFMR=ssFMR[i];
			 strFMRBurArrary[i]=tempFMR.substring(0, tempFMR.indexOf("="));
			 categories.add(strFMRBurArrary[i]);
		 }
		 for(int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				jedis.del(user.getId()+"wuxiao"+strFMRBurArrary[j]);
			}
		 }
		 String[] inventorJly=jedis.get(user.getId()+"inventorJly").split("_");
		 inventorJly[inventorJly.length-1]=inventorJly[inventorJly.length-1].substring(0, inventorJly[inventorJly.length-1].length()-3);    ////删除末尾的Jly
		for(int i=0;i<inventorJly.length;i++){
				String strFMR=inventorJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
				if((strFMR!=null)&&(!"".equals(strFMR))){
					if(strFMR.contains(",")){
						String[] strFMRs=strFMR.split(",");
						for(int jj=0;jj<strFMRs.length;jj++){
							for(int ii=0;ii<10;ii++){
								if(strFMRBurArrary[ii].equals(strFMRs[jj])){
									jedis.append(user.getId()+"wuxiao"+strFMRBurArrary[ii], "1");
								}
							}
						}
					}else{
					for(int ii=0;ii<10;ii++){
						if(strFMRBurArrary[ii].equals(strFMR)){
							jedis.append(user.getId()+"wuxiao"+strFMRBurArrary[ii], "1");
						}
					}
				}
			}
		}
		StringBuilder fmrName=new StringBuilder();   //发明人名称
		StringBuilder ncFmrName0=new StringBuilder();
			Map<String, Object> m = new HashMap<String, Object>();
			 List<BigInteger> data = new ArrayList<BigInteger>();
			 for(int j=0;j<10;j++){
				 fmrName.append(strFMRBurArrary[j]).append(",");
				 Long  strLong=jedis.strlen(user.getId()+"wuxiao"+strFMRBurArrary[j]);
				 data.add(BigInteger.valueOf(strLong));
				ncFmrName0.append(strLong).append(",");
			 }
			 m.put("name","无效专利");
			 m.put("data", data);
			 series.add(m);
		fmrName.deleteCharAt(fmrName.length()-1);
		ncFmrName0.deleteCharAt(ncFmrName0.length()-1);
		 jedis.set(user.getId()+"lawfmrFMRName", fmrName.toString());
		 jedis.expire(user.getId()+"lawfmrFMRName", 10000);
		 jedis.set(user.getId()+"lawfmr0", ncFmrName0.toString());
		 jedis.expire(user.getId()+"lawfmr0", 10000);
		map.put("series", series);
		map.put("categories", categories);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取统计信息json 形成表格（法律状态发明人）
	 */
	public void getPatentSumData_dataGrid_lawFMR() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] inventorBurArrary=jedis.get(user.getId()+"lawfmrFMRName").split(",");
		String[] inventorSumBurArrary=jedis.get(user.getId()+"lawfmr0").split(",");
		
		List<Map> records = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		total=new Long((long)inventorBurArrary.length);
		for (int i = 0; i <10; i++) {
			Map<String, Object> m = new HashMap<String, Object>(); 
			m.put("fmr", inventorBurArrary[i]);
			m.put("lawName0", inventorSumBurArrary[i]);
			records.add(m);
		}
		map.put("total", total);
		map.put("rows", records);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取统计信息json 形成报表图形（法律状态申请人分析）
	 * 
	 * 
	 */
	public void getPatentSumData_lawSQR() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> series = new ArrayList<Map>();
		List<String> categories = new ArrayList<String>();
		 String[] ssFMR= jedis.get(user.getId()+"sqr").split("_");
		 String temp=null;
		 String tempFMR=null;
		 String[] strFMRBurArrary=new String[10];   //不止一个地方用得着
		 for(int i=0;i<(ssFMR.length<10?ssFMR.length:10);i++){
			 tempFMR=ssFMR[i];
			 strFMRBurArrary[i]=tempFMR.substring(0, tempFMR.indexOf("="));
			 categories.add(strFMRBurArrary[i]);
		 }
		 for(int i=0;i<(ssFMR.length<10?ssFMR.length:10);i++){
			for(int j=0;j<(ssFMR.length<10?ssFMR.length:10);j++){
				jedis.del(user.getId()+"wuxiao"+strFMRBurArrary[j]);
			}
		 }
		 String[] applJly=jedis.get(user.getId()+"applJly").split("_");
		 applJly[applJly.length-1]=applJly[applJly.length-1].substring(0, applJly[applJly.length-1].length()-3);    ////删除末尾的Jly
		for(int i=0;i<applJly.length;i++){
					String strFMR=applJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
					if((strFMR!=null)&&(!"".equals(strFMR))){
						if(strFMR.contains(",")){
							String[] strFMRs=strFMR.split(",");
							for(int jj=0;jj<strFMRs.length;jj++){
								for(int ii=0;ii<(ssFMR.length<10?ssFMR.length:10);ii++){
									if(strFMRBurArrary[ii].equals(strFMRs[jj])){
										jedis.append(user.getId()+"wuxiao"+strFMRBurArrary[ii], "1");
									}
								}
							}
						}else{
						for(int ii=0;ii<(ssFMR.length<10?ssFMR.length:10);ii++){
							if(strFMRBurArrary[ii].equals(strFMR)){
								jedis.append(user.getId()+"wuxiao"+strFMRBurArrary[ii], "1");
							}
						}
					}
				}
		}
		StringBuilder fmrName=new StringBuilder();   //发明人名称
		StringBuilder ncFmrName0=new StringBuilder();
			Map<String, Object> m = new HashMap<String, Object>();
			 List<BigInteger> data = new ArrayList<BigInteger>();
			 for(int j=0;j<(ssFMR.length<10?ssFMR.length:10);j++){
				 fmrName.append(strFMRBurArrary[j]).append(",");
				 Long  strLong=jedis.strlen(user.getId()+"wuxiao"+strFMRBurArrary[j]);
				 data.add(BigInteger.valueOf(strLong));
				ncFmrName0.append(strLong).append(",");
			 }
			 m.put("name","无效专利");
			 m.put("data", data);
			 series.add(m);
		fmrName.deleteCharAt(fmrName.length()-1);
		ncFmrName0.deleteCharAt(ncFmrName0.length()-1);
		 jedis.set(user.getId()+"lawsqrSQRName", fmrName.toString());
		 jedis.expire(user.getId()+"lawsqrSQRName", 10000);
		 jedis.set(user.getId()+"lawsqr", ncFmrName0.toString());
		 jedis.expire(user.getId()+"lawsqr", 10000);
		map.put("series", series);
		map.put("categories", categories);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取统计信息json 形成表格（法律状态申请人）
	 */
	public void getPatentSumData_dataGrid_lawSQR() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] inventorBurArrary=jedis.get(user.getId()+"lawsqrSQRName").split(",");
		String[] inventorSumBurArrary=jedis.get(user.getId()+"lawsqr").split(",");
		
		List<Map> records = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		total=new Long((long)inventorBurArrary.length);
		for (int i = 0; i <(inventorBurArrary.length<10?inventorBurArrary.length:10); i++) {
			Map<String, Object> m = new HashMap<String, Object>(); 
			m.put("sqr", inventorBurArrary[i]);
			m.put("lawSQRName", inventorSumBurArrary[i]);
			records.add(m);
		}
		map.put("total", total);
		map.put("rows", records);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取统计信息json 形成报表图形（法律状态国省分析）
	 * 
	 * 
	 */
	public void getPatentSumData_lawNC() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> series = new ArrayList<Map>();
		List<String> categories = new ArrayList<String>();
		 String[] ssFMR= jedis.get(user.getId()+"nc").split("_");
		 String temp=null;
		 String tempFMR=null;
		 String[] strFMRBurArrary=new String[10];   //不止一个地方用得着
		 for(int i=0;i<(ssFMR.length<10?ssFMR.length:10);i++){
			 tempFMR=ssFMR[i];
			 strFMRBurArrary[i]=tempFMR.substring(0, tempFMR.indexOf("="));
			 categories.add(RedisTest.getAddrNameByCode(strFMRBurArrary[i])+"("+strFMRBurArrary[i]+")");
		 }
		 for(int i=0;i<(ssFMR.length<10?ssFMR.length:10);i++){
			for(int j=0;j<(ssFMR.length<10?ssFMR.length:10);j++){
				jedis.del(user.getId()+"wuxiao"+strFMRBurArrary[j]);
			}
		 }
		 String[] ncJly=jedis.get(user.getId()+"ncJly").split("_");
		 ncJly[ncJly.length-1]=ncJly[ncJly.length-1].substring(0, ncJly[ncJly.length-1].length()-3);    ////删除末尾的Jly
		for(int i=0;i<ncJly.length;i++){
			String strFMR=ncJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
			if((strFMR!=null)&&(!"".equals(strFMR))){
				for(int ii=0;ii<(ssFMR.length<10?ssFMR.length:10);ii++){
					if(strFMRBurArrary[ii].equals(strFMR)){
						jedis.append(user.getId()+"wuxiao"+strFMRBurArrary[ii], "1");
					}
				}
			}
		}
		StringBuilder fmrName=new StringBuilder();   //发明人名称
		StringBuilder ncFmrName0=new StringBuilder();
			Map<String, Object> m = new HashMap<String, Object>();
			 List<BigInteger> data = new ArrayList<BigInteger>();
			 for(int j=0;j<(ssFMR.length<10?ssFMR.length:10);j++){
				 fmrName.append(strFMRBurArrary[j]).append(",");
				 Long  strLong=jedis.strlen(user.getId()+"wuxiao"+strFMRBurArrary[j]);
				 data.add(BigInteger.valueOf(strLong));
				ncFmrName0.append(strLong).append(",");
			 }
			 m.put("name","无效专利");
			 m.put("data", data);
			 series.add(m);
		fmrName.deleteCharAt(fmrName.length()-1);
		ncFmrName0.deleteCharAt(ncFmrName0.length()-1);
		 jedis.set(user.getId()+"lawncNCName", fmrName.toString());
		 jedis.expire(user.getId()+"lawncNCName", 10000);
		 jedis.set(user.getId()+"lawnc", ncFmrName0.toString());
		 jedis.expire(user.getId()+"lawnc", 10000);
		map.put("series", series);
		map.put("categories", categories);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取统计信息json 形成表格（法律状态国省）
	 */
	public void getPatentSumData_dataGrid_lawNC() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] inventorBurArrary=jedis.get(user.getId()+"lawncNCName").split(",");
//		String[] inventorSumBurArrary=jedis.get(user.getId()+"lawnc").split(",");

		List<String> categories = new ArrayList<String>();
		 String[] ssFMR= jedis.get(user.getId()+"nc").split("_");
		 String tempFMR=null;
		 String[] inventorSumBurArrary=new String[ssFMR.length<10?ssFMR.length:10];   //不止一个地方用得着
		 for(int i=0;i<(ssFMR.length<10?ssFMR.length:10);i++){
			 tempFMR=ssFMR[i];
			 inventorSumBurArrary[i]=tempFMR.substring(tempFMR.indexOf("=")+1,tempFMR.length());
			 categories.add(RedisTest.getAddrNameByCode(inventorSumBurArrary[i])+"("+inventorSumBurArrary[i]+")");
		 }
		
		List<Map> records = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		total=new Long((long)inventorBurArrary.length);
		for (int i = 0; i <(inventorBurArrary.length<10?inventorBurArrary.length:10); i++) {
			Map<String, Object> m = new HashMap<String, Object>(); 
			m.put("nc",RedisTest.getAddrNameByCode(inventorBurArrary[i])+ "("+inventorBurArrary[i]+")");
			m.put("lawNCName", inventorSumBurArrary[i]);
			records.add(m);
		}
		map.put("total", total);
		map.put("rows", records);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取统计信息json 形成报表图形（法律状态技术分类分析）
	 * 
	 * 
	 */
	public void getPatentSumData_lawIPC() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> series = new ArrayList<Map>();
		List<String> categories = new ArrayList<String>();
		 String[] ssFMR= jedis.get(user.getId()+"ipc").split("_");
		 String temp=null;
		 String tempFMR=null;
		 String[] strFMRBurArrary=new String[10];   //不止一个地方用得着
		 if(ssFMR.length>=10){
			 for(int i=0;i<10;i++){
				 tempFMR=ssFMR[i];
				 strFMRBurArrary[i]=tempFMR.substring(0, tempFMR.indexOf("="));
				 categories.add(strFMRBurArrary[i]);
			 }
			 for(int i=0;i<10;i++){
				for(int j=0;j<10;j++){
					jedis.del(user.getId()+"wuxiao"+strFMRBurArrary[j]);
				}
			 }
		 }else{
			 for(int i=0;i<ssFMR.length;i++){
				 tempFMR=ssFMR[i];
				 strFMRBurArrary[i]=tempFMR.substring(0, tempFMR.indexOf("="));
				 categories.add(strFMRBurArrary[i]);
			 }
			 for(int i=0;i<ssFMR.length;i++){
				for(int j=0;j<10;j++){
					jedis.del(user.getId()+"wuxiao"+strFMRBurArrary[j]);
				}
			 }
		 }
		 String[] ipcJly=jedis.get(user.getId()+"ipcJly").split("_");
		 ipcJly[ipcJly.length-1]=ipcJly[ipcJly.length-1].substring(0, ipcJly[ipcJly.length-1].length()-3);    ////删除末尾的Jly
		for(int i=0;i<ipcJly.length;i++){
				String strFMR=ipcJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
				if((strFMR!=null)&&(!"".equals(strFMR))){
					 if(ssFMR.length>=10){
						for(int ii=0;ii<10;ii++){
							if(strFMRBurArrary[ii].equals(strFMR)){
								jedis.append(user.getId()+"wuxiao"+strFMRBurArrary[ii], "1");
							}
						}
					}else{
						for(int ii=0;ii<ssFMR.length;ii++){
							if(strFMRBurArrary[ii].equals(strFMR)){
								jedis.append(user.getId()+"wuxiao"+strFMRBurArrary[ii], "1");
							}
						}
					}
				}
			}
		StringBuilder fmrName=new StringBuilder();   //发明人名称
		StringBuilder ncFmrName0=new StringBuilder();
			Map<String, Object> m = new HashMap<String, Object>();
			 List<BigInteger> data = new ArrayList<BigInteger>();
			 if(ssFMR.length>=10){
				 for(int j=0;j<10;j++){
					 fmrName.append(strFMRBurArrary[j]).append(",");
					 Long  strLong=jedis.strlen(user.getId()+"wuxiao"+strFMRBurArrary[j]);
					 data.add(BigInteger.valueOf(strLong));
					ncFmrName0.append(strLong).append(",");
				 }
			 }else{
				 for(int j=0;j<ssFMR.length;j++){
					 fmrName.append(strFMRBurArrary[j]).append(",");
					 Long  strLong=jedis.strlen(user.getId()+"wuxiao"+strFMRBurArrary[j]);
					 data.add(BigInteger.valueOf(strLong));
					ncFmrName0.append(strLong).append(",");
				 }
			 }
			 m.put("name","无效专利");
			 m.put("data", data);
			 series.add(m);
		fmrName.deleteCharAt(fmrName.length()-1);
		ncFmrName0.deleteCharAt(ncFmrName0.length()-1);
		 jedis.set(user.getId()+"lawipcIPCName", fmrName.toString());
		 jedis.expire(user.getId()+"lawipcIPCName", 10000);
		 jedis.set(user.getId()+"lawipc", ncFmrName0.toString());
		 jedis.expire(user.getId()+"lawipc", 10000);
		map.put("series", series);
		map.put("categories", categories);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取统计信息json 形成表格（法律状态技术分类）
	 */
	public void getPatentSumData_dataGrid_lawIPC() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] inventorBurArrary=jedis.get(user.getId()+"lawipcIPCName").split(",");
		String[] inventorSumBurArrary=jedis.get(user.getId()+"lawipc").split(",");
		
		List<Map> records = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		total=new Long((long)inventorBurArrary.length);
		if(inventorBurArrary.length>=10){
			for (int i = 0; i <10; i++) {
				Map<String, Object> m = new HashMap<String, Object>(); 
				m.put("ipc",inventorBurArrary[i]);
				m.put("lawIPCName", inventorSumBurArrary[i]);
				records.add(m);
			}
		}else{
			for (int i = 0; i <inventorBurArrary.length; i++) {
				Map<String, Object> m = new HashMap<String, Object>(); 
				m.put("ipc",inventorBurArrary[i]);
				m.put("lawIPCName", inventorSumBurArrary[i]);
				records.add(m);
			}
		}
		map.put("total", total);
		map.put("rows", records);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取统计信息json 形成报表图形（国省发明人分析）
	 * 
	 * 
	 */
	public void getPatentSumData_ncFMR() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> series = new ArrayList<Map>();
		List<String> categories = new ArrayList<String>();
		 String[] ss= jedis.get(user.getId()+"nc").split("_");
		 String[] ssFMR= jedis.get(user.getId()+"fmr").split("_");
		 String temp=null;
		 String tempFMR=null;
		 String[] strBurArrary=new String[10];   //不止一个地方用得着
		 String[] strFMRBurArrary=new String[10];   //不止一个地方用得着
		 for(int i=0;i<(ssFMR.length<10?ssFMR.length:10);i++){
			 tempFMR=ssFMR[i];
			 strFMRBurArrary[i]=tempFMR.substring(0, tempFMR.indexOf("="));
			 categories.add(strFMRBurArrary[i]);
		 }
		 for(int i=0;i<(ss.length<10?ss.length:10);i++){
			 temp=ss[i];
			 if(temp!=null&&!"".equals(temp)){
				    strBurArrary[i]=temp.substring(0, temp.indexOf("="));
					for(int j=0;j<(ssFMR.length<10?ssFMR.length:10);j++){
						jedis.del(user.getId()+strBurArrary[i]+strFMRBurArrary[j]);
					}
				}
		 }
		 String[] ncJly=jedis.get(user.getId()+"ncJly").split("_");
		 ncJly[ncJly.length-1]=ncJly[ncJly.length-1].substring(0, ncJly[ncJly.length-1].length()-3);    ////删除末尾的Jly
		 String[] inventorJly=jedis.get(user.getId()+"inventorJly").split("_");
		 inventorJly[inventorJly.length-1]=inventorJly[inventorJly.length-1].substring(0, inventorJly[inventorJly.length-1].length()-3);    ////删除末尾的Jly
		for(int i=0;i<inventorJly.length;i++){
			String srtNC=ncJly[i];//获得申请人
			if((srtNC!=null)&&(!"".equals(srtNC))){    //申请人不空
				if(srtNC.contains(",")){      //多个申请人
					String[] strs=srtNC.split(",");
					for(int j=0;j<strs.length;j++){
						for(int k=0;k<(ss.length<10?ss.length:10);k++){   //申请人跟10个要统计的进行比对
							if(strBurArrary[k].equals(strs[j])){   //比对成功
								String strFMR=inventorJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
								if((strFMR!=null)&&(!"".equals(strFMR))){
									if(strFMR.contains(",")){
										String[] strFMRs=strFMR.split(",");
										for(int jj=0;jj<strFMRs.length;jj++){
											for(int ii=0;ii<(ssFMR.length<10?ssFMR.length:10);ii++){
												if(strFMRBurArrary[ii].equals(strFMRs[jj])){
													jedis.append(user.getId()+strBurArrary[k]+strFMRBurArrary[ii], "1");
												}
											}
										}
									}else{
										for(int ii=0;ii<(ssFMR.length<10?ssFMR.length:10);ii++){
											if(strFMRBurArrary[ii].equals(strFMR)){
												jedis.append(user.getId()+strBurArrary[k]+strFMRBurArrary[ii], "1");
											}
										}
									}
								}
							}
						}
					}
				}else{  //单个申请人
					for(int k=0;k<(ss.length<10?ss.length:10);k++){   //申请人跟10个要统计的进行比对
						if(strBurArrary[k].equals(srtNC)){   //比对成功
							String strFMR=inventorJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
							if((strFMR!=null)&&(!"".equals(strFMR))){
								if(strFMR.contains(",")){
									String[] strFMRs=strFMR.split(",");
									for(int jj=0;jj<strFMRs.length;jj++){
										for(int ii=0;ii<(ssFMR.length<10?ssFMR.length:10);ii++){
											if(strFMRBurArrary[ii].equals(strFMRs[jj])){
												jedis.append(user.getId()+srtNC+strFMRBurArrary[ii], "1");
											}
										}
									}
								}else{
									for(int ii=0;ii<(ssFMR.length<10?ssFMR.length:10);ii++){
										if(strFMRBurArrary[ii].equals(strFMR)){
											jedis.append(user.getId()+srtNC+strFMRBurArrary[ii], "1");
										}
									}
								}
							}
						}
					}
				}
			}
		}
		StringBuilder ncName=new StringBuilder();    //国省名称
		StringBuilder fmrName=new StringBuilder();   //发明人名称
		StringBuilder ncFmrName0=new StringBuilder();
		StringBuilder ncFmrName1=new StringBuilder();
		StringBuilder ncFmrName2=new StringBuilder();
		StringBuilder ncFmrName3=new StringBuilder();
		StringBuilder ncFmrName4=new StringBuilder();
		StringBuilder ncFmrName5=new StringBuilder();
		StringBuilder ncFmrName6=new StringBuilder();
		StringBuilder ncFmrName7=new StringBuilder();
		StringBuilder ncFmrName8=new StringBuilder();
		StringBuilder ncFmrName9=new StringBuilder();
		for(int i=0;i<(ss.length<10?ss.length:10);i++){
			Map<String, Object> m = new HashMap<String, Object>();
			 List<BigInteger> data = new ArrayList<BigInteger>();
			 ncName.append(strBurArrary[i]).append(",");
			 fmrName.append(strFMRBurArrary[i]).append(",");
			 for(int j=0;j<(ssFMR.length<10?ssFMR.length:10);j++){
				 Long  strLong=jedis.strlen(user.getId()+strBurArrary[i]+strFMRBurArrary[j]);
				 data.add(BigInteger.valueOf(strLong));
				 if(i==0){
					 ncFmrName0.append(strLong).append(",");
				 }else if(i==1){
					 ncFmrName1.append(strLong).append(",");
				 }else if(i==2){
					 ncFmrName2.append(strLong).append(",");
				 }else if(i==3){
					 ncFmrName3.append(strLong).append(",");
				 }else if(i==4){
					 ncFmrName4.append(strLong).append(",");
				 }else if(i==5){
					 ncFmrName5.append(strLong).append(",");
				 }else if(i==6){
					 ncFmrName6.append(strLong).append(",");
				 }else if(i==7){
					 ncFmrName7.append(strLong).append(",");
				 }else if(i==8){
					 ncFmrName8.append(strLong).append(",");
				 }else if(i==9){
					 ncFmrName9.append(strLong).append(",");
				 }
			 }
			 m.put("name",RedisTest.getAddrNameByCode(strBurArrary[i]));
			 m.put("data", data);
			 series.add(m);
		}
		if(ncName.length()>0){
			ncName.deleteCharAt(ncName.length()-1);
		}
		if(fmrName.length()>0){
			fmrName.deleteCharAt(fmrName.length()-1);
		}
		if(ncFmrName0.length()>0){
			ncFmrName0.deleteCharAt(ncFmrName0.length()-1);
		}
		if(ncFmrName1.length()>0){
			ncFmrName1.deleteCharAt(ncFmrName1.length()-1);
		}
		if(ncFmrName2.length()>0){
			ncFmrName2.deleteCharAt(ncFmrName2.length()-1);
		}
		if(ncFmrName3.length()>0){
			ncFmrName3.deleteCharAt(ncFmrName3.length()-1);
		}
		if(ncFmrName4.length()>0){
			ncFmrName4.deleteCharAt(ncFmrName4.length()-1);
		}
		if(ncFmrName5.length()>0){
			ncFmrName5.deleteCharAt(ncFmrName5.length()-1);
		}
		if(ncFmrName6.length()>0){
			ncFmrName6.deleteCharAt(ncFmrName6.length()-1);
		}
		if(ncFmrName7.length()>0){
			ncFmrName7.deleteCharAt(ncFmrName7.length()-1);
		}
		if(ncFmrName8.length()>0){
			ncFmrName8.deleteCharAt(ncFmrName8.length()-1);
		}
		if(ncFmrName9.length()>0){
			ncFmrName9.deleteCharAt(ncFmrName9.length()-1);
		}
		 jedis.set(user.getId()+"ncfmrNCName", ncName.toString());
		 jedis.expire(user.getId()+"ncfmrNCName", 10000);
		 jedis.set(user.getId()+"ncfmrFMRName", fmrName.toString());
		 jedis.expire(user.getId()+"ncfmrFMRName", 10000);
		 jedis.set(user.getId()+"ncfmr0", ncFmrName0.toString());
		 jedis.expire(user.getId()+"ncfmr0", 10000);
		 jedis.set(user.getId()+"ncfmr1", ncFmrName1.toString());
		 jedis.expire(user.getId()+"ncfmr1", 10000);
		 jedis.set(user.getId()+"ncfmr2", ncFmrName2.toString());
		 jedis.expire(user.getId()+"ncfmr2", 10000);
		 jedis.set(user.getId()+"ncfmr3", ncFmrName3.toString());
		 jedis.expire(user.getId()+"ncfmr3", 10000);
		 jedis.set(user.getId()+"ncfmr4", ncFmrName4.toString());
		 jedis.expire(user.getId()+"ncfmr4", 10000);
		 jedis.set(user.getId()+"ncfmr5", ncFmrName5.toString());
		 jedis.expire(user.getId()+"ncfmr5", 10000);
		 jedis.set(user.getId()+"ncfmr6", ncFmrName6.toString());
		 jedis.expire(user.getId()+"ncfmr6", 10000);
		 jedis.set(user.getId()+"ncfmr7", ncFmrName7.toString());
		 jedis.expire(user.getId()+"ncfmr7", 10000);
		 jedis.set(user.getId()+"ncfmr8", ncFmrName8.toString());
		 jedis.expire(user.getId()+"ncfmr8", 10000);
		 jedis.set(user.getId()+"ncfmr9", ncFmrName9.toString());
		 jedis.expire(user.getId()+"ncfmr9", 10000);
		map.put("series", series);
		map.put("categories", categories);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取统计信息json 形成表格（国省发明人）
	 */
	public void getPatentSumData_dataGrid_ncFMR() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] ncsqrNCName=jedis.get(user.getId()+"ncfmrNCName").split(",");
//		String[] ncsqrSQRName=jedis.get(user.getId()+"ncfmrFMRName").split(",");
		String[] ssFMR= jedis.get(user.getId()+"fmr").split("_");
		String[] ncsqrSQRName=new String[ssFMR.length<10?ssFMR.length:10];   //不止一个地方用得着
		String tempFMR=null;
		List<String> categories = new ArrayList<String>();
		for(int i=0;i<(ssFMR.length<10?ssFMR.length:10);i++){
			tempFMR=ssFMR[i];
			ncsqrSQRName[i]=tempFMR.substring(0, tempFMR.indexOf("="));
			categories.add(ncsqrSQRName[i]);
		}
		String[] ncsqr0=jedis.get(user.getId()+"ncfmr0").split(",");
		String[] ncsqr1=jedis.get(user.getId()+"ncfmr1").split(",");
		String[] ncsqr2=jedis.get(user.getId()+"ncfmr2").split(",");
		String[] ncsqr3=jedis.get(user.getId()+"ncfmr3").split(",");
		String[] ncsqr4=jedis.get(user.getId()+"ncfmr4").split(",");
		String[] ncsqr5=jedis.get(user.getId()+"ncfmr5").split(",");
		String[] ncsqr6=jedis.get(user.getId()+"ncfmr6").split(",");
		String[] ncsqr7=jedis.get(user.getId()+"ncfmr7").split(",");
		String[] ncsqr8=jedis.get(user.getId()+"ncfmr8").split(",");
		String[] ncsqr9=jedis.get(user.getId()+"ncfmr9").split(",");
		List<Map> records = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		total=new Long((long)ncsqr0.length);
		Map<String, Object> m0 = new HashMap<String, Object>(); 
		m0.put("fmr", "发明人");
		for(int j=0;j<ncsqrNCName.length;j++){
			m0.put("ncfmrName"+j,RedisTest.getAddrNameByCode(ncsqrNCName[j])+"("+ncsqrNCName[j]+")_");
		}
		for(int j=9;j>=ncsqrNCName.length;j--){
			m0.put("ncfmrName"+j,RedisTest.getAddrNameByCode("_"));
		}
//		m0.put("ncfmrName0",RedisTest.getAddrNameByCode(ncsqrNCName[0])+"("+ncsqrNCName[0]+")_");
//		m0.put("ncfmrName1",RedisTest.getAddrNameByCode(ncsqrNCName[1])+"("+ncsqrNCName[1]+")_");
//		m0.put("ncfmrName2",RedisTest.getAddrNameByCode(ncsqrNCName[2])+"("+ncsqrNCName[2]+")_");
//		m0.put("ncfmrName3",RedisTest.getAddrNameByCode(ncsqrNCName[3])+"("+ncsqrNCName[3]+")_");
//		m0.put("ncfmrName4",RedisTest.getAddrNameByCode(ncsqrNCName[4])+"("+ncsqrNCName[4]+")_");
//		m0.put("ncfmrName5",RedisTest.getAddrNameByCode(ncsqrNCName[5])+"("+ncsqrNCName[5]+")_");
//		m0.put("ncfmrName6",RedisTest.getAddrNameByCode(ncsqrNCName[6])+"("+ncsqrNCName[6]+")_");
//		m0.put("ncfmrName7",RedisTest.getAddrNameByCode(ncsqrNCName[7])+"("+ncsqrNCName[7]+")_");
//		m0.put("ncfmrName8",RedisTest.getAddrNameByCode(ncsqrNCName[8])+"("+ncsqrNCName[8]+")_");
//		m0.put("ncfmrName9",RedisTest.getAddrNameByCode(ncsqrNCName[9])+"("+ncsqrNCName[9]+")_");
		records.add(m0);
		for (int i = 0; i < (ncsqrSQRName.length<10?ncsqrSQRName.length:10); i++) {
			Map<String, Object> m = new HashMap<String, Object>(); 
			m.put("fmr", ncsqrSQRName[i]);
			if(ncsqr0.length>i && ncsqr0.length > 1 ){
				m.put("ncfmrName0",ncsqr0[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("ncfmrName0","-_");
			}
			if(ncsqr1.length>i && ncsqr1.length > 1 ){
				m.put("ncfmrName1",ncsqr1[i]+"_"+ncsqrNCName[1]);
			} else{
				m.put("ncfmrName1","-_");
			}
			if (ncsqr2.length > i && ncsqr2.length > 1 ){
				m.put("ncfmrName2",ncsqr2[i]+"_"+ncsqrNCName[2]);
			} else{
				m.put("ncfmrName2","-_");
			}
			if(ncsqr3.length>i && ncsqr3.length > 1 ){
				m.put("ncfmrName3",ncsqr3[i]+"_"+ncsqrNCName[3]);
			} else{
				m.put("ncfmrName3","-_");
			}
			if(ncsqr4.length>i && ncsqr4.length > 1 ){
				m.put("ncfmrName4",ncsqr4[i]+"_"+ncsqrNCName[4]);
			} else{
				m.put("ncfmrName4","-_");
			}
			if(ncsqr5.length>i && ncsqr5.length > 1 ){
				m.put("ncfmrName5",ncsqr5[i]+"_"+ncsqrNCName[5]);
			} else{
				m.put("ncfmrName5","-_");
			}
			if(ncsqr6.length>i && ncsqr6.length > 1 ){
				m.put("ncfmrName6",ncsqr6[i]+"_"+ncsqrNCName[6]);
			} else{
				m.put("ncfmrName6","-_");
			}
			if(ncsqr7.length>i && ncsqr7.length > 1 ){
				m.put("ncfmrName7",ncsqr7[i]+"_"+ncsqrNCName[7]);
			} else{
				m.put("ncfmrName7","-_");
			}
			if(ncsqr8.length>i && ncsqr8.length > 1 ){
				m.put("ncfmrName8",ncsqr8[i]+"_"+ncsqrNCName[8]);
			} else{
				m.put("ncfmrName8","-_");
			}
			if(ncsqr9.length>i && ncsqr9.length > 1 ){
				m.put("ncfmrName9",ncsqr9[i]+"_"+ncsqrNCName[9]);
			} else{
				m.put("ncfmrName9","-_");
			}
			records.add(m);
		}
		map.put("total", total);
		map.put("rows", records);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取统计信息json 形成报表图形（技术分类申请人分析）
	 * 
	 * 
	 */
	public void getPatentSumData_ipcSQR() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> series = new ArrayList<Map>();
		List<String> categories = new ArrayList<String>();
		String[] ss = jedis.get(user.getId() + "ipc").split("_");
		String[] ssFMR = jedis.get(user.getId() + "sqr").split("_");
		String temp = null;
		String tempFMR = null;
		String[] strBurArrary = new String[10]; // 不止一个地方用得着
		String[] strFMRBurArrary = new String[10]; // 不止一个地方用得着
		for (int i = 0; i < (ssFMR.length < 10 ? ssFMR.length : 10); i++) {
			tempFMR = ssFMR[i];
			strFMRBurArrary[i] = tempFMR.substring(0, tempFMR.indexOf("="));
			categories.add(strFMRBurArrary[i]);
		}
		 for(int i=0;i<(ss.length<10?ss.length:10);i++){
			 temp=ss[i];
			 if(temp!=null&&!"".equals(temp)){
				    strBurArrary[i]=temp.substring(0, temp.indexOf("="));
					for(int j=0;j<10;j++){
						jedis.del(user.getId()+strBurArrary[i]+strFMRBurArrary[j]);
					}
				}
		 }
		 String[] ipcJly=jedis.get(user.getId()+"ipcJly").split("_");
		 ipcJly[ipcJly.length-1]=ipcJly[ipcJly.length-1].substring(0, ipcJly[ipcJly.length-1].length()-3);    ////删除末尾的Jly
		 String[] applJly=jedis.get(user.getId()+"applJly").split("_");
		 applJly[applJly.length-1]=applJly[applJly.length-1].substring(0, applJly[applJly.length-1].length()-3);    ////删除末尾的Jly
		for(int i=0;i<ipcJly.length;i++){
			String srtNC=ipcJly[i];//获得申请人
			if((srtNC!=null)&&(!"".equals(srtNC))){    //申请人不空
				if(srtNC.contains(",")){      //多个申请人
					String[] strs=srtNC.split(",");
					for(int j=0;j<strs.length;j++){
						for(int k=0;k<(ss.length<10?ss.length:10);k++){   //申请人跟10个要统计的进行比对
							if(strBurArrary[k].equals(strs[j])){   //比对成功
								String strFMR=applJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
								if((strFMR!=null)&&(!"".equals(strFMR))){
									if(strFMR.contains(",")){
										String[] strFMRs=strFMR.split(",");
										for(int jj=0;jj<strFMRs.length;jj++){
											for(int ii=0;ii<(ssFMR.length<10?ssFMR.length:10);ii++){
												if(strFMRBurArrary[ii].equals(strFMRs[jj])){
													jedis.append(user.getId()+strBurArrary[k]+strFMRBurArrary[ii], "1");
												}
											}
										}
									}else{
										for(int ii=0;ii<(ssFMR.length<10?ssFMR.length:10);ii++){
											if(strFMRBurArrary[ii].equals(strFMR)){
												jedis.append(user.getId()+strBurArrary[k]+strFMRBurArrary[ii], "1");
											}
										}
									}
								}
							}
						}
					}
				}else{  //单个申请人
					for(int k=0;k<(ss.length<10?ss.length:10);k++){   //申请人跟10个要统计的进行比对
						if(strBurArrary[k].equals(srtNC)){   //比对成功
							String strFMR=applJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
							if((strFMR!=null)&&(!"".equals(strFMR))){
								if(strFMR.contains(",")){
									String[] strFMRs=strFMR.split(",");
									for(int jj=0;jj<strFMRs.length;jj++){
										for(int ii=0;ii<(ssFMR.length<10?ssFMR.length:10);ii++){
											if(strFMRBurArrary[ii].equals(strFMRs[jj])){
												jedis.append(user.getId()+srtNC+strFMRBurArrary[ii], "1");
											}
										}
									}
								}else{
									for(int ii=0;ii<(ssFMR.length<10?ssFMR.length:10);ii++){
										if(strFMRBurArrary[ii].equals(strFMR)){
											jedis.append(user.getId()+srtNC+strFMRBurArrary[ii], "1");
										}
									}
								}
							}
						}
					}
				}
			}
		}
		StringBuilder sqrName=new StringBuilder();    //申请人名称
		StringBuilder fmrName=new StringBuilder();    //ipc名称
		StringBuilder sqrFmrName0=new StringBuilder();
		StringBuilder sqrFmrName1=new StringBuilder();
		StringBuilder sqrFmrName2=new StringBuilder();
		StringBuilder sqrFmrName3=new StringBuilder();
		StringBuilder sqrFmrName4=new StringBuilder();
		StringBuilder sqrFmrName5=new StringBuilder();
		StringBuilder sqrFmrName6=new StringBuilder();
		StringBuilder sqrFmrName7=new StringBuilder();
		StringBuilder sqrFmrName8=new StringBuilder();
		StringBuilder sqrFmrName9=new StringBuilder();
		for(int i=0;i<(ss.length<10?ss.length:10);i++){
			Map<String, Object> m = new HashMap<String, Object>();
			 List<BigInteger> data = new ArrayList<BigInteger>();
			 sqrName.append(strBurArrary[i]).append(",");
			 fmrName.append(strFMRBurArrary[i]).append(",");
			 for(int j=0;j<(ssFMR.length<10?ssFMR.length:10);j++){
				 Long  strLong=jedis.strlen(user.getId()+strBurArrary[i]+strFMRBurArrary[j]);
				 data.add(BigInteger.valueOf(strLong));
				 if(i==0){
					 sqrFmrName0.append(strLong).append(",");
				 }else if(i==1){
					 sqrFmrName1.append(strLong).append(",");
				 }else if(i==2){
					 sqrFmrName2.append(strLong).append(",");
				 }else if(i==3){
					 sqrFmrName3.append(strLong).append(",");
				 }else if(i==4){
					 sqrFmrName4.append(strLong).append(",");
				 }else if(i==5){
					 sqrFmrName5.append(strLong).append(",");
				 }else if(i==6){
					 sqrFmrName6.append(strLong).append(",");
				 }else if(i==7){
					 sqrFmrName7.append(strLong).append(",");
				 }else if(i==8){
					 sqrFmrName8.append(strLong).append(",");
				 }else if(i==9){
					 sqrFmrName9.append(strLong).append(",");
				 }
			 }
			 m.put("name",strBurArrary[i]);
			 m.put("data", data);
			 series.add(m);
		}
		if(sqrName.length()>0){
			sqrName.deleteCharAt(sqrName.length()-1);
		}
		if(fmrName.length()>0){
			fmrName.deleteCharAt(fmrName.length()-1);
		}
		if(sqrFmrName0.length()>0){
			sqrFmrName0.deleteCharAt(sqrFmrName0.length()-1);
		}
		if(sqrFmrName1.length()>0){
			sqrFmrName1.deleteCharAt(sqrFmrName1.length()-1);
		}
		if(sqrFmrName2.length()>0){
			sqrFmrName2.deleteCharAt(sqrFmrName2.length()-1);
		}
		if(sqrFmrName3.length()>0){
			sqrFmrName3.deleteCharAt(sqrFmrName3.length()-1);
		}
		if(sqrFmrName4.length()>0){
			sqrFmrName4.deleteCharAt(sqrFmrName4.length()-1);
		}
		if(sqrFmrName5.length()>0){
			sqrFmrName5.deleteCharAt(sqrFmrName5.length()-1);
		}
		if(sqrFmrName6.length()>0){
			sqrFmrName6.deleteCharAt(sqrFmrName6.length()-1);
		}
		if(sqrFmrName7.length()>0){
			sqrFmrName7.deleteCharAt(sqrFmrName7.length()-1);
		}
		if(sqrFmrName8.length()>0){
			sqrFmrName8.deleteCharAt(sqrFmrName8.length()-1);
		}
		if(sqrFmrName9.length()>0){
			sqrFmrName9.deleteCharAt(sqrFmrName9.length()-1);
		}
		 jedis.set(user.getId()+"ipcSqrIpcName", sqrName.toString());
		 jedis.expire(user.getId()+"ipcSqrIpcName", 10000);
		 jedis.set(user.getId()+"ipcSqrSqrName", fmrName.toString());
		 jedis.expire(user.getId()+"ipcSqrSqrName", 10000);
		 jedis.set(user.getId()+"ipcSqr0", sqrFmrName0.toString());
		 jedis.expire(user.getId()+"ipcSqr0", 10000);
		 jedis.set(user.getId()+"ipcSqr1", sqrFmrName1.toString());
		 jedis.expire(user.getId()+"ipcSqr1", 10000);
		 jedis.set(user.getId()+"ipcSqr2", sqrFmrName2.toString());
		 jedis.expire(user.getId()+"ipcSqr2", 10000);
		 jedis.set(user.getId()+"ipcSqr3", sqrFmrName3.toString());
		 jedis.expire(user.getId()+"ipcSqr3", 10000);
		 jedis.set(user.getId()+"ipcSqr4", sqrFmrName4.toString());
		 jedis.expire(user.getId()+"ipcSqr4", 10000);
		 jedis.set(user.getId()+"ipcSqr5", sqrFmrName5.toString());
		 jedis.expire(user.getId()+"ipcSqr5", 10000);
		 jedis.set(user.getId()+"ipcSqr6", sqrFmrName6.toString());
		 jedis.expire(user.getId()+"ipcSqr6", 10000);
		 jedis.set(user.getId()+"ipcSqr7", sqrFmrName7.toString());
		 jedis.expire(user.getId()+"ipcSqr7", 10000);
		 jedis.set(user.getId()+"ipcSqr8", sqrFmrName8.toString());
		 jedis.expire(user.getId()+"ipcSqr8", 10000);
		 jedis.set(user.getId()+"ipcSqr9", sqrFmrName9.toString());
		 jedis.expire(user.getId()+"ipcSqr9", 10000);
		map.put("series", series);
		map.put("categories", categories);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取统计信息json 形成表格（ipc申请人）
	 */
	public void getPatentSumData_dataGrid_ipcSQR() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] ncsqrNCName=jedis.get(user.getId()+"ipcSqrIpcName").split(",");
//		String[] ncsqrSQRName=jedis.get(user.getId()+"ipcSqrSqrName").split(",");
		List<String> categories = new ArrayList<String>();
		String[] ssFMR = jedis.get(user.getId() + "sqr").split("_");
		String tempFMR = null;
		String[] ncsqrSQRName = new String[ssFMR.length < 10 ? ssFMR.length : 10]; // 不止一个地方用得着
		for (int i = 0; i < (ssFMR.length < 10 ? ssFMR.length : 10); i++) {
			tempFMR = ssFMR[i];
			ncsqrSQRName[i] = tempFMR.substring(0, tempFMR.indexOf("="));
			categories.add(ncsqrSQRName[i]);
		}
		String[] ncsqr0=jedis.get(user.getId()+"ipcSqr0").split(",");
		String[] ncsqr1=jedis.get(user.getId()+"ipcSqr1").split(",");
		String[] ncsqr2=jedis.get(user.getId()+"ipcSqr2").split(",");
		String[] ncsqr3=jedis.get(user.getId()+"ipcSqr3").split(",");
		String[] ncsqr4=jedis.get(user.getId()+"ipcSqr4").split(",");
		String[] ncsqr5=jedis.get(user.getId()+"ipcSqr5").split(",");
		String[] ncsqr6=jedis.get(user.getId()+"ipcSqr6").split(",");
		String[] ncsqr7=jedis.get(user.getId()+"ipcSqr7").split(",");
		String[] ncsqr8=jedis.get(user.getId()+"ipcSqr8").split(",");
		String[] ncsqr9=jedis.get(user.getId()+"ipcSqr9").split(",");
		List<Map> records = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		total=new Long((long)ncsqr0.length);
		Map<String, Object> m0 = new HashMap<String, Object>(); 
		m0.put("sqr", "申请人");
		for(int j=0;j<ncsqrNCName.length;j++){
			m0.put("ipcSQRName"+j,ncsqrNCName[j]+"_");
		}
		for(int j=9;j>=ncsqrNCName.length;j--){
			m0.put("ipcSQRName"+j,"_");
		}
//		m0.put("ipcSQRName0",ncsqrNCName[0]+"_");
//		m0.put("ipcSQRName1",ncsqrNCName[1]+"_");
//		m0.put("ipcSQRName2",ncsqrNCName[2]+"_");
//		m0.put("ipcSQRName3",ncsqrNCName[3]+"_");
//		m0.put("ipcSQRName4",ncsqrNCName[4]+"_");
//		m0.put("ipcSQRName5",ncsqrNCName[5]+"_");
//		m0.put("ipcSQRName6",ncsqrNCName[6]+"_");
//		m0.put("ipcSQRName7",ncsqrNCName[7]+"_");
//		m0.put("ipcSQRName8",ncsqrNCName[8]+"_");
//		m0.put("ipcSQRName9",ncsqrNCName[9]+"_");
		records.add(m0);
		for (int i = 0; i < (ncsqrSQRName.length<10?ncsqrSQRName.length:10); i++) {
			Map<String, Object> m = new HashMap<String, Object>(); 
			m.put("sqr", ncsqrSQRName[i]);
			if(ncsqr0.length>i && !"".equals(ncsqr0[0])){
				m.put("ipcSQRName0",ncsqr0[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("ipcSQRName0","-_");
			}
			if(ncsqr1.length>i && !"".equals(ncsqr1[0])){
				m.put("ipcSQRName1",ncsqr1[i]+"_"+ncsqrNCName[1]);
			} else{
				m.put("ipcSQRName1","-_");
			}
			if(ncsqr2.length>i && !"".equals(ncsqr2[0])){
				m.put("ipcSQRName2",ncsqr2[i]+"_"+ncsqrNCName[2]);
			} else{
				m.put("ipcSQRName2","-_");
			}
			if(ncsqr3.length>i && !"".equals(ncsqr3[0])){
				m.put("ipcSQRName3",ncsqr3[i]+"_"+ncsqrNCName[3]);
			} else{
				m.put("ipcSQRName3","-_");
			}
			if(ncsqr4.length>i && !"".equals(ncsqr4[0])){
				m.put("ipcSQRName4",ncsqr4[i]+"_"+ncsqrNCName[4]);
			} else{
				m.put("ipcSQRName4","-_");
			}
			if(ncsqr5.length>i && !"".equals(ncsqr5[0])){
				m.put("ipcSQRName5",ncsqr5[i]+"_"+ncsqrNCName[5]);
			} else{
				m.put("ipcSQRName5","-_");
			}
			if(ncsqr6.length>i && !"".equals(ncsqr6[0])){
				m.put("ipcSQRName6",ncsqr6[i]+"_"+ncsqrNCName[6]);
			} else{
				m.put("ipcSQRName6","-_");
			}
			if(ncsqr7.length>i && !"".equals(ncsqr7[0])){
				m.put("ipcSQRName7",ncsqr7[i]+"_"+ncsqrNCName[7]);
			} else{
				m.put("ipcSQRName7","-_");
			}
			if(ncsqr8.length>i && !"".equals(ncsqr8[0])){
				m.put("ipcSQRName8",ncsqr8[i]+"_"+ncsqrNCName[8]);
			} else{
				m.put("ipcSQRName8","-_");
			}
			if(ncsqr9.length>i && !"".equals(ncsqr9[0])){
				m.put("ipcSQRName9",ncsqr9[i]+"_"+ncsqrNCName[9]);
			} else{
				m.put("ipcSQRName9","-_");
			}
//			m.put("ipcSQRName0",ncsqr0[i]+"_"+ncsqrNCName[0]);
//			m.put("ipcSQRName1",ncsqr1[i]+"_"+ncsqrNCName[1]);
//			m.put("ipcSQRName2",ncsqr2[i]+"_"+ncsqrNCName[2]);
//			m.put("ipcSQRName3",ncsqr3[i]+"_"+ncsqrNCName[3]);
//			m.put("ipcSQRName4",ncsqr4[i]+"_"+ncsqrNCName[4]);
//			m.put("ipcSQRName5",ncsqr5[i]+"_"+ncsqrNCName[5]);
//			m.put("ipcSQRName6",ncsqr6[i]+"_"+ncsqrNCName[6]);
//			m.put("ipcSQRName7",ncsqr7[i]+"_"+ncsqrNCName[7]);
//			m.put("ipcSQRName8",ncsqr8[i]+"_"+ncsqrNCName[8]);
//			m.put("ipcSQRName9",ncsqr9[i]+"_"+ncsqrNCName[9]);
			records.add(m);
		}
		map.put("total", total);
		map.put("rows", records);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取统计信息json 形成报表图形（申请人技术骨干）
	 * 
	 * 
	 */
	public void getPatentSumData_fmr() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> series = new ArrayList<Map>();
		List<String> categories = new ArrayList<String>();
		 String[] ss= jedis.get(user.getId()+"sqr").split("_");
		 String[] ssFMR= jedis.get(user.getId()+"fmr").split("_");
		 String temp=null;
		 String tempFMR=null;
		 String[] strBurArrary=new String[10];   //不止一个地方用得着
		 String[] strFMRBurArrary=new String[10];   //不止一个地方用得着
		 for(int i=0;i<(ssFMR.length<10?ssFMR.length:10);i++){
			 tempFMR=ssFMR[i];
			 strFMRBurArrary[i]=tempFMR.substring(0, tempFMR.indexOf("="));
			 categories.add(strFMRBurArrary[i]);
		 }
		 for(int i=0;i<(ss.length<10?ss.length:10);i++){
			 temp=ss[i];
			 if(temp!=null&&!"".equals(temp)){
				    strBurArrary[i]=temp.substring(0, temp.indexOf("="));
					for(int j=0;j<10;j++){
						jedis.del(user.getId()+strBurArrary[i]+strFMRBurArrary[j]);
					}
				}
		 }
		 String[] applJly=jedis.get(user.getId()+"applJly").split("_");
		 applJly[applJly.length-1]=applJly[applJly.length-1].substring(0, applJly[applJly.length-1].length()-3);    ////删除末尾的Jly
		 String[] inventorJly=jedis.get(user.getId()+"inventorJly").split("_");
		 inventorJly[inventorJly.length-1]=inventorJly[inventorJly.length-1].substring(0, inventorJly[inventorJly.length-1].length()-3);    ////删除末尾的Jly
		for(int i=0;i<applJly.length;i++){
			String srtSQR=applJly[i];//获得申请人
			if((srtSQR!=null)&&(!"".equals(srtSQR))){    //申请人不空
				if(srtSQR.contains(",")){      //多个申请人
					String[] strs=srtSQR.split(",");
					for(int j=0;j<strs.length;j++){
						for(int k=0;k<(ss.length<10?ss.length:10);k++){   //申请人跟10个要统计的进行比对
							if(strBurArrary[k].equals(strs[j])){   //比对成功
								String strFMR=inventorJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
								if((strFMR!=null)&&(!"".equals(strFMR))){
									if(strFMR.contains(",")){
										String[] strFMRs=strFMR.split(",");
										for(int jj=0;jj<strFMRs.length;jj++){
											for(int ii=0;ii<(ssFMR.length<10?ssFMR.length:10);ii++){
												if(strFMRBurArrary[ii].equals(strFMRs[jj])){
													jedis.append(user.getId()+strBurArrary[k]+strFMRBurArrary[ii], "1");
												}
											}
										}
									}else{
										for(int ii=0;ii<(ssFMR.length<10?ssFMR.length:10);ii++){
											if(strFMRBurArrary[ii].equals(strFMR)){
												jedis.append(user.getId()+strBurArrary[k]+strFMRBurArrary[ii], "1");
											}
										}
									}
								}
							}
						}
					}
				}else{  //单个申请人
					for(int k=0;k<(ss.length<10?ss.length:10);k++){   //申请人跟10个要统计的进行比对
						if(strBurArrary[k].equals(srtSQR)){   //比对成功
							String strFMR=inventorJly[i];     //这里可以jedis.append(srtSHR+strYear,"1");
							if((strFMR!=null)&&(!"".equals(strFMR))){
								if(strFMR.contains(",")){
									String[] strFMRs=strFMR.split(",");
									for(int jj=0;jj<strFMRs.length;jj++){
										for(int ii=0;ii<(ssFMR.length<10?ssFMR.length:10);ii++){
											if(strFMRBurArrary[ii].equals(strFMRs[jj])){
												jedis.append(user.getId()+srtSQR+strFMRBurArrary[ii], "1");
											}
										}
									}
								}else{
									for(int ii=0;ii<(ssFMR.length<10?ssFMR.length:10);ii++){
										if(strFMRBurArrary[ii].equals(strFMR)){
											jedis.append(user.getId()+srtSQR+strFMRBurArrary[ii], "1");
										}
									}
								}
							}
						}
					}
				}
			}
		}
		StringBuilder sqrName=new StringBuilder();    //申请人名称
		StringBuilder fmrName=new StringBuilder();    //ipc名称
		StringBuilder sqrFmrName0=new StringBuilder();
		StringBuilder sqrFmrName1=new StringBuilder();
		StringBuilder sqrFmrName2=new StringBuilder();
		StringBuilder sqrFmrName3=new StringBuilder();
		StringBuilder sqrFmrName4=new StringBuilder();
		StringBuilder sqrFmrName5=new StringBuilder();
		StringBuilder sqrFmrName6=new StringBuilder();
		StringBuilder sqrFmrName7=new StringBuilder();
		StringBuilder sqrFmrName8=new StringBuilder();
		StringBuilder sqrFmrName9=new StringBuilder();
		for(int i=0;i<(ss.length<10?ss.length:10);i++){
			Map<String, Object> m = new HashMap<String, Object>();
			 List<BigInteger> data = new ArrayList<BigInteger>();
			 sqrName.append(strBurArrary[i]).append(",");
			 fmrName.append(strFMRBurArrary[i]).append(",");
			 for(int j=0;j<(ssFMR.length<10?ssFMR.length:10);j++){
				 Long  strLong=jedis.strlen(user.getId()+strBurArrary[i]+strFMRBurArrary[j]);
				 data.add(BigInteger.valueOf(strLong));
				 if(i==0){
					 sqrFmrName0.append(strLong).append(",");
				 }else if(i==1){
					 sqrFmrName1.append(strLong).append(",");
				 }else if(i==2){
					 sqrFmrName2.append(strLong).append(",");
				 }else if(i==3){
					 sqrFmrName3.append(strLong).append(",");
				 }else if(i==4){
					 sqrFmrName4.append(strLong).append(",");
				 }else if(i==5){
					 sqrFmrName5.append(strLong).append(",");
				 }else if(i==6){
					 sqrFmrName6.append(strLong).append(",");
				 }else if(i==7){
					 sqrFmrName7.append(strLong).append(",");
				 }else if(i==8){
					 sqrFmrName8.append(strLong).append(",");
				 }else if(i==9){
					 sqrFmrName9.append(strLong).append(",");
				 }
			 }
			 m.put("name",strBurArrary[i]);
			 m.put("data", data);
			 series.add(m);
		}
		if(sqrName.length()>0){
			sqrName.deleteCharAt(sqrName.length()-1);
		}
		if(fmrName.length()>0){
			fmrName.deleteCharAt(fmrName.length()-1);
		}
		if(sqrFmrName0.length()>0){
			sqrFmrName0.deleteCharAt(sqrFmrName0.length()-1);
		}
		if(sqrFmrName1.length()>0){
			sqrFmrName1.deleteCharAt(sqrFmrName1.length()-1);
		}
		if(sqrFmrName2.length()>0){
			sqrFmrName2.deleteCharAt(sqrFmrName2.length()-1);
		}
		if(sqrFmrName3.length()>0){
			sqrFmrName3.deleteCharAt(sqrFmrName3.length()-1);
		}
		if(sqrFmrName4.length()>0){
			sqrFmrName4.deleteCharAt(sqrFmrName4.length()-1);
		}
		if(sqrFmrName5.length()>0){
			sqrFmrName5.deleteCharAt(sqrFmrName5.length()-1);
		}
		if(sqrFmrName6.length()>0){
			sqrFmrName6.deleteCharAt(sqrFmrName6.length()-1);
		}
		if(sqrFmrName7.length()>0){
			sqrFmrName7.deleteCharAt(sqrFmrName7.length()-1);
		}
		if(sqrFmrName8.length()>0){
			sqrFmrName8.deleteCharAt(sqrFmrName8.length()-1);
		}
		if(sqrFmrName9.length()>0){
			sqrFmrName9.deleteCharAt(sqrFmrName9.length()-1);
		}
		 jedis.set(user.getId()+"sqrFmrSqrName", sqrName.toString());
		 jedis.expire(user.getId()+"sqrFmrSqrName", 10000);
		 jedis.set(user.getId()+"sqrFmrFmrName", fmrName.toString());
		 jedis.expire(user.getId()+"sqrFmrFmrName", 10000);
		 jedis.set(user.getId()+"sqrFmr0", sqrFmrName0.toString());
		 jedis.expire(user.getId()+"sqrFmr0", 10000);
		 jedis.set(user.getId()+"sqrFmr1", sqrFmrName1.toString());
		 jedis.expire(user.getId()+"sqrFmr1", 10000);
		 jedis.set(user.getId()+"sqrFmr2", sqrFmrName2.toString());
		 jedis.expire(user.getId()+"sqrFmr2", 10000);
		 jedis.set(user.getId()+"sqrFmr3", sqrFmrName3.toString());
		 jedis.expire(user.getId()+"sqrFmr3", 10000);
		 jedis.set(user.getId()+"sqrFmr4", sqrFmrName4.toString());
		 jedis.expire(user.getId()+"sqrFmr4", 10000);
		 jedis.set(user.getId()+"sqrFmr5", sqrFmrName5.toString());
		 jedis.expire(user.getId()+"sqrFmr5", 10000);
		 jedis.set(user.getId()+"sqrFmr6", sqrFmrName6.toString());
		 jedis.expire(user.getId()+"sqrFmr6", 10000);
		 jedis.set(user.getId()+"sqrFmr7", sqrFmrName7.toString());
		 jedis.expire(user.getId()+"sqrFmr7", 10000);
		 jedis.set(user.getId()+"sqrFmr8", sqrFmrName8.toString());
		 jedis.expire(user.getId()+"sqrFmr8", 10000);
		 jedis.set(user.getId()+"sqrFmr9", sqrFmrName9.toString());
		 jedis.expire(user.getId()+"sqrFmr9", 10000);
		map.put("series", series);
		map.put("categories", categories);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取统计信息json 形成表格（申请人技术骨干）
	 */
	public void getPatentSumData_dataGrid_sqrFMR() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());   //使用用户id生成唯一UID 进行远程检索
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] ncsqrNCName=jedis.get(user.getId()+"sqrFmrSqrName").split(",");
//		String[] ncsqrSQRName=jedis.get(user.getId()+"sqrFmrFmrName").split(",");

		List<String> categories = new ArrayList<String>();
		 String[] ssFMR= jedis.get(user.getId()+"fmr").split("_");
		 String tempFMR=null;
		 String[] ncsqrSQRName=new String[ssFMR.length<10?ssFMR.length:10];   //不止一个地方用得着
		 for(int i=0;i<(ssFMR.length<10?ssFMR.length:10);i++){
			 tempFMR=ssFMR[i];
			 ncsqrSQRName[i]=tempFMR.substring(0, tempFMR.indexOf("="));
			 categories.add(ncsqrSQRName[i]);
		 }
		String[] ncsqr0=jedis.get(user.getId()+"sqrFmr0").split(",");
		String[] ncsqr1=jedis.get(user.getId()+"sqrFmr1").split(",");
		String[] ncsqr2=jedis.get(user.getId()+"sqrFmr2").split(",");
		String[] ncsqr3=jedis.get(user.getId()+"sqrFmr3").split(",");
		String[] ncsqr4=jedis.get(user.getId()+"sqrFmr4").split(",");
		String[] ncsqr5=jedis.get(user.getId()+"sqrFmr5").split(",");
		String[] ncsqr6=jedis.get(user.getId()+"sqrFmr6").split(",");
		String[] ncsqr7=jedis.get(user.getId()+"sqrFmr7").split(",");
		String[] ncsqr8=jedis.get(user.getId()+"sqrFmr8").split(",");
		String[] ncsqr9=jedis.get(user.getId()+"sqrFmr9").split(",");
		List<Map> records = new ArrayList<Map>();
		Map<String, Object> map = new HashMap<String, Object>();
		total=new Long((long)ncsqr0.length);
		Map<String, Object> m0 = new HashMap<String, Object>(); 
		m0.put("fmr", "技术骨干");
		for(int j=0;j<ncsqrNCName.length;j++){
			m0.put("sqrfmrName"+j,ncsqrNCName[j]+"_");
		}
		for(int j=9;j>=ncsqrNCName.length;j--){
			m0.put("sqrfmrName"+j,"_");
		}
//		m0.put("sqrfmrName0",ncsqrNCName[0]+"_");
//		m0.put("sqrfmrName1",ncsqrNCName[1]+"_");
//		m0.put("sqrfmrName2",ncsqrNCName[2]+"_");
//		m0.put("sqrfmrName3",ncsqrNCName[3]+"_");
//		m0.put("sqrfmrName4",ncsqrNCName[4]+"_");
//		m0.put("sqrfmrName5",ncsqrNCName[5]+"_");
//		m0.put("sqrfmrName6",ncsqrNCName[6]+"_");
//		m0.put("sqrfmrName7",ncsqrNCName[7]+"_");
//		m0.put("sqrfmrName8",ncsqrNCName[8]+"_");
//		m0.put("sqrfmrName9",ncsqrNCName[9]+"_");
		records.add(m0);
		for (int i = 0; i < (ncsqrSQRName.length<10?ncsqrSQRName.length:10); i++) {
			Map<String, Object> m = new HashMap<String, Object>(); 
			m.put("fmr", ncsqrSQRName[i]);
			if(ncsqr0.length>i && !"".equals(ncsqr0[0])){
				m.put("sqrfmrName0",ncsqr0[i]+"_"+ncsqrNCName[0]);
			} else{
				m.put("sqrfmrName0","-_");
			}
			if(ncsqr1.length>i && !"".equals(ncsqr1[0])){
				m.put("sqrfmrName1",ncsqr1[i]+"_"+ncsqrNCName[1]);
			} else{
				m.put("sqrfmrName1","-_");
			}
			if(ncsqr2.length>i && !"".equals(ncsqr2[0])){
				m.put("sqrfmrName2",ncsqr2[i]+"_"+ncsqrNCName[2]);
			} else{
				m.put("sqrfmrName2","-_");
			}
			if(ncsqr3.length>i && !"".equals(ncsqr3[0])){
				m.put("sqrfmrName3",ncsqr3[i]+"_"+ncsqrNCName[3]);
			} else{
				m.put("sqrfmrName3","-_");
			}
			if(ncsqr4.length>i && !"".equals(ncsqr4[0])){
				m.put("sqrfmrName4",ncsqr4[i]+"_"+ncsqrNCName[4]);
			} else{
				m.put("sqrfmrName4","-_");
			}
			if(ncsqr5.length>i && !"".equals(ncsqr5[0])){
				m.put("sqrfmrName5",ncsqr5[i]+"_"+ncsqrNCName[5]);
			} else{
				m.put("sqrfmrName5","-_");
			}
			if(ncsqr6.length>i && !"".equals(ncsqr6[0])){
				m.put("sqrfmrName6",ncsqr6[i]+"_"+ncsqrNCName[6]);
			} else{
				m.put("sqrfmrName6","-_");
			}
			if(ncsqr7.length>i && !"".equals(ncsqr7[0])){
				m.put("sqrfmrName7",ncsqr7[i]+"_"+ncsqrNCName[7]);
			} else{
				m.put("sqrfmrName7","-_");
			}
			if(ncsqr8.length>i && !"".equals(ncsqr8[0])){
				m.put("sqrfmrName8",ncsqr8[i]+"_"+ncsqrNCName[8]);
			} else{
				m.put("sqrfmrName8","-_");
			}
			if(ncsqr9.length>i && !"".equals(ncsqr9[0])){
				m.put("sqrfmrName9",ncsqr9[i]+"_"+ncsqrNCName[9]);
			} else{
				m.put("sqrfmrName9","-_");
			}
//			m.put("sqrfmrName0",ncsqr0[i]+"_"+ncsqrNCName[0]);
//			m.put("sqrfmrName1",ncsqr1[i]+"_"+ncsqrNCName[1]);
//			m.put("sqrfmrName2",ncsqr2[i]+"_"+ncsqrNCName[2]);
//			m.put("sqrfmrName3",ncsqr3[i]+"_"+ncsqrNCName[3]);
//			m.put("sqrfmrName4",ncsqr4[i]+"_"+ncsqrNCName[4]);
//			m.put("sqrfmrName5",ncsqr5[i]+"_"+ncsqrNCName[5]);
//			m.put("sqrfmrName6",ncsqr6[i]+"_"+ncsqrNCName[6]);
//			m.put("sqrfmrName7",ncsqr7[i]+"_"+ncsqrNCName[7]);
//			m.put("sqrfmrName8",ncsqr8[i]+"_"+ncsqrNCName[8]);
//			m.put("sqrfmrName9",ncsqr9[i]+"_"+ncsqrNCName[9]);
			records.add(m);
		}
		map.put("total", total);
		map.put("rows", records);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String uploadFormularFile() throws InvalidFormatException, IOException, ParseException{
			File savefile = this.fileupload;
			if (savefile != null) {

				Workbook rwb = null;
				rwb =  WorkbookFactory.create(savefile);
				int numberOfSheets = rwb.getNumberOfSheets();
				Sheet sheet = rwb.getSheetAt(0);
				int rowNumber = sheet.getLastRowNum();
				Row row = sheet.getRow(0);
				SearchFormula searchFormula;
				for (int i = 1; i <= rowNumber; i++) {
					searchFormula = new SearchFormula();
					row = sheet.getRow(i);
					if (row == null)
						continue;
				
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 E");
					String searchtime = row.getCell(0).toString();
					Date date=simpleDateFormat.parse(searchtime);
					
					String searchno =Float.valueOf(row.getCell(1).toString()).intValue()+"";
					String searchformula = row.getCell(2).toString();
					String hits;
					if(row.getCell(3).toString().equals("")){
						hits="0";
					}
					else{
						hits = Float.valueOf(row.getCell(3).toString()).intValue()+"";
					}
					
					String searchscopeupload=ServletActionContext.getRequest().getParameter("searchscopeupload");
					Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());
					
					searchFormula.setAlterTime(date);
					searchFormula.setFormula(searchformula);
					searchFormula.setHits(Long.valueOf(hits));
					searchFormula.setItemID(Integer.valueOf(searchno));
					searchFormula.setSearchscope(Integer.valueOf(searchscopeupload.trim()));
					searchFormula.setUser(user);
					
					searchFormualService.save(searchFormula);
					
					//System.out.println("searchtime:" + date+" searchno:"+searchno+" searchformula:"+searchformula+" hits:"+hits+" searchscopeupload:"+searchscopeupload);
				}
				
			}
		return "redirectexpertSearchUI";
	}
	
	

}
