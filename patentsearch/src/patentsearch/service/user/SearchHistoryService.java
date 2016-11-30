package patentsearch.service.user;

import java.util.List;

import javax.persistence.Query;

import patentsearch.bean.user.SearchHistory;
import patentsearch.bean.user.Users;
import patentsearch.service.base.DAO;
import patentsearch.utils.base.MD5;

public interface SearchHistoryService extends DAO<SearchHistory>  {
	public List<SearchHistory> findAll(String userid);


	public boolean delById(String id);
}
