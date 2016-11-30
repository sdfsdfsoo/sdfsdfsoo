package patentsearch.service.legalstatus;
import java.util.List;

import patentsearch.bean.base.LegalStatusDetail;
import patentsearch.bean.cndescriptionitem.CnLegalStatus;
import patentsearch.service.base.DAO;

public interface LegalStatusService extends DAO<CnLegalStatus>{
	/**
	 * 根据中国专利申请号来查询法律状态信息
	 */
	public List<CnLegalStatus> getCnLegalStatusByAppnp(String appno);
	 
	/**
	 * 根据中国专利申请号来查询法律状态详细信息，里面内含法律状态类型
	 * 1、暂无
	 * 2、失效
	 * 3、审中
	 * 4、有效
	 * 5、无效
	 */
	public List<LegalStatusDetail> getLegalStatusDetail(String appno);
	public String getLegalStatusDetailSigle(String appno);
	
	public String getLegalDateByAppno(String appno);
	
	public String getAccreditLegalDateByAppno(String appno);
	/**
	 * 根据专利号获得
	 * @param appno
	 * @return
	 */
	public String getLegalStatusDateByAppno(String appno);
	
	

	/**
	 * 根据专利号获得
	 * @param appno
	 * @return
	 */
	public String getLegalStatusInfoByAppno(String appno);
}
