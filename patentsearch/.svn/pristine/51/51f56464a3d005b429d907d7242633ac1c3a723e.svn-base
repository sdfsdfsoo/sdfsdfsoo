package patentsearch.service.privilege.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import patentsearch.bean.privilege.SystemPrivilege;
import patentsearch.service.base.impl.DaoSupport;
import patentsearch.service.privilege.SystemPrivilegeService;

 

@Service
public class SystemPrivilegeServiceBean extends DaoSupport<SystemPrivilege> implements SystemPrivilegeService {
	
	public void batchSave(List<SystemPrivilege> privileges){
		for(SystemPrivilege p : privileges){
			save(p);
		}
	}
}
