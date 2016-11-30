package patentsearch.service.user.impl;

import javax.persistence.Query;

import org.springframework.stereotype.Service;
import patentsearch.bean.user.SearchFormula;
import patentsearch.service.base.impl.DaoSupport;
import patentsearch.service.user.SearchFormulaService;
@Service
public class SearchFormulaServiceImpl extends DaoSupport<SearchFormula> implements SearchFormulaService {

	@Override
	public int deleteByUserItem(int userid, String itemID) {
		String sql="delete from SearchFormula  where user_id ="+userid+" and itemID = "+itemID;
		Query query = em.createQuery(sql);
		//query.setParameter(1, userid);
		//query.setParameter(2, Integer.valueOf(itemID.trim()));
		int res =(Integer)query.executeUpdate();
		return res;
	}

	 
	
}
