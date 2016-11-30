package patentsearch.service.base.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.stereotype.Service;

import patentsearch.bean.base.LegalStatusDetail;
import patentsearch.bean.cndescriptionitem.CnLegalStatus;
import patentsearch.service.base.LegalStatusListService;

@Service
public class LegalStatusListServiceImpl extends DaoSupport<LegalStatusDetail> implements
LegalStatusListService {

	 
 
	public List<LegalStatusDetail> getLegalStatusListByTerm(String term) {
	
		if (term != null && !"".equals(term.trim())) {
			StringBuffer where = new StringBuffer();
			List<Object> parameters = new ArrayList<Object>();
				where.append("  o.legalStatusInfo " + " like ?" + (parameters.size() + 1));
				parameters.add("%" + term + "%");
				where.append(" or o.code " + " like ?" + (parameters.size() + 1));
				parameters.add("%" + term + "%");
			  return this.getScrollData(-1, -1, where.toString(),
					parameters.toArray()).getResultlist();
		}
		return this.getScrollData().getResultlist();
	}

	public List<LegalStatusDetail> getLegalStatusChildList(
			String legalStatusInfo) {
		List<LegalStatusDetail> resultList=new ArrayList<LegalStatusDetail>();
		Query query = em
				.createNativeQuery
				("SELECT A.category,A.code,A.comment,A.legalStatus ,A.legalStatusInfo ,A.parentCode FROM  LegalStatusDetail A ,LegalStatusDetail B where A.parentCode=B.code and B.legalStatusInfo=?1 ");
		query.setParameter(1, legalStatusInfo);
		List<Object[]> list=query.getResultList();
		if(list!=null&&list.size()>0){
			    for(int i=0;i<list.size();i++){
				Object[] objs = (Object[]) list.get(i);
				LegalStatusDetail legalStatusDetail = new LegalStatusDetail();
				legalStatusDetail.setCategory((String) objs[0]);
				legalStatusDetail.setCode((String) objs[1]);
				legalStatusDetail.setComment((String) objs[2]);
				legalStatusDetail.setLegalStatus((String) objs[3]);
				legalStatusDetail.setLegalStatusInfo((String) objs[4]);
				legalStatusDetail.setParentCode((String) objs[5]);
				resultList.add(legalStatusDetail);
			    }
			return  resultList;
		}
	return null;
	}

}
