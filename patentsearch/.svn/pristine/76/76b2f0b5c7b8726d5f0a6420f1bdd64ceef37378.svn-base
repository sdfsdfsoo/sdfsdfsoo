package patentsearch.service.base;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;


import patentsearch.bean.base.QueryResult;


public interface DAO<T> {
	
	/**
	 * 获取记录总数
	 * @param entityClass 实体类
	 * @return
	 */
	public long getCount();
	/*
	 * 增加实体
	 */
	public void save(T entity);
	/*
	 * 更新实体
	 */
	public void update(T entity);
	/**
	 * 根据实体id数组删除实体
	 * @param entityids
	 */
	public void delete(Serializable... entityids);
	/**
	 * 根据id查找实体
	 * @param entityid
	 * @return
	 */
	public T find(Serializable entityid);
	/**
	 * 获取全部数据
	 * @return
	 */
	public QueryResult<T> getScrollData();
	/**
	 * 获取分页数据		
	 * @param firstindex 记录开始索引
	 * @param maxresult 每页的记录数
	 * @return
	 */
	public QueryResult<T> getScrollData(int firstindex,int maxresult);
	/**
	 * 排序之后获取分页数据
	 * @param firstindex
	 * @param maxresult
	 * @param orderby 排序语句，如 typeid desc,name asc
	 * @return
	 */
	public QueryResult<T> getScrollData(int firstindex,int maxresult,LinkedHashMap<String,String> orderby);
	/**
	 * 条件查询不排序
	 * @param firstindex
	 * @param maxresult
	 * @param wherejpql
	 * @param params
	 * @return
	 */
	public QueryResult<T> getScrollData(int firstindex,int maxresult,String wherejpql,Object[] params);
	/**
	 * 
	 * @param firstindex
	 * @param maxresult
	 * @param orderby
	 * @param wherejpql 条件语句如，o.typeid=?1
	 * @return
	 */
	public QueryResult<T> getScrollData(int firstindex,int maxresult,String wherejpql,Object[] params,LinkedHashMap<String,String> orderby);
	/**
	 * 
	 * @param firstindex
	 * @param maxresult
	 * @param orderby
	 * @param wherejpql 条件语句如，o.typeid=?1
	 * @return
	 */
	public QueryResult<T> getScrollData(int firstindex,int maxresult,String wherejpql,Object[] params,LinkedHashMap<String,String> orderby,String zhaiYao,String zhuQuanLi);
	/**
	 * 
	 * @param firstindex
	 * @param maxresult
	 * @param orderby
	 * @param wherejpql 条件语句如，o.typeid=?1
	 * @return
	 */
	public List<String> getAppnoListById(String wherejpql);

}
