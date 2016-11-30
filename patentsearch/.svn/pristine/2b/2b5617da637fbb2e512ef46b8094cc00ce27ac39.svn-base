package patentsearch.service.privilege.impl;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.persistence.Query;
import org.springframework.stereotype.Service;
import patentsearch.bean.privilege.PrivilegeGroup;
import patentsearch.service.base.impl.DaoSupport;
import patentsearch.service.privilege.PrivilegeGroupService;

@Service
public class PrivilegeGroupServiceBean extends DaoSupport<PrivilegeGroup> implements PrivilegeGroupService {

	@Override
	public void delete(Serializable... entityids) {
	/*	for(Serializable id : entityids){
			PrivilegeGroup group = find(id);
			for(Master employee : group.getEmployees()){
				employee.getGroups().remove(group);
			}
			em.remove(group);
		}*/
	}

	@Override
	public void save(PrivilegeGroup entity) {
		entity.setGroupid(UUID.randomUUID().toString());
		super.save(entity);
	}
	@SuppressWarnings("unchecked")
	public List<PrivilegeGroup> getGroupByGroupids(String... groupids) {
		if(groupids != null && groupids.length > 0){
			StringBuilder whereStr = new StringBuilder();
			for(int i=0;i<groupids.length;i++){
				whereStr.append("?").append(i+1).append(",");
			}
			whereStr.deleteCharAt(whereStr.length()-1);
			Query query = em.createQuery("select o from PrivilegeGroup o where o.groupid in(" + whereStr.toString() + ")");//?1,?2
			for(int i=0;i<groupids.length;i++){
				query.setParameter(i+1, groupids[i]);
			}
			return query.getResultList();
		}
		return null;
	}
}
