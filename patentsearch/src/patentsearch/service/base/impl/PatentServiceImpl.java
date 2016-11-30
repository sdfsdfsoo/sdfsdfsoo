package patentsearch.service.base.impl;

import javax.persistence.Query;

import org.springframework.stereotype.Service;


import patentsearch.bean.base.Patent;
import patentsearch.service.base.PatentService;

@Service
public class PatentServiceImpl extends DaoSupport<Patent> implements
		PatentService {

	@Override
	public boolean delAllPatent() {
		int num = 0;
		try{
			Query query = em.createQuery("delete from Patent");
			num = query.executeUpdate();
			if(num > 0)
				return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

}
