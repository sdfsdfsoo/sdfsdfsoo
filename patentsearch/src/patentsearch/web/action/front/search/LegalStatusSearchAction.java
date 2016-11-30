package patentsearch.web.action.front.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.junit.runner.Request;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import patentsearch.bean.base.LegalStatusDetail;
import patentsearch.bean.base.QueryResult;
import patentsearch.bean.cndescriptionitem.CnLegalStatus;
import patentsearch.bean.cndescriptionitem.CnLegalStatus_history;
import patentsearch.bean.search.requestParameter.DoSearchParameter;
import patentsearch.bean.search.requestParameter.GetGeneralDataParameter;
import patentsearch.bean.user.Users;
import patentsearch.bean.util.xml.DateUtil;
import patentsearch.bean.util.xml.XMLUtil;
import patentsearch.service.base.LegalStatusListService;
import patentsearch.service.legalstatus.LegalStatusHistoryService;
import patentsearch.service.legalstatus.LegalStatusService;
import patentsearch.service.patent.SearchService;
import patentsearch.util.webservice.WebServiceLocalClientUtil;
import patentsearch.utils.base.WebTool;

 

/*
 *   法律状态检索控制器
 */
@Controller
@Scope("prototype")
public class LegalStatusSearchAction {
	@Resource
	LegalStatusService legalStatusService;
	@Resource
	LegalStatusHistoryService legalStatusHistoryService;
	@Resource
	SearchService searchService;
	@Resource
	LegalStatusListService legalStatusListService;
	private CnLegalStatus  cnLegalStatus=new CnLegalStatus();
	// 用于接收datagrid查询参数并用于回显以完成翻页请求
	private Long total;
	private Integer  rows = 10;
	private Integer  page = 1;
	private String searchCondition;
	
	private String zhuangTai;
	
	public String getZhuangTai() {
		return zhuangTai;
	}

	public void setZhuangTai(String zhuangTai) {
		this.zhuangTai = zhuangTai;
	}

	public CnLegalStatus getCnLegalStatus() {
		return cnLegalStatus;
	}

	public void setCnLegalStatus(CnLegalStatus cnLegalStatus) {
		this.cnLegalStatus = cnLegalStatus;
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

	public String getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}
	
	private int number;

	public void setNumber(int number) {
		this.number = number;
	}

	public int getNumber() {
		return number;
	}
	/** 
	  * 法律状态检索界面
	  */
	public String legalStatusSearchUI() {
		return "legalStatusSearchUI";
	}
	/**
	  * 处理法律状态检索界面检索请求
	  */
	public String legalSearch() {
		return "searchResultUI";
	}
	/*
	 * 获取Ajax法律状态检索结果
	 */
	@SuppressWarnings("unchecked")
	public void getSearchResult(){   
		StringBuilder where = new StringBuilder("");
		List<Object> params = new LinkedList<Object>();
		//接收异步请求参数
		
		buildLegalStatus(ServletActionContext.getRequest());
		
		//组合检索条件
		buildQueryPara(where, params);
		String legalStstus = ServletActionContext.getRequest().getParameter(
				"zhuangTai");
		if (!"".equals(legalStstus) && null != legalStstus) {
			String status[] = legalStstus.split(",");
			where.append(" and (");
			for (int i = 0; i < status.length; i++) {
				if (i == 0) {
					where.append("o.legalStatusInfo = '").append(status[i])
							.append("'");
				} else {
					where.append(" or o.legalStatusInfo = '").append(status[i])
							.append("'");
				}
			}
		}
		where.append(")");
		int firstindex = (page - 1)* rows;
		LinkedHashMap<String, String> order = new LinkedHashMap<String, String>();
		order.put("legalDate", "desc");
		String zhuQuanLi = cnLegalStatus.getZhuQuanLi();
		String zhaiYao = cnLegalStatus.getZhaiYao();
		QueryResult<CnLegalStatus_history> qr = legalStatusHistoryService.getScrollData(firstindex, rows, where.toString(), params.toArray(),order);
		total=qr.getTotalrecord();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map> records = new ArrayList<Map>();
		for (int i = 0; i < qr.getResultlist().size(); i++) {
			CnLegalStatus_history legalStatus  = qr.getResultlist().get(i);
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("juanQi", legalStatus.getJuanQi());
			m.put("patentType", legalStatus.getPatentType());
			m.put("legalDate", DateUtil.formatStrToStr(legalStatus
					.getLegalDate()));
			m.put("legalStatus", legalStatus.getLegalStatus());
			m.put("legalStatusInfo", legalStatus.getLegalStatusInfo());
			m.put("legalCode", legalStatus.getLegalCode());
			m.put("vol", legalStatus.getVol());
			m.put("num", legalStatus.getNum());
			m.put("pag", legalStatus.getPag());
			m.put("ipc", legalStatus.getIpc());
			m.put("shenQingH", legalStatus.getShenQingHWithDot());
			m.put("appnoValue", legalStatus.getShenQingHWithNotDot());
			m.put("shenQingR", legalStatus.getShenQingR());
			m.put("shenQingGbr", legalStatus.getShenQingGbr());
			m.put("shouquanGgr", legalStatus.getShouquanGgr());
			m.put("wuXiaoXgjdh", legalStatus.getWuXiaoXgjdh());
			m.put("wuXiaoXgjdr", legalStatus.getWuXiaoXgjdr());
			m.put("zhongZhiR", legalStatus.getZhongZhiR());
			m.put("fangQiSxr", legalStatus.getFangQiSxr());
			m.put("yuanMingC", legalStatus.getYuanMingC());
			m.put("yuanGongGr", legalStatus.getYuanGongGr());
			m.put("bianGengSxCode", legalStatus.getBianGengSxCode());
			m.put("bianGengSx", legalStatus.getBianGengSx());
			m.put("bianGengQ", legalStatus.getBianGengQ());
			m.put("bianGengH", legalStatus.getBianGengH());
			m.put("dengJiSxr", legalStatus.getDengJiSxr());
			m.put("heTongBah", legalStatus.getHeTongBah());
			m.put("rangYuR", legalStatus.getRangYuR());
			m.put("shouRangR", legalStatus.getShouRangR());
			m.put("zhuanLiMc", legalStatus.getZhuanLiMc());
			m.put("xuKeZl", legalStatus.getXuKeZl());
			m.put("beiAnRq", legalStatus.getBeiAnRq());
			m.put("bianGengR", legalStatus.getBianGengR());
			m.put("jieChuR", legalStatus.getJieChuR());
			m.put("dengJiH", legalStatus.getDengJiH());
			m.put("chuZhiR", legalStatus.getChuZhiR());
			m.put("zhiQuanR", legalStatus.getZhiQuanR());
			m.put("shouJianR", legalStatus.getShouJianR());
			m.put("wenJianMc", legalStatus.getWenJianMc());
			m.put("ImportData", DateUtil.dateToTextString(legalStatus
					.getImportData()));
			records.add(m);
			
		}
		map.put("total",  total);
		//System.out.println("total="+total);
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
	/*
	 * 构建中国专利法律状态检索查询条件
	 */
	private void buildQueryPara(StringBuilder where, List<Object> params) {
		where.append("1=1");
		if (this.cnLegalStatus.getShenQingH() != null && !"".equals(this.cnLegalStatus.getShenQingH())) {
			
			where.append(" and shenQingH=?"+ (params.size() + 1));
			
			if(this.cnLegalStatus.getShenQingH().length()>12){
				this.cnLegalStatus.setShenQingH(this.cnLegalStatus.getShenQingH().substring(0, 12));
			}
			if(this.cnLegalStatus.getShenQingH().length()>8&&this.cnLegalStatus.getShenQingH().length()<11){
				this.cnLegalStatus.setShenQingH(this.cnLegalStatus.getShenQingH().substring(0, 8));
			}
			
			params.add(XMLUtil.getCheckAppnoWithOutDot(this.cnLegalStatus.getShenQingH()));
		}   
		if (this.cnLegalStatus.getLegalDate() != null && !"".equals(this.cnLegalStatus.getLegalDate())) {
			where.append(" and o.legalDate=?"+ (params.size() + 1));
			params.add(this.cnLegalStatus.getLegalDate());
		} 
		 //法律状态信息比较复杂，应该分为父类与子类两种情况考虑
		if (this.cnLegalStatus.getLegalStatusInfo() != null && !"".equals(this.cnLegalStatus.getLegalStatusInfo())) {
			List<LegalStatusDetail> childList=legalStatusListService.getLegalStatusChildList(this.cnLegalStatus.getLegalStatusInfo());
			if(childList!=null){ 
				//父分类
				where.append(" and o.legalStatusInfo in"+  buildSetQueryStr(childList));
			}else{
				//子分类
				where.append(" and o.legalStatusInfo=?"+ (params.size() + 1));
				params.add(this.cnLegalStatus.getLegalStatusInfo());
			}
			
		} 
		if (this.cnLegalStatus.getZhiQuanR() != null && !"".equals(this.cnLegalStatus.getZhiQuanR())) {
			where.append(" and o.zhiQuanR=?"+ (params.size() + 1));
			params.add(this.cnLegalStatus.getZhiQuanR());
		} 
		//专利权利转移检索
		if (this.cnLegalStatus.getZhuanLiMc() != null && !"".equals(this.cnLegalStatus.getZhuanLiMc())) {
			where.append(" and o.zhuanLiMc"+" like ?"+ (params.size() + 1));
			params.add("%"+this.cnLegalStatus.getZhuanLiMc()+"%");
		} 
		if (this.cnLegalStatus.getDengJiSxr() != null && !"".equals(this.cnLegalStatus.getDengJiSxr())) {
			where.append(" and o.dengJiSxr=?"+ (params.size() + 1));
			params.add(this.cnLegalStatus.getDengJiSxr());
		} 
		if (this.cnLegalStatus.getRangYuR() != null && !"".equals(this.cnLegalStatus.getRangYuR())) {
			where.append(" and o.rangYuR=?"+ (params.size() + 1));
			
			params.add(this.cnLegalStatus.getRangYuR());
		} 
		if (this.cnLegalStatus.getShouRangR() != null && !"".equals(this.cnLegalStatus.getShouRangR())) {
			where.append(" and o.shouRangR=?"+ (params.size() + 1));
			params.add(this.cnLegalStatus.getShouRangR());
		}
		if (this.cnLegalStatus.getBianGengQ() != null && !"".equals(this.cnLegalStatus.getBianGengQ())) {
			where.append(" and o.bianGengQ=?"+ (params.size() + 1));
			params.add(this.cnLegalStatus.getBianGengQ());
		}
		if (this.cnLegalStatus.getBianGengH() != null && !"".equals(this.cnLegalStatus.getBianGengH())) {
			where.append(" and o.bianGengH=?"+ (params.size() + 1));
			params.add(this.cnLegalStatus.getBianGengH());
		}
		if (this.cnLegalStatus.getBianGengR() != null && !"".equals(this.cnLegalStatus.getBianGengR())) {
			where.append(" and o.bianGengR=?"+ (params.size() + 1));
			params.add(this.cnLegalStatus.getBianGengR());
		}
		if (this.cnLegalStatus.getHeTongBah() != null && !"".equals(this.cnLegalStatus.getHeTongBah())) {
			where.append(" and o.heTongBah=?"+ (params.size() + 1));
			params.add(this.cnLegalStatus.getHeTongBah());
		}
		if (this.cnLegalStatus.getJieChuR() != null && !"".equals(this.cnLegalStatus.getJieChuR())) {
			where.append(" and o.jieChuR=?"+ (params.size() + 1));
			params.add(this.cnLegalStatus.getJieChuR());
		}
		if (this.cnLegalStatus.getChuZhiR() != null && !"".equals(this.cnLegalStatus.getChuZhiR())) {
			where.append(" and o.chuZhiR=?"+ (params.size() + 1));
			params.add(this.cnLegalStatus.getChuZhiR());
		}
		if (this.cnLegalStatus.getBeiAnRq() != null && !"".equals(this.cnLegalStatus.getBeiAnRq())) {
			where.append(" and o.beiAnRq=?"+ (params.size() + 1));
			params.add(this.cnLegalStatus.getBeiAnRq());
		}
		if (this.cnLegalStatus.getXuKeZl() != null && !"".equals(this.cnLegalStatus.getXuKeZl())) {
			where.append(" and o.xuKeZl=?"+ (params.size() + 1));
			params.add(this.cnLegalStatus.getXuKeZl());
		}
		if (this.cnLegalStatus.getIpc() != null && !"".equals(this.cnLegalStatus.getIpc())) {
			where.append(" and o.ipc like '%"+ (this.cnLegalStatus.getIpc())+"%'");
		}
		
		if (this.cnLegalStatus.getZhaiYao() != null && !"".equals(this.cnLegalStatus.getZhaiYao())) {
			where.append(" and o.zhaiYao like '%"+ (this.cnLegalStatus.getZhaiYao())+"%'");
		}
		if (this.cnLegalStatus.getZhuQuanLi() != null && !"".equals(this.cnLegalStatus.getZhuQuanLi())) {
			where.append(" and o.zhuQuanLi like '%"+ (this.cnLegalStatus.getZhuQuanLi())+"%'");
		}
		
		//System.out.println(where);
		 
	}
	
	/*
	 * 异步ajax法律状态加载
	 */

	public void getLegalStatusList() {
		String term = ServletActionContext.getRequest().getParameter("term");
		List<LegalStatusDetail> list = legalStatusListService.getLegalStatusListByTerm(term);
		JSONArray json = new JSONArray();
		for (LegalStatusDetail item : list) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("label", item.getLegalStatusInfo()+"("+item.getCode()+")");
			jsonObject.put("value", item.getLegalStatusInfo());
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
	//接收异步请求参数
	private void buildLegalStatus(HttpServletRequest request) {	
		cnLegalStatus.setShenQingH(request.getParameter("shenQingH"));
		cnLegalStatus.setLegalDate(request.getParameter("legalDate"));
		cnLegalStatus.setLegalStatusInfo(request.getParameter("legalStatusInfo"));
		cnLegalStatus.setLegalCode(request.getParameter("legalCode"));
		cnLegalStatus.setZhiQuanR(request.getParameter("zhiQuanR"));
		cnLegalStatus.setZhuanLiMc(request.getParameter("zhuanLiMc"));
		cnLegalStatus.setDengJiSxr(request.getParameter("dengJiSxr"));
		cnLegalStatus.setRangYuR(request.getParameter("rangYuR"));
		cnLegalStatus.setShouRangR(request.getParameter("shouRangR"));
		cnLegalStatus.setBianGengQ(request.getParameter("bianGengQ"));
		cnLegalStatus.setBianGengH(request.getParameter("bianGengH"));
		cnLegalStatus.setBianGengR(request.getParameter("bianGengR"));
		cnLegalStatus.setHeTongBah(request.getParameter("heTongBah"));
		cnLegalStatus.setJieChuR(request.getParameter("jieChuR"));
		cnLegalStatus.setChuZhiR(request.getParameter("chuZhiR"));
		cnLegalStatus.setBeiAnRq(request.getParameter("beiAnRq"));
		cnLegalStatus.setXuKeZl(request.getParameter("xuKeZl"));
		cnLegalStatus.setZhuQuanLi(request.getParameter("zhuQuanLi"));
		cnLegalStatus.setIpc(request.getParameter("ipc"));
		cnLegalStatus.setZhaiYao(request.getParameter("zhaiYao"));
		

	}
	
	//拼接集合查询字符串
	private String buildSetQueryStr(List<LegalStatusDetail> childList) {
		 StringBuilder inSet=new StringBuilder("(");
		 for(int i=0;i<childList.size();i++){
			 inSet.append("'"+childList.get(i).getLegalStatusInfo()+"',");
		 }
		 inSet.deleteCharAt(inSet.length()-1);
		 inSet.append(")");
         return inSet.toString();
	}
}