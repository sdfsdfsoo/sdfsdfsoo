package patentsearch.web.action.front.user;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.axis2.databinding.types.xsd.DateTime;
import org.apache.struts2.ServletActionContext;
import patentsearch.bean.util.xml.XMLUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


import patentsearch.bean.base.LegalStatusDetail;
import patentsearch.bean.base.QueryResult;
import patentsearch.bean.cndescriptionitem.CNDescriptionItem;
import patentsearch.bean.user.PatentCategory;
import patentsearch.bean.user.PatentStoreInfo;
import patentsearch.bean.user.Users;
import patentsearch.bean.util.file.StoreDirectory;
import patentsearch.bean.util.file.SwitchToTreeJson;
import patentsearch.bean.util.xml.DateUtil;
import patentsearch.service.legalstatus.LegalStatusService;
import patentsearch.service.user.CategoryService;
import patentsearch.service.user.PatentStoreInfoService;
import patentsearch.utils.base.WebTool;


/*
 *   目录的处理
 */
@Controller
@Scope("prototype")
public class CategoryManager {

	@Resource
	LegalStatusService legalStatusService;
	@Resource
	CategoryService categoryService;
	@Resource
	PatentStoreInfoService patentStoreInfoService;
	
	// 用于接收datagrid查询参数并用于回显以完成翻页请求
	private Long total;
	private Integer rows = 10;
	private Integer page = 1;
	private String sort;
	private String order;

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

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
	/*
	 * 异步获取个性库分类  AJAX（关注库）
	 */
	public void getTreeJsonAjax0() {
		Users user= WebTool.getLoginedUser(ServletActionContext.getRequest());
		 StringBuilder wherejpql=new StringBuilder();
		 wherejpql.append(" o.user.id= "+user.getId()+" and o.categoryType=0 ");
		 //QueryResult<PatentCategory> queryResult= categoryService.getScrollData(-1, -1, wherejpql.toString(), null);
System.out.println(new Date().getTime());		 
		 QueryResult<PatentCategory> queryResult= categoryService.getProTree(user, 0);
System.out.println(new Date().getTime());			 
		 QueryResult<PatentStoreInfo> collectionsResult = null;
		 
		  StoreDirectory root = new StoreDirectory("0","aaa",null);
		 SwitchToTreeJson  tree = new SwitchToTreeJson();
		 if(queryResult.getTotalrecord()==0){
				PatentCategory patentCategory=new PatentCategory();
				patentCategory.setUser(user);
				patentCategory.setName("我的收藏夹");
				patentCategory.setParent(categoryService.find(1));
				categoryService.save(patentCategory);
				StoreDirectory storeDirectory=new StoreDirectory(patentCategory.getId()+"", patentCategory.getName(), patentCategory.getParent().getId()+"");
			 	tree.addNode(storeDirectory);
		 }else {
				 for(int i=0;i<queryResult.getTotalrecord();i++){
					 PatentCategory category= queryResult.getResultlist().get(i);
					 
					 //StringBuilder wherejpql2=new StringBuilder();
					 //wherejpql2.append("o.patentCategory.id="+category.getId());
					 //collectionsResult= patentStoreInfoService.getScrollData(-1, -1, wherejpql2.toString(), null,null);
					 //Long total1 =collectionsResult.getTotalrecord();
					 int total1 = patentStoreInfoService.getNodeNum(category.getId());
				 	//StoreDirectory storeDirectory=new StoreDirectory(category.getId()+"", category.getName(), category.getParent().getId()+"");
				 	StoreDirectory storeDirectory=new StoreDirectory(category.getId()+"", category.getName()+"("+total1+")", category.getParent().getId()+"");
				 	tree.addNode(storeDirectory);
				 }
		 }		 
		try {
			String  str=tree.getTreeJson(tree, root);
//			System.out.println(str);
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(str);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 异步获取个性库分类  AJAX（企业库）
	 */
	public void getTreeJsonAjax1() {
		Users user= WebTool.getLoginedUser(ServletActionContext.getRequest());
		 StringBuilder wherejpql=new StringBuilder();
		 wherejpql.append(" o.user.id= "+user.getId()+" and o.categoryType=1 ");
		 //QueryResult<PatentCategory> queryResult= categoryService.getScrollData(-1, -1, wherejpql.toString(), null);
		 System.out.println("2  "+new Date().getTime());	
		 QueryResult<PatentCategory> queryResult= categoryService.getProTree(user, 1);
		 System.out.println("2  "+new Date().getTime());	
		 QueryResult<PatentStoreInfo> collectionsResult = null;
		 
		 
		  StoreDirectory root = new StoreDirectory("0","aaa",null);
		 SwitchToTreeJson  tree = new SwitchToTreeJson();
		 if(queryResult.getTotalrecord()==0){
				PatentCategory patentCategory=new PatentCategory();
				patentCategory.setUser(user);
				patentCategory.setName("我的收藏夹");
				patentCategory.setCategoryType(1);
				patentCategory.setParent(categoryService.find(1));
				categoryService.save(patentCategory);
				StoreDirectory storeDirectory=new StoreDirectory(patentCategory.getId()+"", patentCategory.getName(), patentCategory.getParent().getId()+"");
			 	tree.addNode(storeDirectory);
		 }else {
				 for(int i=0;i<queryResult.getTotalrecord();i++){
					 PatentCategory category= queryResult.getResultlist().get(i);
					 
					 //StringBuilder wherejpql2=new StringBuilder();
					 //wherejpql2.append("o.patentCategory.id="+category.getId());
					 int total1 = patentStoreInfoService.getNodeNum(category.getId());
					 //collectionsResult= patentStoreInfoService.getScrollData(-1, -1, wherejpql2.toString(), null,null);
					 //Long total1 =collectionsResult.getTotalrecord();
					 
					 
				 	//StoreDirectory storeDirectory=new StoreDirectory(category.getId()+"", category.getName(), category.getParent().getId()+"");
				 	StoreDirectory storeDirectory=new StoreDirectory(category.getId()+"", category.getName()+"("+total1+")", category.getParent().getId()+"");
				 	tree.addNode(storeDirectory);
				 }
		 }		 
		try {
			String  str=tree.getTreeJson(tree, root);
//			System.out.println(str);
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(str);
//System.out.println(str);			
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 异步修改tree节点  名称
	 */
	public void editTreeNode() {
		String id= ServletActionContext.getRequest().getParameter("id");
		String text= ServletActionContext.getRequest().getParameter("text");
		String newname= ServletActionContext.getRequest().getParameter("newname");
		Map<String, Object> map = new HashMap<String, Object>();
		PatentCategory category=new PatentCategory();
		if(id!=null){	
			 category=categoryService.find(Integer.parseInt(id));
		}else{
			StringBuilder wherejpql=new StringBuilder();
			wherejpql.append(" o.name = '"+text+"'");
			QueryResult<PatentCategory> categoryResult=  categoryService.getScrollData(-1, -1, wherejpql.toString(), null);
			int totalrecords=(int)categoryResult.getTotalrecord();	
		    category= categoryResult.getResultlist().get(totalrecords-1);
		}
		category.setName(newname);
		try{
			categoryService.save(category);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		map.clear();
		map.put("msg", "分类修改成功！");

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
	 * 异步添加tree节点  AJAX
	 */
	public void addTreeNode() {
		
		Users user= WebTool.getLoginedUser(ServletActionContext.getRequest());
		
		String id= ServletActionContext.getRequest().getParameter("id");
		String text= ServletActionContext.getRequest().getParameter("text");
		String categorytype= ServletActionContext.getRequest().getParameter("categorytype");
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			PatentCategory category=new PatentCategory();
			if(id!=null){	
				 category=categoryService.find(Integer.parseInt(id));
			}else{
				StringBuilder wherejpql=new StringBuilder();
				wherejpql.append(" o.name = '"+text+"'");
				QueryResult<PatentCategory> categoryResult=  categoryService.getScrollData(-1, -1, wherejpql.toString(), null);
				int totalrecords=(int)categoryResult.getTotalrecord();	
			    category= categoryResult.getResultlist().get(totalrecords-1);
			}
				
				PatentCategory child=new PatentCategory();
				child.setName(text);
				child.setUser(user);
				child.setParent(category);
				child.setCategoryType(Integer.parseInt(categorytype));//
				Set<PatentCategory> children=new HashSet<PatentCategory>();
				children.add(child);
				category.setChildren(children);
				categoryService.save(child);
				
				
				map.clear();
				map.put("msg", "添加分类成功！");
				
				JSONObject json = new JSONObject();
					json.put("id", child.getId());
					json.put("parent", 	child.getParent().getId());
				//return json.toString();
				
				try {
					ServletActionContext.getResponse().setCharacterEncoding("utf-8");
					ServletActionContext.getResponse().getWriter().print(json.toString());
					ServletActionContext.getResponse().flushBuffer();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
/*
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
	/*
	 * 异步删除tree节点  AJAX
	 */
	public void deleteTreeNode() {
		String id= ServletActionContext.getRequest().getParameter("id");
		String text= ServletActionContext.getRequest().getParameter("text");
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			PatentCategory category=new PatentCategory();
			if(id!=null){	
				 category=categoryService.find(Integer.parseInt(id));
			}else{
				StringBuilder wherejpql=new StringBuilder();
				wherejpql.append(" o.name = '"+text+"'");
				QueryResult<PatentCategory> categoryResult=  categoryService.getScrollData(-1, -1, wherejpql.toString(), null);
				int totalrecords=(int)categoryResult.getTotalrecord();	
			    category= categoryResult.getResultlist().get(totalrecords-1);
			}
			if(category.getChildren().isEmpty()&&category.getParent().getId()!=1){
				StringBuilder wheresql=new StringBuilder();
				wheresql.append("o.patentCategory.id="+category.getId());
			    QueryResult<PatentStoreInfo> queryResult=patentStoreInfoService.getScrollData(-1, -1,wheresql.toString(), null);
				List<PatentStoreInfo> infos=new ArrayList<PatentStoreInfo>();
				infos=queryResult.getResultlist();
			    if(infos.size()>0){
			    	for(int i=0;i<infos.size();i++){
			    		patentStoreInfoService.delete(infos.get(i).getId());
			    	}
				}
				categoryService.delete(category.getId());//不能直接删除，因外保存专利表外键连着改id
				map.clear();
				map.put("result", "success");
				map.put("msg", "该节点已删除！");
			}else if(category.getParent().getId()==1){
				map.clear();
				map.put("result", "fail");
				map.put("msg", "该节点为根节点，不能删除！");
			}else{
				map.clear();
				map.put("result", "fail");
				map.put("msg", "该节点非叶子节点，暂不能删除！");
			}
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
	
/*	public String getLeagelStatic(CNDescriptionItem cnDescriptionItemTemp) {
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
	}*/
	
	
	/**
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
	 * 异步添加专利号
	 */
	public void addPatentIn() {
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest()); 
		String appnoValue= ServletActionContext.getRequest().getParameter("appnoValue");
		String searchscope= ServletActionContext.getRequest().getParameter("searchscope");
		String titleValue= ServletActionContext.getRequest().getParameter("title");
		String apdValue= ServletActionContext.getRequest().getParameter("apd");
		String applValue= ServletActionContext.getRequest().getParameter("appl");
		String id= ServletActionContext.getRequest().getParameter("id");
		String text= ServletActionContext.getRequest().getParameter("text");
		String legalstateold="";
		CNDescriptionItem cNDescriptionItem=XMLUtil.getCNDescriptionItemByAppno(appnoValue);
		int legalStateInt=getLegalStatusType(cNDescriptionItem.getAppno(),cNDescriptionItem.getPubnr(),
				cNDescriptionItem.getPud(),cNDescriptionItem.getAppnr(),cNDescriptionItem.getAppd());
				
		if(legalStatusService.getLegalStatusInfoByAppno(XMLUtil.getCheckAppnoWithOutDot(appnoValue)) == null){
			legalstateold="无";
		} else {
			legalstateold= legalStatusService.getLegalStatusInfoByAppno(XMLUtil.getCheckAppnoWithOutDot(appnoValue)).equals("文件的公告送达")?"实质审查的生效":legalStatusService.getLegalStatusInfoByAppno(XMLUtil.getCheckAppnoWithOutDot(appnoValue));
		}
		
		//String legalstateold=
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuilder wherejpqlStrBul=new StringBuilder();
		int totalpatentStoreInfos=0;
		if(id!=null&&!"".equals(id)){
					wherejpqlStrBul.append(" o.appno = '"+appnoValue+"' and o.patentCategory.id = '"+id +"'");
					QueryResult<PatentStoreInfo> patentStoreInfoResult=  patentStoreInfoService.getScrollData(-1, -1, wherejpqlStrBul.toString(), null);
				    totalpatentStoreInfos=(int)patentStoreInfoResult.getTotalrecord();	
		}
		if(totalpatentStoreInfos==0){ 
			try{
				PatentStoreInfo patent=new PatentStoreInfo();
				if(id!=null&&!"".equals(id)){	
					patent.setPatentCategory(categoryService.find(Integer.parseInt(id)));
				}else{
					StringBuilder wherejpql=new StringBuilder();
					wherejpql.append(" o.name = '"+text+"' and o.user.id='"+user.getId()+"'");
					QueryResult<PatentCategory> categoryResult=  categoryService.getScrollData(-1, -1, wherejpql.toString(), null);
					int totalrecords=(int)categoryResult.getTotalrecord();	
					PatentCategory category= categoryResult.getResultlist().get(totalrecords-1);
					patent.setPatentCategory(category);
				}
				patent.setAppno(appnoValue);
				patent.setTitle(titleValue);
				patent.setApd(apdValue);
				patent.setAppl(applValue);
				
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
				
				
				
				
				
				if("DocDB".equals(searchscope)){
					patent.setSearchscope(1);
				}else{
					patent.setSearchscope(0);
				}
				patent.setLegalstateold(legalstateold);
				//patent.setLegalstatenew(legalstateold);
				map.clear();
				map.put("msg", "添加专利成功！");
				patentStoreInfoService.save(patent);
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			map.clear();
			map.put("msg", "您所添加的专利已存在！");
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
	
	public String profileTab1() {

		return "profileTab1";
	}
	
	public String profileTab2() {

		String text= ServletActionContext.getRequest().getParameter("nodetext");
		String mid= ServletActionContext.getRequest().getParameter("nodeid");
		
		ServletActionContext.getRequest().setAttribute("tabtext",text);
		ServletActionContext.getRequest().setAttribute("tabmid",mid);
		return "profileTab2";
	}
	public String profileTab3() {
		
		String text= ServletActionContext.getRequest().getParameter("nodetext");
		String mid= ServletActionContext.getRequest().getParameter("nodeid");
		
		ServletActionContext.getRequest().setAttribute("tabtext",text);
		ServletActionContext.getRequest().setAttribute("tabmid",mid);
		
		return "profileTab3";
	}
	
public void getPatentStoreInfo() {
	int page= Integer.valueOf(ServletActionContext.getRequest().getParameter("page"));
	int rows= Integer.valueOf(ServletActionContext.getRequest().getParameter("rows"));
		QueryResult<PatentStoreInfo> collectionsResult=null;
		String rowId=	 ServletActionContext.getRequest().getParameter("rowId");
		String id=	 ServletActionContext.getRequest().getParameter("id");
		String text= ServletActionContext.getRequest().getParameter("text");
		ServletActionContext.getRequest().setAttribute("rowId","");
		ServletActionContext.getRequest().setAttribute("id","");
		ServletActionContext.getRequest().setAttribute("text","");
		ServletActionContext.getRequest().setAttribute("nowpage",page);
		
		String searchPatentNo= ServletActionContext.getRequest().getParameter("searchPatentNo");
		String searchPatentName= ServletActionContext.getRequest().getParameter("searchPatentName");
		String searchApd= ServletActionContext.getRequest().getParameter("searchApd");
		String searchLegalState= ServletActionContext.getRequest().getParameter("searchLegalState");
		
		String order1=	 ServletActionContext.getRequest().getParameter("sort");
		String order2=	 ServletActionContext.getRequest().getParameter("order");
		String orderby = order1 + " " +order2;
		
		StringBuilder wherejpql=new StringBuilder();
		wherejpql.append("o.patentCategory.id="+id);
		
		
		
		if(rowId!=null){             //点击置顶会传来这个参数
			//System.out.println("rowId"+rowId);
			PatentStoreInfo infoSelected=	patentStoreInfoService.find(Integer.parseInt(rowId));
			infoSelected.setImportantLevel(new Date());
			//infoSelected.setImportantLevel(infoSelected.getImportantLevel()+1);
			patentStoreInfoService.update(infoSelected); 
		}
		
		
		LinkedHashMap<String, String> order = new LinkedHashMap<String, String>();
		
//		if (this.sort != null && !"".equals(this.sort) && this.order != null    //点击排序，会使this.sort    this.order不为空
//				&& !"".equals(this.order)) {
//			order.put(this.sort, this.order);
//			collectionsResult= patentStoreInfoService.getScrollData(-1, -1, wherejpql.toString(), null,order);
//		} else {
			//order.put("importantLevel","desc");
			//collectionsResult= patentStoreInfoService.getScrollData(-1, -1, wherejpql.toString(), null,order);
//		}
//		collectionsResult= patentStoreInfoService.getScrollData(-1, -1, wherejpql.toString(), null);
		//List<PatentStoreInfo> patentStoreInfoList=collectionsResult.getResultlist();
		StringBuffer str1 = new StringBuffer();
		StringBuffer str2 = new StringBuffer();
		if(searchPatentNo!=null&&!searchPatentNo.equals("")){
			str1.append(" and aa.appno like '%"+searchPatentNo+"%' ");
			str2.append(" and appno like '%"+searchPatentNo+"%' ");
		}
		if(searchPatentName!=null&&!searchPatentName.equals("")){
			str1.append(" and aa.title like '%"+searchPatentName+"%' ");
			str1.append(" and title like '%"+searchPatentName+"%' ");
		}
		if(searchApd!=null&&!searchApd.equals("")){
			str1.append(" and aa.apd like '%"+searchApd+"%' ");
			str1.append(" and apd like '%"+searchApd+"%' ");
		}
		if(searchLegalState!=null&&!searchLegalState.equals("")){
			str1.append(" and aa.legalstateold like '%"+searchLegalState+"%' ");
			str1.append(" and legalstateold like '%"+searchLegalState+"%' ");
		}
		
		List<Map> records = patentStoreInfoService.getNodeDetailList(Integer.valueOf(id.trim()),(page-1)*rows,orderby,str1.toString(),rows);
		int allRrecoderSum = patentStoreInfoService.getNodeDetailListCount(Integer.valueOf(id.trim()),str2.toString());
		Map<String, Object> map = new HashMap<String, Object>();
		total =Long.valueOf(records.size());
		/*if (patentStoreInfoList != null && patentStoreInfoList.size() > 0) {
			
			for (int i = 0; i < patentStoreInfoList.size(); i++) {
				Map<String, Object> m = new HashMap<String, Object>(); 
				PatentStoreInfo patentStoreInfo=patentStoreInfoList.get(i);
				m.put("id", patentStoreInfo.getId());
				m.put("appno", patentStoreInfo.getAppno());
				if(legalStatusService.getLegalStatusInfoByAppno(XMLUtil.getCheckAppnoWithOutDot(patentStoreInfo.getAppno())) == null){
					m.put("legalStatus", null);
				} else {
					m.put("legalStatus", legalStatusService.getLegalStatusInfoByAppno(XMLUtil.getCheckAppnoWithOutDot(patentStoreInfo.getAppno())).equals("文件的公告送达")?"实质审查的生效":legalStatusService.getLegalStatusInfoByAppno(XMLUtil.getCheckAppnoWithOutDot(patentStoreInfo.getAppno())));
				}
				m.put("appname", patentStoreInfo.getTitle());
				m.put("apd", "".equals(patentStoreInfo.getApd())?"":DateUtil.formatStrToStr(patentStoreInfo.getApd()));
				m.put("appl", patentStoreInfo.getAppl());
				m.put("time", DateUtil.dateToTextString(patentStoreInfo.getCreateTime()));
				m.put("searchscope", patentStoreInfo.getSearchscope());
				
				Date date = DateUtil.formatStrToDate(patentStoreInfo.getApd());
				Calendar ca = Calendar.getInstance();
				ca.add(Calendar.DATE,30);
				
				m.put("yearFeeDate", patentStoreInfo.getYearfeedate());
				m.put("legalstateold", patentStoreInfo.getLegalstateold());
				
				
				records.add(m);
			}
		}*/
		map.put("total", allRrecoderSum);
		map.put("rows", records);
		//System.out.println("收藏专利条数为:"+total);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void delPatentStoreInfo() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ids= ServletActionContext.getRequest().getParameter("ids");
		String[] idsStr= ids.split(",");
		for(int i=0;i<idsStr.length;i++){
		     Integer temp=Integer.parseInt(idsStr[i]);
			patentStoreInfoService.delete(temp);
		}
		map.clear();
		map.put("msg", "删除专利成功！");
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void payYearFee() throws ParseException, IOException{
		String id=ServletActionContext.getRequest().getParameter("id");
		//PatentStoreInfo patentStoreInfo=patentStoreInfoService.find(Integer.valueOf(id.trim()));
		String yearFeeDate=categoryService.getYearFeeDateById(Integer.valueOf(id.trim()));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
		Date yearDate = simpleDateFormat.parse(yearFeeDate);
		
		Calendar cl = Calendar.getInstance();
		cl.setTime(yearDate);
		cl.add(Calendar.YEAR, 1);
		String newYearDate = DateUtil.dateToTextString(cl.getTime());
		
		categoryService.updateYearDateById(id, newYearDate);
		
		ServletActionContext.getResponse().setCharacterEncoding("utf-8");
		ServletActionContext.getResponse().getWriter().print("成功");
	}

}
