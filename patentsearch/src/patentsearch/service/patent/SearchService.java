package patentsearch.service.patent;

 
import java.io.File;
import java.util.List;
import java.util.Map;

import patentsearch.bean.cndescriptionitem.CNDescriptionItem;
import patentsearch.bean.cndescriptionitem.EnDescriptionItem;
import patentsearch.bean.search.requestParameter.DoSearchParameter;
import patentsearch.bean.search.requestParameter.GetGeneralDataParameter;
import patentsearch.service.base.DAO;

public interface SearchService extends DAO<CNDescriptionItem>{
	/*
	 * 根据输入条件，返回专利命中次数（本地）
	 */
	public Long handleDoSearch(DoSearchParameter doSearch);
	/*
	 * 根据输入条件，返回专利命中次数(远程)
	 */
	public Long handleDoSearchRemote(DoSearchParameter doSearch);
	/*
	 * 根据输入条件，返回相应的分页数据（本地）
	 */
	public List<CNDescriptionItem> handleGetGeneralData(GetGeneralDataParameter getGeneralData);
	/*
	 * 根据输入条件，返回相应的分页数据(远程)
	 */
	public List<EnDescriptionItem> handleGetGeneralDataRemote(GetGeneralDataParameter getGeneralData);
	/*
	 * 根据输入专利申请号，返回专利主权权得要求、说明书等相关数据
	 */
	public String getPatentData(String appno,int searchType);
	/*
	 * 根据输入专利申请号，返回专利PDF文档访问路径（谷内）
	 */
	public String getPdfUrl(String appno);
	/*
	 * 根据输入专利公开号，返回专利PDF文档访问路径（国外）
	 */
	public String getPdfUrlEn(String pubnr);
	/*
	 * 根据输入专利申请号，返回专利PDF File
	 */
	public File getPdfFile(String appno);
	/*
	 * 根据输入专利申请号数组，返回专利著录项集合（国内）
	 */
	public  List<CNDescriptionItem> getPatentItemList(List<String> appnoList);
	
	/*
	 * 根据输入专利申请号数组，返回专利著录项集合（国外）
	 */
	public  List<EnDescriptionItem> getPatentItemListEN(List<String> pubnrList);
	
	/*
	 * 根据输入专利申请号，返回专利著录项数据（国内）
	 */
	public  CNDescriptionItem getPatentItem(String appno);
	/*
	 * 根据输入专利公开号，返回专利著录项数据（国外）
	 */
	public EnDescriptionItem getPatentItemEN(String pubnr);
	
	/*
	 * 根据输入专利申请号集合，返回专利基本数据，此时不需要解析XML，可提高性能(国内)
	 */
	public  List<Map<String,String>> getPatentBasicInfo(List<String> appnos);
	/**
	 * 返回日期 用于统计
	 * @param appnos
	 * @return
	 */
	public  List<Map<String,String>> getPatentApdDate(List<String> appnos);
	
}
