package patentsearch.web.action.front.user;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import patentsearch.bean.base.QueryResult;
import patentsearch.bean.search.requestParameter.DoSearchParameter;
import patentsearch.bean.user.FormulaPackage;
import patentsearch.bean.user.SearchFormula;
import patentsearch.bean.user.Users;
import patentsearch.bean.util.xml.DateUtil;
import patentsearch.bean.util.xml.XMLUtil;
import patentsearch.service.patent.SearchService;
import patentsearch.service.user.FormulaPackageService;
import patentsearch.service.user.SearchFormulaService;
import patentsearch.utils.base.ExcelTool2007;
import patentsearch.utils.base.FileTool;
import patentsearch.utils.base.WebTool;

import com.opensymphony.xwork2.ActionContext;

/*
 *   表格检索与智能检索控制器
 */
@Controller
@Scope("prototype")
public class SearchFormulaAction {
	@Resource
	SearchFormulaService searchFormualService;
	@Resource
	SearchService searchService;
	@Resource
	FormulaPackageService formulaPackageService;
	// 用于接收datagrid查询参数并用于回显以完成翻页请求
	private Long total;
	private Integer rows = 10;
	private Integer page = 1;
	private String sort;
	private String order;
	private String searchFormula;
	private SearchFormula searchFormulaEntity = new SearchFormula();
	
	//接受上传的xls文件
	private File uploadfile;
	/* 获取上传文件的文件名，属性名写法固定 */
	private String uploadfileFileName;
	/* 获取上传文件的类型，写法仍然固定 */
	private String uploadfileContentType;
	
	

	

	public File getUploadfile() {
		return uploadfile;
	}

	public void setUploadfile(File uploadfile) {
		this.uploadfile = uploadfile;
	}

	

	public String getUploadfileFileName() {
		return uploadfileFileName;
	}

	public void setUploadfileFileName(String uploadfileFileName) {
		this.uploadfileFileName = uploadfileFileName;
	}

	public String getUploadfileContentType() {
		return uploadfileContentType;
	}

	public void setUploadfileContentType(String uploadfileContentType) {
		this.uploadfileContentType = uploadfileContentType;
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

	public String getSearchFormula() {
		return searchFormula;
	}

	public void setSearchFormula(String searchFormula) {
		this.searchFormula = searchFormula;
	}

	public SearchFormula getSearchFormulaEntity() {
		return searchFormulaEntity;
	}

	public void setSearchFormulaEntity(SearchFormula searchFormulaEntity) {
		this.searchFormulaEntity = searchFormulaEntity;
	}

	/*
	 * 异步获取检索式检索结果 AJAX
	 */
	public void getSearchFormulaResult() {
		String searchscope= ServletActionContext.getRequest().getParameter("searchscope");
		int page= Integer.valueOf(ServletActionContext.getRequest().getParameter("page"));
		int rows= Integer.valueOf(ServletActionContext.getRequest().getParameter("rows"));
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());
		QueryResult<SearchFormula> queryResult = null;
		StringBuilder wherejpql=new StringBuilder();
		if("DocDB".equals(searchscope)){
			wherejpql.append("o.user.id="+user.getId()+" and o.searchscope="+1);
		}else {
			wherejpql.append("o.user.id="+user.getId()+" and o.searchscope="+0);
		}
		if (this.sort != null && !"".equals(this.sort) && this.order != null
				&& !"".equals(this.order)) {
			LinkedHashMap<String, String> order = new LinkedHashMap<String, String>();
			order.put(this.sort, "desc");
			queryResult = searchFormualService.getScrollData((page-1)*rows,rows, wherejpql.toString(),null, order);
//			queryResult = searchFormualService.getScrollData((page - 1) * rows,rows, order);
		} else {
			LinkedHashMap<String, String> order = new LinkedHashMap<String, String>();
			order.put(this.sort, "desc");
			queryResult = searchFormualService.getScrollData((page-1)*rows, page*rows, wherejpql.toString(), null,order);
//			queryResult = searchFormualService.getScrollData((page - 1) * rows,rows);
		}
		total = queryResult.getTotalrecord();
		List<SearchFormula> searchFormulaList = queryResult.getResultlist();
		int max=0;
		for(int i=0;i<searchFormulaList.size();i++){
          int temp=searchFormulaList.get(i).getItemID();
          if(temp>max){
        	  max=temp;
          }
		}
		////////////////////////////////////////////做完后删除,itemID不等于0的不处理
		for(int i=0;i<searchFormulaList.size();i++){
			SearchFormula formula=searchFormulaList.get(i);
			if(formula.getItemID()==0){	
				formula.setItemID(max+1);
			}
			searchFormualService.update(formula);
		}
		////////////////////////////////////////////
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> records = new ArrayList<Map>();
		if (searchFormulaList != null && searchFormulaList.size() > 0) {
			for (int i = 0; i < searchFormulaList.size(); i++) {
				SearchFormula searchFormula = searchFormulaList.get(i);
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("id",searchFormula.getItemID());
				m.put("formula", searchFormula.getFormula());
				m.put("hits", searchFormula.getHits());
				m.put("alterTime", DateUtil.dateToCnTextString(searchFormula
						.getAlterTime()));
				m.put("item",searchFormula.getId());
				records.add(m);
			}
		}
		map.put("total", total);
		map.put("rows", records);
		//System.out.println("检索式Ajax检索条数为:" + total);
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
	 * 异步增加检索式 AJAX
	 */
	public void addSearchFormula() {
		QueryResult<SearchFormula> queryResult = null;
		StringBuilder wherejpql=new StringBuilder();
		String result = "success";
		String message = "成功保存用户检索算式：" + this.searchFormula;
		String searchscope= ServletActionContext.getRequest().getParameter("searchscope");
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());
		searchFormulaEntity.setUser(user);
		if("DocDB".equals(searchscope)){
			wherejpql.append("o.user.id="+user.getId()+" and o.searchscope="+1);
		}else {
			wherejpql.append("o.user.id="+user.getId()+" and o.searchscope="+0);
		}
		queryResult=searchFormualService.getScrollData(-1, -1,wherejpql.toString(), null);
      	List<SearchFormula> formualLists= queryResult.getResultlist();
		int max=0;
		//影响效率 getID waiting update
		for(int i=0;i<formualLists.size();i++){
          int temp=formualLists.get(i).getItemID();
          if(temp>max){
        	  max=temp;
          }
		}
		searchFormulaEntity.setItemID(max+1);
		try{
			searchFormualService.save(searchFormulaEntity);     //解决id号无法获得
		}catch(Exception e){
			result = "fail";
			message = "保存用户检索算式失败,数据库插入失败!";
			e.printStackTrace();
		}
		if (this.searchFormula != null && !"".equals(this.searchFormula)
				&& user != null) {
			DoSearchParameter doSearch=null;
			Long total=null;
			if("DocDB".equals(searchscope)){
				 doSearch = new DoSearchParameter(user.getId().toString(), "DocDB",searchFormulaEntity.getItemID()+"", this.searchFormula);
				 total = searchService.handleDoSearchRemote(doSearch);
			}else{
				 doSearch = new DoSearchParameter(user.getId().toString(), "Cn",searchFormulaEntity.getItemID()+"", this.searchFormula);
				 total = searchService.handleDoSearch(doSearch);
			}
			this.searchFormulaEntity.setHits(total);
			this.searchFormulaEntity.setFormula(this.searchFormula);
			if("DocDB".equals(searchscope)){
				this.searchFormulaEntity.setSearchscope(1);
		    }else{
		    	this.searchFormulaEntity.setSearchscope(0);
		    }
			//System.out.println(this.searchFormula + "，检索式保存成功！");
			try{
				searchFormualService.save(searchFormulaEntity);
			}catch(Exception e){
				result = "fail";
				message = "保存用户检索算式失败,数据库插入失败!";
				e.printStackTrace();
			}

		} else {
			result = "fail";
			message = "保存用户检索算式失败,请您登录!";
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		map.put("message", message);
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
	 * 删除检索式
	 */
	public void deleteFormulas() {
		String rowids = ServletActionContext.getRequest().getParameter("rowids");
		String searchscope = ServletActionContext.getRequest().getParameter("searchscope");
		String result = "success";
		String message = "已成功删除用户检索算式！";
		String[] rowIdList = rowids.split(",");
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());
		QueryResult<SearchFormula> queryResult = null;
		Integer idList[] = new Integer[rowIdList.length];
		for(int i=0;i<rowIdList.length;i++){
			/*StringBuilder wherejpql=new StringBuilder();
			if("DocDB".equals(searchscope)){
				wherejpql.append("o.user.id="+user.getId()+" and o.searchscope="+1+"  ");
			}else {
				wherejpql.append("o.user.id="+user.getId()+" and o.searchscope="+0+"  ");
			}
			wherejpql.append(" and o.itemID= "+rowIdList[i]+"  ");
			queryResult=searchFormualService.getScrollData(-1, -1, wherejpql.toString(), null);
			List<SearchFormula> searchFormulas= queryResult.getResultlist();
			idList[i]=searchFormulas.get(0).getId();*/
			
			searchFormualService.deleteByUserItem(user.getId(), rowIdList[i]);
		}
		/*try {
			searchFormualService.delete(idList);
		} catch (Exception e) {
			result = "fail";
			message = "删除用户检索算式失败!";
		}*/
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		map.put("message", message);
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
	 * 导出excel检索式
	 * 
	 */
	public String exportFormulasToExcel() {
		String rowids = ServletActionContext.getRequest().getParameter("rowids");
		String searchscope = ServletActionContext.getRequest().getParameter("searchscope");
		rowids=rowids.substring(0, rowids.length()-1);
		String[] rowIdList = rowids.split(",");
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());
		QueryResult<SearchFormula> results = null;
		Integer idList[] = new Integer[rowIdList.length];
		for(int i=0;i<rowIdList.length;i++){
			StringBuilder wherejpql=new StringBuilder();
			if("DocDB".equals(searchscope)){
				wherejpql.append("o.user.id="+user.getId()+" and o.searchscope="+1+"  ");
			}else {
				wherejpql.append("o.user.id="+user.getId()+" and o.searchscope="+0+"  ");
			}
			wherejpql.append(" and o.itemID= "+rowIdList[i]+"  ");
			results=searchFormualService.getScrollData(-1, -1, wherejpql.toString(), null);
			List<SearchFormula> searchFormulas= results.getResultlist();
			idList[i]=searchFormulas.get(0).getId();
		}
		StringBuilder whereStr = new StringBuilder("");

		whereStr.append("o.id in(");
		for (int i = 0; i < idList.length; i++) {
			whereStr.append(idList[i] + ",");
		}
		whereStr.deleteCharAt(whereStr.length() - 1);
		whereStr.append(")");
		QueryResult<SearchFormula> queryResult = searchFormualService
				.getScrollData(-1, -1, whereStr.toString(), null);
		
		List<SearchFormula> searchFormulaList = queryResult.getResultlist();
		String fileSavePath = "/upload/excel/";// 生成文件路径
		String fileSaveName = getUserFileName(".xls");// 生成保存文件名
		FileTool.generateFile(fileSavePath, fileSaveName);
		try {
			ExcelTool2007.exportFormulasToExcel(searchFormulaList,fileSavePath,fileSaveName);
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
	/*
	 * 根据文件后缀生成文件名如.xls
	 */
	private String getUserFileName(String postfixx ) {
		String fileSaveName = DateUtil.dateToValueString(new Date()) + postfixx;
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());
		if (user != null) {
			fileSaveName = user.getUsername() + "_"
					+ DateUtil.dateToValueString(new Date()) + postfixx;

		}
		return fileSaveName;
	}
	/**
	 * findFormulas
	 * 
	 */
	public void findFormulas() {
		String num1= ServletActionContext.getRequest().getParameter("num1");
		String num2= ServletActionContext.getRequest().getParameter("num2");
		int int1=Integer.parseInt(num1);
		int int2=Integer.parseInt(num2);
		String searchscope= ServletActionContext.getRequest().getParameter("searchscope");
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());
		QueryResult<SearchFormula> queryResult = null;
		StringBuilder wherejpql=new StringBuilder();
		if("DocDB".equals(searchscope)){
			wherejpql.append("o.user.id="+user.getId()+" and o.searchscope="+1+"  ");
		}else {
			wherejpql.append("o.user.id="+user.getId()+" and o.searchscope="+0+"  ");
		}
		if (this.sort != null && !"".equals(this.sort) && this.order != null
				&& !"".equals(this.order)) {
			queryResult = searchFormualService.getScrollData((page - 1) * rows,rows, wherejpql.toString(),null, null);
		} else {
			queryResult = searchFormualService.getScrollData((page - 1) * rows, rows, wherejpql.toString(), null,null);
		}
		total = queryResult.getTotalrecord();
		List<SearchFormula> searchFormulaList = queryResult.getResultlist();
		String searchFormulaId1=searchFormulaList.get(int1-1).getId()+"";
		String searchFormulaId2=searchFormulaList.get(int2-1).getId()+"";
		Map<String, Object> map = new HashMap<String, Object>();
			map.put("searchFormulaId1", searchFormulaId1);
			map.put("searchFormulaId2", searchFormulaId2);
		JSONObject resultObj = JSONObject.fromObject(map);
		try {
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(resultObj);
			ServletActionContext.getResponse().flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public String myFormular(){
		
		Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());
		String whereStr = "o.userid="+user.getId();
		QueryResult<FormulaPackage> queryResult= formulaPackageService.getScrollData(-1,-1,whereStr,null);
		List<FormulaPackage> formulaPackages = queryResult.getResultlist();
		ActionContext ct = ActionContext.getContext();
		ct.put("formulaPackages", formulaPackages);
		System.out.println(formulaPackages);
		return "myFormular";
	}
	
	/**
	 * 生成文件保存路径
	 * @return
	 */
	protected String generateFileSavePath(){
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
		//数据库所要存储的相对图片路径
		String uploadpath = "/upload/" + dateformat.format(new Date());
		return uploadpath;
	}
	
	/**
	 * 获取上传文件的扩展名
	 * @return
	 */
	public String getExt(String fileName){
		return fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();//获取文件的扩展名
	}
	protected String generateFileSaveName(String fileFileName){
		String ext = this.getExt(fileFileName);
		String fileSaveName = UUID.randomUUID().toString() + "." +ext;///生成文件名
		return fileSaveName;
	}
	/**
	 * 保存文件到指定目录
	 * @param pathdir 文件保存目录
	 * @return
	 */
	protected void saveFile(File saveFile, String pathdir, String fileSaveName){
		if(saveFile!=null && saveFile.length()>0){
			//获取图片的真实存储路径
			String realPath = ServletActionContext.getServletContext().getRealPath(pathdir);
			File filepathdir = new File(realPath);
			if(!filepathdir.exists()){
				filepathdir.mkdirs();
			}
			try {
				FileUtils.copyFile(saveFile, new File(filepathdir,fileSaveName));
//				ImgSizeChange.changeSize(saveFile, realPath, fileFileName, fileSaveName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//return fileSaveName;
		}
		//return null;
	}
	public String uploadFormularPackage(){
		if (this.uploadfile != null) {// 如果有上传文件
			// 保存图片
				File savefile = this.uploadfile;
				if (savefile != null) {
					String fileSavePath = this.generateFileSavePath();// 生成文件路径
					String fileSaveName = this.generateFileSaveName(this.uploadfileFileName);// 生成保存文件名
					
					/* 保存图片 */
					// this.saveFile(savefile, fileSavePath, fileSaveName);
					this.saveFile(savefile, fileSavePath, fileSaveName);
					Users user = WebTool.getLoginedUser(ServletActionContext.getRequest());
						
						// }
						// 构建declare material实体
						FormulaPackage formulaPackage = new FormulaPackage();
						formulaPackage.setAddtime(new Date());
						formulaPackage.setLength(String.valueOf(savefile.length()));
						formulaPackage.setPath(fileSavePath);
						formulaPackage.setRealname(this.uploadfileFileName);
						formulaPackage.setSavename(fileSaveName);
						formulaPackage.setUserid(user.getId());
						formulaPackageService.save(formulaPackage);
						
						
					}
			return "myFormularredirect";
		} else {

		}
		return "myFormularredirect";
	}
	
	public void delPackage() throws IOException{
		String packageid=ServletActionContext.getRequest().getParameter("packageid");
		
		FormulaPackage formulaPackage = formulaPackageService.find(Integer.valueOf(packageid));
		String fileSavePath = formulaPackage.getPath();
		String savePath = ServletActionContext.getServletContext().getRealPath(fileSavePath);
		String saveName = formulaPackage.getSavename();
		String filePath = savePath+"\\"+saveName;
		
		File file = new File(filePath);
		if(file.exists()){
			file.delete();
		}
		
		formulaPackageService.delete(Integer.valueOf(packageid));
		
		
		//File file = 
		
		
		ServletActionContext.getResponse().setCharacterEncoding("utf-8");
		ServletActionContext.getResponse().getWriter().print("删除成功");
		ServletActionContext.getResponse().flushBuffer();
		
		
		
	}
	
	/* 
	 * 测试
	 */
	public void test() {
	 String src=ServletActionContext.getRequest().getParameter("testStr");
	 
	 //System.out.println("src:"+src);
	 String a1=java.net.URLDecoder.decode(src);
	 //System.out.println("a1:"+a1);
	}
	
}
