package patentsearch.service.base;

import java.util.List;

import patentsearch.bean.base.LegalStatusDetail;

public interface LegalStatusListService extends DAO<LegalStatusDetail> {
	 

	/*
	 * 根据法律状态关键字返回法律状态列表
	 */
	public List<LegalStatusDetail> getLegalStatusListByTerm(String term);
	/*
	 * 根据法律状态信息返回法律状态信息子类列表
	 */
	public List<LegalStatusDetail> getLegalStatusChildList(String legalStatusInfo);

}
