package patentsearch.service.user.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Service;

import patentsearch.bean.user.SearchHistory;
import patentsearch.service.user.SearchHistoryService;
import patentsearch.service.base.impl.DaoSupport;
import patentsearch.utils.base.MD5;
@Service
public class SearchHistoryImpl extends DaoSupport<SearchHistory> implements SearchHistoryService {

	@SuppressWarnings("unchecked")
	@Override
	public List<SearchHistory> findAll(String userid) {
		Query query = em.createNativeQuery("select  top 100 id,searchformula,addtime,resultnum,userid from SearchHistory o where o.userid=?1 order by id desc");
		query.setParameter(1, userid);
		 
		List<Object[]> list = query.getResultList();
		List<SearchHistory> searchHistorys = new ArrayList<SearchHistory>();
		SearchHistory searchHistory;
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[]) list.get(i);
				searchHistory = new SearchHistory();
				searchHistory.setId(Integer.valueOf(objs[0].toString().trim()));
				searchHistory.setSearchformula(objs[1].toString().trim());
				searchHistory.setAddtime(objs[2].toString().trim());
				searchHistory.setResultnum(objs[3].toString().trim());
				searchHistory.setUserid(objs[4].toString().trim());
				searchHistorys.add(searchHistory);
			}
			return searchHistorys;
		}
		return null;
	}

	@Override
	public boolean delById(String id) {
		Query query = em.createQuery("delete from  SearchHistory o where  o.id=?1 ");
		query.setParameter(1, Integer.valueOf(id.trim()));
		return query.executeUpdate() > 0;
		
	}

}
