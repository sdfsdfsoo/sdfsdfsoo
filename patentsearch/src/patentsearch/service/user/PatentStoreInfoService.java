package patentsearch.service.user;

import java.util.List;
import java.util.Map;

import patentsearch.bean.base.QueryResult;
import patentsearch.bean.user.PatentCategory;
import patentsearch.bean.user.PatentStoreInfo;
import patentsearch.bean.user.Users;
import patentsearch.service.base.DAO;

 


public interface PatentStoreInfoService extends DAO<PatentStoreInfo> {
	public int getNodeNum(int catid);
	
	public List<Map> getNodeDetailList(int catid,int sum,String order,String whereStr,int rows);
	
	public int getNodeDetailListCount(int catid,String whereStr);
}
