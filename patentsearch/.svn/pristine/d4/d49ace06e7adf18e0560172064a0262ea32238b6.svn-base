package patentsearch.service.base;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import patentsearch.bean.base.QueryResult;
import patentsearch.utils.base.GenericsUtils;

@SuppressWarnings("unchecked")
@Transactional
public class DaoSupport<T> implements DAO<T> {

	@PersistenceContext protected EntityManager em;
	protected Class<T> entityClass = GenericsUtils.getSuperClassGenericType(this.getClass());
	
	public void delete(Serializable... entityids){
		for(Serializable entityid : entityids){
			em.remove(em.getReference(entityClass, entityid));
		}
	}

	
	@Transactional(readOnly=true,propagation=Propagation.NOT_SUPPORTED)
	public T find(Serializable entityid) {
		if(entityid==null) 
			throw new RuntimeException(this.entityClass.getName()+ ":传入的实体id不能为空");
		return em.find(entityClass, entityid);
	}

	
	public void save(T entity) {
		em.persist(entity);
		
	}

	
	public void update(T entity) {
		em.merge(entity);
		
	}
    //=============================================分页====================================
	
	
	
	@Transactional(readOnly=true,propagation=Propagation.NOT_SUPPORTED)
	public QueryResult<T> getScrollData(int firstindex, int maxresult) {
		return getScrollData(firstindex,maxresult,null,null,null);
	}

	
	
	public QueryResult<T> getScrollData() {
		return getScrollData(-1,-1,null,null,null);
	}

	
	@Transactional(readOnly=true,propagation=Propagation.NOT_SUPPORTED)
	public QueryResult<T> getScrollData(int firstindex, int maxresult,
			LinkedHashMap<String, String> orderby) {
		return getScrollData(firstindex,maxresult,null,null,orderby);
	}
	
	

	public QueryResult<T> getScrollData(int firstindex, int maxresult,
			String wherejpql, Object[] params) {
		return getScrollData(firstindex,maxresult,wherejpql,params,null);
	}


	
	@Transactional(readOnly=true,propagation=Propagation.NOT_SUPPORTED)
	public QueryResult<T> getScrollData(int firstindex, int maxresult,
			String wherejpql, Object[] params,
			LinkedHashMap<String, String> orderby) {
		QueryResult<T> qr = new QueryResult<T>();
		Query query = em.createQuery("select o from " + getEntityName() + " o " +(wherejpql==null || "".equals(wherejpql)?"":"where "+wherejpql)+ getOrderByJpql(orderby));
		setQueryParam(query, params);
		if(firstindex != -1 && maxresult != -1){
			query.setFirstResult(firstindex).setMaxResults(maxresult);
		}
		
		qr.setResultlist(query.getResultList());
		/*获取总记录数*/
		query = em.createQuery("select count("+getCountField(entityClass)+") from " + getEntityName() + " o " +(wherejpql==null || "".equals(wherejpql)?"":"where "+wherejpql));
		setQueryParam(query, params);
		qr.setTotalrecord((Long)query.getSingleResult());
		return qr;
	}
	/**
	 * 设置查询条件参数
	 * @param query
	 * @param params
	 */
	protected void setQueryParam(Query query,Object[] params){
		if(params != null && params.length>0){
			for(int i=0;i<params.length;i++){
				query.setParameter(i+1, params[i]);
			}
		}
	}

	/**
	 * 组装orderby语句
	 * @param orderby key为字段名如typeid，value为升序或降序如desc
	 * @return
	 */
	public String getOrderByJpql(LinkedHashMap<String,String> orderby){
		StringBuilder orderjpql = new StringBuilder("");
		if(orderby != null && orderby.size()>0){
			orderjpql.append(" order by ");
			for(Map.Entry<String, String> entity : orderby.entrySet()){
				orderjpql.append("o.")
						.append(entity.getKey()).append(" ").append(entity.getValue()).append(",");
			}
			orderjpql.deleteCharAt(orderjpql.length()-1);
		}
		return orderjpql.toString();
	}
	
	
	/**
	 * 获取实体名称
	 * @return
	 */
	protected String getEntityName(){
		String entityName = entityClass.getSimpleName();
		Entity entity = entityClass.getAnnotation(Entity.class);
		if(entity.name()!=null && !"".equals(entity.name())){
			entityName = entity.name();
			
		}
		return entityName;
	}

	public long getCount() {
		Query query = em.createQuery("select count("+getCountField(entityClass)+") from " + getEntityName() + " o ");
		return (Long) query.getSingleResult();
	}
	
	/**
	 * 获取统计属性,该方法是为了解决hibernate解析联合主键select count(o) from Xxx o语句BUG而增加,hibernate对此jpql解析后的sql为select count(field1,field2,...),显示使用count()统计多个字段是错误的
	 * @param <E>
	 * @param clazz
	 * @return
	 */
	protected static <E> String getCountField(Class<E> clazz){
		String out = "o";
		try {
			PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
			for(PropertyDescriptor propertydesc : propertyDescriptors){
				Method method = propertydesc.getReadMethod();
				if(method!=null && method.isAnnotationPresent(EmbeddedId.class)){					
					PropertyDescriptor[] ps = Introspector.getBeanInfo(propertydesc.getPropertyType()).getPropertyDescriptors();
					out = "o."+ propertydesc.getName()+ "." + (!ps[1].getName().equals("class")? ps[1].getName(): ps[0].getName());
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        return out;
	}
	
	
}
