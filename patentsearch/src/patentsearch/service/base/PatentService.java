package patentsearch.service.base;

import patentsearch.bean.base.Patent;



public interface PatentService extends DAO<Patent> {
	public boolean delAllPatent();
}
