package patentsearch.bean.user;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
 
/**
 * 检索历史实体
 */
@Entity
public class SearchHistory implements Serializable {
 
	private static final long serialVersionUID = 1647127399065513102L;
	/* ID */
	/* 检索序号 */
	@Id@GeneratedValue
	private Integer id;
	/* 检索式 */
	@Column(length = 200)
	private String searchformula;
	
	@Column(length = 200)
	private String addtime;
	/* 命中次数 */
	
	@Column(length = 200)
	private String resultnum;
	/* 命中次数 */
	
	@Column(length = 200)
	private String userid;
	/* 命中次数 */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getSearchformula() {
		return searchformula;
	}

	public void setSearchformula(String searchformula) {
		this.searchformula = searchformula;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getResultnum() {
		return resultnum;
	}

	public void setResultnum(String resultnum) {
		this.resultnum = resultnum;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Override
	public String toString() {
		return "SearchHistory [addtime=" + addtime + ", id=" + id
				+ ", resultnum=" + resultnum + ", searchformula="
				+ searchformula + ", userid=" + userid + "]";
	}
	
	
	

 
	 

}
