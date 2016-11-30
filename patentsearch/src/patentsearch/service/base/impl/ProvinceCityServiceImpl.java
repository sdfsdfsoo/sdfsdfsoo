package patentsearch.service.base.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Service;

import patentsearch.bean.base.ProvinceCity;
import patentsearch.service.base.ProvinceCityService;

@Service
public class ProvinceCityServiceImpl extends DaoSupport<ProvinceCity> implements
		ProvinceCityService {

	public String detailInfo(List<ProvinceCity> provinceCityList, String code) {
		if (code != null && !"".equals(code)) {
			for (ProvinceCity provinceCity : provinceCityList) {
				if (code.equals(provinceCity.getCode())) {
					return provinceCity.getName() + "(代码" + code + ")";
				}
			}
		}
		return "无";
	}

	public List<ProvinceCity> getCoListByTerm(String term) {
		if (term != null && !"".equals(term)) {
			StringBuffer where = new StringBuffer();
			List<Object> parameters = new ArrayList<Object>();
				where.append("  o.code " + " like ?" + (parameters.size() + 1));
				parameters.add("%" + term + "%");

				where.append(" or o.name " + " like ?"
						+ (parameters.size() + 1));
				parameters.add("%" + term + "%");

				where.append(" or o.pinyin " + " like ?"
						+ (parameters.size() + 1));
				parameters.add(term.toLowerCase());
			  return this.getScrollData(-1, -1, where.toString(),
					parameters.toArray()).getResultlist();
		}
		return null;

	}

}
