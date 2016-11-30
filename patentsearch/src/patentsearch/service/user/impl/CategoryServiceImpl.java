package patentsearch.service.user.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Service;

import patentsearch.bean.base.QueryResult;
import patentsearch.bean.user.PatentCategory;
import patentsearch.bean.user.PatentStoreInfo;
import patentsearch.bean.user.Users;
import patentsearch.service.base.impl.DaoSupport;
import patentsearch.service.user.CategoryService;
@Service
public class CategoryServiceImpl extends DaoSupport<PatentCategory> implements  CategoryService {

	@Override
	public int updateYearDateById(String id,String newDate) {
		Query query = em.createQuery("update  PatentStoreInfo o set o.yearfeedate =?2 where  o.id=?1 ");
		query.setParameter(1, Integer.valueOf(id.trim()));
		query.setParameter(2, newDate.trim());
		return query.executeUpdate();
	}

	@Override
	public QueryResult<PatentCategory> getProTree(Users user, int type) {
		
		Query query = em.createNativeQuery("select id,createTime,name,parent_id,user_id,categoryType from PatentCategory o where o.user_id=?1 and o.categoryType=?2");
		query.setParameter(1, user.getId());
		query.setParameter(2, type);
		List<PatentCategory> patentCategorys = new ArrayList<PatentCategory>();
		List<Object> res = query.getResultList();
		for (Object obj : res) {
			Object[] objs = (Object[]) obj;
			PatentCategory patentCategory = new PatentCategory();
			patentCategory.setId((Integer) objs[0]);
			//patentCategory.setCreateTime(obj[1]);
			patentCategory.setName((String)objs[2]);
			if(String.valueOf(((Integer)objs[3])).equals("1")){
				Object[] temp = {1,new Date(),"整个世界",0,13,0}; 
				patentCategory.setParent(arrayToBean(user,type,temp,res));
			}
			else{
				for (Object obj2 : res) {
					Object[] objs2 = (Object[]) obj2;
					if(((Integer)objs2[0]).equals(((Integer)objs[3]))){
						patentCategory.setParent(arrayToBean(user,type,objs2,res));
						break;
					}
				}
			}
			
			
			//patentCategory.setParent(this.find((Integer)objs[3]));
			patentCategory.setUser(user);
			patentCategory.setCategoryType(type);
			patentCategorys.add(patentCategory);
		}
		QueryResult<PatentCategory> result=new QueryResult<PatentCategory>();
		result.setResultlist(patentCategorys);
		result.setTotalrecord(patentCategorys.size());
		return result;
	}
	
	public PatentCategory arrayToBean(Users user, int type,Object[] objs,List<Object> allobj){
		PatentCategory patentCategory = new PatentCategory();
		try{
			patentCategory.setId(((Integer)objs[0]));
		}catch(Exception e){
			patentCategory.setId(((Integer)objs[0]));
		}
		
		//patentCategory.setCreateTime(obj[1]);
		patentCategory.setName((String)objs[2]);
		if(null==objs[3]||((Integer)objs[3]).equals(0)){
			patentCategory.setParent(null);
		}
		else if(((Integer)objs[3]).equals(1)){
			Object[] temp = {1,new Date(),"整个世界",0,13,0};  
			patentCategory.setParent(arrayToBean(user,type,temp,allobj));
		}
		else{
			for (Object obj2 : allobj) {
				Object[] objs2 = (Object[]) obj2;
				if(((Integer)objs2[0])==((Integer)objs[3])){
					patentCategory.setParent(arrayToBean(user,type,objs2,allobj));
					break;
				}
			}
		}
		
		
		//patentCategory.setParent(this.find((Integer)objs[3]));
		patentCategory.setUser(user);
		patentCategory.setCategoryType(type);
		return patentCategory;
		
	}

	@Override
	public String getYearFeeDateById(int id) {
		Query query = em.createNativeQuery("select yearfeedate from PatentStoreInfo where id=?1");
		query.setParameter(1, id);
		String res=(String)query.getSingleResult();
		return res;
	}
	
}
