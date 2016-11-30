package patentsearch.service.base.impl;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Service;

import patentsearch.bean.base.Agency;
import patentsearch.service.base.AgencyService;
 @Service
public class AgencyServiceImpl extends DaoSupport<Agency>
		implements AgencyService {
  
	@SuppressWarnings("unchecked")
	public Agency detailInfo(String code) {
		  
			Query query = em.createQuery("select o from Agency o where o.code=?1");
			query.setParameter(1, code);
			List<Agency> agencyList = query.getResultList();
			if(agencyList.size() > 0){
				return agencyList.get(0);
			}
			return null;
	}

	public List<Agency> getAgListByTerm(String term) {
		if (term != null && !"".equals(term)) {
			StringBuffer where = new StringBuffer();
			List<Object> parameters = new ArrayList<Object>();
			if (term != null && !"".equals(term)) {

				where.append("  o.code " + " like ?" + (parameters.size() + 1));
				parameters.add("%" + term + "%");

				where.append(" or o.name " + " like ?"
						+ (parameters.size() + 1));
				parameters.add("%" + term + "%");

			}

			return this.getScrollData(-1, -1, where.toString(),
					parameters.toArray()).getResultlist();
		}
		return null;
	}
}


 