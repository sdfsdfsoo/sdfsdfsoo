package patentsearch.service.user;

import patentsearch.bean.base.QueryResult;
import patentsearch.bean.user.PatentCategory;
import patentsearch.bean.user.PatentStoreInfo;
import patentsearch.bean.user.Users;
import patentsearch.service.base.DAO;

 


public interface CategoryService extends DAO<PatentCategory> {
	public int updateYearDateById(String id,String newDate);
	
	public QueryResult<PatentCategory> getProTree(Users user,int type);
	
	public String getYearFeeDateById(int id);
}
