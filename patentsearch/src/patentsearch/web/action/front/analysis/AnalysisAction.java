package patentsearch.web.action.front.analysis;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
import patentsearch.bean.user.SearchFormula;
import patentsearch.bean.user.Users;
import patentsearch.bean.util.xml.DateUtil;
import patentsearch.bean.util.xml.XMLUtil;
import patentsearch.service.base.AgencyService;
import patentsearch.service.base.ProvinceCityService;
import patentsearch.service.legalstatus.LegalStatusService;
import patentsearch.service.patent.SearchService;
import patentsearch.service.user.SearchFormulaService;
import patentsearch.utils.base.ConfigTool;
import patentsearch.utils.base.ExcelTool2007;
import patentsearch.utils.base.FileTool;
import patentsearch.utils.base.StringUtil;
import patentsearch.utils.base.WebTool;

import com.opensymphony.xwork2.ActionContext;

/*
 *   表格检索与智能检索控制器
 */
@Controller
@Scope("prototype")
public class AnalysisAction {
	@Resource
	SearchService searchService;
	@Resource
	LegalStatusService legalStatusService;
	@Resource
	ProvinceCityService provinceCityService;
	@Resource
	AgencyService agencyService;
	//接收多个申请号，以,号隔开
	private String appnos;
	private CNDescriptionItem itemLeft;
	private EnDescriptionItem itemLeftEN;
	private CNDescriptionItem itemRight;
	private EnDescriptionItem itemRightEN;
	private static List<ProvinceCity> provinceCityList;
	
	
	
	public EnDescriptionItem getItemLeftEN() {
		return itemLeftEN;
	}

	public void setItemLeftEN(EnDescriptionItem itemLeftEN) {
		this.itemLeftEN = itemLeftEN;
	}

	public EnDescriptionItem getItemRightEN() {
		return itemRightEN;
	}

	public void setItemRightEN(EnDescriptionItem itemRightEN) {
		this.itemRightEN = itemRightEN;
	}

	public String getAppnos() {
		return appnos;
	}

	public void setAppnos(String appnos) {
		this.appnos = appnos;
	}

	public CNDescriptionItem getItemLeft() {
		return itemLeft;
	}

	public void setItemLeft(CNDescriptionItem itemLeft) {
		this.itemLeft = itemLeft;
	}

	public CNDescriptionItem getItemRight() {
		return itemRight;
	}

	public void setItemRight(CNDescriptionItem itemRight) {
		this.itemRight = itemRight;
	}

	/**
	 * 双专利对比界面
	 */
	public String analysisUI() {
		
		String searchscope= ServletActionContext.getRequest().getParameter("searchscope");
		//接收前台传过来的申请号
		String searchFormula="F XX ";
		List<String> appnoList = new ArrayList<String>();
		if (appnos != null && !"".equals(appnos)) {
			for (String appno : appnos.split(",")) {
				appnoList.add(appno);
			}
		}
		List<Map<String, String>> resultList=null;
		List<EnDescriptionItem> resultListEN=null;
		if("DocDB".equals(searchscope)){
			resultListEN=searchService.getPatentItemListEN(appnoList);
			ActionContext.getContext().put("resultList",resultListEN);
			 //左边专利信息
			if (this.itemLeftEN != null && this.itemLeftEN.getPubnr() != null&&!"".equals(this.itemLeftEN.getPubnr())) {
	
				// 左边著录项数据和摘要信息
				this.itemLeftEN = searchService.getPatentItemEN(this.itemLeftEN.getPubnr());
				
//				//左边对国省代码与代理机构信息进行处理
//				this.itemLeft.setNc(handleCodeInfo(this.itemLeft).getNc());
//				this.itemLeft.setAgency(handleCodeInfo(this.itemLeft).getAgency());
				//左边 摘要附图
				ActionContext.getContext().put("futuURL_left",
						XMLUtil.getFuTuByAppno(this.itemLeftEN.getAppno()));
//				// 左边法律状态
//				List<CnLegalStatus> cnLegalStatusList_left = legalStatusService
//						.getCnLegalStatusByAppnp(XMLUtil
//								.getCheckAppnoWithOutDot(this.itemLeft.getAppno()));
//				ActionContext.getContext().put("cnLegalStatusList_left",
//						cnLegalStatusList_left);
//				//左边 权利要求书
//				ActionContext.getContext().put("clmXmlTxt_left",
//						searchService.getPatentData(this.itemLeft.getAppno(), 3));
//				// 左边说明书
//				ActionContext.getContext().put("cnDesXmlTxt_left",
//						searchService.getPatentData(this.itemLeft.getAppno(), 2));
			}
			 //右边专利信息
			if (this.itemRightEN != null && this.itemRightEN.getPubnr() != null&&!"".equals(this.itemRightEN.getPubnr())) {
				// 左边著录项数据和摘要信息
				this.itemRightEN = searchService.getPatentItemEN(this.itemRightEN.getPubnr());
//				//左边对国省代码与代理机构信息进行处理
//				this.itemRight.setNc(handleCodeInfo(this.itemRight).getNc());
//				this.itemRight.setAgency(handleCodeInfo(this.itemRight).getAgency());
				//左边 摘要附图
				ActionContext.getContext().put("futuURL_right",
						XMLUtil.getFuTuByAppno(this.itemRightEN.getAppno()));
//				// 左边法律状态
//				List<CnLegalStatus> cnLegalStatusList_right = legalStatusService
//						.getCnLegalStatusByAppnp(XMLUtil
//								.getCheckAppnoWithOutDot(this.itemRight.getAppno()));
//				ActionContext.getContext().put("cnLegalStatusList_right",
//						cnLegalStatusList_right);
//				
//				//左边 权利要求书
//				ActionContext.getContext().put("clmXmlTxt_right",
//						searchService.getPatentData(this.itemRight.getAppno(), 3));
//				// 左边说明书
//				ActionContext.getContext().put("cnDesXmlTxt_right",
//						searchService.getPatentData(this.itemRight.getAppno(), 2));
			}
			
			
			return "analysisUIEN"; 
		}
			 resultList = searchService.getPatentBasicInfo(appnoList);
				ActionContext.getContext().put("resultList",
						resultList);
				//左边专利信息
			if (this.itemLeft != null && this.itemLeft.getAppno() != null&&!"".equals(this.itemLeft.getAppno())) {
	
				// 左边著录项数据和摘要信息
				this.itemLeft = searchService.getPatentItem(this.itemLeft
						.getAppno());
				//左边对国省代码与代理机构信息进行处理
				this.itemLeft.setNc(handleCodeInfo(this.itemLeft).getNc());
				this.itemLeft.setAgency(handleCodeInfo(this.itemLeft).getAgency());
				//左边 摘要附图
				ActionContext.getContext().put("futuURL_left",
						XMLUtil.getFuTuByAppno(this.itemLeft.getAppno()));
				// 左边法律状态
				List<CnLegalStatus> cnLegalStatusList_left = legalStatusService
						.getCnLegalStatusByAppnp(XMLUtil
								.getCheckAppnoWithOutDot(this.itemLeft.getAppno()));
				ActionContext.getContext().put("cnLegalStatusList_left",
						cnLegalStatusList_left);
				//左边 权利要求书
				ActionContext.getContext().put("clmXmlTxt_left",
						searchService.getPatentData(this.itemLeft.getAppno(), 3));
				// 左边说明书
				ActionContext.getContext().put("cnDesXmlTxt_left",
						searchService.getPatentData(this.itemLeft.getAppno(), 2));
			}
			 //右边专利信息
			if (this.itemRight != null && this.itemRight.getAppno() != null&&!"".equals(this.itemRight.getAppno())) {
				// 左边著录项数据和摘要信息
				this.itemRight = searchService.getPatentItem(this.itemRight
						.getAppno());
				//左边对国省代码与代理机构信息进行处理
				this.itemRight.setNc(handleCodeInfo(this.itemRight).getNc());
				this.itemRight.setAgency(handleCodeInfo(this.itemRight).getAgency());
				//左边 摘要附图
				ActionContext.getContext().put("futuURL_right",
						XMLUtil.getFuTuByAppno(this.itemRight.getAppno()));
				// 左边法律状态
				List<CnLegalStatus> cnLegalStatusList_right = legalStatusService
						.getCnLegalStatusByAppnp(XMLUtil
								.getCheckAppnoWithOutDot(this.itemRight.getAppno()));
				ActionContext.getContext().put("cnLegalStatusList_right",
						cnLegalStatusList_right);
				
				//左边 权利要求书
				ActionContext.getContext().put("clmXmlTxt_right",
						searchService.getPatentData(this.itemRight.getAppno(), 3));
				// 左边说明书
				ActionContext.getContext().put("cnDesXmlTxt_right",
						searchService.getPatentData(this.itemRight.getAppno(), 2));
			}
			
		ActionContext.getContext().put("legalstatus_friendly",
				ConfigTool.getValue("legalstatus_friendly"));
		 
		return "analysisUI";
	}
	
	/*
	 * 将国家/省市代码、代理机构代码化信息进行处理
	 */
	private CNDescriptionItem handleCodeInfo(CNDescriptionItem cnDescriptionItem) {
		 
		CNDescriptionItem formatcnDescriptionItem = new CNDescriptionItem();
		provinceCityList = provinceCityService.getScrollData().getResultlist();
		formatcnDescriptionItem.setNc(provinceCityService.detailInfo(provinceCityList, cnDescriptionItem.getNc()));
		Agency agency = agencyService.detailInfo(cnDescriptionItem.getAgency());
		formatcnDescriptionItem.setAgency(agency == null ? "无" : agency
				.getName()
				+ "(代码" + agency.getCode() + ")");
		return formatcnDescriptionItem;
	}

}
