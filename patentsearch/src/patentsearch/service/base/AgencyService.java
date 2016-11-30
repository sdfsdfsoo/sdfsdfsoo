package patentsearch.service.base;

import java.util.List;

import patentsearch.bean.base.Agency;
import patentsearch.bean.base.ProvinceCity;

public interface AgencyService extends DAO<Agency> {
	/*
	 * 根据代理机构代码返回Agency信息
	 */
	public Agency detailInfo(String code);

	/*
	 * 根据代理机构代码或者名称关键字返回代理机构列表
	 */
	public List<Agency> getAgListByTerm(String term);

}
