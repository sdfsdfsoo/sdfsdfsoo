package patentsearch.service.base;

import java.util.List;

import patentsearch.bean.base.ProvinceCity;
import patentsearch.service.base.DAO;

public interface ProvinceCityService extends DAO<ProvinceCity> {
	/*
	 * 根据省市代码返回省市详细信息
	 */
	public String detailInfo(List<ProvinceCity> provinceCityList, String code);
	/*
	 * 根据国省代码或者名称关键字返回国省列表
	 */
	public List<ProvinceCity> getCoListByTerm(String term);
	
	

}
