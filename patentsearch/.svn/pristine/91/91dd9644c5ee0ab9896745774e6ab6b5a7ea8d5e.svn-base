package patentsearch.bean.util.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import patentsearch.bean.cndescriptionitem.CNDescriptionItem;
import patentsearch.bean.search.requestParameter.DoSearchParameter;
import patentsearch.bean.search.requestParameter.GetGeneralDataParameter;
import patentsearch.service.patent.SearchService;
import patentsearch.util.webservice.WebServiceLocalClientUtil;

public class GetPatentByYearThread extends Thread {
     
	SearchService searchService;
	//检索式
	private String searchFormula;
	//页码
	private int page;
	//页码
	private int startPage;
	
	
	public GetPatentByYearThread(String searchFormula,int startPage, int page,SearchService searchService) {
		super();
		this.searchFormula = searchFormula;
		this.page = page;
		this.startPage = startPage;
		this.searchService=searchService;
		
	}



	public int getStartPage() {
		return startPage;
	}



	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}



	public String getSearchFormula() {
		return searchFormula;
	}



	public void setSearchFormula(String searchFormula) {
		this.searchFormula = searchFormula;
	}



	public int getPage() {
		return page;
	}



	public void setPage(int page) {
		this.page = page;
	}



	@Override
	public void run() {
		List<String> appnos=new ArrayList<String>();
		List<Map<String, String>> mm=new ArrayList<Map<String, String>>();
		int totalPage=this.page;
		for(int i=startPage;i<=totalPage;i++){
			GetGeneralDataParameter getGeneralDataParameter = new GetGeneralDataParameter("1", "Cn", "001", i, 50);
			appnos.addAll(WebServiceLocalClientUtil.getAppnolistBySearch(getGeneralDataParameter));
			mm.addAll(searchService.getPatentApdDate(appnos));
		}
		
	}

	
	
	
	
}
