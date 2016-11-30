package patentsearch.service.user.impl;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Query;
import org.springframework.stereotype.Service;
import patentsearch.bean.user.Users;
import patentsearch.service.base.impl.DaoSupport;
import patentsearch.service.user.UserService;
import patentsearch.utils.base.MD5;
@Service
public class UserServiceImpl extends DaoSupport<Users> implements UserService {

	public boolean checkByUsername(String username) {
		Query query = em.createQuery("select count(o) from Users o where o.username=?1");
		query.setParameter(1, username);
		long count = (Long) query.getSingleResult();
		return count>0;
	}

	@SuppressWarnings("unchecked")
	public Users find(String username, String password) {
		Query query = em.createQuery("select o from Users o where o.username=?1 and o.password=?2");
		query.setParameter(1, username).setParameter(2, MD5.MD5Encode(password));
		 
		List<Users> users = query.getResultList();
		if(users.size() > 0){
			return users.get(0);
		}
		return null;
	}

	public boolean updatePassword(String username, String oldpwd, String newpwd) {
		Query query = em.createQuery("update Users o set o.password=?1 where o.username=?2 and o.password=?3");
		query.setParameter(1, MD5.MD5Encode(newpwd)).setParameter(2, username).setParameter(3, MD5.MD5Encode(oldpwd));
		return query.executeUpdate() > 0;
	}
	
 
	public boolean editState(Short state, String username,String suggest) {
		Query query = em.createQuery("update Users o set o.state=?1, o.suggest=?2 where o.username=?3");
		query.setParameter(1, state).setParameter(2, suggest).setParameter(3, username);
		return query.executeUpdate() > 0;
	}

	public boolean checkByOrganisationCode(String organisationCode) {
		Query query = em.createQuery("select count(o) from Users o where o.organisationCode=?1");
		query.setParameter(1, organisationCode);
		long count = (Long) query.getSingleResult();
		return count>0;
	}

	public boolean checkByIDNumber(String idNumber) {
		Query query = em.createQuery("select count(o) from Users o where o.IDNumber=?1");
		query.setParameter(1, idNumber);
		long count = (Long) query.getSingleResult();
		return count>0;
	}

	public boolean editUserInfo(Users user) {
		if(user != null && user.getUsername() != null && !"".equals(user.getUsername())){
			Query query = null;
			String sql = "update Users o set o.name=?1,";
			if(user.getOrganisationCode() != null && !"".equals(user.getOrganisationCode())){
				sql = sql + "o.organisationCode=?2 where o.username=?3";
				query = em.createQuery(sql);
				query.setParameter(1, user.getName()).setParameter(2, user.getOrganisationCode())
						.setParameter(3, user.getUsername());
			}else if(user.getIDNumber() != null && !"".equals(user.getIDNumber())){
				sql = sql + "o.IDNumber=?2 where o.username=?3";
				query = em.createQuery(sql);
				query.setParameter(1, user.getName()).setParameter(2, user.getIDNumber())
						.setParameter(3, user.getUsername());
			}
			//System.out.println(sql);
			return query.executeUpdate() > 0; 

		}
		return false;
		
	}

	public boolean setPassword(String username, String pwd) {
		Query query = em.createQuery("update Users o set o.password=?1 where o.username=?2");
		query.setParameter(1, MD5.MD5Encode(pwd)).setParameter(2, username);
		return query.executeUpdate() > 0;
	}
/*
 * 硬删除*/
	@Override
	public void delete(Serializable... entityids) {
		/*if(entityids != null && entityids.length > 0){
			for(Serializable entityid : entityids){
				User user = find(entityid);
				Set<Attachment> attachments = user.getAttachments();
				if(attachments != null && attachments.size() > 0){
					for(Attachment attachment : attachments){
						String filePath = ServletActionContext.getServletContext().getRealPath(attachment.getPath() + "/" + attachment.getStorageFilename());
						File file = new File(filePath);
						if(file.exists()){
							file.delete();
						}
							
					}
				}
			}
		}*/
		super.delete(entityids);
	}

	 
	 
	 
	
	/**
	 * 软删除
	 
	@Override
	public void delete(Serializable... entityids) {
		if(entityids != null && entityids.length > 0){
			for(Serializable entityid : entityids){
				User user = find(entityid);
				user.setIsdeled((short)1);
				update(user);
			}
		}
		
	}*/
	

	public Users checkCompany(String companyName) {
//System.out.println("companyName="+companyName);		
		if(companyName==null||"".equals(companyName.trim())){
			return null;
		}
		else{
			Query query = em.createQuery("select o from Users o where o.name=?1");
			query.setParameter(1, companyName);
			List<Users> users = query.getResultList();	
			if(users.size()>0){
				return users.get(0);
			}
			return null;
		}		
	}
	
	public Users selectByUsername(String username){
		if(username==null||"".equals(username.trim())){
			return null;
		}
		else{
			Query query = em.createQuery("select o from Users o where o.username=?1");
			query.setParameter(1, username);
			List<Users> users = query.getResultList();	
			if(users.size()>0){
				return users.get(0);
			}
			return null;
		}
	}
	
	
}
