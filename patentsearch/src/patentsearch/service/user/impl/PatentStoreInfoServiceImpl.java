package patentsearch.service.user.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.stereotype.Service;

import patentsearch.bean.base.QueryResult;
import patentsearch.bean.user.PatentCategory;
import patentsearch.bean.user.PatentStoreInfo;
import patentsearch.bean.util.xml.DateUtil;
import patentsearch.service.base.impl.DaoSupport;
import patentsearch.service.user.CategoryService;
import patentsearch.service.user.PatentStoreInfoService;
@Service
public class PatentStoreInfoServiceImpl extends DaoSupport<PatentStoreInfo> implements PatentStoreInfoService {

	@Override
	public int getNodeNum(int catid) {
		Query query = em.createNativeQuery("select count(*) from PatentStoreInfo where patentCategory_id = ?1");
		query.setParameter(1, catid);
		int res =(Integer)query.getSingleResult();
		return res;
	}

	@Override
	public List<Map>  getNodeDetailList(int catid,int sum,String order,String whereStr ,int rows) {
		
		String sql = "select top "+ rows +" id,appno,createTime,title,patentCategory_id,importantLevel,searchscope,appl,apd,legalstateold,legalstatenew,addtime,formula,resultnum,userid,yearfeedate from PatentStoreInfo aa where aa.patentCategory_id = ?1"+whereStr+" and (aa.id not in ( select top "+ sum +" id from PatentStoreInfo bb where bb.patentCategory_id = ?1  order by "+ order +", importantLevel desc)) order by "+ order +",importantLevel desc";
		//Query query = em.createNativeQuery("select top 10 id,appno,createTime,title,patentCategory_id,importantLevel,searchscope,appl,apd,legalstateold,legalstatenew,addtime,formula,resultnum,userid,yearfeedate from PatentStoreInfo where patentCategory_id = ?1 and (id not in ( select top 0 id from PatentStoreInfo )) order by importantLevel desc");
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, catid);
		//query.setParameter(2, sum);
		List<Object> queryResult =query.getResultList();
		List<Map> records = new ArrayList<Map>();
		for (Object obj : queryResult) {
			Object[] objs = (Object[]) obj;
			Map<String, Object> m = new HashMap<String, Object>(); 
			m.put("id", objs[0]);
			m.put("appno",  objs[1]);
			m.put("appname",  objs[3]);
			m.put("apd", "".equals((String)objs[8])?"":DateUtil.formatStrToStr((String)objs[8]));
			m.put("appl",  objs[7]);
			//m.put("time", DateUtil.dateToTextString(patentStoreInfo.getCreateTime()));
			m.put("time", DateUtil.dateToTextString((Date)objs[2]));
			m.put("searchscope",  objs[6]);
			
			m.put("yearFeeDate",  objs[15]);
			m.put("legalstateold",  objs[9]);
			records.add(m);
		}
		
		return records;
	}
	
	@Override
	public int getNodeDetailListCount(int catid,String whereStr) {
		
		String sql = "select count(*) from PatentStoreInfo where patentCategory_id = ?1 " +whereStr;
		//Query query = em.createNativeQuery("select top 10 id,appno,createTime,title,patentCategory_id,importantLevel,searchscope,appl,apd,legalstateold,legalstatenew,addtime,formula,resultnum,userid,yearfeedate from PatentStoreInfo where patentCategory_id = ?1 and (id not in ( select top 0 id from PatentStoreInfo )) order by importantLevel desc");
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, catid);
		//query.setParameter(2, sum);
		int queryResult =(Integer)query.getSingleResult();
		
		
		return queryResult;
	}

	
}
