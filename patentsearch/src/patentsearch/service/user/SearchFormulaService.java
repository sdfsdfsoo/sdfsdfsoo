package patentsearch.service.user;

import patentsearch.bean.user.SearchFormula;
import patentsearch.service.base.DAO;

 


public interface SearchFormulaService extends DAO<SearchFormula> {
	 
	 public int deleteByUserItem(int userid, String itemID);
	
	 
}
